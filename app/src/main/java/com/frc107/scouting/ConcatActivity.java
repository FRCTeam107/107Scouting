package com.frc107.scouting;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.utils.StringUtils;

import java.util.ArrayList;

public class ConcatActivity extends BaseActivity {
    private Spinner fileTypeSpinner;
    private ArrayAdapter<String> fileTypeAdapter;
    private ListView availableFilesListView;
    private FileArrayAdapter availableFilesAdapter;
    private ListView selectedFilesListView;
    private FileArrayAdapter selectedFilesAdapter;
    private Button concatButton;

    private ConcatModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concat);
        fileTypeSpinner = findViewById(R.id.file_type_spinner);
        availableFilesListView = findViewById(R.id.available_files_list_view);
        selectedFilesListView = findViewById(R.id.selected_files_list_view);
        concatButton = findViewById(R.id.concat_button);

        model = ViewModelProviders.of(this).get(ConcatModel.class);
        model.initialize(this::refresh);

        fileTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, model.getConcatOptions());
        fileTypeSpinner.setAdapter(fileTypeAdapter);
        fileTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.selectConcatOption(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        availableFilesAdapter = new FileArrayAdapter(this, model.getAvailableFileDefs());
        availableFilesListView.setAdapter(availableFilesAdapter);
        availableFilesListView.setOnItemClickListener((parent, view, position, id) -> model.selectFile(position));

        selectedFilesAdapter = new FileArrayAdapter(this, model.getSelectedFileDefs());
        selectedFilesListView.setAdapter(selectedFilesAdapter);
        selectedFilesListView.setOnItemClickListener((parent, view, position, id) -> model.unselectFile(position));
    }

    private void refresh() {
        availableFilesAdapter.notifyDataSetChanged();
        selectedFilesAdapter.notifyDataSetChanged();
        concatButton.setEnabled(model.userHasSelectedFiles());
    }

    public void onConcatButtonTapped(View view) {
        if (!StringUtils.isEmptyOrNull(Scouting.getInstance().getUserInitials())) {
            concatenateAndFinish();
            return;
        }

        showInitialsDialog(this::concatenateAndFinish);
    }

    void concatenateAndFinish() {
        String result = model.concatenate();
        showMessage(result, Toast.LENGTH_LONG);
        finish();
    }
}
