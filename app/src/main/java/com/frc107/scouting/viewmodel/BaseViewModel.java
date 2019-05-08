package com.frc107.scouting.viewmodel;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.model.BaseModel;
import com.frc107.scouting.model.FormStatus;

public abstract class BaseViewModel extends ViewModel {
    protected BaseModel model;

    public FormStatus getFormStatus() {
        return model.getFormStatus();
    }

    public int getFirstUnfinishedQuestionId() {
        return model.getFirstUnfinishedQuestionId();
    }

    public boolean isFormComplete() {
        return model.areAllQuestionsFinished();
    }

    public boolean areNoQuestionsAnswered() {
        return model.areNoQuestionsAnswered();
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

    public String getAnswerForQuestion(int questionId) {
        return model.getAnswerForQuestion(questionId);
    }

    public String save() {
        return model.save();
    }

    public void clearAllAnswers() {
        model.clearAllQuestions();
    }
}
