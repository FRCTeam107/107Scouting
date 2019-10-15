package com.frc107.scouting.core.ui;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frc107.scouting.core.file.FileDefinition;
import com.frc107.scouting.R;

import java.util.List;

public class FileArrayAdapter extends RecyclerView.Adapter<FileArrayAdapter.FileViewHolder> {
    private List<FileDefinition> fileDefinitions;
    private LayoutInflater layoutInflater;
    private OnItemClickListener clickListener;

    public FileArrayAdapter(Context context, List<FileDefinition> fileDefinitions) {
        layoutInflater = LayoutInflater.from(context);
        this.fileDefinitions = fileDefinitions;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_file_item, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileDefinition fileDefinition = fileDefinitions.get(position);
        holder.bind(fileDefinition, position);
    }

    @Override
    public int getItemCount() {
        return fileDefinitions.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView fileTypeView;
        private TextView creatorInitialsView;
        private TextView dateTextView;

        FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileTypeView = itemView.findViewById(R.id.file_type_view);
            creatorInitialsView = itemView.findViewById(R.id.creator_initials_view);
            dateTextView = itemView.findViewById(R.id.date_created_view);
        }

        void bind(FileDefinition fileDefinition, int position) {
            fileTypeView.setText(fileDefinition.getType());
            creatorInitialsView.setText(fileDefinition.getInitials());

            String date = DateFormat.format("h:mm A, MMM d", fileDefinition.getDateCreated()).toString();
            dateTextView.setText(date);

            itemView.setOnClickListener(view -> {
                if (clickListener != null)
                    clickListener.onItemClick(position);
            });
        }
    }
}
