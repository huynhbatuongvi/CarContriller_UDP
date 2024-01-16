package com.example.final_exam;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class HubFragment extends Fragment implements SensorEventListener {

    ImageView gameImg;
    Switch switchImg;

    private TextView textIPAddress, textPort;
    TextView editDirect1, editDirect2;
    RelativeLayout relativeLayout;

    private UdpManager udpManager;


    private DataStorage dataStorage; // Khai báo DataStorage

    private final float THRESHOLD = 2.5f; // Ngưỡng để xác định thiết bị có đang chuyển động hay không
    private boolean isMoving = false;

//Cam bien gia toc dien thoai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataStorage = new DataStorage(requireContext());
        udpManager = UdpManager.getInstance();
        return inflater.inflate(R.layout.fragment_hub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gameImg = view.findViewById(R.id.gameimg);
        switchImg = view.findViewById(R.id.switchControl);
        relativeLayout = view.findViewById(R.id.layoutControl);
        editDirect1 = view.findViewById(R.id.editDirect1);
        editDirect2 =view.findViewById(R.id.editDirect2);
        textIPAddress = view.findViewById(R.id.textIPAddress);
        textPort = view.findViewById(R.id.textPort);


        textIPAddress.setText(dataStorage.getIpAddress()); // Hiển thị IP đã lưu
        textPort.setText(dataStorage.getPortNumber());

        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);


        switchImg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    relativeLayout.setVisibility(View.VISIBLE);
                    gameImg.setImageResource(R.drawable.controller_on);


                    if (sensorManager != null)
                    {
                        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        if (sensor != null)
                        {
                            sensorManager.registerListener(HubFragment.this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
                        }
                    }
                } else
                {
                    relativeLayout.setVisibility(View.GONE);
                    gameImg.setImageResource(R.drawable.controller_off);
                    if (sensorManager != null) {
                    sensorManager.unregisterListener(HubFragment.this);
                }
                }
            }
        });



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            
            float x = event.values[0];
            float y = event.values[1];

            float totalAcceleration = Math.abs(x) + Math.abs(y) ;

            if (totalAcceleration > THRESHOLD) {
                isMoving = true;
                if (Math.abs(x) > Math.abs(y)) {
                    if (x < 0) {
                        editDirect2.setText("Right");
                    } else {
                        editDirect2.setText("Left");
                    }
                } else if (Math.abs(y) > Math.abs(x)) {
                    if (y > 0) {
                        editDirect1.setText("Down");
                    } else {
                        editDirect1.setText("Up");
                    }
                }
            }
            else {
                // Điện thoại đang nằm yên
                isMoving = false;
                editDirect1.setText("Stop");
                editDirect2.setText("No Turn");
            }
            sendControlDataToESP32(editDirect1.getText().toString(), editDirect2.getText().toString());

        }


    }

    private void sendControlDataToESP32(String editDirect1, String editDirect2) {
        final String editDirection1 = editDirect1;
        final String editDirection2 = editDirect2;

        sendDirectionSensorToESP32(editDirection1);
        sendDirectionSensorToESP32(editDirection2);
    }

    private void sendDirectionSensorToESP32(String editDirection) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName("ESP32_IP_ADDRESS");
                    int port = Integer.parseInt(textPort.getText().toString());
                    UdpManager.getInstance().setServerAddress(String.valueOf(address), port);

                    UdpManager.getInstance().sendData(editDirection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        udpManager.disconnect(); // Ngắt kết nối UDP khi fragment bị hủy
    }
}