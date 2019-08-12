package com.frc107.scouting.core.table.column;

import androidx.annotation.NonNull;

public class Column<T> {
    private int id;
    private String name;
    private Class<T> typeClass; // Used for typechecking

    public Column(int id, String name, Class<T> typeClass) {
        this.id = id;
        this.name = name;
        this.typeClass = typeClass;
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
