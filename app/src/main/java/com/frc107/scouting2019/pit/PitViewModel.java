package com.frc107.scouting2019.pit;

import com.frc107.scouting2019.viewmodel.BaseViewModel;

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
