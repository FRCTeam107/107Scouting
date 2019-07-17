package com.frc107.scouting.bluetooth;

import android.os.Bundle;

import com.frc107.scouting.ui.BaseActivity;

import androidx.lifecycle.ViewModelProviders;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.frc107.scouting.R;

public class BluetoothSetMasterDeviceActivity extends BaseActivity {
    private BluetoothSetMasterDeviceViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_set_master_device);
        viewModel = ViewModelProviders.of(this).get(BluetoothSetMasterDeviceViewModel.class);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewModel.getDeviceNames());
        ListView deviceNameListView = findViewById(R.id.masterDeviceNameListView);
        deviceNameListView.setAdapter(adapter);

        deviceNameListView.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.setMasterDevice(position);
            finish();
        });
    }
}
