package com.frc107.scouting.admin;

import com.frc107.scouting.callbacks.ICallbackWithParam;

class AdminViewModel {
    private AdminModel model;

    AdminViewModel(ICallbackWithParam<Boolean> callback) {
        model = new AdminModel(callback);
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
