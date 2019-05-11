package com.frc107.scouting.admin;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.frc107.scouting.BuildConfig;
import com.frc107.scouting.ui.IUIListener;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.analysis.IOPRListener;
import com.frc107.scouting.analysis.OPRTask;
import com.frc107.scouting.analysis.tba.OPR;
import com.frc107.scouting.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;

public class AdminModel implements IOPRListener {
    public static final int MATCH = 0;
    public static final int PIT = 1;

    private IUIListener listener;
    private OPR opr;

    public AdminModel(IUIListener listener) {
        this.listener = listener;
    }

    public List<Uri> getPhotoUriList(Context context) {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (File photo :  Scouting.FILE_UTILS.getPhotos()) {
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photo);
            uriList.add(uri);
        }
        return uriList;
    }

    public boolean concatenateData(int type) {
        String prefix = "Match";
        String header = ScoutingStrings.MATCH_HEADER;
        if (type == PIT) {
            prefix = "Pit";
            header = ScoutingStrings.PIT_HEADER;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(header);
        builder.append("\n");

        FileUtils fileUtils = Scouting.FILE_UTILS;

        File[] files = fileUtils.getFilesInDirectory();

        for (File file : files) {
            if (!file.getName().startsWith(prefix))
                continue;

            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                String line = bufferedReader.readLine();
                while (line != null) {
                    String[] columns = line.split(",");
                    int teamNumber = Integer.parseInt(columns[1]);

                    String oprAndDpr = getOPRAndDPR(teamNumber);

                    builder.append(line).append(oprAndDpr).append('\n');
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                Log.d("Scouting", e.getMessage());
            }
        }

        String fileName = "Concatenated" + prefix + ".csv";
        File newFile = new File(fileUtils.getScoutingDirectory(), fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile, false);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD())) {
            if (newFile.exists()) {
                fileWriter.write("");
            }

            fileWriter.write(builder.toString());
            return true;
        } catch (IOException e) {
            Log.d("Scouting", e.getMessage());
        }

        return false;
    }

    private String getOPRAndDPR(int teamNumber) {
        String empty = "-1,-1";
        if (opr == null)
            return empty;

        if (!opr.containsTeam(teamNumber))
            return empty;

        return opr.getOPR(teamNumber) + "," + opr.getDPR(teamNumber);
    }

    public File getMatchFile(boolean concatenated) {
        if (concatenated) {
            return Scouting.FILE_UTILS.getConcatMatchFile();
        } else {
            return Scouting.FILE_UTILS.getMatchFile();
        }
    }

    public File getPitFile(boolean concatenated) {
        if (concatenated) {
            return Scouting.FILE_UTILS.getConcatPitFile();
        } else {
            return Scouting.FILE_UTILS.getPitFile();
        }
    }

    public void setEventKey(String eventKey) {
        Scouting.getInstance().setEventKey(eventKey);
    }

    public String getEventKey() {
        return Scouting.getInstance().getEventKey();
    }

    public void downloadOPRs() {
        OPRTask task = new OPRTask(this);
        task.execute(Scouting.getInstance().getEventKey());
    }

    @Override
    public void onOPRLoaded(OPR opr) {
        this.opr = opr;
        listener.callback(opr == null);
    }
}