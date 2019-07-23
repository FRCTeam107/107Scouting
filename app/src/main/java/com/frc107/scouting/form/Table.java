package com.frc107.scouting.form;

import android.util.SparseArray;

import java.util.ArrayList;

public class Table {
    private SparseArray<Column<Object>> columns = new SparseArray<>();

    public Table() {}

    public void enterValue(int id, Object value) {
        if (columns.indexOfKey(id) == -1)
            throw new IllegalArgumentException("No column with id " + id);

        Column<Object> col = columns.get(id);
        if (value != null && !col.getTypeClass().equals(value.getClass()))
            throw new IllegalArgumentException("Type of value must be the same as the type of column. value type: " + value.getClass().getName() + ", column type: " + col.getTypeClass().getName());

        col.enterValue(value);
    }

    public class Column<T> {
        private int id;
        private String name;
        private ArrayList<T> values = new ArrayList<>();
        private Class<T> typeClass; // Used for typechecking

        public Column(int id, String name, Class<T> typeClass) {
            this.id = id;
            this.name = name;
            this.typeClass = typeClass;
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

        public Class<T> getTypeClass() {
            return typeClass;
        }
    }
}
