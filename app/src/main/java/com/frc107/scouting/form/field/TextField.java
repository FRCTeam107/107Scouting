package com.frc107.scouting.form.field;

import androidx.annotation.NonNull;

import com.frc107.scouting.form.IFieldGetter;
import com.frc107.scouting.form.IFieldSetter;

public class TextField extends Field<String> {
    public TextField(String name, int id, boolean needsAnswer) {
        super(String.class, name, id, needsAnswer);
    }

    public TextField(String name, int id, boolean needsAnswer, IFieldSetter<String> customSetter) {
        super(String.class, name, id, needsAnswer, customSetter);
    }

    public TextField(String name, int id, boolean needsAnswer, IFieldGetter<String> customGetter) {
        super(String.class, name, id, needsAnswer, customGetter);
    }

    public TextField(String name, int id, boolean needsAnswer, IFieldSetter<String> customSetter, IFieldGetter<String> customGetter) {
        super(String.class, name, id, needsAnswer, customSetter, customGetter);
    }

    @Override
    public boolean hasAnswer() {
        return getAnswer() != null && getAnswer().length() > 0;
    }

    @Override
    public String toString() {
        String answer = getAnswer();
        if (answer == null)
            return "";

        return answer;
    }
}
