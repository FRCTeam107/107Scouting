package com.frc107.scouting.form.field;

import com.frc107.scouting.form.IFieldGetter;
import com.frc107.scouting.form.IFieldSetter;

/**
 * @param <T> The type of the answer.
 */
public abstract class Field<T> {
    /**
     * name is used for debugging, so that we can easily have a way to make
     * sure that questions are being saved in the correct order.
     */
    private String name;
    private int id;
    private boolean needsAnswer;
    private boolean ignoreAnswer;
    private T answer;
    private IFieldSetter<T> customSetter;
    private IFieldGetter<T> customGetter;

    // Used for type checking
    private Class<T> clazz;

    public Field(Class<T> clazz, String name, int id, boolean needsAnswer) {
        this.clazz = clazz;
        this.name = name;
        this.id = id;
        this.needsAnswer = needsAnswer;
    }

    public Field(Class<T> clazz, String name, int id, boolean needsAnswer, IFieldSetter<T> customSetter) {
        this(clazz, name, id, needsAnswer);
        this.customSetter = customSetter;
    }

    public Field(Class<T> clazz, String name, int id, boolean needsAnswer, IFieldGetter<T> customGetter) {
        this(clazz, name, id, needsAnswer);
        this.customGetter = customGetter;
    }

    public Field(Class<T> clazz, String name, int id, boolean needsAnswer, IFieldSetter<T> customSetter, IFieldGetter<T> customGetter) {
        this(clazz, name, id, needsAnswer);
        this.customSetter = customSetter;
        this.customGetter = customGetter;
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

    public void setAnswer(T answer) {
        if (customSetter != null)
            customSetter.set(answer);

        this.answer = answer;
    }

    public T getAnswer() {
        if (customGetter != null)
            return customGetter.get();

        return answer;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isObjectSameType(Object object) {
        return object.getClass().equals(clazz);
    }
}
