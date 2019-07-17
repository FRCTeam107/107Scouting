package com.frc107.scouting.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.frc107.scouting.Scouting;

import java.util.ArrayList;

public class BluetoothChildDevicesModel {
    private BluetoothManager bluetoothManager;
    private String[] deviceNameArray;
    private BluetoothDevice[] deviceArray;
    private boolean[] selectedArray;

    public BluetoothChildDevicesModel() {
        bluetoothManager = Scouting.getInstance().getBluetoothManager();

        deviceArray = bluetoothManager.getPairedDevices();
        selectedArray = new boolean[deviceArray.length];
        deviceNameArray = new String[deviceArray.length];
        for (int i = 0; i < deviceArray.length; i++) {
            deviceNameArray[i] = deviceArray[i].getName();
        }
    }

    public String[] getDeviceNames() {
        return deviceNameArray;
    }

    public void setDeviceSelection(int i, boolean selected) {
        selectedArray[i] = selected;
    }

    public boolean isDeviceSelected(int i) {
        return selectedArray[i];
    }

    public boolean hasHitLimit() {
        return getSelectedDevices().size() >= 6;
    }

    private ArrayList<BluetoothDevice> getSelectedDevices() {
        ArrayList<BluetoothDevice> childDevices = new ArrayList<>();
        for (int i = 0; i < deviceArray.length; i++) {
            if (!selectedArray[i])
                continue;

            childDevices.add(deviceArray[i]);
        }
        return childDevices;
    }

    public void saveChildDevices() {
        ArrayList<BluetoothDevice> childDevices = getSelectedDevices();
        Scouting.getInstance().getBluetoothManager().setChildDevices(childDevices);
    }
}
