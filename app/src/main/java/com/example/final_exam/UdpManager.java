package com.example.final_exam;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpManager {

    private static final String TAG = "UdpManager";
    private static UdpManager instance;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;

    private boolean isConnected = false;


    public UdpManager() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static synchronized UdpManager getInstance() {
        if (instance == null) {
            instance = new UdpManager();
        }
        return instance;
    }



    // Set địa chỉ và cổng của server
    public void setServerAddress(String ipAddress, int port) {
        try {
            serverAddress = InetAddress.getByName(ipAddress);
            serverPort = port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    // Gửi dữ liệu thông qua kết nối UDP
    public void sendData(String data) {
        if (serverAddress != null) {
            new Thread(() -> {
                try {
                    byte[] buffer = data.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, serverPort);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void disconnect() {
        // Ngắt kết nối với server
        // ...
        isConnected = false;
        socket.close();
    }


    public void connectToEsp32(String ipAddress, int port) {
        closeAllConnections(); // Đóng kết nối hiện tại trước khi tạo kết nối mới

        try {
            serverAddress = InetAddress.getByName(ipAddress);
            serverPort = port;

            socket = new DatagramSocket();

        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ khi kết nối thất bại
        }
    }
    public void closeAllConnections() {
        // Thực hiện đóng tất cả các kết nối ở đây
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        // Đặt các giá trị khác về null hoặc giá trị mặc định nếu cần
        serverAddress = null;
        serverPort = 0;
    }

}