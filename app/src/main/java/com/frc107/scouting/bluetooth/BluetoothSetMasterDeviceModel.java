package com.frc107.scouting.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.frc107.scouting.Scouting;

public class BluetoothSetMasterDeviceModel {
    private BluetoothManager bluetoothManager;
    private String[] deviceNameArray;
    private BluetoothDevice[] deviceArray;

    public BluetoothSetMasterDeviceModel() {
        bluetoothManager = Scouting.getInstance().getBluetoothManager();
        deviceNameArray = new String[bluetoothManager.getPairedDevices().length];
        deviceArray = new BluetoothDevice[deviceNameArray.length];
        populateDeviceNames();
    }

    private void populateDeviceNames() {
        BluetoothDevice[] devices = bluetoothManager.getPairedDevices();
        for (int i = 0; i < devices.length; i++) {
            deviceNameArray[i] = devices[i].getName();
            deviceArray[i] = devices[i];
        }
    }

    public void setMasterDevice(int index) {
        BluetoothDevice device = deviceArray[index];
        Scouting.getInstance().getBluetoothManager().setMasterDevice(device);
    }

    public String[] getDeviceNames() {
        return deviceNameArray;
    }
}
