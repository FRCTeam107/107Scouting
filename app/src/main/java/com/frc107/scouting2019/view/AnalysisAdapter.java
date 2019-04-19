package com.frc107.scouting2019.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frc107.scouting2019.R;
import com.frc107.scouting2019.model.AnalysisElement;

import java.util.ArrayList;

public class AnalysisAdapter extends ArrayAdapter<AnalysisElement> {
    private ArrayList<AnalysisElement> elements;

    public AnalysisAdapter(Context context, ArrayList<AnalysisElement> elements) {
        super(context, 0, elements);
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public AnalysisElement getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnalysisElement element = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_analysis_element, parent, false);
        }

        TextView teamNumTextView = convertView.findViewById(R.id.elementTeamNumTextView);
        TextView elementTextView = convertView.findViewById(R.id.elementAttributeTextView);

        teamNumTextView.setText(element.getTeamNumber());
        elementTextView.setText(element.getAttribute());

        return convertView;
    }

}
