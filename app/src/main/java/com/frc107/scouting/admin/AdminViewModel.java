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

    List<Uri> getPhotoUriList(Context context) {
        return model.getPhotoUriList(context);
    }

    File getMatchFile(boolean concatenated) {
        return model.getMatchFile(concatenated);
    }

    File getPitFile(boolean concatenated) {
        return model.getPitFile(concatenated);
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
