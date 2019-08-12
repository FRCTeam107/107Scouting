package com.frc107.scouting.core.table;

import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;
import com.frc107.scouting.core.table.column.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private String name;
    private String header;

    private Map<Integer, Column> idColumnMap = new HashMap<Integer, Column>();
    private Map<Integer, Integer> colIdIndexMap = new HashMap<Integer, Integer>();

    private List<Column> columns;
    private List<String> columnNames = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    public Table(String name, Column... columnsToAdd) {
        this.name = name;

        columns = Collections.unmodifiableList(Arrays.asList(columnsToAdd));
        StringBuilder headerBuilder = new StringBuilder();
        for (int i = 0; i < columnsToAdd.length; i++) {
            Column column = columnsToAdd[i];
            if (idColumnMap.containsKey(column.getId()))
                throw new IllegalArgumentException("Column with id " + column.getId() + " already exists");

            idColumnMap.put(column.getId(), column);

            headerBuilder.append(column.getName());
            if (i < columnsToAdd.length - 1)
                headerBuilder.append(",");

            columnNames.add(column.getName());
            colIdIndexMap.put(column.getId(), i);
        }

        header = headerBuilder.toString();
        idColumnMap = Collections.unmodifiableMap(idColumnMap);
    }

    public String getName() {
        return name;
    }

    public String getHeader() {
        return header;
    }

    public Map<Integer, Column> getColumns() {
        return idColumnMap;
    }

    /**
     * This is used to enter in a whole row at once. Make sure your object types match the order of
     * the idColumnMap as well as the types of each relevant column.
     *
     * If I had 3 idColumnMap A{String} B{Boolean} C{Integer}, then I might call enterValues with
     * the arguments:
     *      enterValues("I am a string", 5, true);
     * @param values
     */
    public String enterNewRow(Object... values) {
        if (values.length != columns.size())//idColumnMap.size())
            throw new IllegalArgumentException("Cannot pass in a different amount of values than there are idColumnMap!\nValue count: " + values.length + "\nColumn count: " + idColumnMap.size());

        Row row = new Row(values);
        rows.add(row);

        return getRowAsString(rows.size() - 1);
    }

    /**
     * Be light with what you do with forEachRow. Since it runs every single row (of which there can be thousands),
     * this can be expensive.
     */
    public boolean importData(String data, ICallbackWithParam<Row> forEachRow) {
        String[] lines = data.split(ScoutingStrings.NEW_LINE);
        String[] columnNames = lines[0].split(",");
        if (columnNames.length != columns.size())
            return false; // The imported data has a different amount of columns than we do.

        // Alright, to start off, we'll go through the first line: the header.

        for (int i = 0; i < columnNames.length; i++) {
            String colName = columnNames[i];
            if (!this.columnNames.contains(colName))
                return false; // The imported data contains a column that we don't have. Abort.
        }

        // Now that we've gotten the first line (header) out of the way, let's look at the actual
        // data. We start at i = 1, of course, because we already parsed the first line.
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty())
                continue;

            Row row = parseRow(line);
            rows.add(row);

            forEachRow.call(row);
        }

        return true;
    }

    private Row parseRow(String line) {
        Object[] values = new Object[columns.size()];
        String[] parts = line.split(",");
        for (int j = 0; j < parts.length; j++) {
            Column column = columns.get(j);
            Class typeClass = column.getTypeClass();

            Object value = null;

            if (typeClass.equals(Integer.class)) {
                value = Integer.parseInt(parts[j]);
            }

            if (typeClass.equals(String.class)) {
                value = parts[j];
            }

            values[j] = value;
        }
        return new Row(values);
    }

    public void clear() {
        rows.clear();
    }

    private String getRowAsString(int rowIndex) {
        Row row = rows.get(rowIndex);

        // I would use String.join(",", values) but that's only supported in API level 26 and up

        // FormatStringUtils.addDelimiter does something almost identical to this, but I rewrote it
        // here since I'm working off of an Object[] and need to call toString.
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            builder.append(row.getValue(i).toString());

            if (i < columns.size() - 1)
                builder.append(',');
        }
        return builder.toString();
    }

    public Object getColumnValueAtRow(int columnId, int rowIndex) {
        int colIndex = colIdIndexMap.get(columnId);
        Row row = rows.get(rowIndex);
        return row.getValue(colIndex);
    }
}
