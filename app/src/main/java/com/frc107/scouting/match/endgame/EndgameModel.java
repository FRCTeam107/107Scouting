package com.frc107.scouting.match.endgame;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.form.Table;
import com.frc107.scouting.form.eTable;
import com.frc107.scouting.match.cycle.CyclesData;
import com.frc107.scouting.match.sandstorm.SandstormData;

import java.io.IOException;

public class EndgameModel extends ViewModel {
    private SandstormData sandstormData;
    private CyclesData cyclesData;

    void setSandstormAndCyclesData(SandstormData sandstormData, CyclesData cyclesData) {
        this.sandstormData = sandstormData;
        this.cyclesData = cyclesData;
    }

    private int habLevel = -1;
    private int defenseRating = -1;
    private boolean defenseAllMatch;
    private boolean fouls;

    void setHabLevel(int habLevelId) {
        habLevel = EndgameAnswers.getAnswerFromId(habLevelId);
    }

    void setDefenseRating(int defenseRatingId) {
        defenseRating = EndgameAnswers.getAnswerFromId(defenseRatingId);
    }

    void setDefenseAllMatch(boolean defenseAllMatch) {
        this.defenseAllMatch = defenseAllMatch;
    }

    void setFouls(boolean fouls) {
        this.fouls = fouls;
    }

    void clear() {
        habLevel = -1;
        defenseRating = -1;
        defenseAllMatch = false;
        fouls = false;
    }

    boolean save() {
        Table matchTable = Scouting.getInstance().getTable(eTable.MATCH);

        int matchNumber = sandstormData.getMatchNumber();
        int teamNumber = sandstormData.getTeamNumber();
        int startingLocation = sandstormData.getStartingLocation();
        int startingGamePiece = sandstormData.getStartingGamePiece();
        int placedLocation = sandstormData.getPlacedLocation();
        boolean crossedBaseline = sandstormData.getCrossedBaseline();

        int maxCycles = 567;
        double opr = 123.456;
        double dpr = 654.321;
        String initials = Scouting.getInstance().getUserInitials();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cyclesData.getCycleNum(); i++) {
            int cycleNum = i + 1;
            int pickupLocation = cyclesData.getPickupLocation(i);
            int itemPickedUp = cyclesData.getItemPickedUp(i);
            int locationPlaced = cyclesData.getLocationPlaced(i);
            boolean defense = cyclesData.getDefense(i);

            String row = matchTable.enterNewRow(matchNumber,
                                                teamNumber,
                                                startingLocation,
                                                startingGamePiece,
                                                placedLocation,
                                                crossedBaseline,
                                                cycleNum,
                                                pickupLocation,
                                                itemPickedUp,
                                                locationPlaced,
                                                defense,
                                                habLevel,
                                                defenseAllMatch,
                                                defenseRating,
                                                fouls,
                                                maxCycles,
                                                initials,
                                                opr,
                                                dpr);

            builder.append(row);
            builder.append(ScoutingStrings.NEW_LINE);
        }

        try {
            Scouting.FILE_SERVICE.saveScoutingData(eTable.MATCH, initials, builder.toString());
        } catch (IOException e) {
            Log.e(ScoutingStrings.SCOUTING_TAG, e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    int getUnfinishedQuestionId() {
        if (habLevel == -1)
            return EndgameIDs.HAB_LEVEL_QUESTION;

        if (defenseRating == -1)
            return EndgameIDs.DEFENSE_RATING_QUESTION;

        return -1;
    }
}
