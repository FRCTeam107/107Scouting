package com.frc107.scouting.form;

import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {
    protected FormModel model;

    public int getUnfinishedQuestionId() {
        return model.getUnfinishedQuestionId();
    }

    public boolean isFinished() {
        return model.isFinished();
    }

    public boolean setAnswer(int questionId, String answer) {
        return model.setAnswer(questionId, answer);
    }

    public boolean setAnswer(int questionId, int answer) {
        return model.setAnswer(questionId, answer);
    }

    public boolean setAnswer(int questionId, boolean answer) {
        return model.setAnswer(questionId, answer);
    }

    public String getAnswer(int questionId) {
        return model.getAnswer(questionId);
    }

    public boolean finish() {
        return model.finish();
    }
}
