package com.frc107.scouting.initials;

import com.frc107.scouting.initials.InitialsModel;

import androidx.lifecycle.ViewModel;

public class InitialsViewModel extends ViewModel {
    private InitialsModel model;

    public InitialsViewModel() {
        model = new InitialsModel();
    }

    public void setInitials(String initials) {
        model.setInitials(initials);
    }

    public String getInitials() {
        return model.getInitials();
    }
}
