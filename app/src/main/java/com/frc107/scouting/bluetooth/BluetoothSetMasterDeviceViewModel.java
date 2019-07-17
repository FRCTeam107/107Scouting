package com.frc107.scouting.bluetooth;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class BluetoothSetMasterDeviceViewModel extends AndroidViewModel {
    public BluetoothSetMasterDeviceModel model;

    public BluetoothSetMasterDeviceViewModel(Application application) {
        super(application);
        model = new BluetoothSetMasterDeviceModel();
    }

    public String[] getDeviceNames() {
        return model.getDeviceNames();
    }

    public void setMasterDevice(int index) {
        model.setMasterDevice(index);
    }
}
