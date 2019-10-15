package com.frc107.scouting.core;

import androidx.annotation.NonNull;

public class ScoutingStrings {
    private ScoutingStrings() {}
    public static final String TABLE_EXTRA_KEY = "TABLE_EXTRA_KEY";
    public static final String FILE_SELECTION_EXTRA_KEY = "FILE_SELECTION_EXTRA";

    public static final String UNFINISHED_QUESTION_MESSAGE = "Unfinished questions.";
    public static final String PREFERENCES_NAME = "ScoutingPreferences";
    public static final String EVENT_KEY_PREFERENCE = "eventKey";
    public static final String USER_INITIALS_PREFERENCE = "userInitials";

    @NonNull
    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final String SCOUTING_TAG = "Scouting";
}
