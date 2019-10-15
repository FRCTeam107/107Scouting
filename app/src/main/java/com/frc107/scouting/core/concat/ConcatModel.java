package com.frc107.scouting.core.concat;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.file.FileDefinition;
import com.frc107.scouting.core.utils.callbacks.ICallback;
import com.frc107.scouting.eTableType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConcatModel extends ViewModel {
    private ICallback refreshUI;
    private ArrayList<FileDefinition> availableFileDefs = new ArrayList<>();
    private ArrayList<FileDefinition> selectedFileDefs = new ArrayList<>();
    private ArrayList<String> availableFileNames = new ArrayList<>();
    private ArrayList<String> selectedFileNames = new ArrayList<>();

    private eTableType[] tableTypes;
    private String[] tableTypeNames;

    void initialize(ICallback refreshUI) {
        this.refreshUI = refreshUI;

        tableTypes = eTableType.values();
        tableTypeNames = new String[tableTypes.length];
        for (int i = 0; i < tableTypes.length; i++) {
            tableTypeNames[i] = tableTypes[i].toString();
        }
    }

    String[] getConcatOptions() {
        return tableTypeNames;
    }

    private eTableType tableType;
    void selectConcatOption(int position) {
        tableType = tableTypes[position];

        availableFileDefs.clear();

        // Since we're concatenating files, we don't want to be able to concatenate files that are concatenations.
        availableFileDefs.addAll(Scouting.getFileService().getFileDefinitionsOfType(tableType, false));
        availableFileNames.clear();

        selectedFileNames.clear();
        selectedFileDefs.clear();

        for (FileDefinition fileDefinition : availableFileDefs) {
            String name = fileDefinition.getFile().getName();
            availableFileNames.add(name);
        }

        refreshUI.call();
    }

    void selectFile(int position) {
        if (availableFileDefs.isEmpty())
            return;

        String fileName = availableFileNames.get(position);
        FileDefinition fileDef = availableFileDefs.get(position);

        availableFileNames.remove(fileName);
        availableFileDefs.remove(fileDef);
        selectedFileNames.add(fileName);
        selectedFileDefs.add(fileDef);

        refreshUI.call();
    }

    void unselectFile(int position) {
        if (selectedFileDefs.isEmpty())
            return;

        String fileName = selectedFileNames.get(position);
        FileDefinition fileDef = selectedFileDefs.get(position);

        selectedFileNames.remove(fileName);
        selectedFileDefs.remove(fileDef);
        availableFileNames.add(fileName);
        availableFileDefs.add(fileDef);

        refreshUI.call();
    }

    ArrayList<FileDefinition> getAvailableFileDefs() {
        return availableFileDefs;
    }

    ArrayList<FileDefinition> getSelectedFileDefs() {
        return selectedFileDefs;
    }

    ArrayList<String> getAvailableFileNames() {
        return availableFileNames;
    }

    ArrayList<String> getSelectedFileNames() {
        return selectedFileNames;
    }

    boolean userHasSelectedFiles() {
        return !selectedFileDefs.isEmpty();
    }

    /**
     * @return Either a success message or an error.
     */
    String concatenate() {
        File[] files = new File[selectedFileDefs.size()];
        for (int i = 0; i < files.length; i++) {
            files[i] = selectedFileDefs.get(i).getFile();
        }

        try {
            File file = Scouting.getFileService().concatenateFiles(tableType, files);
            return "Concatenated to " + file.getName();
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
    }
}
