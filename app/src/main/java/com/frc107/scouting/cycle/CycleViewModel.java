package com.frc107.scouting.cycle;

import com.frc107.scouting.viewmodel.BaseViewModel;

class CycleViewModel extends BaseViewModel {
    CycleViewModel(int teamNumber, boolean hasUsedStartingItem) {
        model = new CycleModel(teamNumber, hasUsedStartingItem);
    }

    void setAllDefense(boolean allDefense) {
        ((CycleModel) model).setAllDefense(allDefense);
    }

    boolean hasUsedStartingItem() {
        return ((CycleModel) model).hasUsedStartingItem();
    }

    int getTeamNumber() {
        return ((CycleModel) model).getTeamNumber();
    }
}
