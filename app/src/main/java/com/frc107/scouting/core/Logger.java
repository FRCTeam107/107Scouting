package com.frc107.scouting.core;

import android.util.Log;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.file.FileService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {
    private static FileService fileService = Scouting.getFileService();
    private static final String FILE_NAME = "log.txt";
    private static File logFile = new File(fileService.getScoutingDirectory(), FILE_NAME);

    public static void log(String message) {
        Log.d(ScoutingStrings.SCOUTING_TAG, message);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String timeDateMessage = simpleDateFormat.format(new Date());
        String messageWithTime = timeDateMessage + ":\t" + message;

        if (!logFile.exists()) {
            try {
                logFile = fileService.createAndWriteToNewFileCore(fileService.getScoutingDirectory(), FILE_NAME, messageWithTime);
            } catch (IOException e) {
                Log.d(ScoutingStrings.SCOUTING_TAG, "Error while creating log file!");
                Log.d(ScoutingStrings.SCOUTING_TAG, e.getLocalizedMessage());
            }
        }  else {
            fileService.writeLineToEndOfFile(logFile, messageWithTime);
        }
    }
}
