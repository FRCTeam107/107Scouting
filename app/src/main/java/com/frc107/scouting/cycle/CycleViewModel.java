package com.frc107.scouting.cycle;

import com.frc107.scouting.viewmodel.BaseViewModel;

public class CycleViewModel extends BaseViewModel {
    public CycleViewModel(int teamNumber) {
        model = new CycleModel(teamNumber);
    }

    public void finishCycle() {
        ((CycleModel) model).finishCycle();
    }

    public void setAllDefense(boolean allDefense) {
        ((CycleModel) model).setAllDefense(allDefense);
    }

    public boolean cycleCanBeFinished() {
        return ((CycleModel) model).cycleCanBeFinished();
    }

    public boolean hasUsedStartingItem() {
        return ((CycleModel) model).hasUsedStartingItem();
    }

    public void disableStartingItem() {
        ((CycleModel) model).disableStartingItem();
    }

    public int getTeamNumber() {
        return ((CycleModel) model).getTeamNumber();
    }
}
