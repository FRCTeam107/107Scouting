package com.frc107.scouting.pit;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.model.BaseModel;
import com.frc107.scouting.model.question.Question;
import com.frc107.scouting.model.question.RadioQuestion;
import com.frc107.scouting.model.question.TextQuestion;

import java.io.File;

public class PitModel extends BaseModel {
    private static final String FILE_NAME_HEADER = "Pit";

    private String getTeamNumber() {
        return getAnswer(R.id.pit_teamNumber_editText);
    }

    public File createPhotoFile() {
        return Scouting.FILE_UTILS.createPhotoFile(getTeamNumber());
    }

    public boolean rotateAndCompressPhoto() {
        return Scouting.FILE_UTILS.rotateAndCompressPhoto(getTeamNumber());
    }

    @Override
    public Question[] getQuestions() {
        return new Question[] {
                new TextQuestion("pitTeamNum", R.id.pit_teamNumber_editText, true),
                new RadioQuestion("pitSandstormOp", R.id.sandstormOperationsRadioQuestion, true,
                        new RadioQuestion.Option(R.id.visionSystemSandstorm_Radiobtn, 0),
                        new RadioQuestion.Option(R.id.cameraDrivingSandstorm_Radiobtn, 1),
                        new RadioQuestion.Option(R.id.blindDrivingSandstorm_Radiobtn, 2),
                        new RadioQuestion.Option(R.id.noDrivingSandstorm_Radiobtn, 3)),
                new RadioQuestion("pitSandstormPref", R.id.sandstormPreferenceRadioQuestion, true,
                        new RadioQuestion.Option(R.id.cargoshipPreferenceSandstorm_Radiobtn, 0),
                        new RadioQuestion.Option(R.id.rocketshipPreferenceSandstorm_Radiobtn, 1),
                        new RadioQuestion.Option(R.id.noPreferenceSandstorm_Radiobtn, 2)),
                new RadioQuestion("pitHighestRocketLevel", R.id.highestRocketLevelSandstormRadioQuestion, true,
                        new RadioQuestion.Option(R.id.topRocketLevelSandstorm_Radiobtn, 0),
                        new RadioQuestion.Option(R.id.middleRocketLevelSandstorm_Radiobtn, 1),
                        new RadioQuestion.Option(R.id.bottomRocketLevelSandstorm_Radiobtn, 2),
                        new RadioQuestion.Option(R.id.noRocketLevelSandstorm_Radiobtn, 3)),
                new RadioQuestion("pitHighestHabLevel", R.id.highestHabitatLevelRadioQuestion, true,
                        new RadioQuestion.Option(R.id.topHabitatLevel_Radiobtn, 0),
                        new RadioQuestion.Option(R.id.middleHabitatLevel_Radiobtn, 1),
                        new RadioQuestion.Option(R.id.bottomHabitatLevel_Radiobtn, 2),
                        new RadioQuestion.Option(R.id.noHabitatLevel_Radiobtn, 3)),
                new TextQuestion("pitHabTime", R.id.pit_habitatTime_editText, true),
                new RadioQuestion("pitLanguage", R.id.programmingLanguageRadioQuestion, true,
                        new RadioQuestion.Option(R.id.javaProgrammingLanguage_Radiobtn, 0),
                        new RadioQuestion.Option(R.id.cppProgrammingLanguage_Radiobtn, 1),
                        new RadioQuestion.Option(R.id.labviewProgrammingLanguage_Radiobtn, 2),
                        new RadioQuestion.Option(R.id.otherProgrammingLanguage_Radiobtn, 3)),

                new TextQuestion("pitBonus", R.id.pit_bonusQuestion_editText, true),
                new TextQuestion("pitComments", R.id.pit_comments_editText, true)
        };
    }

    @Override
    public void onNumberQuestionAnswered(int questionId, Integer answer) { }

    @Override
    public void onTextQuestionAnswered(int questionId, String answer) { }

    @Override
    public void onRadioQuestionAnswered(int questionId, int answerId) { }

    @Override
    public boolean finish() {
        String dataToWrite = getAnswerCSVRow() + '\n';
        return Scouting.FILE_UTILS.writeData(FILE_NAME_HEADER, dataToWrite);
    }
}