package com.frc107.scouting.form.field;

import com.frc107.scouting.form.IFieldGetter;
import com.frc107.scouting.form.IFieldSetter;

import java.util.ArrayList;

public class NumberField extends Field<Integer> {
    private ArrayList<Integer> illegalValues;

    public NumberField(String name, int id, boolean needsAnswer) {
        super(Integer.class, name, id, needsAnswer);
    }

    public NumberField(String name, int id, boolean needsAnswer, IFieldSetter<Integer> customSetter) {
        super(Integer.class, name, id, needsAnswer, customSetter);
    }

    public NumberField(String name, int id, boolean needsAnswer, IFieldGetter<Integer> customGetter) {
        super(Integer.class, name, id, needsAnswer, customGetter);
    }

    public NumberField(String name, int id, boolean needsAnswer, IFieldSetter<Integer> customSetter, IFieldGetter<Integer> customGetter) {
        super(Integer.class, name, id, needsAnswer, customSetter, customGetter);
    }

    @Override
    public void setAnswer(Integer answer) {
        if (illegalValues.contains(answer))
            return;

        super.setAnswer(answer);
    }

    public void addIllegalValue(int value) {
        if (illegalValues == null)
            illegalValues = new ArrayList<>();

        illegalValues.add(value);
    }

    @Override
    public String toString() {
        Integer answer = getAnswer();
        if (answer == null)
            return "";

        return answer.toString();
    }
}
