package com.frc107.scouting.admin;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;

import java.io.InputStream;

public class ImportCsvViewModel extends ViewModel {
    private ImportCsvModel model = new ImportCsvModel();

    public void importCsv(Scouting.eTable targetTable, InputStream inputStream) {
        model.importCsv(targetTable, inputStream);
    }
}
