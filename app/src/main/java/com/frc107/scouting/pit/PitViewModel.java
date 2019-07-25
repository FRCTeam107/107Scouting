package com.frc107.scouting.pit;

import com.frc107.scouting.form.FormViewModel;

import java.io.File;

public class PitViewModel {
    private PitModel model;

    public PitViewModel() {
        model = new PitModel();
    }

    public void setTeamNumber(String teamNumberAsString) {
        model.setTeamNumber(teamNumberAsString);
    }

    public void setSandstormOperation(int sandstormOperation) {
        model.setSandstormOperation(sandstormOperation);
    }

    public void setSandstormPreference(int sandstormPreference) {
        model.setSandstormPreference(sandstormPreference);
    }

    public void setSandstormHighestRocketLevel(int sandstormHighestRocketLevel) {
        model.setSandstormHighestRocketLevel(sandstormHighestRocketLevel);
    }

    public void setHighestHabitat(int highestHabitat) {
        model.setHighestHabitat(highestHabitat);
    }

    public void setHabitatTime(String habitatTime) {
        model.setHabitatTime(habitatTime);
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        model.setProgrammingLanguage(programmingLanguage);
    }

    public void setBonus(String bonus) {
        model.setBonus(bonus);
    }

    public void setComments(String comments) {
        model.setComments(comments);
    }

    public boolean save() {
        return model.save();
    }

    public boolean isFinished() {
        return model.isFinished();
    }

    public File createPhotoFile() {
        return ((PitModel) model).createPhotoFile();
    }

    public boolean rotateAndCompressPhoto() {
        return ((PitModel) model).rotateAndCompressPhoto();
    }
}
