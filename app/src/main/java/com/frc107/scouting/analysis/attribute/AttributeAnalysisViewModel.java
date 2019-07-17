package com.frc107.scouting.analysis.attribute;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.frc107.scouting.utils.ISimpleCallback;
import com.frc107.scouting.analysis.attribute.AttributeAnalysisModel.AnalysisElement;

import java.util.ArrayList;

public class AttributeAnalysisViewModel extends AndroidViewModel implements ISimpleCallback {
    private AttributeAnalysisModel model;
    private MutableLiveData<Boolean> dataLoadedLiveData;
    private MutableLiveData<Integer> attributeLiveData;

    public AttributeAnalysisViewModel(Application application) {
        super(application);
        model = new AttributeAnalysisModel(this);
        dataLoadedLiveData = new MutableLiveData<>();
        attributeLiveData = new MutableLiveData<>();
    }

    public void loadData() {
        model.loadData();
    }

    public ArrayList<AnalysisElement> getElements() {
        return model.getElements();
    }

    public boolean isDataLoaded() {
        return model.isDataLoaded();
    }

    public MutableLiveData<Boolean> getDataLoadedLiveData() {
        return dataLoadedLiveData;
    }

    public void setAttributeAndUpdateElements(int attribute) {
        model.setAttributeAndUpdateElements(attribute);
        attributeLiveData.setValue(attribute);
    }

    public int getCurrentAttributeType() {
        return model.getCurrentAttributeType();
    }

    public MutableLiveData<Integer> getAttributeLiveData() {
        return attributeLiveData;
    }

    @Override
    public void callback(boolean success) {
        if (dataLoadedLiveData.getValue() == null || dataLoadedLiveData.getValue() != success)
            dataLoadedLiveData.setValue(success);
    }
}
