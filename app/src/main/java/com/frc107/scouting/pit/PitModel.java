package com.frc107.scouting.pit;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.form.Table;
import com.frc107.scouting.utils.FileService;
import com.frc107.scouting.utils.StringUtils;

import java.io.File;

public class PitModel {
    private static final Scouting.eTable TABLE = Scouting.eTable.PIT;
    private static final FileService.eFile FILE = FileService.eFile.PIT;

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
        boolean success;
        if (fileService.fileExists(FILE))
            success = fileService.writeToEndOfFile(FILE, entry);
        else
            success = fileService.writeDataToNewFile(FILE, entry);
        return success;
    }

    private String photoFileName;
    private boolean hasCreatedPhotoFile;
    public File createPhotoFile() {
        if (teamNumber == null)
            throw new IllegalStateException("Team number must not be null or empty");

        File file = Scouting.FILE_SERVICE.createPhotoFile(teamNumber.toString());
        if (file != null && file.exists()) {
            hasCreatedPhotoFile = true;
            photoFileName = file.getName();
        }

        return file;
    }

    public boolean rotateAndCompressPhoto() {
        if (!hasCreatedPhotoFile)
            throw new IllegalStateException("Photo file has not been created yet!");

        return Scouting.FILE_SERVICE.rotateAndCompressPhoto(photoFileName);
    }
}
