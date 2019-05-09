package com.frc107.scouting.cycle;

import com.frc107.scouting.viewmodel.BaseViewModel;

public class CycleViewModel extends BaseViewModel {
    public CycleViewModel(int teamNumber, boolean hasUsedStartingItem) {
        model = new CycleModel(teamNumber, hasUsedStartingItem);
    }

    public void setAllDefense(boolean allDefense) {
        ((CycleModel) model).setAllDefense(allDefense);
    }

    public boolean hasUsedStartingItem() {
        return ((CycleModel) model).hasUsedStartingItem();
    }

    public int getTeamNumber() {
        return ((CycleModel) model).getTeamNumber();
    }
}
