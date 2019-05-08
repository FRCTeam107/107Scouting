package com.frc107.scouting.model;

public class FormStatus {
    private int questionId;
    private boolean finished;

    public FormStatus(int questionId) {
        this.questionId = questionId;
    }

    public int getUnfinishedQuestionId() {
        return questionId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;
    }
}
