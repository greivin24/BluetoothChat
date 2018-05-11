package com.example.lab_6.controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.util.UUID;

public class BluetoothConnectionService {

    private static final String TAG = "BluetoothConnectionService";
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final String appName = "MYAPP";
    private final BluetoothAdapter bluetoothAdapter;
    Context context;

    private AcceptThead insecureAcceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private ProgressDialog progressDialog;

    public BluetoothConnectionService(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    @SuppressLint("LongLogTag")
    public synchronized void start() {
        Log.d(TAG, "Start");
        // Cancel any thread attempting to make a connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (insecureAcceptThread == null) {
            insecureAcceptThread = new AcceptThead(bluetoothAdapter, MY_UUID_INSECURE, appName);
            insecureAcceptThread.start();
        }
    }

    @SuppressLint("LongLogTag")
    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: Started.");

        //initprogress dialog
        progressDialog = ProgressDialog.show(context,"Connecting Bluetooth","Please Wait...",true);

        connectThread = new ConnectThread(bluetoothAdapter, device, uuid);
        connectThread.start();
    }

    @SuppressLint("LongLogTag")
    public void connected(BluetoothSocket bSocket, BluetoothDevice bDevice) {
        Log.d(TAG, "connected: Starting.");
        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(bSocket, progressDialog);
        connectedThread.start();
    }

    @SuppressLint("LongLogTag")
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        connectedThread.write(out);
    }
}
