package com.frc107.vanguard.core.file;

import android.os.Bundle;

import com.frc107.vanguard.Vanguard;
import com.frc107.vanguard.core.file.selectfile.SelectFileActivity;

import java.util.List;

public class SendFileActivity extends SelectFileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<FileDefinition> fileDefinitions = Vanguard.getFileService().getAllFileDefinitions();
        initialize(fileDefinitions, fileDef -> sendFile(fileDef.getFile()));
    }
}
