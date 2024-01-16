package com.example.final_exam;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStorage {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "DataStorage";
    private static final String IP_ADDRESS = "ip_address";
    private static final String PORT_NUMBER = "port_number";
    private static final String CONNECTION_STATUS = "connection_status";
    private static final String RESULT = "result";

    public DataStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveIpAddress(String ipAddress) {
        sharedPreferences.edit().putString(IP_ADDRESS, ipAddress).apply();
    }

    public String getIpAddress() {
        return sharedPreferences.getString(IP_ADDRESS, "");
    }

    public void savePortNumber(String portNumber) {
        sharedPreferences.edit().putString(PORT_NUMBER, portNumber).apply();
    }

    public String getPortNumber() {
        return sharedPreferences.getString(PORT_NUMBER, "");
    }

    public void saveConnectionStatus(String status) {
        sharedPreferences.edit().putString(CONNECTION_STATUS, status).apply();
    }

    public String getConnectionStatus() {
        return sharedPreferences.getString(CONNECTION_STATUS, "");
    }

    public void saveResult(String textViewResult) {
        sharedPreferences.edit().putString(RESULT, textViewResult).apply();
    }

    public String getResult() {
        return sharedPreferences.getString(RESULT, "");
    }

    public void saveIpAddressAndPort(String ipAddress, String portNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IP_ADDRESS, ipAddress);
        editor.putString(PORT_NUMBER, portNumber);
        editor.apply();
    }

}
