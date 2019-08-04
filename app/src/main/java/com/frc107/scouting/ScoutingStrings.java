package com.frc107.scouting;

import androidx.annotation.NonNull;

public class ScoutingStrings {
    public static final String UNFINISHED_QUESTION_MESSAGE = "Unfinished questions.";

    public static final String PIT_HEADER = "pit_team_number,pit_sandstorm_op,pit_sandstorm_preference,pit_sandstorm_highest_rocket_level,Highest HAB,HAB Time,Programming Language,(?)Planet wanting to visit(?),comments";
    public static final String MATCH_HEADER = "Match number,Team number,Starting location,Starting item,Starting item placed,Crossed baseline,Cycle number,Pick up location,Item picked up,Item placed,Defense,Hab level,All match,Match defense,Fouls,Max Cycles,Scouter Initials,OPR,DPR";

    public static final String PREFERENCES_NAME = "ScoutingPreferences";
    public static final String EVENT_KEY_PREFERENCE = "eventKey";
    public static final String USER_INITIALS_PREFERENCE = "userInitials";

    @NonNull
    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final String SCOUTING_TAG = "Scoutinator";
}
