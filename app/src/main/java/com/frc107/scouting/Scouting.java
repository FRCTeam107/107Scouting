package com.frc107.scouting;

import com.frc107.scouting.utils.FileUtils;

import java.util.ArrayList;

public class Scouting {
    public static final String VERSION_DATE = "4/21/2019 - 1:25";
    public static final String PREFERENCES_NAME = "ScoutingPreferences";
    public static final String EVENT_KEY_PREFERENCE = "eventKey";

    public static final int SANDSTORM_HAB_ONE = 0,
                            SANDSTORM_HAB_TWO = 1,
                            SANDSTORM_CARGO = 1,
                            SANDSTORM_PANEL = 2,
                            SANDSTORM_NO_STARTING_PIECE = 0,
                            SANDSTORM_TOP_ROCKET = 3,
                            SANDSTORM_MIDDLE_ROCKET = 2,
                            SANDSTORM_BOTTOM_ROCKET = 1,
                            SANDSTORM_CARGO_SHIP = 0,
                            SANDSTORM_FLOOR = 4,
                            SANDSTORM_NOTHING_PLACED = 5;
    public static final int CYCLE_PORT_PICKUP = 0,
                            CYCLE_FLOOR_PICKUP = 1,
                            CYCLE_STARTED_WITH_ITEM = 2,
                            CYCLE_CARGO = 0,
                            CYCLE_PANEL = 1,
                            CYCLE_TOP_ROCKET = 3,
                            CYCLE_MIDDLE_ROCKET = 2,
                            CYCLE_BOTTOM_ROCKET = 1,
                            CYCLE_CARGO_SHIP = 0,
                            CYCLE_FLOOR = 4,
                            CYCLE_NOTHING_PLACED = 5;
    public static final int ENDGAME_HAB_ONE = 1,
                            ENDGAME_HAB_TWO = 2,
                            ENDGAME_HAB_THREE = 3,
                            ENDGAME_HAB_NONE = 0,
                            ENDGAME_DEFENSE_EFFECTIVE = 1,
                            ENDGAME_DEFENSE_INEFFECTIVE = 2,
                            ENDGAME_DEFENSE_NONE = 0;

    public static final FileUtils FILE_UTILS = new FileUtils();
    public static final boolean SAVE_QUESTION_NAMES_AS_ANSWERS = false;

    private static Scouting scouting;
    public static Scouting getInstance() {
        if (scouting == null)
            scouting = new Scouting();
        return scouting;
    }

    private Scouting() {
        cycles = new ArrayList<>();
    }

    private String uniqueId;
    public void setUniqueId(String newUniqueId) {
        uniqueId = newUniqueId;
    }
    public String getUniqueId() {
        return uniqueId;
    }

    private int teamNumber = -1;
    public void setTeamNumber(int newTeamNumber) {
        teamNumber = newTeamNumber;
    }
    public int getTeamNumber() {
        return teamNumber;
    }

    private int matchNumber = -1;
    public void setMatchNumber(int newMatchNumber) {
        matchNumber = newMatchNumber;
    }
    public int getMatchNumber() {
        return matchNumber;
    }

    private String sandstormData;
    public void setSandstormData(String newSandstormData) {
        sandstormData = newSandstormData;
    }
    public String getSandstormData() {
        return sandstormData;
    }

    private ArrayList<String> cycles;
    public void addCycle(String cycle) {
        cycles.add(cycle);
    }
    public ArrayList<String> getCycles() {
        return cycles;
    }
    public void clearCycles() {
        cycles.clear();
    }

    private String initials;
    public void setInitials(String initials) {
        this.initials = initials;
    }
    public String getInitials() { return initials; }

    private String eventKey;
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
    public String getEventKey() {
        return eventKey;
    }
}
