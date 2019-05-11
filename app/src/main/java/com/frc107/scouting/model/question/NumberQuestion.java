package com.frc107.scouting.model.question;

import java.util.ArrayList;

public class NumberQuestion extends Question<Integer> {
    private Integer answer;
    private ArrayList<Integer> illegalValues;

    public NumberQuestion(String name, int id, boolean needsAnswer) {
        super(name, id, needsAnswer);
        illegalValues = new ArrayList<>();
    }

    @Override
    public boolean hasAnswer() {
        return answer != null;
    }

    @Override
    public void setAnswer(Integer answer) {
        if (!illegalValues.contains(answer))
            this.answer = answer;
    }

    @Override
    public Integer getAnswer() {
        return answer;
    }

    @Override
    public String getAnswerAsString() {
        return String.valueOf(answer);
    }

    public void addIllegalValue(int value) {
        illegalValues.add(value);
    }
}
