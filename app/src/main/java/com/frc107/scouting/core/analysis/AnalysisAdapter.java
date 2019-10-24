package com.frc107.scouting.core.analysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.databinding.ObservableList;

import com.frc107.scouting.R;

import java.util.List;

public class AnalysisAdapter extends ArrayAdapter<ChartElement> {
    private List<ChartElement> elements;

    AnalysisAdapter(Context context, ObservableList<ChartElement> elements) {
        super(context, 0, elements);
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public ChartElement getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChartElement element = getItem(position);
        if (element == null)
            return convertView;

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_analysis_element, parent, false);

        TextView teamNumTextView = convertView.findViewById(R.id.elementTeamNumTextView);
        TextView elementTextView = convertView.findViewById(R.id.elementAttributeTextView);

        teamNumTextView.setText(element.getTeamNumber());
        elementTextView.setText(String.valueOf(element.getAttribute()));

        return convertView;
    }
}
