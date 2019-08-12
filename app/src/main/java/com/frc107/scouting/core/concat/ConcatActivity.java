package com.frc107.scouting.core.concat;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.ui.FileArrayAdapter;
import com.frc107.scouting.core.utils.StringUtils;

public class ConcatActivity extends BaseActivity {
    private Spinner fileTypeSpinner;
    private ArrayAdapter<String> fileTypeAdapter;
    private RecyclerView availableFilesRecyclerView;
    private FileArrayAdapter availableFilesAdapter;
    private RecyclerView selectedFilesRecyclerView;
    private FileArrayAdapter selectedFilesAdapter;
    private Button concatButton;

    private ConcatModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concat);
        fileTypeSpinner = findViewById(R.id.file_type_spinner);
        availableFilesRecyclerView = findViewById(R.id.available_files_recycler_view);
        selectedFilesRecyclerView = findViewById(R.id.selected_files_recycler_view);
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
        availableFilesAdapter.setOnItemClickListener(position -> model.selectFile(position));
        setupRecyclerView(availableFilesRecyclerView, availableFilesAdapter);

        selectedFilesAdapter = new FileArrayAdapter(this, model.getSelectedFileDefs());
        selectedFilesAdapter.setOnItemClickListener(position -> model.unselectFile(position));
        setupRecyclerView(selectedFilesRecyclerView, selectedFilesAdapter);
    }

    void setupRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
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

    private void concatenateAndFinish() {
        String result = model.concatenate();
        showMessage(result, Toast.LENGTH_LONG);
        finish();
    }
}
