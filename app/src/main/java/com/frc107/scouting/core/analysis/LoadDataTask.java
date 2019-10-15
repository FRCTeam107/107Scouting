package com.frc107.scouting.core.analysis;

import android.os.AsyncTask;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;
import com.frc107.scouting.core.table.Table;

import java.io.File;
import java.io.IOException;

public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {
    private ICallbackWithParam<Boolean> onDataLoaded;
    private IAnalysisManager analysisManager;
    private File file;

    public LoadDataTask(ICallbackWithParam<Boolean> onDataLoaded, IAnalysisManager analysisManager, String filePath) {
        this.onDataLoaded = onDataLoaded;
        this.analysisManager = analysisManager;
        file = new File(filePath);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return loadData();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        onDataLoaded.call(result);
    }

    private boolean loadData() {
        String data;
        try {
            data = Scouting.getFileService().getFileData(file);
        } catch (IOException e) {
            Logger.log(e.getLocalizedMessage());
            return false;
        }

        Table table = Scouting.getInstance().getTable(analysisManager.getTableType());
        table.clear();
        return table.importData(data, row -> analysisManager.handleRow(row.getValues()));
    }
}
