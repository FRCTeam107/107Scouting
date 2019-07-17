package com.frc107.scouting.bluetooth;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.frc107.scouting.R;
import com.frc107.scouting.ui.BaseActivity;

public class BluetoothChildDevicesActivity extends BaseActivity {
    private BluetoothChildDevicesViewModel viewModel;
    private ListView deviceNameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_child_devices);
        viewModel = ViewModelProviders.of(this).get(BluetoothChildDevicesViewModel.class);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewModel.getDeviceNames());
        deviceNameListView = findViewById(R.id.childDeviceNameListView);
        deviceNameListView.setAdapter(adapter);

        deviceNameListView.setOnItemClickListener((parent, view, position, id) -> {
            boolean isDeviceSelected = viewModel.isDeviceSelected(position);
            if (isDeviceSelected) {
                view.setBackgroundColor(-1);
                // unfinished and maybe broken
                viewModel.setDeviceSelection(position, false);
                return;
            }

            boolean canSelect = !viewModel.hasHitLimit();
            if (!canSelect)
                return;

            view.setBackgroundColor(Color.LTGRAY);
            viewModel.setDeviceSelection(position, true);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.saveChildDevices();
    }
}
