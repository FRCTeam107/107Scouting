package com.frc107.scouting.analysis.attribute;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.analysis.attribute.AttributeAnalysisModel.AnalysisElement;
import com.frc107.scouting.callbacks.ICallbackWithParam;

import java.util.ArrayList;

public class AttributeAnalysisViewModel extends ViewModel {
    private AttributeAnalysisModel model;

    public AttributeAnalysisViewModel() { }

    public void initialize(ICallback onDataLoaded, ICallback refreshUI, ICallbackWithParam<String> onError) {
        model = new AttributeAnalysisModel(onDataLoaded, refreshUI, onError);
    }

    public boolean hasDataBeenLoaded() {
        return model.hasDataBeenLoaded();
    }

    public void loadData() {
        model.loadData();
    }

    public ArrayList<AnalysisElement> getElements() {
        return model.getElements();
    }

    public void setAttributeAndUpdateElements(int attribute) {
        model.setAttributeAndUpdateElements(attribute);
    }

    public void setTeamNumberAndUpdateElements(int which) {
        model.setTeamNumberAndUpdateElements(which);
    }

    public int getCurrentAttributeType() {
        return model.getCurrentAttributeType();
    }

    public String[] getAttributeNames() {
        return model.getAttributeNames();
    }

    public String[] getTeamNumbers() {
        return model.getTeamNumbers();
    }
}
