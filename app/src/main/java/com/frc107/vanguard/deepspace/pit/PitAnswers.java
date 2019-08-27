package com.frc107.vanguard.deepspace.pit;

import android.util.SparseIntArray;

import com.frc107.vanguard.R;

public class PitAnswers {
    private PitAnswers() {}

    public static final int VISION_SYSTEM = 0;
    public static final int CAMERA_DRIVING = 1;
    public static final int BLIND_DRIVING = 2;
    public static final int NO_DRIVING = 3;

    public static final int ROCKET_SHIP_PREF = 0;
    public static final int CARGO_SHIP_PREF = 1;
    public static final int NO_PREF = 2;

    public static final int TOP_ROCKET = 0;
    public static final int MID_ROCKET = 1;
    public static final int BOTTOM_ROCKET = 2;
    public static final int NO_ROCKET = 3;

    public static final int TOP_HAB = 0;
    public static final int MID_HAB = 1;
    public static final int BOTTOM_HAB = 2;
    public static final int NO_HAB = 3;

    private static SparseIntArray buttonIdToAnswerNumMap = new SparseIntArray();
    static {
        buttonIdToAnswerNumMap.put(R.id.visionSystemSandstorm_Radiobtn, PitAnswers.VISION_SYSTEM);
        buttonIdToAnswerNumMap.put(R.id.cameraDrivingSandstorm_Radiobtn, PitAnswers.CAMERA_DRIVING);
        buttonIdToAnswerNumMap.put(R.id.blindDrivingSandstorm_Radiobtn, PitAnswers.BLIND_DRIVING);
        buttonIdToAnswerNumMap.put(R.id.noDrivingSandstorm_Radiobtn, PitAnswers.NO_DRIVING);

        buttonIdToAnswerNumMap.put(R.id.rocketshipPreferenceSandstorm_Radiobtn, PitAnswers.ROCKET_SHIP_PREF);
        buttonIdToAnswerNumMap.put(R.id.cargoshipPreferenceSandstorm_Radiobtn, PitAnswers.CARGO_SHIP_PREF);
        buttonIdToAnswerNumMap.put(R.id.noPreferenceSandstorm_Radiobtn, PitAnswers.NO_PREF);

        buttonIdToAnswerNumMap.put(R.id.topRocketLevelSandstorm_Radiobtn, PitAnswers.TOP_ROCKET);
        buttonIdToAnswerNumMap.put(R.id.middleRocketLevelSandstorm_Radiobtn, PitAnswers.MID_ROCKET);
        buttonIdToAnswerNumMap.put(R.id.bottomRocketLevelSandstorm_Radiobtn, PitAnswers.BOTTOM_ROCKET);
        buttonIdToAnswerNumMap.put(R.id.noRocketLevelSandstorm_Radiobtn, PitAnswers.NO_ROCKET);

        buttonIdToAnswerNumMap.put(R.id.topHabitatLevel_Radiobtn, PitAnswers.TOP_HAB);
        buttonIdToAnswerNumMap.put(R.id.middleHabitatLevel_Radiobtn, PitAnswers.MID_HAB);
        buttonIdToAnswerNumMap.put(R.id.rb_pit_bottom_hab, PitAnswers.BOTTOM_HAB);
        buttonIdToAnswerNumMap.put(R.id.rb_pit_no_hab, PitAnswers.NO_HAB);
    }

    public static int getAnswerFromId(int id) {
        if (buttonIdToAnswerNumMap.indexOfKey(id) < 0)
            return -1; // There is no assigned answer, so just use -1 as an answer.

        return buttonIdToAnswerNumMap.get(id);
    }
}
