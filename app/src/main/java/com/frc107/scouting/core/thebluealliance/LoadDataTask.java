package com.frc107.scouting.core.thebluealliance;

import android.os.AsyncTask;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.analysis.IAnalysisManager;
import com.frc107.scouting.core.utils.callbacks.ICallback;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;
import com.frc107.scouting.core.table.Table;

import java.io.IOException;

public class LoadDataTask extends AsyncTask<IAnalysisManager, Void, Void> {
    private ICallback listener;
    private ICallbackWithParam<String> onError;

    public LoadDataTask(ICallback listener, ICallbackWithParam<String> onError) {
        this.listener = listener;
        this.onError = onError;
    }

    @Override
    protected Void doInBackground(IAnalysisManager... attributeManagers) {
        loadData(attributeManagers[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        listener.call();
    }

    private void loadData(IAnalysisManager attributeManager) {
        String data;
        try {
            data = Scouting.FILE_SERVICE.getFileData(attributeManager.getFile());
        } catch (IOException e) {
            onError.call(e.getLocalizedMessage());
            return;
        }

        Table table = Scouting.getInstance().getTable(attributeManager.getTableType());
        table.importData(data, row -> attributeManager.makeCalculationsFromRows(row.getValues()));
    }
}
