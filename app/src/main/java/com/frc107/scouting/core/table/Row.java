package com.frc107.scouting.core.table;

public class Row {
    private Object[] values;
    private String stringValue;

    /**
     * Create a new row with a collection of values.
     */
    Row(Object[] values) {
        this.values = values;

        // I would use String.join(",", values) but that's only supported in API level 26 and up

        // FormatStringUtils.addDelimiter does something almost identical to the below code, but I
        // rewrote it here since I'm working off of an Object[] and need to call toString.
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i].toString());

            if (i < values.length - 1)
                builder.append(',');
        }
        stringValue = builder.toString();
    }

    public Object[] getValues() {
        return values;
    }

    /**
     * Get a value at a specific column in this row.
     * @return The Object at this position.
     */
    public Object getValue(int columnIndex) {
        return values[columnIndex];
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
