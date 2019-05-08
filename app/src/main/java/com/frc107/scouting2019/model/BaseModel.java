package com.frc107.scouting2019.model;

import com.frc107.scouting2019.Scouting;
import com.frc107.scouting2019.model.question.ToggleQuestion;
import com.frc107.scouting2019.model.question.NumberQuestion;
import com.frc107.scouting2019.model.question.Question;
import com.frc107.scouting2019.model.question.RadioQuestion;
import com.frc107.scouting2019.model.question.TextQuestion;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseModel {
    private ArrayList<Question> questions;
    private String fileNameHeader;

    public BaseModel() {
        this.questions = new ArrayList<>(Arrays.asList(getQuestions()));
    }

    public abstract Question[] getQuestions();

    public void setFileNameHeader(String fileNameHeader) {
        this.fileNameHeader = fileNameHeader;
    }

    public int getFirstUnfinishedQuestionId() {
        for (Question question : questions) {
            if (!question.needsAnswer())
                continue;

            if (!question.hasAnswer())
                return question.getId();
        }
        return -1;
    }

    // TODO: This should probably have an implementation per-model, so we can avoid things like cycleCanBeFinished. It's bad pattern.
    public boolean isFormComplete() {
        for (Question question : questions) {
            if (!question.needsAnswer())
                continue;

            if (!question.hasAnswer())
                return false;
        }
        return true;
    }

    public boolean areNoQuestionsAnswered() {
        for (Question question : questions) {
            if (!question.needsAnswer())
                continue;

            if (question instanceof ToggleQuestion)
                continue;

            if (question.hasAnswer())
                return false;
        }
        return true;
    }

    public abstract void onNumberQuestionAnswered(int questionId, Integer answer);
    public abstract void onTextQuestionAnswered(int questionId, String answer);
    public abstract void onRadioQuestionAnswered(int questionId, int answerId);

    public boolean setAnswer(int questionId, String answer) {
        Question question = getQuestion(questionId);
        if (question == null)
            return false;

        if (question instanceof NumberQuestion) {
            Integer numAnswer = answer.length() == 0 ? null : Integer.valueOf(answer);
            onNumberQuestionAnswered(questionId, numAnswer);
            question.setAnswer(numAnswer);
            return true;
        } else if (question instanceof TextQuestion) {
            onTextQuestionAnswered(questionId, answer);
            question.setAnswer(answer);
            return true;
        }
        return false;
    }

    public boolean setAnswer(int questionId, int answer) {
        Question question = getQuestion(questionId);
        if (question == null)
            return false;

        if (question instanceof RadioQuestion) {
            onRadioQuestionAnswered(questionId, answer);
            question.setAnswer(answer);
            return true;
        }

        return false;
    }

    public boolean setAnswer(int questionId, boolean answer) {
        Question question = getQuestion(questionId);
        if (question == null)
            return false;

        if (question instanceof ToggleQuestion) {
            ((ToggleQuestion) question).setAnswer(answer);
            return true;
        }

        return false;
    }

    public Question getQuestion(int id) {
        for (Question question : questions) {
            if (question.getId() == id)
                return question;
        }
        return null;
    }

    public String getAnswerForQuestion(int id) {
        Question question = getQuestion(id);
        if (question == null)
            return null;

        return question.getAnswerAsString();
    }

    public Object getRawAnswerForQuestion(int id) {
        Question question = getQuestion(id);
        if (question == null)
            return null;

        return question.getAnswer();
    }

    public String getAnswerCSVRow() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            if (!question.answerCanBeIgnored()) {
                if (i > 0)
                    stringBuilder.append(',');

                if (Scouting.SAVE_QUESTION_NAMES_AS_ANSWERS)
                    stringBuilder.append(question.getName());
                else
                    stringBuilder.append(question.getAnswerAsString());
            }
        }
        return stringBuilder.toString();
    }

    public String save() {
        String dataToWrite = getAnswerCSVRow() + '\n';
        String result = Scouting.FILE_UTILS.writeData(fileNameHeader, dataToWrite);
        return result;
    }

    public void clearAllQuestions() {
        for (Question question : questions) {
            question.setAnswer(null);
        }
    }
}
