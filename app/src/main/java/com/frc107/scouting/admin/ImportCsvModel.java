package com.frc107.scouting.admin;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ImportCsvModel {
    public void importCsv(InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            ArrayList<String> lines;
            lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            // TODO: ask users what form to load this as, and try to load it, move it to the Scouting folder
        } catch (IOException e) {
            Log.d("Scouting", e.getLocalizedMessage());
        }
    }
}
