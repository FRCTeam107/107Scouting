package com.frc107.scouting.core.analysis;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;

public class ChartFragment extends Fragment {
    private AnalysisAdapter listAdapter;
    private AnalysisModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new IllegalStateException("why is activity null?");

        model = ViewModelProviders.of(getActivity()).get(AnalysisModel.class);
        model.addOnElementsUpdatedCallback(this::updateUI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        initializeUI(view);
        return view;
    }

    private void initializeUI(View view) {
        ListView elementListView = view.findViewById(R.id.element_list_view);
        elementListView.setSelectionAfterHeaderView();

        listAdapter = new AnalysisAdapter(getActivity(), model.getChartElements());
        elementListView.setAdapter(listAdapter);

        updateUI();
    }

    private void updateUI() {
        if (listAdapter == null)
            return;

        listAdapter.notifyDataSetChanged();
    }
}
