package com.frc107.scouting.analysis.attribute;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.ui.IUIListener;

import java.util.ArrayList;

public class AttributeAnalysisViewModel extends ViewModel {
    private AttributeAnalysisModel model;

    public AttributeAnalysisViewModel(IUIListener listener) {
        model = new AttributeAnalysisModel(listener);
    }

    public void loadData() {
        model.loadData();
    }

    public ArrayList<AnalysisElement> getElements() {
        return model.getElements();
    }

    public String[] getAttributeTypes() {
        return model.getAttributeTypes();
    }

    public void setAttribute(int attribute) {
        model.setAttribute(attribute);
    }

    public String getCurrentAttributeTypeName() {
        return model.getCurrentAttributeTypeName();
    }

    public int getCurrentAttributeType() {
        return model.getCurrentAttributeType();
    }
}
