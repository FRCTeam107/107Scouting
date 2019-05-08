package com.frc107.scouting2019.sandstorm;

import com.frc107.scouting2019.viewmodel.BaseViewModel;

public class SandstormViewModel extends BaseViewModel {
    public SandstormViewModel() {
        model = new SandstormModel();
    }

    public void finish() {
        ((SandstormModel) model).finish();
    }

    public int getTeamNumber() {
        return ((SandstormModel) model).getTeamNumber();
    }

    public boolean shouldAllowStartingPiece() {
        return ((SandstormModel) model).shouldAllowStartingPiece();
    }
}
