package com.frc107.scouting.pit;

import com.frc107.scouting.form.BaseViewModel;

import java.io.File;

public class PitViewModel extends BaseViewModel {
    public PitViewModel() {
        model = new PitModel();
    }

    public File createPhotoFile() {
        return ((PitModel) model).createPhotoFile();
    }

    public boolean rotateAndCompressPhoto() {
        return ((PitModel) model).rotateAndCompressPhoto();
    }
}
