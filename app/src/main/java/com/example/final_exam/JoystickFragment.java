package com.example.final_exam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class JoystickFragment extends Fragment implements View.OnTouchListener {

    private ImageButton joystickHandle;
    private TextView directionTextView1, directionTextView2, actionText;
    private TextView textIPAddress, textPort;
    private float centerX, centerY;
    private int baseRadius;
    private Button buttonA, buttonB, buttonC, buttonD;

    private DataStorage dataStorage;


    private UdpManager udpManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataStorage = new DataStorage(requireContext());
        udpManager = udpManager.getInstance();

        return inflater.inflate(R.layout.fragment_joystick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout joystickContainer = view.findViewById(R.id.joystickContainer);
        joystickHandle = view.findViewById(R.id.joystickHandle);

        directionTextView1 = view.findViewById(R.id.directionTextView1);
        directionTextView2 = view.findViewById(R.id.directionTextView2);

        actionText = view.findViewById(R.id.actionText);

        buttonA = view.findViewById(R.id.button1);
        buttonB = view.findViewById(R.id.button2);
        buttonC = view.findViewById(R.id.button4);
        buttonD = view.findViewById(R.id.button3);


        textIPAddress = view.findViewById(R.id.textIp);
        textPort = view.findViewById(R.id.textPort);

        textIPAddress.setText(dataStorage.getIpAddress()); // Hiển thị IP đã lưu
        textPort.setText(dataStorage.getPortNumber());

        joystickContainer.setOnTouchListener(this);
        joystickHandle.setOnTouchListener(this);

        joystickHandle.post(new Runnable() {
            @Override
            public void run() {
                centerX = joystickHandle.getX() + joystickHandle.getWidth() / 2;
                centerY = joystickHandle.getY() + joystickHandle.getHeight() / 2;
                baseRadius = ((RelativeLayout.LayoutParams) joystickHandle.getLayoutParams()).width / 2;
            }
        });

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "Action: A";
                actionText.setText(action);
                sendControlButton(action);
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "Action: B";
                actionText.setText(action);
                sendControlButton(action);
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "Action: C";
                actionText.setText(action);
                sendControlButton(action);
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = "Action: D";
                actionText.setText(action);
                sendControlButton(action);
            }
        });

    }

    private void sendControlButton(String actionText) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("ESP32_IP_ADDRESS");
                    int port = Integer.parseInt(textPort.getText().toString());
                    UdpManager.getInstance().setServerAddress(String.valueOf(address), port);

                    UdpManager.getInstance().sendData(actionText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.joystickHandle) {
            float displacementX = event.getRawX() - centerX;
            float displacementY = event.getRawY() - centerY;
            double distance = Math.sqrt(Math.pow(displacementX, 2) + Math.pow(displacementY, 2));

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (distance <= baseRadius) {
                        joystickHandle.setX(event.getRawX() - joystickHandle.getWidth() / 2);
                        joystickHandle.setY(event.getRawY() - joystickHandle.getHeight() / 2);
                    } else {
                        float ratio = baseRadius / (float) distance;
                        float limitedX = centerX + displacementX * ratio;
                        float limitedY = centerY + displacementY * ratio;

                        joystickHandle.setX(limitedX - joystickHandle.getWidth() / 2);
                        joystickHandle.setY(limitedY - joystickHandle.getHeight() / 2);
                    }

                    // Tính toán và hiển thị hướng trong TextView
                    float angleX = displacementX / baseRadius;
                    float angleY = -displacementY / baseRadius;

                    updateDirection(angleX, angleY);
//
                    break;
                case MotionEvent.ACTION_UP:
                    joystickHandle.setX(centerX - joystickHandle.getWidth() / 2);
                    joystickHandle.setY(centerY - joystickHandle.getHeight() / 2);
                    updateDirection(0, 0); // Đặt hướng về trung tâm khi người dùng nhả joystick

                    break;
            }
            return true;
        }
        return false;
    }

    private void updateDirection(float angleX, float angleY) {

        String direction1 = "";
        String direction2 = "";

        if (angleX > 0.5) {
            direction2 = "Right";
        } else if (angleX < -0.5) {
            direction2 = "Left";
        }

        if (angleY > 0.5) {
            direction1 = "Up";
        } else if (angleY < -0.5) {
            direction1 = "Down";
        }

        if (angleX == 0 && angleY ==0)
        {
            direction1 = "Stop";
            direction2 = "No Turn";
        }

        directionTextView1.setText("Forward/Backward: " + direction1);
        directionTextView2.setText("Left/Right: " + direction2);


        sendControlDataToESP32(direction1, direction2);
    }

    private void sendControlDataToESP32(final String direction1,final String direction2) {
        sendDirectionToESP32(direction1);
        sendDirectionToESP32(direction2);
    }

    private void sendDirectionToESP32(String direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("ESP32_IP_ADDRESS");
                    int port = Integer.parseInt(textPort.getText().toString());
                    UdpManager.getInstance().setServerAddress(String.valueOf(address), port);

                    UdpManager.getInstance().sendData(direction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}