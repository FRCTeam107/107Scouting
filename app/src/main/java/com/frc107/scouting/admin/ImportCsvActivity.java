package com.frc107.scouting.admin;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.form.FormActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImportCsvActivity extends FormActivity {
    private ImportCsvViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_csv_file);
        viewModel = ViewModelProviders.of(this).get(ImportCsvViewModel.class);

        openPurposeDialog();
    }

    enum Purpose {
        IMPORT,
        ANALYZE,
        CONCATENATE
    }

    private void openPurposeDialog() {
        String[] names = new String[] {
                "Import into table",
                "Concatenate",
                "Analyze"
        };

        boolean[] checked = new boolean[] {
                false,
                false,
                false
        };

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("What do you want to do?");
        alertBuilder.setMultiChoiceItems(names, checked, null);
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            dialog.dismiss();
            finish();
        });
        alertBuilder.setPositiveButton("Continue", (dialog, which) -> {
            Purpose purpose = Purpose.values()[which];
            handlePurpose(purpose);
        });
    }

    private void handlePurpose(Purpose purpose) {
        switch (purpose) {
            case IMPORT:
                Uri uri = getIntent().getData();
                if (uri == null)
                    return;

                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    Log.d("Scouting", e.getLocalizedMessage());
                    return;
                }
                viewModel.importCsv(inputStream);
                break;
        }
    }

    /*private Table getTableToImportTo() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}