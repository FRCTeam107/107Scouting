package com.frc107.scouting.core.table.column;

import androidx.annotation.NonNull;

public class Column {
    private int id;
    private String name;
    private Class typeClass; // Used for typechecking

    public Column(int id, String name, eColumnType columnType) {
        this.id = id;
        this.name = name;
        typeClass = columnType.getClassType();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class getTypeClass() {
        return typeClass;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
