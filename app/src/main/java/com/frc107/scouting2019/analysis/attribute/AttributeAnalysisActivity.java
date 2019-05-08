package com.frc107.scouting2019.analysis.attribute;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.frc107.scouting2019.ui.BaseActivity;
import com.frc107.scouting2019.ui.IUIListener;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.frc107.scouting2019.R;

public class AttributeAnalysisActivity extends BaseActivity implements IUIListener {
    private AnalysisAdapter adapter;
    private ListView elementListView;
    private TextView attributeTypeTextView;
    private AttributeAnalysisViewModel viewModel;
    private int currentAttributeType;

    private static final String CURRENT_ATTRIBUTE = "currentAttribute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute_analysis);
        viewModel = new AttributeAnalysisViewModel(this);

        elementListView = findViewById(R.id.elementListView);

        adapter = new AnalysisAdapter(this, viewModel.getElements());
        elementListView.setAdapter(adapter);

        attributeTypeTextView = findViewById(R.id.attributeTypeTextView);

        viewModel.loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_ATTRIBUTE, viewModel.getCurrentAttributeType());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentAttributeType = savedInstanceState.getInt(CURRENT_ATTRIBUTE);
    }

    @Override
    public void callback(boolean error) {
        if (error) {
            Toast.makeText(getApplicationContext(), "Error while loading data.", Toast.LENGTH_LONG).show();
            return;
        }

        findViewById(R.id.analysisProgressBar).setVisibility(View.GONE);
        elementListView.setVisibility(View.VISIBLE);
        setAttributeType(currentAttributeType);
    }

    private void setAttributeType(int type) {
        currentAttributeType = type;
        viewModel.setAttribute(type);
        attributeTypeTextView.setText(viewModel.getCurrentAttributeTypeName());
        adapter.notifyDataSetChanged();
        elementListView.setSelectionAfterHeaderView();
    }

    public void attributeButtonPressed(View view) {
        String[] attributeTypes = viewModel.getAttributeTypes();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Pick an attribute");
        dialogBuilder.setItems(attributeTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAttributeType(which);
            }
        });
        dialogBuilder.show();
    }
}
