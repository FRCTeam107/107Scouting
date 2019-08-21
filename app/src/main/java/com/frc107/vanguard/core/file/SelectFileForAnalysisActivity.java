package com.frc107.vanguard.core.file;

import android.content.Intent;
import android.os.Bundle;

import com.frc107.vanguard.Vanguard;
import com.frc107.vanguard.core.VanguardStrings;
import com.frc107.vanguard.core.file.selectfile.SelectFileActivity;
import com.frc107.vanguard.eTableType;

import java.util.List;

public class SelectFileForAnalysisActivity extends SelectFileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eTableType tableType = (eTableType) getIntent().getSerializableExtra(VanguardStrings.TABLE_EXTRA_KEY);

        List<FileDefinition> fileDefinitions = Vanguard.getFileService().getFileDefinitionsOfType(tableType);
        if (fileDefinitions.isEmpty()) {
            Intent data = new Intent();
            data.putExtra(VanguardStrings.FILE_SELECTION_EXTRA_KEY, (String) null);
            setResult(RESULT_OK, data);
            finish();
            return;
        }

        initialize(fileDefinitions, fileDef -> {
            Intent data = new Intent();
            data.putExtra(VanguardStrings.FILE_SELECTION_EXTRA_KEY, fileDef.getFile().getPath());
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
