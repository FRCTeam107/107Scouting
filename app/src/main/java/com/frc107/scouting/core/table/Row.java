package com.frc107.scouting.core.table;

public class Row {
    private Object[] values;

    public Row(Object[] values) {
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }

    public Object getValue(int i) {
        return values[i];
    }
}
