package com.frc107.scouting.admin;

import android.content.Context;
import android.net.Uri;

import com.frc107.scouting.ui.IUIListener;

import java.io.File;
import java.util.List;

class AdminViewModel {
    private AdminModel model;

    AdminViewModel(IUIListener listener) {
        model = new AdminModel(listener);
    }

    boolean concatenateMatchData() {
        return model.concatenateData(AdminModel.MATCH);
    }

    boolean concatenatePitData() {
        return model.concatenateData(AdminModel.PIT);
    }

    void setEventKey(String eventKey) {
        model.setEventKey(eventKey);
    }

    String getEventKey() {
        return model.getEventKey();
    }

    void downloadOPRs() {
        model.downloadOPRs();
    }
}
