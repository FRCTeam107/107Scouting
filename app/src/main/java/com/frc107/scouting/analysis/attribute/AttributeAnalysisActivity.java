package com.frc107.scouting.analysis.attribute;

import android.app.AlertDialog;
import android.os.Bundle;

import com.frc107.scouting.BaseActivity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;

public class AttributeAnalysisActivity extends BaseActivity {
    private AnalysisAdapter adapter;
    private ListView elementListView;
    private TextView attributeTypeTextView;
    private AttributeAnalysisViewModel viewModel;

    private static final String[] ATTRIBUTE_TYPES = new String[]{
            "Average Cargo",
            "Average Hatch Panel",
            "Average Cargo Ship",
            "Average Rocket Level 1",
            "Average Rocket Level 2",
            "Average Rocket Level 3",
            "Hab 2 Climb Amount",
            "Hab 3 Climb Amount",
            "Successful Defense Amount",
            "OPR",
            "DPR"
    };

    private static final String CURRENT_ATTRIBUTE_KEY = "CURRENT_ATTRIBUTE_KEY";

    private int currentAttributeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute_analysis);

        viewModel = ViewModelProviders.of(this).get(AttributeAnalysisViewModel.class);

        adapter = new AnalysisAdapter(this, viewModel.getElements());
        elementListView = findViewById(R.id.elementListView);
        elementListView.setAdapter(adapter);

        attributeTypeTextView = findViewById(R.id.attributeTypeTextView);

        // These don't run when onCreate is called.
        viewModel.getDataLoadedLiveData().observe(this, this::onDataLoaded);
        viewModel.getAttributeLiveData().observe(this, this::onAnalysisElementsUpdated);

        if (viewModel.isDataLoaded())
            setAttributeType(viewModel.getCurrentAttributeType());
        else
            viewModel.loadData();
    }

    private void onAnalysisElementsUpdated(int attribute) {
        adapter.notifyDataSetChanged();
        findViewById(R.id.analysisProgressBar).setVisibility(View.GONE);

        elementListView.setSelectionAfterHeaderView();
        elementListView.setVisibility(View.VISIBLE);

        currentAttributeType = attribute;
    }

    private void onDataLoaded(boolean dataLoaded) {
        if (!dataLoaded)
            return;

        setAttributeType(currentAttributeType);
    }

    private void setAttributeType(int type) {
        viewModel.setAttributeAndUpdateElements(type);
        attributeTypeTextView.setText(ATTRIBUTE_TYPES[type]);
    }

    public void attributeButtonPressed(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Pick an attribute");
        dialogBuilder.setItems(ATTRIBUTE_TYPES, (dialog, which) -> setAttributeType(which));
        dialogBuilder.show();
    }
}
