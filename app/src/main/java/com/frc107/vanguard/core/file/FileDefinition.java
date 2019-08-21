package com.frc107.vanguard.core.file;

import com.frc107.vanguard.eTableType;

import java.io.File;
import java.util.Calendar;

public class FileDefinition {
    private File file;
    private Calendar dateCreated;
    private String initials;
    private eTableType tableType;
    private boolean isConcat;

    public FileDefinition(eTableType tableType, File file, Calendar dateCreated, String initials, boolean isConcat) {
        this.file = file;
        this.dateCreated = dateCreated;
        this.initials = initials;
        this.tableType = tableType;
        this.isConcat = isConcat;
    }

    public File getFile() {
        return file;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public String getInitials() {
        return initials;
    }

    public eTableType getTableType() {
        return tableType;
    }

    public boolean isConcatenated() {
        return isConcat;
    }

    public String getType() {
        if (isConcat)
            return tableType.toStringConcat();

        return tableType.toString();
    }
}
