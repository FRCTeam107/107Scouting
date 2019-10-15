package com.frc107.scouting.deepspace.match.cycle;

import android.util.SparseIntArray;

public class CycleAnswers {
    private CycleAnswers() {}

    public static final int PORT =                  0;
    public static final int FLOOR =                 1;
    public static final int STARTED_WITH_ITEM =     2;

    public static final int CARGO =                 0;
    public static final int HATCH_PANEL =           1;

    public static final int TOP_ROCKET =            3;
    public static final int MIDDLE_ROCKET =         2;
    public static final int BOTTOM_ROCKET =         1;
    public static final int CARGO_SHIP =            0;
    public static final int DROPPED =               4;
    public static final int NOT_PLACED =            5;

    private static SparseIntArray buttonIdToAnswerNumMap = new SparseIntArray();
    static {
        buttonIdToAnswerNumMap.put(CycleIDs.PORT_OPTION,                PORT);
        buttonIdToAnswerNumMap.put(CycleIDs.FLOOR_OPTION,               FLOOR);
        buttonIdToAnswerNumMap.put(CycleIDs.STARTED_WITH_ITEM_OPTION,   STARTED_WITH_ITEM);

        buttonIdToAnswerNumMap.put(CycleIDs.CARGO_OPTION,               CARGO);
        buttonIdToAnswerNumMap.put(CycleIDs.HATCH_PANEL_OPTION,         HATCH_PANEL);

        buttonIdToAnswerNumMap.put(CycleIDs.CARGO_SHIP_OPTION,          CARGO_SHIP);
        buttonIdToAnswerNumMap.put(CycleIDs.BOTTOM_ROCKET_OPTION,       BOTTOM_ROCKET);
        buttonIdToAnswerNumMap.put(CycleIDs.MID_ROCKET_OPTION,          MIDDLE_ROCKET);
        buttonIdToAnswerNumMap.put(CycleIDs.TOP_ROCKET_OPTION,          TOP_ROCKET);
        buttonIdToAnswerNumMap.put(CycleIDs.DROPPED_OPTION,             DROPPED);
        buttonIdToAnswerNumMap.put(CycleIDs.NOT_PLACED_OPTION,          NOT_PLACED);
    }

    public static int getAnswerFromId(int id) {
        if (buttonIdToAnswerNumMap.indexOfKey(id) < 0)
            return -1; // There is no assigned answer, so just use -1 as an answer.

        return buttonIdToAnswerNumMap.get(id);
    }
}
