package com.frc107.scouting.form.question;

import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.frc107.scouting.form.IFieldGetter;
import com.frc107.scouting.form.IFieldSetter;

import java.util.ArrayList;
import java.util.Arrays;

public class RadioField extends Field<RadioField.Option> {
    private SparseArray<Option> options;

    public RadioField(String name, int id, boolean needsAnswer, Option... options) {
        super(name, id, needsAnswer);
        init(options);
    }

    public RadioField(String name, int id, boolean needsAnswer, IFieldSetter<Option> customSetter, Option... options) {
        super(name, id, needsAnswer, customSetter);
        init(options);
    }

    public RadioField(String name, int id, boolean needsAnswer, IFieldGetter<Option> customGetter, Option... options) {
        super(name, id, needsAnswer, customGetter);
        init(options);
    }

    public RadioField(String name, int id, boolean needsAnswer, IFieldSetter<Option> customSetter, IFieldGetter<Option> customGetter, Option... options) {
        super(name, id, needsAnswer, customSetter, customGetter);
        init(options);
    }

    private void init(Option... options) {
        this.options = new SparseArray<>();
        for (Option option : options) {
            this.options.put(option.id, option);
        }
    }

    @Override
    public void setAnswer(Option option) {
        if (option == null || option.getId() == -1) {
            super.setAnswer(null);
            return;
        }

        super.setAnswer(option);

        /*for (Option option : options) {
            if (option.getId() == option) {
                selectedOption = option;
            }
        }*/
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

