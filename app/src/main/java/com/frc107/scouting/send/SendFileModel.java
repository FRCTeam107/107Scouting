package com.frc107.scouting.send;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.FileDefinition;
import com.frc107.scouting.Scouting;

import java.util.List;

public class SendFileModel extends ViewModel {
    public List<FileDefinition> getFileDefinitions() {
        return Scouting.FILE_SERVICE.getFileDefinitions();
    }
}
