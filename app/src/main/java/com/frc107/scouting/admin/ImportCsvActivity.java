package com.frc107.scouting.admin;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.form.FormActivity;
import com.frc107.scouting.form.Table;

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

    enum ePurpose {
        IMPORT,
        ANALYZE,
        CONCATENATE
    }

    private ePurpose selectedPurpose;
    private void openPurposeDialog() {
        String[] names = new String[] {
                "Import into table",
                "Concatenate",
                "Analyze"
        };

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("What do you want to do?");
        alertBuilder.setSingleChoiceItems(names, -1, ((dialog, which) -> {
            selectedPurpose = ePurpose.values()[which];
        }));
        alertBuilder.setPositiveButton("Continue", (dialog, which) -> {
            dialog.dismiss();
            handlePurpose(selectedPurpose);
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        alertBuilder.setOnCancelListener(dialog -> {
            dialog.dismiss();
            finish();
        });
        alertBuilder.show();
    }

    private void handlePurpose(ePurpose purpose) {
        switch (purpose) {
            case IMPORT:
                openTableDialog();
                break;
        }
    }

    private Scouting.eTable selectedTableType;
    private void openTableDialog() {
        Scouting.eTable[] tableTypes = Scouting.eTable.values();
        String[] names = new String[tableTypes.length];
        for (int i = 0; i < tableTypes.length; i++) {
            Table table = Scouting.getInstance().getTable(tableTypes[i]);
            names[i] = table.getName();
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setSingleChoiceItems(names, -1, (dialog, which) -> {
            selectedTableType = tableTypes[which];
        });
        alertBuilder.setPositiveButton("Continue", (dialog, which) -> {
            dialog.dismiss();
            importCsvFile(selectedTableType);
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        alertBuilder.setOnCancelListener(dialog -> {
            dialog.dismiss();
            finish();
        });
        alertBuilder.show();
    }

    private void importCsvFile(Scouting.eTable targetTable) {
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
        viewModel.importCsv(targetTable, inputStream);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}