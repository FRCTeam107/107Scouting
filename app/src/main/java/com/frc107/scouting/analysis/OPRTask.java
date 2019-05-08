package com.frc107.scouting.analysis;

import android.os.AsyncTask;

import com.frc107.scouting.analysis.tba.OPR;
import com.frc107.scouting.analysis.tba.TBA;

import java.io.IOException;

public class OPRTask extends AsyncTask<String, Void, OPR> {
    private IOPRListener listener;
    private TBA tba;

    public OPRTask(IOPRListener listener) {
        this.listener = listener;
        tba = new TBA();
    }

    @Override
    protected OPR doInBackground(String... eventKey) {
        try {
            return tba.getOPRs(eventKey[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(OPR opr) {
        super.onPostExecute(opr);
        listener.onOPRLoaded(opr);
    }
}
