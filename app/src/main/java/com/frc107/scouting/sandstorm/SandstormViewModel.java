package com.frc107.scouting.sandstorm;

import com.frc107.scouting.viewmodel.BaseViewModel;

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
