package com.frc107.scouting.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothManager {
    private BluetoothDevice masterDevice;
    public BluetoothDevice getMasterDevice() {
        return masterDevice;
    }

    public void setMasterDevice(BluetoothDevice masterDevice) {
        this.masterDevice = masterDevice;
    }

    private ArrayList<BluetoothDevice> childDevices;
    public ArrayList<BluetoothDevice> getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(ArrayList<BluetoothDevice> devices) {
        this.childDevices = devices;
    }

    private BluetoothDevice[] pairedDevices;
    public BluetoothDevice[] getPairedDevices() {
        if (pairedDevices == null)
            updatePairedDevices();

        return pairedDevices;
    }

    public void updatePairedDevices() {
        Set<BluetoothDevice> pairedDeviceSet = adapter.getBondedDevices();
        pairedDevices = new BluetoothDevice[pairedDeviceSet.size()];
        pairedDevices = pairedDeviceSet.toArray(pairedDevices);
    }

    private BluetoothAdapter adapter;

    public BluetoothManager() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }
}
