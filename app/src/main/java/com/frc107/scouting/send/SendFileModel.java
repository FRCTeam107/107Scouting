package com.frc107.scouting.send;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.file.FileDefinition;
import com.frc107.scouting.Scouting;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class SendFileModel extends ViewModel {
    private List<FileDefinition> fileDefinitions;

    public SendFileModel() {
        fileDefinitions = Scouting.FILE_SERVICE.getFileDefinitions();

        // sort in descending order
        Collections.sort(fileDefinitions, (fd1, fd2) -> fd2.getDateCreated().compareTo(fd1.getDateCreated()));
    }

    List<FileDefinition> getFileDefinitions() {
        return fileDefinitions;
    }

    File getFile(int position) {
        return fileDefinitions.get(position).getFile();
    }
}
