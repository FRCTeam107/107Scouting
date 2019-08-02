package com.frc107.scouting.send;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frc107.scouting.FileDefinition;
import com.frc107.scouting.R;

import java.util.List;

public class FileArrayAdapterRecyclerView extends RecyclerView.Adapter<FileArrayAdapterRecyclerView.FileViewHolder> {
    private List<FileDefinition> fileDefinitions;
    private LayoutInflater layoutInflater;
    private OnItemClickListener clickListener;

    public FileArrayAdapterRecyclerView(Context context, List<FileDefinition> fileDefinitions) {
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
        holder.bind(fileDefinition);
    }

    @Override
    public int getItemCount() {
        return fileDefinitions.size();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(FileDefinition fileDefinition);
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView fileTypeView;
        private TextView creatorInitialsView;
        private TextView dateTextView;
        private FileDefinition fileDefinition;

        FileViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            fileTypeView = itemView.findViewById(R.id.file_type_view);
            creatorInitialsView = itemView.findViewById(R.id.creator_initials_view);
            dateTextView = itemView.findViewById(R.id.date_created_view);
        }

        void bind(FileDefinition fileDefinition) {
            this.fileDefinition = fileDefinition;

            fileTypeView.setText(fileDefinition.getType());
            creatorInitialsView.setText(fileDefinition.getInitials());

            String date = DateFormat.format("h:mm A, MMM d", fileDefinition.getDateCreated()).toString();
            dateTextView.setText(date);

            itemView.setOnClickListener(view -> {
                if (clickListener != null)
                    clickListener.onItemClick(this.fileDefinition);
            });
        }
    }
}
