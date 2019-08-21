package com.frc107.vanguard.core;

import android.util.Log;

import com.frc107.vanguard.Vanguard;
import com.frc107.vanguard.core.file.FileService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {
    private Logger() {}

    private static FileService fileService = Vanguard.getFileService();
    private static final String FILE_NAME = "log.txt";
    private static File logFile = new File(fileService.getScoutingDirectory(), FILE_NAME);

    /**
     * Call this to log something both to the console as well as to the log file.
     * @param message A message to log.
     */
    public static void log(String message) {
        Log.d(VanguardStrings.VANGUARD_TAG, message);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String timeDateMessage = simpleDateFormat.format(new Date());
        String messageWithTime = timeDateMessage + ":\t" + message;

        try {
            if (!logFile.exists()) {
                logFile = fileService.createAndWriteToNewFileCore(fileService.getScoutingDirectory(), FILE_NAME, messageWithTime);
            } else {
                fileService.writeDataToEndOfFile(logFile, messageWithTime);
            }
        } catch (IOException e) {
            Log.e(VanguardStrings.VANGUARD_TAG, e.getLocalizedMessage());
        }
    }
}
