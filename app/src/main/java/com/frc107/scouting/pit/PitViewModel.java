package com.frc107.scouting.pit;

import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;

public class PitViewModel extends ViewModel {
    private PitModel model;

    public PitViewModel() {
        model = new PitModel();
    }

    void setTeamNumber(String teamNumberAsString) {
        model.setTeamNumber(teamNumberAsString);
    }

    void setSandstormOperation(int sandstormOperation) {
        model.setSandstormOperation(sandstormOperation);
    }

    void setSandstormPreference(int sandstormPreference) {
        model.setSandstormPreference(sandstormPreference);
    }

    void setSandstormHighestRocketLevel(int sandstormHighestRocketLevel) {
        model.setSandstormHighestRocketLevel(sandstormHighestRocketLevel);
    }

    void setHighestHabitat(int highestHabitat) {
        model.setHighestHabitat(highestHabitat);
    }

    void setHabitatTime(String habitatTime) {
        model.setHabitatTime(habitatTime);
    }

    void setProgrammingLanguage(String programmingLanguage) {
        model.setProgrammingLanguage(programmingLanguage);
    }

    void setBonus(String bonus) {
        model.setBonus(bonus);
    }

    void setComments(String comments) {
        model.setComments(comments);
    }

    boolean save() {
        return model.save();
    }

    int getUnfinishedQuestionId() {
        return model.getUnfinishedQuestionId();
    }

    File createPhotoFile() throws IOException  {
        return model.createPhotoFile();
    }

    void compressPhoto() throws IOException {
        model.compressPhoto();
    }
}
