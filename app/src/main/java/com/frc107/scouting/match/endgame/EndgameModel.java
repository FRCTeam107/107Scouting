package com.frc107.scouting.match.endgame;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.core.table.Row;
import com.frc107.scouting.core.table.Table;
import com.frc107.scouting.core.table.eTableType;
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
        Table matchTable = Scouting.getInstance().getTable(eTableType.MATCH);

        int matchNumber = sandstormData.getMatchNumber();
        int teamNumber = sandstormData.getTeamNumber();
        int startingLocation = sandstormData.getStartingLocation();
        int startingGamePiece = sandstormData.getStartingGamePiece();
        int placedLocation = sandstormData.getPlacedLocation();
        int crossedBaseline = sandstormData.getCrossedBaseline() ? 1 : 0;

        int maxCycles = 567;
        String initials = Scouting.getInstance().getUserInitials();

        int defenseAllMatchInt = defenseAllMatch ? 1 : 0;
        int foulsInt = fouls ? 1 : 0;

        StringBuilder builder = new StringBuilder();

        Row sandstormRow = matchTable.enterNewRow(
                matchNumber,
                teamNumber,
                startingLocation,
                startingGamePiece,
                placedLocation,
                crossedBaseline,
                -1,
                -1,
                -1,
                -1,
                -1,
                habLevel,
                defenseAllMatchInt,
                defenseRating,
                foulsInt,
                maxCycles,
                initials);

        builder.append(sandstormRow.toString());
        builder.append(ScoutingStrings.NEW_LINE);

        for (int i = 0; i < cyclesData.getCycleAmount(); i++) {
            int cycleNum = i + 1;
            int pickupLocation = cyclesData.getPickupLocation(i);
            int itemPickedUp = cyclesData.getItemPickedUp(i);
            int locationPlaced = cyclesData.getLocationPlaced(i);
            int defense = cyclesData.getDefense(i) ? 1 : 0;

            Row cycleRow = matchTable.enterNewRow(
                    matchNumber,
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
                    defenseAllMatchInt,
                    defenseRating,
                    foulsInt,
                    maxCycles,
                    initials);

            builder.append(cycleRow.toString());
            builder.append(ScoutingStrings.NEW_LINE);
        }

        try {
            Scouting.getFileService().saveScoutingData(eTableType.MATCH, initials, builder.toString());
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
