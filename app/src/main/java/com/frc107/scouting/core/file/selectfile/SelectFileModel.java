package com.frc107.scouting.core.file.selectfile;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.core.file.FileDefinition;

import java.util.Collections;
import java.util.List;

public class SelectFileModel extends ViewModel {
    private List<FileDefinition> fileDefinitions;

    void initialize(List<FileDefinition> fileDefinitions) {
        this.fileDefinitions = fileDefinitions;

        // sort in descending order
        Collections.sort(fileDefinitions, (fd1, fd2) -> fd2.getDateCreated().compareTo(fd1.getDateCreated()));
    }

    List<FileDefinition> getFileDefinitions() {
        return fileDefinitions;
    }

    FileDefinition getFile(int position) {
        return fileDefinitions.get(position);
    }
}
