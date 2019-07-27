package com.frc107.scouting.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.frc107.scouting.R;
import com.frc107.scouting.BaseActivity;

public class BluetoothActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }

    public void chooseChildDevices(View view) {
        Intent intent = new Intent(getApplicationContext(), BluetoothChildDevicesActivity.class);
        startActivity(intent);
    }

    public void chooseMasterDevice(View view) {
        Intent intent = new Intent(getApplicationContext(), BluetoothSetMasterDeviceActivity.class);
        startActivity(intent);
    }
}
