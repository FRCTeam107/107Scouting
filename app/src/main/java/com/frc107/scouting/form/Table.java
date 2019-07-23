package com.frc107.scouting.form;

import android.util.SparseArray;

import java.util.ArrayList;

public class Table {
    private SparseArray<Column> columns = new SparseArray<>();

    public Table() {}

    public void enterValue(int id, Object value) {
        if (columns.indexOfKey(id) == -1)
            throw new IllegalArgumentException("No column with id " + id);

        Column col = columns.get(id);
    }

    public class Column<T> {
        private String name;
        private ArrayList<T> values = new ArrayList<>();

        public Column(String name) {
            this.name = name;
        }

        public void enterValue(T value) {
            values.add(value);
        }

        public T getValueAtRow(int row) {
            return values.get(row);
        }

        public String getName() {
            return name;
        }
    }
}
