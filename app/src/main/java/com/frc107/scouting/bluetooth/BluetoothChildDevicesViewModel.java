package com.frc107.scouting.bluetooth;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class BluetoothChildDevicesViewModel extends AndroidViewModel {
    private BluetoothChildDevicesModel model;

    public BluetoothChildDevicesViewModel(Application application) {
        super(application);
        model = new BluetoothChildDevicesModel();
    }

    public void setDeviceSelection(int i, boolean selected) {
        model.setDeviceSelection(i, selected);
    }

    public boolean isDeviceSelected(int i) {
        return model.isDeviceSelected(i);
    }

    public boolean hasHitLimit() {
        return model.hasHitLimit();
    }

    public String[] getDeviceNames() {
        return model.getDeviceNames();
    }

    public void saveChildDevices() {
        model.saveChildDevices();
    }
}
