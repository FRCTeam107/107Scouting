package com.frc107.scouting.analysis.attribute;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.frc107.scouting.ui.IUIListener;

import java.util.ArrayList;

public class AttributeAnalysisViewModel extends AndroidViewModel {
    private AttributeAnalysisModel model;

    public AttributeAnalysisViewModel(Application application) {
        super(application);
        model = new AttributeAnalysisModel();
    }

    public void loadData() {
        model.loadData();
    }

    public ArrayList<AnalysisElement> getElements() {
        return model.getElements();
    }

    public MutableLiveData<ArrayList<AnalysisElement>> getElementsLiveData() {
        return model.getElementsLiveData();
    }

    public boolean isDataLoaded() {
        return model.isDataLoaded();
    }

    public MutableLiveData<Boolean> getDataLoadedLiveData() {
        return model.getDataLoadedLiveData();
    }

    public void setAttribute(int attribute) {
        model.setAttribute(attribute);
    }

    public int getCurrentAttributeType() {
        return model.getCurrentAttributeType();
    }
}
