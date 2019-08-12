package com.frc107.scouting.analysis;

import android.os.AsyncTask;
import android.util.Log;

import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.analysis.tba.OPR;
import com.frc107.scouting.analysis.tba.TBA;
import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.callbacks.ICallbackWithParam;

import java.io.IOException;

public class OPRTask extends AsyncTask<String, Void, OPR> {
    private ICallbackWithParam<OPR> listener;
    private TBA tba;

    public OPRTask(ICallbackWithParam<OPR> listener) {
        this.listener = listener;
        tba = new TBA();
    }

    @Override
    protected OPR doInBackground(String... eventKey) {
        try {
            return tba.getOPRs(eventKey[0]);
        } catch (IOException e) {
            Log.e(ScoutingStrings.SCOUTING_TAG, e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(OPR opr) {
        super.onPostExecute(opr);
        listener.call(opr);
    }
}
