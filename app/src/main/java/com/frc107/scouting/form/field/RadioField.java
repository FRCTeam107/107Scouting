package com.frc107.scouting.form.field;

import android.util.SparseArray;

public class RadioField extends Field<Integer> {
    private SparseArray<Option> options;
    private Option selectedOption;

    public RadioField(String name, int id, boolean needsAnswer, Option... options) {
        super(Integer.class, name, id, needsAnswer);
        init(options);
    }

    /*public RadioField(String name, int id, boolean needsAnswer, IFieldSetter<Option> customSetter, Option... options) {
        super(Integer.class, name, id, needsAnswer, customSetter);
        init(options);
    }

    public RadioField(String name, int id, boolean needsAnswer, IFieldGetter<Option> customGetter, Option... options) {
        super(Integer.class, name, id, needsAnswer, customGetter);
        init(options);
    }

    public RadioField(String name, int id, boolean needsAnswer, IFieldSetter<Option> customSetter, IFieldGetter<Option> customGetter, Option... options) {
        super(Integer.class, name, id, needsAnswer, customSetter, customGetter);
        init(options);
    }*/

    private void init(Option... options) {
        this.options = new SparseArray<>();
        for (Option option : options) {
            this.options.put(option.id, option);
        }
    }

    @Override
    public void setAnswer(Integer value) {
        if (value == null || value == -1) {
            super.setAnswer(null);
            return;
        }

        selectedOption = options.get(value);
        super.setAnswer(value);
    }

    @Override
    public String toString() {
        return selectedOption.getId() + "";
    }

    public static class Option {
        private int id;
        private int order;

        public Option(int id, int order) {
            this.id = id;
            this.order = order;
        }

        public int getId() {
            return id;
        }

        public int getOrder() {
            return order;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}

