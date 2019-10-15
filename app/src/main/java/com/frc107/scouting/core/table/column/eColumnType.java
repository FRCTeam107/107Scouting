package com.frc107.scouting.core.table.column;

public enum eColumnType {
    INT,
    STRING;

    Class getClassType() {
        switch (this) {
            case INT: return Integer.class;
            case STRING: return String.class;
            default: return null;
        }
    }
}
