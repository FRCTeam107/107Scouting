package com.frc107.scouting.analysis;

import android.os.AsyncTask;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.analysis.attribute.IAnalysisManager;
import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.callbacks.ICallbackWithParam;
import com.frc107.scouting.form.Table;

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

        // todo: replace with something in attribute manager
        Table table = Scouting.getInstance().getTable(attributeManager.getTableType());
        table.importData(data, row -> attributeManager.makeCalculationsFromRows(row.getValues()));
    }
}
