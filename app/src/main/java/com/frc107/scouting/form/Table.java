package com.frc107.scouting.form;

import android.util.SparseArray;

import com.frc107.scouting.Scouting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Table {
    private String name;
    private Map<Integer, Column> columns = new HashMap<>();

    public Table(String name, Column... columnsToAdd) {
        this.name = name;

        for (Column column : columnsToAdd) {
            if (columns.containsKey(column.getId()))
                throw new IllegalArgumentException("Column with id " + column.getId() + " already exists");

            columns.put(column.getId(), column);
        }

        columns = Collections.unmodifiableMap(columns);
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Column> getColumns() {
        return columns;
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

        Integer[] keys = columns.keySet().toArray(new Integer[0]);
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(keys[i]);
            Object value = values[i];
            column.enterValue(value);
        }

        rowAmount++;

        return getRowAsString(rowAmount - 1);
    }

    public boolean importData(String data) {
        Map.Entry<Integer, Column>[] entries = columns.entrySet().toArray(new Map.Entry[0]);

        String[] lines = data.split(Scouting.NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            if (i == 0) {
                // header
                for (int j = 0; j < parts.length; j++) {
                    String columnName = parts[j];
                    Column column = entries[j].getValue();
                    if (!columnName.equals(column.getName())) // Columns don't match; exit.
                        return false;
                }
                continue;
            }

            for (int j = 0; j < parts.length; j++) {
                Column column = entries[j].getValue();
                Class typeClass = column.getTypeClass();

                Object value = null;
                if (typeClass.equals(Integer.class)) {
                    value = Integer.parseInt(parts[j]);
                }

                if (typeClass.equals(String.class)) {
                    value = parts[j];
                }

                column.enterValue(value);
            }
        }
        return true;
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
        if (!columns.containsKey(id))
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
        Integer[] keys = columns.keySet().toArray(new Integer[0]);

        String[] values = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(keys[i]);
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
