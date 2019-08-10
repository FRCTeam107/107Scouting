package com.frc107.scouting.analysis.attribute;

import android.os.Bundle;

import com.frc107.scouting.ui.BaseActivity;

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

public class AttributeAnalysisActivity extends BaseActivity {
    private AnalysisAdapter listAdapter;
    private ListView elementListView;

    private Spinner teamNumberSpinner;
    private Spinner attributeSpinner;

    private AttributeAnalysisViewModel viewModel;
    private ArrayList<String> teamNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute_analysis);

        teamNumberSpinner = findViewById(R.id.team_spinner);
        attributeSpinner = findViewById(R.id.attribute_spinner);

        viewModel = ViewModelProviders.of(this).get(AttributeAnalysisViewModel.class);
        viewModel.initialize(this::onDataLoaded, this::onDataLoadError);

        if (!viewModel.hasDataBeenLoaded()) {
            viewModel.loadData();
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

        String[] attributeNames = viewModel.getAttributeNames();
        ArrayAdapter<String> attributeAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_element, attributeNames);
        attributeSpinner.setAdapter(attributeAdapter);
        attributeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setAttributeAndUpdateElements(position);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        teamNumbers = new ArrayList<>();
        teamNumbers.add("All");
        teamNumbers.addAll(Arrays.asList(viewModel.getTeamNumbers()));

        ArrayAdapter<String> teamAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_element, teamNumbers);
        teamAdapter.notifyDataSetChanged();

        teamNumberSpinner.setAdapter(teamAdapter);
        teamNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setTeamNumberAndUpdateElements(position);
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        elementListView = findViewById(R.id.elementListView);
        elementListView.setSelectionAfterHeaderView();
        elementListView.setVisibility(View.VISIBLE);

        listAdapter = new AnalysisAdapter(this, viewModel.getElements());
        elementListView.setAdapter(listAdapter);
    }

    private void updateUI() {
        teamNumberSpinner.setSelection(viewModel.getCurrentTeamNumberIndex());
        attributeSpinner.setSelection(viewModel.getCurrentAttributeType());

        listAdapter.notifyDataSetChanged();
        listAdapter.sort((element1, element2) -> Double.compare(element2.getAttribute(), element1.getAttribute()));
    }
}
