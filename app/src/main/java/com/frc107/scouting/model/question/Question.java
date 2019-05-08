package com.frc107.scouting.model.question;

/**
 * @param <T> The type of the answer.
 */
public abstract class Question<T> {
    /**
     * name is used for debugging, so that we can easily have a way to make
     * sure that questions are being saved in the correct order.
     */
    private String name;
    private int id;
    private boolean needsAnswer;
    private boolean ignoreAnswer;

    public Question(String name, int id, boolean needsAnswer) {
        this.name = name;
        this.id = id;
        this.needsAnswer = needsAnswer;
    }

    public boolean needsAnswer() {
        return needsAnswer;
    }

    public void setNeedsAnswer(boolean needsAnswer) {
        this.needsAnswer = needsAnswer;
    }

    public void setIgnoreAnswer(boolean ignoreAnswer) {
        this.ignoreAnswer = ignoreAnswer;
    }

    public boolean answerCanBeIgnored() {
        return ignoreAnswer;
    }

    public boolean hasAnswer() {
        return getAnswer() != null;
    }

    public abstract void setAnswer(T answer);
    public abstract T getAnswer();
    public abstract String getAnswerAsString();

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
