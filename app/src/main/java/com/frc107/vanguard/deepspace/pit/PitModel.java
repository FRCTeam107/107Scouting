package com.frc107.vanguard.deepspace.pit;

import com.frc107.vanguard.Vanguard;
import com.frc107.vanguard.core.Logger;
import com.frc107.vanguard.core.table.Row;
import com.frc107.vanguard.core.table.Table;
import com.frc107.vanguard.eTableType;
import com.frc107.vanguard.core.file.FileService;
import com.frc107.vanguard.core.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class PitModel {
    private static final eTableType TABLE = eTableType.PIT;

    private Integer teamNumber;
    private Integer sandstormOperation;
    private Integer sandstormPreference;
    private Integer sandstormHighestRocketLevel;
    private Integer highestHabitat;
    private String habitatTime;
    private String programmingLanguage;
    private String bonus;
    private String comments;

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
        sandstormOperation = PitAnswers.getAnswerFromId(sandstormOperationId);
    }

    void setSandstormPreference(int sandstormPreferenceId) {
        sandstormPreference = PitAnswers.getAnswerFromId(sandstormPreferenceId);
    }

    void setSandstormHighestRocketLevel(int sandstormHighestRocketLevelId) {
        sandstormHighestRocketLevel = PitAnswers.getAnswerFromId(sandstormHighestRocketLevelId);
    }

    void setHighestHabitat(int highestHabitatId) {
        highestHabitat = PitAnswers.getAnswerFromId(highestHabitatId);
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
            return PitIDs.TEAM_NUM;
        if (sandstormOperation == null)
            return PitIDs.SANDSTORM_OP;
        if (sandstormPreference == null)
            return PitIDs.SANDSTORM_PREF;
        if (sandstormHighestRocketLevel == null)
            return PitIDs.SANDSTORM_HIGHEST_ROCKET_LEVEL;
        if (highestHabitat == null)
            return PitIDs.HIGHEST_HAB;
        if (StringUtils.isEmptyOrNull(habitatTime))
            return PitIDs.HAB_TIME;
        if (StringUtils.isEmptyOrNull(programmingLanguage))
            return PitIDs.PROGRAMMING_LANG;
        if (StringUtils.isEmptyOrNull(bonus))
            return PitIDs.BONUS;
        if (StringUtils.isEmptyOrNull(comments))
            return PitIDs.COMMENTS;

        return -1;
    }

    boolean save() {
        Table table = Vanguard.getInstance().getTable(TABLE);
        Row row = table.enterNewRow(
                teamNumber,
                sandstormOperation,
                sandstormPreference,
                sandstormHighestRocketLevel,
                highestHabitat,
                habitatTime,
                programmingLanguage,
                bonus,
                comments);

        FileService fileService = Vanguard.getFileService();
        try {
            fileService.saveScoutingData(TABLE, Vanguard.getInstance().getUserInitials(), row.toString());
        } catch (IOException e) {
            Logger.log(e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    private String photoFileName;
    private boolean hasCreatedPhotoFile;
    File createPhotoFile() throws IOException {
        if (teamNumber == null)
            throw new IllegalStateException("Team number must not be null or empty");

        File file = Vanguard.getFileService().createPhotoFile(teamNumber.toString());
        if (file != null && file.exists()) {
            hasCreatedPhotoFile = true;
            photoFileName = file.getName();
        }

        return file;
    }

    void compressPhoto() throws IOException {
        if (!hasCreatedPhotoFile)
            throw new FileNotFoundException("Photo file named \"" + teamNumber.toString() + ".jpg\" has not been created yet!");

        Vanguard.getFileService().compressPhoto(photoFileName);
    }
}