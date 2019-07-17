package com.frc107.scouting.form.question;

public class ToggleField extends Field<Boolean> {
    public ToggleField(String name, int id) {
        super(name, id, false);
    }

    @Override
    public boolean hasAnswer() {
        // Since it's a toggle, we can't exactly differentiate between an intentional or unintentional empty selection.
        return true;
    }
}
