package com.frc107.scouting.core.analysis;

import android.os.Bundle;

import com.frc107.scouting.core.ui.BaseActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * todo todo todo do this now
         *
         * ask user what table to analyze, and then what file
         *
         * load data once an option has been chosen in the popup
         */

        setContentView(R.layout.activity_attribute_analysis);

        teamNumberSpinner = findViewById(R.id.team_spinner);
        attributeSpinner = findViewById(R.id.attribute_spinner);

        model = ViewModelProviders.of(this).get(AnalysisModel.class);
        model.setCallbacks(this::onDataLoaded, this::onDataLoadError);

        if (!model.hasDataBeenLoaded()) {
            model.loadData();
        } else {
            initializeUI();
            updateUI();
        }
    }

    private void onDataLoadError(String error) {
        showMessage(error, Toast.LENGTH_LONG);
        finish();
    }

    private void onDataLoaded() {
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
}
