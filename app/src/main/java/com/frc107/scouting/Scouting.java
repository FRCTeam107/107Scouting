package com.frc107.scouting;

import android.util.Log;

import com.frc107.scouting.file.FileDefinition;
import com.frc107.scouting.form.column.IntColumn;
import com.frc107.scouting.form.column.StringColumn;
import com.frc107.scouting.form.Table;
import com.frc107.scouting.form.eTable;
import com.frc107.scouting.pit.PitIDs;
import com.frc107.scouting.file.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scouting {
    public static final boolean SAVE_QUESTION_NAMES_AS_ANSWERS = false;

    private static Scouting scouting = new Scouting();
    public static Scouting getInstance() {
        return scouting;
    }

    private List<Table> tables = new ArrayList<>();
    private Table pitTable;
    private Table matchTable;

    public Table getTable(eTable table) {
        switch (table) {
            case PIT:
                return pitTable;
            case MATCH:
                return matchTable;
            default:
                return null;
        }
    }

    private static final String COL_PIT_TEAM_NUM = "pit_team_number";
    private static final String COL_PIT_SANDSTORM_OP = "pit_sandstorm_op";
    private static final String COL_PIT_SANDSTORM_PREF = "pit_sandstorm_preference";
    private static final String COL_PIT_SANDSTORM_HIGHEST_ROCKET_LEVEL = "pit_sandstorm_highest_rocket_level";
    private static final String COL_PIT_HIGHEST_HABITAT = "pit_highest_habitat";
    private static final String COL_PIT_HABITAT_TIME = "pit_habitat_time";
    private static final String COL_PIT_PROGRAMMING_LANG = "pit_programming_language";
    private static final String COL_PIT_BONUS = "pit_bonus";
    private static final String COL_PIT_COMMENTS = "pit_comments";

    private String pitHeader;
    private String matchHeader;

    private Scouting() {
        pitTable = new Table("PitAnswers",
                new IntColumn(PitIDs.TEAM_NUM, COL_PIT_TEAM_NUM),
                new IntColumn(PitIDs.SANDSTORM_OP, COL_PIT_SANDSTORM_OP),
                new IntColumn(PitIDs.SANDSTORM_PREF, COL_PIT_SANDSTORM_PREF),
                new IntColumn(PitIDs.SANDSTORM_HIGHEST_ROCKET_LEVEL, COL_PIT_SANDSTORM_HIGHEST_ROCKET_LEVEL),
                new IntColumn(PitIDs.HIGHEST_HAB, COL_PIT_HIGHEST_HABITAT),
                new StringColumn(PitIDs.HAB_TIME, COL_PIT_HABITAT_TIME),
                new StringColumn(PitIDs.PROGRAMMING_LANG, COL_PIT_PROGRAMMING_LANG),
                new StringColumn(PitIDs.BONUS, COL_PIT_BONUS),
                new StringColumn(PitIDs.COMMENTS, COL_PIT_COMMENTS));

        pitHeader = pitTable.getHeader();

        tables.add(pitTable);

        matchTable = new Table("Match",
                new IntColumn(0, "Match number"),
                new IntColumn(1, "Team number"),
                new IntColumn(2, "Starting location"),
                new IntColumn(3, "Starting item"),
                new IntColumn(4, "Starting item placed"),
                new IntColumn(5, "Crossed baseline"),
                new IntColumn(6, "Cycle number"),
                new IntColumn(7, "Pick up location"),
                new IntColumn(8, "Item picked up"),
                new IntColumn(9, "Item placed"),
                new IntColumn(10, "Defense"),
                new IntColumn(11, "Hab level"),
                new IntColumn(12, "All match"),
                new IntColumn(13, "Match defense"),
                new IntColumn(14, "Fouls"),
                new IntColumn(15, "Max Cycles"),
                new StringColumn(16, "Scouter Initials"),
                new StringColumn(17, "OPR"),
                new StringColumn(18, "DPR"));

        matchHeader = matchTable.getHeader();

        tables.add(matchTable);

        reloadData();
    }

    public String getPitHeader() {
        return pitHeader;
    }

    public String getMatchHeader() {
        return matchHeader;
    }

    private void reloadData() {
        pitTable.clear();

        fileService.clearFileDefinitions();
        fileService.loadFileDefinitions();

        FileDefinition fileDef = fileService.getMostRecentFileDefinition(eTable.PIT, userInitials);
        if (fileDef != null) {
            String fileData;
            try {
                fileData = fileService.getFileData(fileDef.getFile());
            } catch (IOException e) {
                Log.d(ScoutingStrings.SCOUTING_TAG, e.getLocalizedMessage());
                return;
            }
            pitTable.importData(fileData, row -> {});
        }
    }

    public List<Table> getTables() {
        return tables;
    }

    public static final FileService FILE_SERVICE = getInstance().getFileService();

    private FileService fileService = new FileService();

    public FileService getFileService() {
        return fileService;
    }

    private String userInitials;
    public void setUserInitials(String userInitials) {
        this.userInitials = userInitials;
    }

    public String getUserInitials() {
        return userInitials;
    }

    private String eventKey;
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
    public String getEventKey() {
        return eventKey;
    }
}
