package com.frc107.scouting.endgame;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.model.BaseModel;
import com.frc107.scouting.model.FormStatus;
import com.frc107.scouting.model.question.Question;
import com.frc107.scouting.model.question.RadioQuestion;
import com.frc107.scouting.model.question.ToggleQuestion;

import java.util.ArrayList;

public class EndGameModel extends BaseModel {
    private static final String FILE_NAME_HEADER = "Match";

    @Override
    public Question[] getQuestions() {
        Question[] questions = {

                new RadioQuestion("endgameHabitatLevel", R.id.endGameHabitatLevelRadioQuestion, true,
                        new RadioQuestion.Option(R.id.habOneEndGame_Radiobtn, Scouting.ENDGAME_HAB_ONE),
                        new RadioQuestion.Option(R.id.habTwoEndGame_Radiobtn, Scouting.ENDGAME_HAB_TWO),
                        new RadioQuestion.Option(R.id.habThreeEndGame_Radiobtn, Scouting.ENDGAME_HAB_THREE),
                        new RadioQuestion.Option(R.id.habNoneEndGame_Radiobtn, Scouting.ENDGAME_HAB_NONE)),
                new ToggleQuestion("endgameDefenseAllMatch", R.id.endGameDefenseAllMatch_chkbx),
                new RadioQuestion("endgameDefense", R.id.endGameDefenseRadioQuestion, true,
                        new RadioQuestion.Option(R.id.endGameDefenseEffective_Radiobtn, Scouting.ENDGAME_DEFENSE_EFFECTIVE),
                        new RadioQuestion.Option(R.id.endGameDefenseIneffective_Radiobtn, Scouting.ENDGAME_DEFENSE_INEFFECTIVE),
                        new RadioQuestion.Option(R.id.endGameDefenseNone_Radiobtn, Scouting.ENDGAME_DEFENSE_NONE)),
                new ToggleQuestion("endgameFouls", R.id.endGameFouls_chkbx)

        };
        return questions;
    }

    @Override
    public void onNumberQuestionAnswered(int questionId, Integer answer) { }

    @Override
    public void onTextQuestionAnswered(int questionId, String answer) { }

    @Override
    public void onRadioQuestionAnswered(int questionId, int answerId) { }

    @Override
    public FormStatus getFormStatus() {
        int unfinishedQuestionId = getFirstUnfinishedQuestionId();
        FormStatus status = new FormStatus(unfinishedQuestionId);

        if (unfinishedQuestionId == -1) {
            status.setFinished();
        }

        return status;
    }

    public String finish() {
        StringBuilder builder = new StringBuilder();

        String sandstormData = Scouting.getInstance().getSandstormData();
        ArrayList<String> cycles = Scouting.getInstance().getCycles();
        if (cycles.size() == 0) {
            String cycle = "-1,-1,-1,-1,-1";
            String maxCycles = cycles.size() + "";
            if (Scouting.SAVE_QUESTION_NAMES_AS_ANSWERS)
                maxCycles = "maxCycles";

            String row = sandstormData + "," + cycle + "," + getAnswerCSVRow() + "," + maxCycles + "," + Scouting.getInstance().getInitials();
            builder.append(row);
            builder.append("\n");
        }

        for (int i = 0; i < cycles.size(); i++) {
            String cycle = cycles.get(i);
            String maxCycles = cycles.size() + "";
            if (Scouting.SAVE_QUESTION_NAMES_AS_ANSWERS)
                maxCycles = "maxCycles";

            String row = sandstormData + "," + cycle + "," + getAnswerCSVRow() + "," + maxCycles + "," + Scouting.getInstance().getInitials();
            builder.append(row);
            builder.append("\n");
        }

        Scouting.getInstance().clearCycles();
        int matchNum = Scouting.getInstance().getMatchNumber();
        Scouting.getInstance().setMatchNumber(matchNum + 1);

        return Scouting.FILE_UTILS.writeData(FILE_NAME_HEADER, builder.toString());
    }
}
