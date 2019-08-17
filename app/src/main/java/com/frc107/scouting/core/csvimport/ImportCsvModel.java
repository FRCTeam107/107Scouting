package com.frc107.scouting.core.csvimport;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.file.FileService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ImportCsvModel extends ViewModel {
    private static FileService fileService = Scouting.getFileService();

    boolean doesFileExist(String name) {
        return fileService.doesFileExist(name);
    }

    void copyFile(InputStream inputStream, String name) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                builder.append(ScoutingStrings.NEW_LINE);
                line = reader.readLine();
            }
        }

        File targetFile = new File(fileService.getScoutingDirectory(), name);
        if (targetFile.exists())
            throw new IOException("File already exists! Path: " + targetFile.getPath());

        fileService.createAndWriteToNewFileCore(targetFile.getParentFile(), targetFile.getName(), builder.toString());
    }
}
