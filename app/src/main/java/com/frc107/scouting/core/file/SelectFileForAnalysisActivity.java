package com.frc107.scouting.core.file;

import android.content.Intent;
import android.os.Bundle;

import com.frc107.scouting.core.file.selectfile.SelectFileActivity;

public class SelectFileForAnalysisActivity extends SelectFileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(null, fileDef -> {
            Intent data = new Intent();
            data.putExtra(FileConstants.FILE_SELECTION_EXTRA, fileDef.getFile().getPath());
            setResult(RESULT_OK, data);
        });
    }
}
