package com.frc107.scouting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.frc107.scouting.form.IntColumn;
import com.frc107.scouting.form.StringColumn;
import com.frc107.scouting.form.Table;
import com.frc107.scouting.form.eTable;
import com.frc107.scouting.utils.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scouting {
    public static final String PREFERENCES_NAME = "ScoutingPreferences";
    public static final String EVENT_KEY_PREFERENCE = "eventKey";
    public static final String USER_INITIALS_PREFERENCE = "userInitials";

    @NonNull
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String SCOUTING_TAG = "Scoutinator";

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

    public static final String COL_PIT_TEAM_NUM = "pit_team_number";
    public static final String COL_PIT_SANDSTORM_OP = "pit_sandstorm_op";
    public static final String COL_PIT_SANDSTORM_PREF = "pit_sandstorm_preference";
    public static final String COL_PIT_SANDSTORM_HIGHEST_ROCKET_LEVEL = "pit_sandstorm_highest_rocket_level";
    public static final String COL_PIT_HIGHEST_HABITAT = "pit_highest_habitat";
    public static final String COL_PIT_HABITAT_TIME = "pit_habitat_time";
    public static final String COL_PIT_PROGRAMMING_LANG = "pit_programming_language";
    public static final String COL_PIT_BONUS = "pit_bonus";
    public static final String COL_PIT_COMMENTS = "pit_comments";
    private String pitHeader;

    private Scouting() {
        pitTable = new Table("Pit",
                new IntColumn(R.id.pit_team_number, COL_PIT_TEAM_NUM),
                new IntColumn(R.id.pit_sandstorm_op, COL_PIT_SANDSTORM_OP),
                new IntColumn(R.id.pit_sandstorm_preference, COL_PIT_SANDSTORM_PREF),
                new IntColumn(R.id.pit_sandstorm_highest_rocket_level, COL_PIT_SANDSTORM_HIGHEST_ROCKET_LEVEL),
                new IntColumn(R.id.pit_highest_habitat, COL_PIT_HIGHEST_HABITAT),
                new StringColumn(R.id.pit_habitat_time, COL_PIT_HABITAT_TIME),
                new StringColumn(R.id.pit_programming_language, COL_PIT_PROGRAMMING_LANG),
                new StringColumn(R.id.pit_bonus, COL_PIT_BONUS),
                new StringColumn(R.id.pit_comments, COL_PIT_COMMENTS));

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

        tables.add(matchTable);

        reloadData();
    }

    public String getPitHeader() {
        return pitHeader;
    }

    public String getMatchHeader() {
        return "eeee";
    }

    private void reloadData() {
        pitTable.clear();

        fileService.clearFileDefinitions();
        fileService.loadFiles();

        FileService.FileDefinition fileDef = fileService.getMostRecentFileDefinition(eTable.PIT, userInitials);
        if (fileDef != null) {
            String fileData;
            try {
                fileData = fileService.getFileData(fileDef.getFile());
            } catch (IOException e) {
                Log.d(SCOUTING_TAG, e.getLocalizedMessage());
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
