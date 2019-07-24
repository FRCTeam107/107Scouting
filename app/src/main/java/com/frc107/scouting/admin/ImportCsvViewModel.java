package com.frc107.scouting.admin;

import androidx.lifecycle.ViewModel;

import java.io.InputStream;

public class ImportCsvViewModel extends ViewModel {
    private ImportCsvModel model = new ImportCsvModel();

    public void importCsv(InputStream inputStream) {
        model.importCsv(inputStream);
    }
}
