package com.frc107.scouting.core.analysis;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.frc107.scouting.R;

public class GraphFragment extends Fragment {
    private AnalysisModel model;
    private GraphView graphView;

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
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        graphView = view.findViewById(R.id.graph_view);
        return view;
    }

    private void updateUI() {
        if (graphView == null)
            return;

        String attributeName = model.getAttributeNames()[model.getCurrentAttributeTypeIndex()];
        graphView.setSource(model.getMatchNumAttributeMap(), "Match number", attributeName);
    }
}
