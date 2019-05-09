package com.frc107.scouting.sandstorm;

import com.frc107.scouting.viewmodel.BaseViewModel;

public class SandstormViewModel extends BaseViewModel {
    public SandstormViewModel() {
        model = new SandstormModel();
    }

    public int getTeamNumber() {
        return ((SandstormModel) model).getTeamNumber();
    }

    public boolean hasUsedStartingItem() {
        return ((SandstormModel) model).hasUsedStartingItem();
    }
}
