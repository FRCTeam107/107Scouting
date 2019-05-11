package com.frc107.scouting.analysis.attribute;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.frc107.scouting.ui.BaseActivity;
import com.frc107.scouting.ui.IUIListener;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;

import java.util.ArrayList;

public class AttributeAnalysisActivity extends BaseActivity {
    private AnalysisAdapter adapter;
    private ListView elementListView;
    private TextView attributeTypeTextView;
    private AttributeAnalysisViewModel viewModel;
    private int currentAttributeType = -1;

    private static final String CURRENT_ATTRIBUTE = "currentAttribute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute_analysis);

        viewModel = ViewModelProviders.of(this).get(AttributeAnalysisViewModel.class);
        viewModel.getElementsLiveData().observe(this, analysisElements -> {
            adapter.notifyDataSetChanged();
            elementListView.setSelectionAfterHeaderView();

            if (currentAttributeType == -1)
                setAttributeType(0);

            findViewById(R.id.analysisProgressBar).setVisibility(View.GONE);
            elementListView.setVisibility(View.VISIBLE);
        });

        elementListView = findViewById(R.id.elementListView);

        adapter = new AnalysisAdapter(this, viewModel.getElementsLiveData());
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

    private void setAttributeType(int type) {
        currentAttributeType = type;
        viewModel.setAttribute(type);
        attributeTypeTextView.setText(viewModel.getCurrentAttributeTypeName());
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
