package com.frc107.scouting.core.table;

import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;
import com.frc107.scouting.core.table.column.Column;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParamAndResult;

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

    /**
     * Create a new Table.
     * @param name The name of the table.
     * @param columnsToAdd A collection of columns. Make sure these each have different IDs and names.
     */
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
     * Enter in a whole row at once.
     * @param values A collection of values to enter in a new row. Make sure there's a value for
     *               every column in the same order as the columns were defined.
     * @return The new row.
     */
    public Row enterNewRow(Object... values) {
        if (values.length != columns.size())
            throw new IllegalArgumentException("Cannot pass in a different amount of values than there are idColumnMap!\nValue count: " + values.length + "\nColumn count: " + idColumnMap.size());

        Row row = new Row(values);
        rows.add(row);

        return row;
    }

    /**
     * Import data from a String into this table.
     * @param data Your input. This can be as many lines as you want, as long as each line contains
     *             every column in this table with the same names and order.
     * @param forEachRow A callback to run for every single Row created from lines in the data.
     *                   Be light with what you do with forEachRow. Since it runs every single
     *                   row (of which there can be thousands), this can be expensive.
     * @return True if successful, false if not.
     */
    public boolean importData(String data, ICallbackWithParamAndResult<Row, Boolean> forEachRow) {
        String[] lines = data.split(ScoutingStrings.NEW_LINE);
        String[] columnNamesInData = lines[0].split(",");

        // Alright, to start off, we'll go through the first line: the header.
        for (int i = 0; i < columns.size(); i++) {
            String colName = columnNamesInData[i];
            if (!this.columnNames.contains(colName)) {
                Logger.log("Could not import data. Column \"" + colName + "\" is not contained in this table.");
                return false; // The imported data contains a column that we don't have. Abort.
            }
        }

        // Now that we've gotten the first line (header) out of the way, let's look at the actual
        // data. We start at i = 1, of course, because we already parsed the first line.
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty())
                continue;

            Object[] objects = getObjectsFromLine(line);
            if (objects == null) { // There was an error parsing the line into an object array.
                Logger.log("Could not import data. Error while parsing line \"" + line + "\"");
                return false;
            }

            Row row = enterNewRow(objects);
            rows.add(row);

            boolean successfulRow = forEachRow.call(row);
            if (!successfulRow) {
                clear();
                Logger.log("Could not import data. Operation halted by callback parameter \"forEachRow\".");
                return false; // The callback needed to stop the import.
            }
        }

        Logger.log("Successfully imported data into table \"" + name + "\".");
        return true;
    }

    /**
     * Get a Row object from a String of values separated by commas.
     * @param line Your input, formatted like: "1,2,3,banana"
     * @return An array of Object containing the parsed values from your input.
     */
    private Object[] getObjectsFromLine(String line) {
        Object[] values = new Object[columns.size()];
        String[] parts = line.split(",");
        for (int j = 0; j < values.length; j++) {
            Column column = columns.get(j);
            Class typeClass = column.getTypeClass();

            Object value = null;

            if (typeClass.equals(Integer.class)) {
                try {
                    value = Integer.parseInt(parts[j]);
                } catch (NumberFormatException e) {
                    Logger.log(e.getLocalizedMessage());
                    return null;
                }
            }

            if (typeClass.equals(String.class)) {
                value = parts[j];
            }

            values[j] = value;
        }
        return values;
    }

    /**
     * Get the String value of a row and all its elements.
     * @param rowIndex The vertical index of the row.
     * @return A String of elements separated by commas.
     */
    private String getStringFromRow(int rowIndex) {
        Row row = rows.get(rowIndex);
        return row.toString();
    }

    public void clear() {
        rows.clear();
    }

    /**
     * Get a value at a certain position.
     * @param columnId The ID of the column. Not the position/index, but the ID.
     * @param rowIndex The vertical index of the row.
     * @return An Object, which you need to cast or parse to whatever data you expect.
     */
    public Object getValueAtColumnAndRow(int columnId, int rowIndex) {
        int colIndex = colIdIndexMap.get(columnId);
        Row row = rows.get(rowIndex);
        return row.getValue(colIndex);
    }
}
