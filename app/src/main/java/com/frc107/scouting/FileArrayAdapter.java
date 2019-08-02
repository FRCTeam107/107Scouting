package com.frc107.scouting;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FileArrayAdapter extends ArrayAdapter<FileDefinition> {
    private List<FileDefinition> elements = new ArrayList<>();

    public FileArrayAdapter(Context context, List<FileDefinition> elements) {
        super(context, R.layout.layout_file_item, elements);
        this.elements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public FileDefinition getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_file_item, parent, false);

        FileDefinition element = getItem(position);
        if (element == null)
            return convertView;

        TextView fileTypeView = convertView.findViewById(R.id.file_type_view);
        TextView creatorInitialsView = convertView.findViewById(R.id.creator_initials_view);
        TextView dateTextView = convertView.findViewById(R.id.date_created_view);

        fileTypeView.setText(element.getType());
        creatorInitialsView.setText(element.getInitials());

        String date = DateFormat.format("h:mm A, MMM d", element.getDateCreated()).toString();
        dateTextView.setText(date);

        return convertView;
    }
}
