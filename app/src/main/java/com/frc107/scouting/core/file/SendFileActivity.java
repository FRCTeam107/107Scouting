package com.frc107.scouting.core.file;

import android.os.Bundle;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.file.selectfile.SelectFileActivity;

import java.util.List;

public class SendFileActivity extends SelectFileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<FileDefinition> fileDefinitions = Scouting.getFileService().getAllFileDefinitions();
        initialize(fileDefinitions, fileDef -> sendFile(fileDef.getFile()));
    }
}
