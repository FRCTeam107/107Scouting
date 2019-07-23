package com.frc107.scouting.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import com.frc107.scouting.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {
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

    private static final UUID DEVICE_UUID = UUID.fromString("600d3b7a-bdef-45ec-bd6a-695cebb4cea9");

    /**
     * StackOverflow link: https://stackoverflow.com/questions/9976050/how-to-send-files-to-all-the-connected-devices-in-android-using-bluetooth
     * BluetoothShare.java: https://android.googlesource.com/platform/packages/apps/Bluetooth/+/froyo/src/com/android/bluetooth/opp/BluetoothShare.java
     */
    private static final String URI = "uri";
    private static final String DESTINATION = "destination";
    private static final String DIRECTION = "direction";
    private static final String TIMESTAMP = "timestamp";
    private static final int DIRECTION_OUTBOUND = 0;
    private static final Uri CONTENT_URI = Uri.parse("content://com.android.bluetooth.opp/btopp");
    public void sendFileToMasterDevice(Context context, File file) {
        ContentValues values = new ContentValues();
        values.put(URI, Uri.fromFile(file).toString());
        values.put(DESTINATION, masterDevice.getAddress());
        values.put(DIRECTION, DIRECTION_OUTBOUND);
        Long ts = System.currentTimeMillis();
        values.put(TIMESTAMP, ts);
        Uri contentUri = context.getContentResolver().insert(CONTENT_URI, values);

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                    //now file transmitting has finished, can do something to the file
                    //if you know the file name, better to check if the file is actually there
                    // - make sure this disconnection not initiated by any other reason.
                    String fileName = "Session-8-Ben-Kyle-21.jpg";
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(mReceiver, filter);
    }

    private BluetoothAdapter adapter;

    public BluetoothService() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }
}
