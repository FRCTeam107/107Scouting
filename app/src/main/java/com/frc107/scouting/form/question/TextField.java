package com.frc107.scouting.form.question;

import com.frc107.scouting.form.IFieldGetter;
import com.frc107.scouting.form.IFieldSetter;

public class TextField extends Field<String> {
    public TextField(String name, int id, boolean needsAnswer) {
        super(name, id, needsAnswer);
    }

    public TextField(String name, int id, boolean needsAnswer, IFieldSetter<String> customSetter) {
        super(name, id, needsAnswer, customSetter);
    }

    public TextField(String name, int id, boolean needsAnswer, IFieldGetter<String> customGetter) {
        super(name, id, needsAnswer, customGetter);
    }

    public TextField(String name, int id, boolean needsAnswer, IFieldSetter<String> customSetter, IFieldGetter<String> customGetter) {
        super(name, id, needsAnswer, customSetter, customGetter);
    }

    @Override
    public boolean hasAnswer() {
        return getAnswer() != null && getAnswer().length() > 0;
    }
}
