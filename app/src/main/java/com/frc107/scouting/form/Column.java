package com.frc107.scouting.form;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Column<T> {
    private int id;
    private String name;
    //private ArrayList<T> values = new ArrayList<>();
    private Class<T> typeClass; // Used for typechecking

    public Column(int id, String name, Class<T> typeClass) {
        this.id = id;
        this.name = name;
        this.typeClass = typeClass;
    }

    /*public void enterValue(T value) {
        values.add(value);
    }

    public boolean hasValueAtRow(int row) {
        return row > values.size() - 1;
    }

    public T getValueAtRow(int row) {
        return values.get(row);
    }

    public T getValueAtLastRow() {
        return values.get(values.size() - 1);
    }

    public List<T> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void clear() {
        values.clear();
    }*/

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
