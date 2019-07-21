package com.frc107.scouting.form;

import android.util.SparseArray;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.form.field.Field;
import com.frc107.scouting.form.field.ToggleField;

public abstract class FormModel {
    private SparseArray<Field> fields;
    private int unansweredQuestionId;

    private static final String INVALID_QUESTION_ID = "Invalid question id";

    public FormModel() {
        this.fields = new SparseArray<>();
        initializeQuestions();
    }

    private void initializeQuestions() {
        Field[] fieldArray = getFields();
        for (Field field : fieldArray) {
            fields.put(field.getId(), field);
        }
    }

    public abstract Field[] getFields();

    public int getUnfinishedQuestionId() {
        return unansweredQuestionId;
    }

    private int findUnfinishedQuestionId() {
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.valueAt(i);
            if (!field.needsAnswer())
                continue;

            if (!field.hasAnswer())
                return field.getId();
        }
        return -1;
    }

    public boolean isFinished() {
        unansweredQuestionId = findUnfinishedQuestionId();
        return unansweredQuestionId == -1;
    }

    public abstract boolean finish();

    public boolean areNoQuestionsAnswered() {
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.valueAt(i);
            if (!field.needsAnswer() || field instanceof ToggleField)
                continue;

            if (field.hasAnswer())
                return false;
        }
        return true;
    }

    public abstract void onQuestionAnswered(int questionId, Object answer);

    public boolean setAnswer(int fieldId, Object answer) {
        if (fields.indexOfKey(fieldId) == -1)
            throw new IllegalArgumentException(INVALID_QUESTION_ID);

        Field field = fields.get(fieldId);
        if (!field.isObjectSameType(answer))
            return false;

        onQuestionAnswered(fieldId, answer);
        field.setAnswer(answer);
        return true;
    }

    public Field getQuestion(int questionId) {
        if (fields.indexOfKey(questionId) == -1)
            throw new IllegalArgumentException(INVALID_QUESTION_ID);

        return fields.get(questionId);
    }

    public String getAnswer(int questionId) {
        Field field = getQuestion(questionId);
        return field.getAnswer().toString();
    }

    public String getAnswerCSVRow() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.valueAt(i);
            if (field.answerCanBeIgnored())
                continue;

            if (i > 0)
                stringBuilder.append(',');

            if (Scouting.SAVE_QUESTION_NAMES_AS_ANSWERS)
                stringBuilder.append(field.getName()); // Instead of the answer, save the field name so that one can make sure that the data is saving correctly.
            else
                stringBuilder.append(field.toString());
        }
        return stringBuilder.toString();
    }
}
