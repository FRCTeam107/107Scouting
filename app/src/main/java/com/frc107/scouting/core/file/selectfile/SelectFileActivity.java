package com.frc107.scouting.core.file.selectfile;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.core.ui.FileArrayAdapter;
import com.frc107.scouting.core.file.FileDefinition;
import com.frc107.scouting.R;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

import java.util.List;

/**
 * Don't use this class. Instead, create a child class with specific behaviors. Call initialize in onCreate.
 */
public class SelectFileActivity extends BaseActivity {
    private SelectFileModel model;

    protected void initialize(List<FileDefinition> fileDefinitions, ICallbackWithParam<FileDefinition> onFileSelected) {
        model.initialize(fileDefinitions);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.send_file_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);

        FileArrayAdapter adapter = new FileArrayAdapter(this, fileDefinitions);
        adapter.setOnItemClickListener(position -> {
            onFileSelected.call(model.getFile(position));
            finish();
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        model = ViewModelProviders.of(this).get(SelectFileModel.class);
    }
}
