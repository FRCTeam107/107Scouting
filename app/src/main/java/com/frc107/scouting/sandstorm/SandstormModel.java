package com.frc107.scouting.sandstorm;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.model.BaseModel;
import com.frc107.scouting.model.question.NumberQuestion;
import com.frc107.scouting.model.question.Question;
import com.frc107.scouting.model.question.RadioQuestion;
import com.frc107.scouting.model.question.ToggleQuestion;

public class SandstormModel extends BaseModel {
    private boolean startedWithGamePiece;
    private boolean hasPlacedStartingGamePiece;

    public SandstormModel() {
        super();
    }

    @Override
    public Question[] getQuestions() {
        NumberQuestion matchNumQuestion = new NumberQuestion("sandstormMatchNumber", R.id.matchNumberEditText, true);
        NumberQuestion teamNumQuestion = new NumberQuestion("sandstormTeamNumber", R.id.teamNumberEditText, true);
        matchNumQuestion.addIllegalValue(0);
        teamNumQuestion.addIllegalValue(0);

        return new Question[] {
                matchNumQuestion,
                teamNumQuestion,
                new RadioQuestion("sandstormStartPos", R.id.sandstormStartingPositionRadioQuestion, true,
                        new RadioQuestion.Option(R.id.habTwoSandstorm_Radiobtn, Scouting.SANDSTORM_HAB_TWO),
                        new RadioQuestion.Option(R.id.habOneSandstorm_Radiobtn, Scouting.SANDSTORM_HAB_ONE)),
                new RadioQuestion("sandstormStartPiece", R.id.sandstormStartingGamePieceRadioQuestion, true,
                        new RadioQuestion.Option(R.id.cargoSandstormStartingGamePiece_Radiobtn, Scouting.SANDSTORM_CARGO),
                        new RadioQuestion.Option(R.id.panelSandstormStartingGamePiece_Radiobtn, Scouting.SANDSTORM_PANEL),
                        new RadioQuestion.Option(R.id.noSandstormStartingGamePiece_Radiobtn, Scouting.SANDSTORM_NO_STARTING_PIECE)),
                new RadioQuestion("sandstormItemPlaced", R.id.sandstormItemPlacedRadioQuestion, true,
                        new RadioQuestion.Option(R.id.sandstormTopRocketItemPlaced_Radiobtn, Scouting.SANDSTORM_TOP_ROCKET),
                        new RadioQuestion.Option(R.id.sandstormMiddleRocketItemPlaced_Radiobtn, Scouting.SANDSTORM_MIDDLE_ROCKET),
                        new RadioQuestion.Option(R.id.sandstormBottomRocketItemPlaced_Radiobtn, Scouting.SANDSTORM_BOTTOM_ROCKET),
                        new RadioQuestion.Option(R.id.sandstormCargoshipItemPlaced_Radiobtn, Scouting.SANDSTORM_CARGO_SHIP),
                        new RadioQuestion.Option(R.id.sandstormFloorItemPlaced_Radiobtn, Scouting.SANDSTORM_FLOOR),
                        new RadioQuestion.Option(R.id.sandstormNothingPlacedItemPlaced_Radiobtn, Scouting.SANDSTORM_NOTHING_PLACED)),
                new ToggleQuestion("sandstormBaseline", R.id.sandstormBaseline_chkbx)
        };
    }

    @Override
    public void onNumberQuestionAnswered(int questionId, Integer answer) {
        if (questionId == R.id.matchNumberEditText) {
            int matchNum = answer == null ? -1 : answer;
            Scouting.getInstance().setMatchNumber(matchNum);
        }
    }

    @Override
    public void onTextQuestionAnswered(int questionId, String answer) { }

    @Override
    public void onRadioQuestionAnswered(int questionId, int answerId) {
        switch (questionId) {
            case R.id.sandstormStartingGamePieceRadioQuestion:
                startedWithGamePiece = answerId != R.id.noSandstormStartingGamePiece_Radiobtn;
                break;
            case R.id.sandstormItemPlacedRadioQuestion:
                hasPlacedStartingGamePiece = answerId != R.id.sandstormNothingPlacedItemPlaced_Radiobtn;
                break;
            default:
                break;
        }
    }

    public int getTeamNumber() {
        Integer teamNumber = (Integer) getQuestion(R.id.teamNumberEditText).getAnswer();
        if (teamNumber == null)
            return -1;

        return teamNumber;
    }

    @Override
    public boolean finish() {
        String sandstormData = getAnswerCSVRow();
        Scouting.getInstance().setSandstormData(sandstormData);
        return true;
    }

    public boolean hasUsedStartingItem() {
        return !startedWithGamePiece || hasPlacedStartingGamePiece;
    }
}
