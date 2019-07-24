package com.frc107.scouting.admin;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.frc107.scouting.R;
import com.frc107.scouting.form.FormActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OpenCsvFileActivity extends FormActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_csv_file);

        Uri uri = getIntent().getData();
        loadCsvFile(uri);
    }

    private void loadCsvFile(Uri uri) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(getContentResolver().openInputStream(uri));
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            ArrayList<String> lines;
            lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            // TODO: ask users what form to load this as, and try to load it, move it to the Scouting folder
        } catch (IOException e) {
            Log.d("Scouting", e.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}