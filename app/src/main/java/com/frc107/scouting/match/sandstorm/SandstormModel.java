package com.frc107.scouting.match.sandstorm;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.utils.StringUtils;

public class SandstormModel extends ViewModel {
    private SandstormData data = new SandstormData();

    void setMatchNumber(String matchNumberString) {
        if (StringUtils.isEmptyOrNull(matchNumberString)) {
            data.setMatchNumber(null);
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
            data.setTeamNumber(null);
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

    int getUnfinishedQuestionId() {
        if (data.getMatchNumber() == null)
            return SandstormIDs.MATCH_NUM;

        if (data.getTeamNumber() == null)
            return SandstormIDs.TEAM_NUM;

        if (data.getStartingLocation() == null)
            return SandstormIDs.STARTING_LOCATION;

        if (data.getStartingGamePiece() == null)
            return SandstormIDs.STARTING_GAME_PIECE;

        if (data.getPlacedLocation() == null)
            return SandstormIDs.PLACED_LOCATION;

        return -1;
    }

    SandstormData getData() {
        return data;
    }
}
