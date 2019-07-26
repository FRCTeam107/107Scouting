package com.frc107.scouting.admin;

import android.util.Log;

import com.frc107.scouting.Scouting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ImportCsvModel {
    public void importCsv(Scouting.eTable targetTable, InputStream inputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            ArrayList<String> lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            Log.d(Scouting.SCOUTING_TAG, lines.toString());
            // TODO: load the csv data, ask if user wants to merge with existing, replace, or add new file
            // TODO: allow for saving of data to specific location? nah
        } catch (IOException e) {
            Log.d(Scouting.SCOUTING_TAG, e.getLocalizedMessage());
        }
    }
}
