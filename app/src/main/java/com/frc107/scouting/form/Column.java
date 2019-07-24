package com.frc107.scouting.form;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
    private String name;
    private ArrayList<T> values = new ArrayList<>();
    private Class<T> typeClass; // Used for typechecking

    public Column(String name, Class<T> typeClass) {
        this.name = name;
        this.typeClass = typeClass;
    }

    public void enterValue(T value) {
        values.add(value);
    }

    public boolean hasValueAtRow(int row) {
        return row > values.size() - 1;
    }

    public T getValueAtRow(int row) {
        return values.get(row);
    }

    public List<T> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }
}
