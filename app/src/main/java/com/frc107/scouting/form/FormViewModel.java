package com.frc107.scouting.form;

import androidx.lifecycle.ViewModel;

public abstract class FormViewModel extends ViewModel implements IQuestionAnswerer {
    protected Form model;

    public int getUnfinishedQuestionId() {
        return model.getUnfinishedQuestionId();
    }

    public boolean isFinished() {
        return model.isFinished();
    }

    public boolean setAnswer(int questionId, Object answer) {
        return model.setAnswer(questionId, answer);
    }

    public String getAnswer(int questionId) {
        return model.getAnswer(questionId);
    }

    public boolean finish() {
        return model.finish();
    }
}
