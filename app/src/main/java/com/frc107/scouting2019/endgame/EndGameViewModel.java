package com.frc107.scouting2019.endgame;

import com.frc107.scouting2019.viewmodel.BaseViewModel;

public class EndGameViewModel extends BaseViewModel {
    public EndGameViewModel() {
        model = new EndGameModel();
    }

    public String finish() {
        return ((EndGameModel) model).finish();
    }
}
