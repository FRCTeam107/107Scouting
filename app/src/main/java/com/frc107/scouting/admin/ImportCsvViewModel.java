package com.frc107.scouting.admin;

import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.io.InputStream;

public class ImportCsvViewModel extends ViewModel {
    private ImportCsvModel model = new ImportCsvModel();

    boolean doesFileExist(String name) {
        return model.doesFileExist(name);
    }

    void copyFile(InputStream inputStream, String name) throws IOException {
        model.copyFile(inputStream, name);
    }
}
