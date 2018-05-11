package com.example.lab_6.controllers;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ConnectedThread(BluetoothSocket bSocket, ProgressDialog progressDialog) {
        Log.d(TAG, "ConnectedThread: Starting.");
        this.bluetoothSocket = bSocket;
        InputStream temInputStream = null;
        OutputStream temOutputStream = null;

        try {
            progressDialog.dismiss();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        try {
            temInputStream = bluetoothSocket.getInputStream();
            temOutputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = temInputStream;
        outputStream = temOutputStream;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true){
            // Read from the InputStream
            try {
                bytes = inputStream.read(buffer);
                String incomingMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "InputStream: " + incomingMessage);
            } catch (IOException e) {
                Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                break;
            }
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) { }
    }

    //Call this from the main activity to send data to the remote device
    public void write(byte[] bytes) {
        String text = new String(bytes, Charset.defaultCharset());
        Log.d(TAG, "write: Writing to outputstream: " + text);
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
        }
    }


}
