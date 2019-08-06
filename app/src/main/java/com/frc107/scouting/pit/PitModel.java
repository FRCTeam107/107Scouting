package com.frc107.scouting.pit;

import android.util.Log;
import android.util.SparseIntArray;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.ScoutingStrings;
import com.frc107.scouting.form.Table;
import com.frc107.scouting.form.eTable;
import com.frc107.scouting.file.FileService;
import com.frc107.scouting.utils.StringUtils;

import java.io.File;
import java.io.IOException;

class PitModel {
    private static final eTable TABLE = eTable.PIT;

    private Integer teamNumber;
    private Integer sandstormOperation;
    private Integer sandstormPreference;
    private Integer sandstormHighestRocketLevel;
    private Integer highestHabitat;
    private String habitatTime;
    private String programmingLanguage;
    private String bonus;
    private String comments;

    private static SparseIntArray radioButtonMappings = new SparseIntArray();

    static {
        radioButtonMappings.put(R.id.visionSystemSandstorm_Radiobtn, PitAnswers.VISION_SYSTEM);
        radioButtonMappings.put(R.id.cameraDrivingSandstorm_Radiobtn, PitAnswers.CAMERA_DRIVING);
        radioButtonMappings.put(R.id.blindDrivingSandstorm_Radiobtn, PitAnswers.BLIND_DRIVING);
        radioButtonMappings.put(R.id.noDrivingSandstorm_Radiobtn, PitAnswers.NO_DRIVING);

        radioButtonMappings.put(R.id.rocketshipPreferenceSandstorm_Radiobtn, PitAnswers.ROCKET_SHIP_PREF);
        radioButtonMappings.put(R.id.cargoshipPreferenceSandstorm_Radiobtn, PitAnswers.CARGO_SHIP_PREF);
        radioButtonMappings.put(R.id.noPreferenceSandstorm_Radiobtn, PitAnswers.NO_PREF);

        radioButtonMappings.put(R.id.topRocketLevelSandstorm_Radiobtn, PitAnswers.TOP_ROCKET);
        radioButtonMappings.put(R.id.middleRocketLevelSandstorm_Radiobtn, PitAnswers.MID_ROCKET);
        radioButtonMappings.put(R.id.bottomRocketLevelSandstorm_Radiobtn, PitAnswers.BOTTOM_ROCKET);
        radioButtonMappings.put(R.id.noRocketLevelSandstorm_Radiobtn, PitAnswers.NO_ROCKET);

        radioButtonMappings.put(R.id.topHabitatLevel_Radiobtn, PitAnswers.TOP_HAB);
        radioButtonMappings.put(R.id.middleHabitatLevel_Radiobtn, PitAnswers.MID_HAB);
        radioButtonMappings.put(R.id.rb_pit_bottom_hab, PitAnswers.BOTTOM_HAB);
        radioButtonMappings.put(R.id.rb_pit_no_hab, PitAnswers.NO_HAB);
    }

    void setTeamNumber(String teamNumberAsString) {
        if (StringUtils.isEmptyOrNull(teamNumberAsString)) {
            teamNumber = null;
            return;
        }

        try {
            teamNumber = Integer.parseInt(teamNumberAsString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("teamNumberAsString with value \"" + teamNumberAsString + "\" could not be parsed into an integer");
        }
    }

    void setSandstormOperation(int sandstormOperationId) {
        sandstormOperation = radioButtonMappings.get(sandstormOperationId);
    }

    void setSandstormPreference(int sandstormPreferenceId) {
        sandstormPreference = radioButtonMappings.get(sandstormPreferenceId);
    }

    void setSandstormHighestRocketLevel(int sandstormHighestRocketLevelId) {
        sandstormHighestRocketLevel = radioButtonMappings.get(sandstormHighestRocketLevelId);
    }

    void setHighestHabitat(int highestHabitatId) {
        highestHabitat = radioButtonMappings.get(highestHabitatId);
    }

    void setHabitatTime(String habitatTime) {
        this.habitatTime = habitatTime;
    }

    void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    void setBonus(String bonus) {
        this.bonus = bonus;
    }

    void setComments(String comments) {
        this.comments = comments;
    }

    int getUnfinishedQuestionId() {
        if (teamNumber == null)
            return R.id.pit_team_number;
        if (sandstormOperation == null)
            return R.id.pit_sandstorm_op;
        if (sandstormPreference == null)
            return R.id.pit_sandstorm_preference;
        if (sandstormHighestRocketLevel == null)
            return R.id.pit_sandstorm_highest_rocket_level;
        if (highestHabitat == null)
            return R.id.pit_highest_habitat;
        if (StringUtils.isEmptyOrNull(habitatTime))
            return R.id.edittext_pit_habitat_time;
        if (StringUtils.isEmptyOrNull(programmingLanguage))
            return R.id.edittext_pit_programming_language;
        if (StringUtils.isEmptyOrNull(bonus))
            return R.id.edittext_pit_bonus;
        if (StringUtils.isEmptyOrNull(comments))
            return R.id.edittext_pit_comments;

        return -1;
    }

    boolean save() {
        Table table = Scouting.getInstance().getTable(TABLE);
        String entry = table.enterNewRow(
                teamNumber,
                sandstormOperation,
                sandstormPreference,
                sandstormHighestRocketLevel,
                highestHabitat,
                habitatTime,
                programmingLanguage,
                bonus,
                comments);

        FileService fileService = Scouting.getInstance().getFileService();
        try {
            fileService.saveScoutingData(TABLE, Scouting.getInstance().getUserInitials(), entry);
            return true;
        } catch (IOException e) {
            Log.d(ScoutingStrings.SCOUTING_TAG, e.getLocalizedMessage());
            return false;
        }
    }

    private String photoFileName;
    private boolean hasCreatedPhotoFile;
    File createPhotoFile() {
        if (teamNumber == null)
            throw new IllegalStateException("Team number must not be null or empty");

        File file = Scouting.FILE_SERVICE.createPhotoFile(teamNumber.toString());
        if (file != null && file.exists()) {
            hasCreatedPhotoFile = true;
            photoFileName = file.getName();
        }

        return file;
    }

    boolean compressPhoto() {
        if (!hasCreatedPhotoFile)
            throw new IllegalStateException("Photo file has not been created yet!");

        return Scouting.FILE_SERVICE.compressPhoto(photoFileName);
    }
}
