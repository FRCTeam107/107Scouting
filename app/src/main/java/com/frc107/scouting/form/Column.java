package com.frc107.scouting.form;

import java.util.ArrayList;

public class Column<T> {
    private int id;
    private String name;
    private ArrayList<T> values = new ArrayList<>();

    public Column(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void enterValue(T value) {
        values.add(value);
    }

    public T getValueAtRow(int row) {
        return values.get(row);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
