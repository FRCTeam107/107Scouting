package com.frc107.scouting.admin;

import android.os.Bundle;

import com.frc107.scouting.R;
import com.frc107.scouting.form.FormActivity;

public class OpenCsvFileActivity extends FormActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_csv_file);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}