package com.frc107.scouting.analysis.attribute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.frc107.scouting.R;

import java.util.ArrayList;

public class AnalysisAdapter extends ArrayAdapter<AnalysisElement> {
    private MutableLiveData<ArrayList<AnalysisElement>> elements;

    AnalysisAdapter(Context context, MutableLiveData<ArrayList<AnalysisElement>> elements) {
        super(context, 0, elements.getValue());
        this.elements = elements;
    }

    @Override
    public int getCount() {
        if (elements.getValue() == null)
            return 0;

        return elements.getValue().size();
    }

    @Override
    public AnalysisElement getItem(int position) {
        if (elements.getValue() == null)
            return null;

        return elements.getValue().get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnalysisElement element = getItem(position);
        if (element == null)
            return convertView;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_analysis_element, parent, false);
        }

        TextView teamNumTextView = convertView.findViewById(R.id.elementTeamNumTextView);
        TextView elementTextView = convertView.findViewById(R.id.elementAttributeTextView);

        teamNumTextView.setText(element.getTeamNumber());
        elementTextView.setText(String.valueOf(element.getAttribute()));

        return convertView;
    }

}
