package com.frc107.scouting.model;

import android.util.SparseArray;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.model.question.ToggleQuestion;
import com.frc107.scouting.model.question.NumberQuestion;
import com.frc107.scouting.model.question.Question;
import com.frc107.scouting.model.question.RadioQuestion;
import com.frc107.scouting.model.question.TextQuestion;

public abstract class BaseModel {
    private SparseArray<Question> questions;
    private int unansweredQuestionId;

    public BaseModel() {
        this.questions = new SparseArray<>();
        initializeQuestions();
    }

    private void initializeQuestions() {
        Question[] questionArray = getQuestions();
        for (Question question : questionArray) {
            questions.put(question.getId(), question);
        }
    }

    public abstract Question[] getQuestions();

    public int getUnfinishedQuestionId() {
        return unansweredQuestionId;
    }

    private int findUnfinishedQuestionId() {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.valueAt(i);
            if (!question.needsAnswer())
                continue;

            if (!question.hasAnswer())
                return question.getId();
        }
        return -1;
    }

    public boolean isFinished() {
        unansweredQuestionId = findUnfinishedQuestionId();
        return unansweredQuestionId == -1;
    }

    public abstract boolean finish();

    public boolean areNoQuestionsAnswered() {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.valueAt(i);
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
        if (questions.indexOfKey(questionId) == -1)
            throw new IllegalArgumentException("Invalid question id");

        Question question = getQuestion(questionId);
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
        if (questions.indexOfKey(questionId) == -1)
            throw new IllegalArgumentException("Invalid question id");

        Question question = getQuestion(questionId);
        if (question instanceof RadioQuestion) {
            onRadioQuestionAnswered(questionId, answer);
            question.setAnswer(answer);
            return true;
        }

        return false;
    }

    public boolean setAnswer(int questionId, boolean answer) {
        if (questions.indexOfKey(questionId) == -1)
            throw new IllegalArgumentException("Invalid question id");

        Question question = getQuestion(questionId);
        if (question instanceof ToggleQuestion) {
            ((ToggleQuestion) question).setAnswer(answer);
            return true;
        }

        return false;
    }

    public Question getQuestion(int questionId) {
        if (questions.indexOfKey(questionId) == -1)
            throw new IllegalArgumentException("Invalid question id");

        return questions.get(questionId);
    }

    public String getAnswerForQuestion(int id) {
        Question question = getQuestion(id);
        return question.getAnswerAsString();
    }

    public Object getRawAnswerForQuestion(int id) {
        Question question = getQuestion(id);
        return question.getAnswer();
    }

    public String getAnswerCSVRow() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.valueAt(i);
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
}
