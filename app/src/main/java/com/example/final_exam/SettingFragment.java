package com.example.final_exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends Fragment {

    private EditText editTextIPAddress, editTextPort;
    private Button buttonSend, buttonDisconnect;
    private TextView textViewResult;
    private DataStorage dataStorage;

    private UdpManager udpManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataStorage = new DataStorage(requireContext());
        // Inflate the layout for this fragment

        udpManager = UdpManager.getInstance();

        return inflater.inflate(R.layout.fragment_setting, container, false);
        // trả về một View là giao diện của Fragment và hiển thị trên màn hình

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextIPAddress = view.findViewById(R.id.editTextIPAddress);
        editTextPort = view.findViewById(R.id.editTextPort);
        buttonSend = view.findViewById(R.id.buttonSend);
        buttonDisconnect = view.findViewById(R.id.buttonDisconnect);
        textViewResult = view.findViewById(R.id.textViewResult);


        editTextIPAddress.setText(dataStorage.getIpAddress());
        editTextPort.setText(dataStorage.getPortNumber());



        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = editTextIPAddress.getText().toString();
                String portNumber = editTextPort.getText().toString();

                    dataStorage.saveIpAddressAndPort(ipAddress, portNumber);

                    conectToEsp32(ipAddress, portNumber);
                udpManager.setServerAddress(ipAddress, Integer.parseInt(portNumber));
                udpManager.getInstance().connectToEsp32(ipAddress, Integer.parseInt(portNumber));
                }
        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
                udpManager.getInstance().closeAllConnections();

            }
        });

        // Khôi phục giá trị textViewResult từ SharedPreferences
        String savedResult = dataStorage.getResult();
        if (!savedResult.isEmpty()) {
            textViewResult.setText(savedResult);
        }


    }


    private void conectToEsp32(String ipAddress, String portNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress address = InetAddress.getByName(ipAddress);
                    int port = Integer.parseInt(portNumber);

                    byte[] sendData = "Connected!".getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                    socket.send(sendPacket);

                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.setSoTimeout(5000); // Set a timeout for receiving response (5 seconds)

                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // Update UI using runOnUiThread()
                    final String status;
                    if (response.equals("Successful")) {
                        status = "Connected";

                    } else {
                        status = "Disconnected";
                    }
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Connect Sucessfully", Toast.LENGTH_LONG).show();
                            textViewResult.setText("Status: " + status + " Ip Address: " + address + " Port: " + port);
                            dataStorage.saveIpAddress(ipAddress);
                            dataStorage.savePortNumber(portNumber);
                            dataStorage.saveResult(textViewResult.getText().toString());
                            dataStorage.saveConnectionStatus(status);
                        }
                    });

                    socket.close();


                } catch (Exception e) {
                    e.printStackTrace();
                    // Update UI on the main thread
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewResult.setText("Connection Failed");
                        }
                    });
                }
            }

        }).start();
    }


    private void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    String ipAddress = editTextIPAddress.getText().toString();
                    String portNumber = editTextPort.getText().toString();
                    InetAddress address = InetAddress.getByName(ipAddress);
                    int port = Integer.parseInt(portNumber);

                    byte[] disconnectSignal = "Disconnect".getBytes(); // Chuỗi sẽ gửi đến ESP32 để yêu cầu ngắt kết nối

                    DatagramPacket sendPacket = new DatagramPacket(disconnectSignal, disconnectSignal.length, address, port);
                    socket.send(sendPacket);

                    // Cập nhật UI trên luồng chính
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewResult.setText("Disconnected");
                            Toast.makeText(requireContext(), "Connect Unsucessfully", Toast.LENGTH_LONG).show();
                            dataStorage.saveConnectionStatus("Disconnected");
                            dataStorage.saveResult(textViewResult.getText().toString());
                        }
                    });
                    socket.close();


                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewResult.setText("Failed to Disconnect");
                        }
                    });
                }

            }
        }).start();
    }
}