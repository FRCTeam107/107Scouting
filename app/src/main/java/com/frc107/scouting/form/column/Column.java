package com.frc107.scouting.form.column;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
