package com.frc107.scouting.analysis.attribute;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.analysis.attribute.AttributeAnalysisModel.AnalysisElement;
import com.frc107.scouting.callbacks.ICallbackWithParam;

import java.util.ArrayList;

public class AttributeAnalysisViewModel extends ViewModel {
    private AttributeAnalysisModel model;

    public AttributeAnalysisViewModel() {
        model = new AttributeAnalysisModel();
    }

    void initialize(ICallback onDataLoaded, ICallbackWithParam<String> onError) {
        model.setCallbacks(onDataLoaded, onError);
    }

    boolean hasDataBeenLoaded() {
        return model.hasDataBeenLoaded();
    }

    void loadData() {
        model.loadData();
    }

    ArrayList<AnalysisElement> getElements() {
        return model.getElements();
    }

    void setAttributeAndUpdateElements(int attribute) {
        model.setAttributeAndUpdateElements(attribute);
    }

    void setTeamNumberAndUpdateElements(int which) {
        model.setTeamNumberAndUpdateElements(which);
    }

    int getCurrentTeamNumberIndex() {
        return model.getCurrentTeamNumberIndex();
    }

    int getCurrentAttributeType() {
        return model.getCurrentAttributeTypeIndex();
    }

    String[] getAttributeNames() {
        return model.getAttributeNames();
    }

    String[] getTeamNumbers() {
        return model.getTeamNumbers();
    }
}
