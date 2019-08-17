package com.frc107.scouting.core.file;

import android.content.Intent;
import android.os.Bundle;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.file.selectfile.SelectFileActivity;
import com.frc107.scouting.core.table.eTableType;

import java.util.List;

public class SelectFileForAnalysisActivity extends SelectFileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eTableType tableType = (eTableType) getIntent().getSerializableExtra(ScoutingStrings.TABLE_EXTRA_KEY);

        List<FileDefinition> fileDefinitions = Scouting.getFileService().getFileDefinitionsOfType(tableType);
        if (fileDefinitions.isEmpty()) {
            Intent data = new Intent();
            data.putExtra(ScoutingStrings.FILE_SELECTION_EXTRA_KEY, (String) null);
            setResult(RESULT_OK, data);
            finish();
            return;
        }

        initialize(fileDefinitions, fileDef -> {
            Intent data = new Intent();
            data.putExtra(ScoutingStrings.FILE_SELECTION_EXTRA_KEY, fileDef.getFile().getPath());
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
