package com.frc107.scouting.admin;

import com.frc107.scouting.callbacks.ICallbackWithParam;

class AdminViewModel {
    private AdminModel model;

    AdminViewModel(ICallbackWithParam<Boolean> callback) {
        model = new AdminModel(callback);
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
