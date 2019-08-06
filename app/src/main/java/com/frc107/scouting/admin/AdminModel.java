package com.frc107.scouting.admin;

import android.util.Log;

import com.frc107.scouting.callbacks.ICallbackWithParam;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.analysis.IOPRListener;
import com.frc107.scouting.analysis.OPRTask;
import com.frc107.scouting.analysis.tba.OPR;
import com.frc107.scouting.file.FileService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdminModel implements IOPRListener {
    public static final int MATCH = 0;
    public static final int PIT = 1;

    private ICallbackWithParam<Boolean> callback;
    private OPR opr;

    public AdminModel(ICallbackWithParam<Boolean> callback) {
        this.callback = callback;
    }

    private String getOPRAndDPR(int teamNumber) {
        String empty = "-1,-1";
        if (opr == null)
            return empty;

        if (!opr.containsTeam(teamNumber))
            return empty;

        return opr.getOPR(teamNumber) + "," + opr.getDPR(teamNumber);
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
        callback.call(opr == null);
    }
}
