package com.frc107.scouting.match.sandstorm;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.core.utils.StringUtils;

public class SandstormModel extends ViewModel {
    private SandstormData data = new SandstormData();

    void setMatchNumber(String matchNumberString) {
        if (StringUtils.isEmptyOrNull(matchNumberString)) {
            data.setMatchNumber(-1);
            return;
        }

        try {
            data.setMatchNumber(Integer.parseInt(matchNumberString));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse \"" + matchNumberString + "\" into an integer");
        }
    }

    void setTeamNumber(String teamNumberString) {
        if (StringUtils.isEmptyOrNull(teamNumberString)) {
            data.setTeamNumber(-1);
            return;
        }

        try {
            data.setTeamNumber(Integer.parseInt(teamNumberString));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse \"" + teamNumberString + "\" into an integer");
        }
    }

    void setStartingLocation(int answerId) {
        data.setStartingLocation(SandstormAnswers.getAnswerFromId(answerId));
    }

    void setStartingGamePiece(int answerId) {
        data.setStartingGamePiece(SandstormAnswers.getAnswerFromId(answerId));
    }

    void setPlacedLocation(int answerId) {
        data.setPlacedLocation(SandstormAnswers.getAnswerFromId(answerId));
    }

    void setCrossedBaseline(boolean crossedBaseline) {
        data.setCrossedBaseline(crossedBaseline);
    }

    void clear() {
        data.clear();
    }

    int getUnfinishedQuestionId() {
        if (data.getMatchNumber() == -1)
            return SandstormIDs.MATCH_NUM;

        if (data.getTeamNumber() == -1)
            return SandstormIDs.TEAM_NUM;

        if (data.getStartingLocation() == -1)
            return SandstormIDs.STARTING_LOCATION;

        if (data.getStartingGamePiece() == -1)
            return SandstormIDs.STARTING_GAME_PIECE;

        if (data.getPlacedLocation() == -1)
            return SandstormIDs.PLACED_LOCATION;

        return -1;
    }

    SandstormData getData() {
        return data;
    }
}
