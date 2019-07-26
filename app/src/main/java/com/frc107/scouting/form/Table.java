package com.frc107.scouting.form;

import android.util.SparseArray;

import com.frc107.scouting.Scouting;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private SparseArray<Column<Object>> columns = new SparseArray<>();

    public void addColumn(int id, Column column) {
        if (columns.indexOfKey(id) >= 0)
            throw new IllegalArgumentException("Column with id " + id + " already exists");

        columns.put(id, column);
    }

    private int rowAmount;

    /**
     * This is used to enter in a whole row at once. Make sure your object types match the order of
     * the columns as well as the types of each relevant column.
     *
     * If I had 3 columns A{String} B{Boolean} C{Integer}, then I might call enterValues with
     * the arguments:
     *      enterValues("I am a string", 5, true);
     * @param values
     */
    public String enterNewRow(Object... values) {
        if (values.length != columns.size())
            throw new IllegalArgumentException("Cannot pass in a different amount of values than there are columns!\nValue count: " + values.length + "\nColumn count: " + columns.size());

        for (int i = 0; i < columns.size(); i++) {
            Column<Object> column = columns.get(columns.keyAt(i));
            Object value = values[i];
            column.enterValue(value);
        }

        rowAmount++;

        return getRowAsString(rowAmount - 1);
    }

    // please don't use this, otherwise you risk having a different amount of rows per column than in total, that's bad
    /*public void enterValue(int id, Object value) {
        if (columns.indexOfKey(id) < 0)
            throw new IllegalArgumentException("No column with id " + id);

        Column<Object> col = columns.get(id);
        if (value != null && !col.getTypeClass().equals(value.getClass()))
            throw new IllegalArgumentException("Type of value must be the same as the type of column. value type: " + value.getClass().getName() + ", column type: " + col.getTypeClass().getName());

        col.enterValue(value);
    }*/

    public List getValuesOfColumn(int id) {
        if (columns.indexOfKey(id) < 0)
            throw new IllegalArgumentException("No column with id " + id);

        Column column = columns.get(id);
        return column.getValues();
    }

    public String getRowsInRangeAsString(int beginIndex, int endIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = beginIndex; i <= endIndex; i++) {
            String row = getRowAsString(i);
            builder.append(row);
            builder.append(Scouting.NEW_LINE);
        }
        return builder.toString();
    }

    public String getLastRowAsString() {
        return getRowAsString(rowAmount - 1);
    }

    public String getRowAsString(int row) {
        String[] values = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            Column<Object> column = columns.get(columns.keyAt(i));
            Object lastValue = column.getValueAtRow(row);
            values[i] = lastValue.toString();
        }

        // I would use String.join(",", values) but that's only supported in API level 26 and up
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);

            if (i < values.length - 1)
                builder.append(',');
        }
        return builder.toString();
    }
}
