package com.frc107.scouting.pit;

import android.util.Log;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.form.Form;
import com.frc107.scouting.form.Table;
import com.frc107.scouting.form.field.Field;
import com.frc107.scouting.form.field.RadioField;
import com.frc107.scouting.form.field.TextField;
import com.frc107.scouting.utils.FileService;
import com.frc107.scouting.utils.StringUtils;

import java.io.File;

public class PitModel extends Form {
    private static final String FILE_NAME_HEADER = "Pit";

    private Integer teamNumber;
    private Integer sandstormOperation;
    private Integer sandstormPreference;
    private Integer sandstormHighestRocketLevel;
    private Integer highestHabitat;
    private String habitatTime;
    private String programmingLanguage;
    private String bonus;
    private String comments;

    public void setTeamNumber(String teamNumberAsString) {
        try {
            teamNumber = Integer.parseInt(teamNumberAsString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("teamNumberAsString with value \"" + teamNumberAsString + "\" could not be parsed into an integer");
        }
    }

    public void setSandstormOperation(int sandstormOperation) {
        this.sandstormOperation = sandstormOperation;
    }

    public void setSandstormPreference(int sandstormPreference) {
        this.sandstormPreference = sandstormPreference;
    }

    public void setSandstormHighestRocketLevel(int sandstormHighestRocketLevel) {
        this.sandstormHighestRocketLevel = sandstormHighestRocketLevel;
    }

    public void setHighestHabitat(int highestHabitat) {
        this.highestHabitat = highestHabitat;
    }

    public void setHabitatTime(String habitatTime) {
        this.habitatTime = habitatTime;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isFinished() {
        return teamNumber != null &&
                sandstormOperation != null &&
                sandstormPreference != null &&
                sandstormHighestRocketLevel != null &&
                highestHabitat != null &&
                !StringUtils.isEmptyOrNull(habitatTime) &&
                !StringUtils.isEmptyOrNull(programmingLanguage) &&
                !StringUtils.isEmptyOrNull(bonus) &&
                !StringUtils.isEmptyOrNull(comments);
    }

    public boolean save() {
        Table table = Scouting.getInstance().getTable(Scouting.eTable.PIT);
        table.enterValues(
                teamNumber,
                sandstormOperation,
                sandstormPreference,
                sandstormHighestRocketLevel,
                highestHabitat,
                habitatTime,
                programmingLanguage,
                bonus,
                comments);

        String entry = table.getLastRowAsString();
        Scouting.getInstance().getFileService().writeToEndOfFile(FileService.eFile.PIT, entry);
        return true;
    }

    private String getTeamNumber() {
        return getAnswer(R.id.pit_team_number);
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
                new TextField("pitTeamNum", R.id.pit_team_number, true,
                        teamNum -> {
                            try {
                                int num = Integer.parseInt(teamNum);
                                Scouting.getInstance().setTeamNumber(num);
                            } catch (NumberFormatException e) {
                                Log.d("Scouting", e.getLocalizedMessage());
                            }
                        },
                        () -> Scouting.getInstance().getTeamNumber() + ""),
                new RadioField("pitSandstormOp", R.id.pit_sandstorm_op, true,
                        new RadioField.Option(R.id.visionSystemSandstorm_Radiobtn, 0),
                        new RadioField.Option(R.id.cameraDrivingSandstorm_Radiobtn, 1),
                        new RadioField.Option(R.id.blindDrivingSandstorm_Radiobtn, 2),
                        new RadioField.Option(R.id.noDrivingSandstorm_Radiobtn, 3)),
                new RadioField("pitSandstormPref", R.id.pit_sandstorm_preference, true,
                        new RadioField.Option(R.id.cargoshipPreferenceSandstorm_Radiobtn, 0),
                        new RadioField.Option(R.id.rocketshipPreferenceSandstorm_Radiobtn, 1),
                        new RadioField.Option(R.id.noPreferenceSandstorm_Radiobtn, 2)),
                new RadioField("pitHighestRocketLevel", R.id.pit_sandstorm_highest_rocket_level, true,
                        new RadioField.Option(R.id.topRocketLevelSandstorm_Radiobtn, 0),
                        new RadioField.Option(R.id.middleRocketLevelSandstorm_Radiobtn, 1),
                        new RadioField.Option(R.id.bottomRocketLevelSandstorm_Radiobtn, 2),
                        new RadioField.Option(R.id.noRocketLevelSandstorm_Radiobtn, 3)),
                new RadioField("pitHighestHabLevel", R.id.pit_highest_habitat, true,
                        new RadioField.Option(R.id.topHabitatLevel_Radiobtn, 0),
                        new RadioField.Option(R.id.middleHabitatLevel_Radiobtn, 1),
                        new RadioField.Option(R.id.bottomHabitatLevel_Radiobtn, 2),
                        new RadioField.Option(R.id.noHabitatLevel_Radiobtn, 3)),
                new TextField("pitHabTime", R.id.pit_habitat_time, true),
                new RadioField("pitLanguage", R.id.pit_programming_language, true,
                        new RadioField.Option(R.id.javaProgrammingLanguage_Radiobtn, 0),
                        new RadioField.Option(R.id.cppProgrammingLanguage_Radiobtn, 1),
                        new RadioField.Option(R.id.labviewProgrammingLanguage_Radiobtn, 2),
                        new RadioField.Option(R.id.otherProgrammingLanguage_Radiobtn, 3)),

                new TextField("pitBonus", R.id.pit_bonus, true),
                new TextField("pitComments", R.id.pit_comments, true)
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
