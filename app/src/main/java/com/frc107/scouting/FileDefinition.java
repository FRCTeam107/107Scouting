package com.frc107.scouting;

import com.frc107.scouting.form.eTable;

import java.io.File;
import java.util.Calendar;

public class FileDefinition {
    private File file;
    private Calendar dateCreated;
    private String initials;
    private eTable tableType;
    private boolean isConcat;

    public FileDefinition(eTable tableType, File file, Calendar dateCreated, String initials, boolean isConcat) {
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

    public eTable getTableType() {
        return tableType;
    }

    public boolean isConcat() {
        return isConcat;
    }

    public String getType() {
        if (isConcat)
            return tableType.toStringConcat();

        return tableType.toString();
    }
}
