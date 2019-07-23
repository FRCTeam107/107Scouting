package com.frc107.scouting.pit;

import android.util.Log;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.form.Form;
import com.frc107.scouting.form.field.Field;
import com.frc107.scouting.form.field.RadioField;
import com.frc107.scouting.form.field.TextField;

import java.io.File;

public class PitModel extends Form {
    private static final String FILE_NAME_HEADER = "Pit";

    private String getTeamNumber() {
        return getAnswer(R.id.pit_teamNumber_editText);
    }

    public File createPhotoFile() {
        return Scouting.FILE_SERVICE.createPhotoFile(getTeamNumber());
    }

    public boolean rotateAndCompressPhoto() {
        return Scouting.FILE_SERVICE.rotateAndCompressPhoto(getTeamNumber());
    }

    @Override
    public Field[] getFields() {
        return new Field[] {
                new TextField("pitTeamNum", R.id.pit_teamNumber_editText, true,
                        teamNum -> {
                            try {
                                int num = Integer.parseInt(teamNum);
                                Scouting.getInstance().setTeamNumber(num);
                            } catch (NumberFormatException e) {
                                Log.d("Scouting", e.getLocalizedMessage());
                            }
                        },
                        () -> Scouting.getInstance().getTeamNumber() + ""),
                new RadioField("pitSandstormOp", R.id.sandstormOperationsRadioQuestion, true,
                        new RadioField.Option(R.id.visionSystemSandstorm_Radiobtn, 0),
                        new RadioField.Option(R.id.cameraDrivingSandstorm_Radiobtn, 1),
                        new RadioField.Option(R.id.blindDrivingSandstorm_Radiobtn, 2),
                        new RadioField.Option(R.id.noDrivingSandstorm_Radiobtn, 3)),
                new RadioField("pitSandstormPref", R.id.sandstormPreferenceRadioQuestion, true,
                        new RadioField.Option(R.id.cargoshipPreferenceSandstorm_Radiobtn, 0),
                        new RadioField.Option(R.id.rocketshipPreferenceSandstorm_Radiobtn, 1),
                        new RadioField.Option(R.id.noPreferenceSandstorm_Radiobtn, 2)),
                new RadioField("pitHighestRocketLevel", R.id.highestRocketLevelSandstormRadioQuestion, true,
                        new RadioField.Option(R.id.topRocketLevelSandstorm_Radiobtn, 0),
                        new RadioField.Option(R.id.middleRocketLevelSandstorm_Radiobtn, 1),
                        new RadioField.Option(R.id.bottomRocketLevelSandstorm_Radiobtn, 2),
                        new RadioField.Option(R.id.noRocketLevelSandstorm_Radiobtn, 3)),
                new RadioField("pitHighestHabLevel", R.id.highestHabitatLevelRadioQuestion, true,
                        new RadioField.Option(R.id.topHabitatLevel_Radiobtn, 0),
                        new RadioField.Option(R.id.middleHabitatLevel_Radiobtn, 1),
                        new RadioField.Option(R.id.bottomHabitatLevel_Radiobtn, 2),
                        new RadioField.Option(R.id.noHabitatLevel_Radiobtn, 3)),
                new TextField("pitHabTime", R.id.pit_habitatTime_editText, true),
                new RadioField("pitLanguage", R.id.programmingLanguageRadioQuestion, true,
                        new RadioField.Option(R.id.javaProgrammingLanguage_Radiobtn, 0),
                        new RadioField.Option(R.id.cppProgrammingLanguage_Radiobtn, 1),
                        new RadioField.Option(R.id.labviewProgrammingLanguage_Radiobtn, 2),
                        new RadioField.Option(R.id.otherProgrammingLanguage_Radiobtn, 3)),

                new TextField("pitBonus", R.id.pit_bonusQuestion_editText, true),
                new TextField("pitComments", R.id.pit_comments_editText, true)
        };
    }

    @Override
    public void onQuestionAnswered(int questionId, Object answer) { }

    @Override
    public boolean finish() {
        String dataToWrite = getAnswerCSVRow() + '\n';
        return Scouting.FILE_SERVICE.writeData(FILE_NAME_HEADER, dataToWrite);
    }
}
