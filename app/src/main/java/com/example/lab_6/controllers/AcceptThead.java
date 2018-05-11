package com.example.lab_6.controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class AcceptThead extends Thread {
    private static final String TAG = "AcceptThead";
    private BluetoothServerSocket bluetoothServerSocket;

    public AcceptThead(BluetoothAdapter bluetoothAdapter, UUID code, String appname) {
        BluetoothServerSocket temBluetoothServerSocket = null;
        try {
            temBluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appname, code);
            Log.d(TAG, "AcceptThread: Setting up Server using: " + code);

        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothServerSocket = temBluetoothServerSocket;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: AcceptThread Running.");
        BluetoothSocket socket = null;

        try{
            // This is a blocking call and will only return on a
            // successful connection or an exception
            Log.d(TAG, "run: RFCOM server socket start.....");
            socket = bluetoothServerSocket.accept();
            Log.d(TAG, "run: RFCOM server socket accepted connection.");

        }catch (IOException e){
            Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
        }

        if(socket != null){
            //connected(socket,mmDevice);
        }

        Log.i(TAG, "END AcceptThread ");
    }

    public void cancel() {
        Log.d(TAG, "cancel: Canceling AcceptThread.");
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
        }
    }
}
