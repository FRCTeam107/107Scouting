package com.frc107.scouting.match.sandstorm;

import android.util.SparseIntArray;

public class SandstormAnswers {
    private SandstormAnswers() {}

    public static final int HAB_ONE =       0;
    public static final int HAB_TWO =       1;

    public static final int NO_PIECE =      0;
    public static final int CARGO =         1;
    public static final int HATCH_PANEL =   2;

    public static final int CARGO_SHIP =    0;
    public static final int BOTTOM_ROCKET = 1;
    public static final int MIDDLE_ROCKET =    2;
    public static final int TOP_ROCKET =    3;
    public static final int FLOOR =         4;
    public static final int NOT_PLACED =    5;

    private static SparseIntArray buttonIdToAnswerNumMap = new SparseIntArray();
    static {
        buttonIdToAnswerNumMap.put(SandstormIDs.HAB_ONE_OPTION,         HAB_ONE);
        buttonIdToAnswerNumMap.put(SandstormIDs.HAB_TWO_OPTION,         HAB_TWO);

        buttonIdToAnswerNumMap.put(SandstormIDs.NO_PIECE_OPTION,        NO_PIECE);
        buttonIdToAnswerNumMap.put(SandstormIDs.CARGO_OPTION,           CARGO);
        buttonIdToAnswerNumMap.put(SandstormIDs.HATCH_PANEL_OPTION,     HATCH_PANEL);

        buttonIdToAnswerNumMap.put(SandstormIDs.CARGO_SHIP_OPTION,      CARGO_SHIP);
        buttonIdToAnswerNumMap.put(SandstormIDs.BOTTOM_ROCKET_OPTION,   BOTTOM_ROCKET);
        buttonIdToAnswerNumMap.put(SandstormIDs.MID_ROCKET_OPTION,      MIDDLE_ROCKET);
        buttonIdToAnswerNumMap.put(SandstormIDs.TOP_ROCKET_OPTION,      TOP_ROCKET);
        buttonIdToAnswerNumMap.put(SandstormIDs.FLOOR_OPTION,           FLOOR);
        buttonIdToAnswerNumMap.put(SandstormIDs.NOT_PLACED_OPTION,      NOT_PLACED);
    }

    public static int getAnswerFromId(int id) {
        if (buttonIdToAnswerNumMap.indexOfKey(id) < 0)
            return -1; // There is no assigned answer, so just use -1 as an answer.

        return buttonIdToAnswerNumMap.get(id);
    }
}
