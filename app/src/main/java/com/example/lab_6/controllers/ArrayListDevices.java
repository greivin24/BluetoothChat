package com.example.lab_6.controllers;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class ArrayListDevices {
    private ArrayList<BluetoothDevice> arrayListDevices;

    public ArrayListDevices() {
        arrayListDevices = new ArrayList<>();
    }

    public ArrayList<BluetoothDevice> getArrayListDevices() {
        return arrayListDevices;
    }

    public void insertInArrayListDevices(BluetoothDevice device){
        if(!checkIsExistDevice(device))
            arrayListDevices.add(device);
    }

    private boolean checkIsExistDevice(BluetoothDevice device){
        for (BluetoothDevice bluetoothDevice: arrayListDevices) {
            if(bluetoothDevice.getName().equals(device.getName())){
                return true;
            }
        }
        return false;
    }

    public void clearArrayListDevices(){
        this.arrayListDevices.clear();
    }

    public BluetoothDevice getArrayListDevicesById(int position){
        return arrayListDevices.get(position);
    }
}
