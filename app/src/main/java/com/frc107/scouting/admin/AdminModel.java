package com.frc107.scouting.admin;

import com.frc107.scouting.callbacks.ICallbackWithParam;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.analysis.OPRTask;
import com.frc107.scouting.analysis.tba.OPR;

public class AdminModel {
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
        OPRTask task = new OPRTask(opr -> {
            this.opr = opr;
            callback.call(opr == null);
        });
        task.execute(Scouting.getInstance().getEventKey());
    }
}
