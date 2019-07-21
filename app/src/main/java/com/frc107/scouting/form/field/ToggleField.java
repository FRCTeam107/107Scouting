package com.frc107.scouting.form.field;

import androidx.annotation.NonNull;

public class ToggleField extends Field<Boolean> {
    public ToggleField(String name, int id) {
        super(Boolean.class, name, id, false);
    }

    @Override
    public boolean hasAnswer() {
        // Since it's a toggle, we can't exactly differentiate between an intentional or unintentional empty selection.
        return true;
    }

    @Override
    public String toString() {
        Boolean answer = getAnswer();
        if (answer == null)
            return "";

        return String.valueOf(answer);
    }
}
