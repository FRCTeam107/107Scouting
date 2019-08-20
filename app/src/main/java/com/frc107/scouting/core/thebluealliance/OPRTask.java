package com.frc107.scouting.core.thebluealliance;

import android.os.AsyncTask;
import android.util.Log;

import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

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
            String oprData = tba.downloadOPRData(eventKey[0]);
            return tba.parseOPRs(oprData);
        } catch (IOException e) {
            Logger.log(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(OPR opr) {
        super.onPostExecute(opr);
        listener.call(opr);
    }
}
