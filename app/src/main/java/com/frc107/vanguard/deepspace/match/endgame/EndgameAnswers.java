package com.frc107.vanguard.deepspace.match.endgame;

import android.util.SparseIntArray;

public class EndgameAnswers {
    private EndgameAnswers() {}

    public static final int HAB_ONE = 1;
    public static final int HAB_TWO = 2;
    public static final int HAB_THREE = 3;
    public static final int NO_HAB = 0;

    public static final int EFFECTIVE_DEFENSE = 1;
    public static final int INEFFECTIVE_DEFENSE = 2;
    public static final int NO_DEFENSE = 0;

    private static SparseIntArray buttonIdToAnswerNumMap = new SparseIntArray();
    static {
        buttonIdToAnswerNumMap.put(EndgameIDs.HAB_ONE_OPTION,   HAB_ONE);
        buttonIdToAnswerNumMap.put(EndgameIDs.HAB_TWO_OPTION,   HAB_TWO);
        buttonIdToAnswerNumMap.put(EndgameIDs.HAB_THREE_OPTION, HAB_THREE);
        buttonIdToAnswerNumMap.put(EndgameIDs.NO_HAB_OPTION,    NO_HAB);

        buttonIdToAnswerNumMap.put(EndgameIDs.EFFECTIVE_DEFENSE_OPTION,     EFFECTIVE_DEFENSE);
        buttonIdToAnswerNumMap.put(EndgameIDs.INEFFECTIVE_DEFENSE_OPTION,   INEFFECTIVE_DEFENSE);
        buttonIdToAnswerNumMap.put(EndgameIDs.NO_DEFENSE_OPTION,            NO_DEFENSE);
    }

    public static int getAnswerFromId(int id) {
        if (buttonIdToAnswerNumMap.indexOfKey(id) < 0)
            return -1; // There is no assigned answer, so just use -1 as an answer.

        return buttonIdToAnswerNumMap.get(id);
    }
}
