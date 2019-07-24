package com.frc107.scouting.form;

import android.util.SparseArray;

import java.util.List;

public class Table {
    private SparseArray<Column<Object>> columns = new SparseArray<>();

    public void addColumn(int id, Column column) {
        if (columns.indexOfKey(id) >= 0)
            throw new IllegalArgumentException("Column with id " + id + " already exists");

        columns.put(id, column);
    }

    public void enterValue(int id, Object value) {
        if (columns.indexOfKey(id) < 0)
            throw new IllegalArgumentException("No column with id " + id);

        Column<Object> col = columns.get(id);
        if (value != null && !col.getTypeClass().equals(value.getClass()))
            throw new IllegalArgumentException("Type of value must be the same as the type of column. value type: " + value.getClass().getName() + ", column type: " + col.getTypeClass().getName());

        col.enterValue(value);
    }

    public List getValuesOfColumn(int id) {
        if (columns.indexOfKey(id) < 0)
            throw new IllegalArgumentException("No column with id " + id);

        Column column = columns.get(id);
        return column.getValues();
    }
}
