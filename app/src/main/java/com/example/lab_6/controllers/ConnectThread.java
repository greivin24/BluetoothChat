package com.example.lab_6.controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private BluetoothSocket bluetoothSocket;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private UUID deviceUUID;

    private BluetoothConnectionService bluetoothConnectionService;

    public ConnectThread (BluetoothAdapter adapter, BluetoothDevice device, UUID uuid){
        this.bluetoothAdapter = adapter;
        this.device = device;
        this.deviceUUID = uuid;
    }

    @Override
    public void run() {
        BluetoothSocket temBluetoothServerSocket=null;

        try {
            temBluetoothServerSocket = device.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "+deviceUUID);
        } catch (IOException e) {
            Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
        }
        bluetoothSocket = temBluetoothServerSocket;
        bluetoothAdapter.cancelDiscovery();
        try {
            bluetoothSocket.connect();
            Log.d(TAG, "run: ConnectThread connected.");
        } catch (IOException ex) {
            try {
                bluetoothSocket.close();
                Log.d(TAG, "run: Closed Socket.");
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: run: Unable to close connection in socket " + e.getMessage());
            }
            Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + deviceUUID );
        }

       bluetoothConnectionService.connected(bluetoothSocket, device);
    }

    public void cancel() {
        try {
            Log.d(TAG, "cancel: Closing Client Socket.");
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "cancel: close() of bluetoothSocket in Connectthread failed. " + e.getMessage());
        }
    }
}
