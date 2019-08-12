package com.frc107.scouting.core.csvimport;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;
import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.ScoutingStrings;

import java.io.IOException;
import java.io.InputStream;

public class ImportCsvActivity extends BaseActivity {
    private ImportCsvModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_csv_file);
        model = ViewModelProviders.of(this).get(ImportCsvModel.class);

        Uri uri = getIntent().getData();
        String name = getFileName(uri);

        if (model.doesFileExist(name)) {
            showMessage("File named \"" + name + "\" already exists.", Toast.LENGTH_LONG);
            finish();
            return;
        }

        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            model.copyFile(inputStream, name);
        } catch (IOException e) {
            showMessage(e.getLocalizedMessage(), Toast.LENGTH_LONG);
            Log.d(ScoutingStrings.SCOUTING_TAG, e.getLocalizedMessage());
            return;
        }

        showMessage("Successfully copied \"" + name + "\" to scouting folder.", Toast.LENGTH_LONG);
        finish();
    }

    // https://developer.android.com/training/secure-file-sharing/retrieve-info.html
    private String getFileName(Uri uri) {
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
}