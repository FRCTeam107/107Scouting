package com.frc107.scouting.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.utils.BluetoothService;

public class BluetoothSetMasterDeviceModel {
    private BluetoothService bluetoothService;
    private String[] deviceNameArray;
    private BluetoothDevice[] deviceArray;

    public BluetoothSetMasterDeviceModel() {
        bluetoothService = Scouting.getInstance().getBluetoothService();
        deviceNameArray = new String[bluetoothService.getPairedDevices().length];
        deviceArray = new BluetoothDevice[deviceNameArray.length];
        populateDeviceNames();
    }

    private void populateDeviceNames() {
        BluetoothDevice[] devices = bluetoothService.getPairedDevices();
        for (int i = 0; i < devices.length; i++) {
            deviceNameArray[i] = devices[i].getName();
            deviceArray[i] = devices[i];
        }
    }

    public void setMasterDevice(int index) {
        BluetoothDevice device = deviceArray[index];
        Scouting.getInstance().getBluetoothService().setMasterDevice(device);
    }

    public String[] getDeviceNames() {
        return deviceNameArray;
    }
}
