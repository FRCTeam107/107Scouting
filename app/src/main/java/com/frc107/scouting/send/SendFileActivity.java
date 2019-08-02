package com.frc107.scouting.send;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frc107.scouting.BaseActivity;
import com.frc107.scouting.FileDefinition;
import com.frc107.scouting.R;

import java.util.List;

public class SendFileActivity extends BaseActivity {
    private SendFileModel model;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FileArrayAdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        model = ViewModelProviders.of(this).get(SendFileModel.class);

        recyclerView = findViewById(R.id.send_file_recycler_view);

        List<FileDefinition> fileDefinitions = model.getFileDefinitions();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);

        adapter = new FileArrayAdapterRecyclerView(this, fileDefinitions);
        adapter.setOnItemClickListener(fileDef -> {
            sendFile(fileDef.getFile());
            finish();
        });

        recyclerView.setAdapter(adapter);
    }
}
