package com.frc107.scouting.core.analysis;

import android.content.Intent;
import android.os.Bundle;

import com.frc107.scouting.MainActivity;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.file.SelectFileForAnalysisActivity;
import com.frc107.scouting.core.ui.BaseActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;

import java.util.ArrayList;
import java.util.Arrays;

public class AnalysisActivity extends BaseActivity {
    private AnalysisAdapter listAdapter;

    private Spinner teamNumberSpinner;
    private Spinner attributeSpinner;

    private AnalysisModel model;
    private ArrayList<String> teamNumbers;

    private static final int SELECT_FILE_INTENT_CODE = 8213; // no significance to the number, just a random number that doesn't collide with other possible intent/activity result codes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * todo todo todo do this now
         *
         * ask user what table to analyze, then open file picker
         */

        setContentView(R.layout.activity_attribute_analysis);

        teamNumberSpinner = findViewById(R.id.team_spinner);
        attributeSpinner = findViewById(R.id.attribute_spinner);

        model = ViewModelProviders.of(this).get(AnalysisModel.class);
        model.setCallbacks(this::onDataLoaded);

        Intent intent = new Intent(this, SelectFileForAnalysisActivity.class);
        startActivityForResult(intent, SELECT_FILE_INTENT_CODE);
    }

    private void onDataLoaded(Boolean result) {
        if (Boolean.FALSE.equals(result)) {
            showMessage("Error while loading data. Go talk to the programmers.", Toast.LENGTH_LONG);
            finish();
        }

        initializeUI();
    }

    private void initializeUI() {
        findViewById(R.id.analysisProgressBar).setVisibility(View.GONE);

        String[] attributeNames = model.getAttributeNames();
        ArrayAdapter<String> attributeAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_element, attributeNames);
        attributeSpinner.setAdapter(attributeAdapter);
        attributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setAttributeAndUpdateElements(position);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        teamNumbers = new ArrayList<>();
        teamNumbers.add("All");
        teamNumbers.addAll(Arrays.asList(model.getTeamNumbers()));

        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_element, teamNumbers);
        teamAdapter.notifyDataSetChanged();

        teamNumberSpinner.setAdapter(teamAdapter);
        teamNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setTeamNumberAndUpdateElements(position);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // todo: this should be eventually converted to a recyclerview
        ListView elementListView = findViewById(R.id.elementListView);
        elementListView.setSelectionAfterHeaderView();
        elementListView.setVisibility(View.VISIBLE);

        listAdapter = new AnalysisAdapter(this, model.getElements());
        elementListView.setAdapter(listAdapter);
    }

    private void updateUI() {
        teamNumberSpinner.setSelection(model.getCurrentTeamNumberIndex());
        attributeSpinner.setSelection(model.getCurrentAttributeTypeIndex());

        listAdapter.notifyDataSetChanged();
        listAdapter.sort((element1, element2) -> Double.compare(element2.getAttribute(), element1.getAttribute()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != SELECT_FILE_INTENT_CODE || resultCode != RESULT_OK)
            return;

        String path = data.getStringExtra(ScoutingStrings.FILE_SELECTION_EXTRA_KEY);
        model.setFilePath(path);

        if (!model.hasDataBeenLoaded()) {
            model.loadData();
        } else {
            initializeUI();
            updateUI();
        }
    }
}
