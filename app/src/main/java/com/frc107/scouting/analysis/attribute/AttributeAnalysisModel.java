package com.frc107.scouting.analysis.attribute;

import android.os.Build;
import android.util.SparseArray;

import androidx.lifecycle.MutableLiveData;

import com.frc107.scouting.analysis.IAnalysisListener;
import com.frc107.scouting.ui.IUIListener;
import com.frc107.scouting.analysis.LoadDataTask;
import com.frc107.scouting.analysis.TeamDetails;
import com.frc107.scouting.utils.ISimpleCallback;

import java.util.ArrayList;

public class AttributeAnalysisModel implements IAnalysisListener {
    private ArrayList<AnalysisElement> elements;
    private SparseArray<TeamDetails> detailsArray;

    private boolean dataLoaded;

    private static final int AVG_CARGO = 0;
    private static final int AVG_HATCH_PANEL = 1;
    private static final int AVG_CARGO_SHIP = 2;
    private static final int AVG_ROCKET_1 = 3;
    private static final int AVG_ROCKET_2 = 4;
    private static final int AVG_ROCKET_3 = 5;
    private static final int HAB_2_AMOUNT = 6;
    private static final int HAB_3_AMOUNT = 7;
    private static final int SUCCESSFUL_DEFENSE_AMOUNT = 8;
    private static final int OPR = 9;
    private static final int DPR = 10;

    private int currentAttributeType = -1;

    private ISimpleCallback onDataLoaded;

    public AttributeAnalysisModel(ISimpleCallback onDataLoaded) {
        elements = new ArrayList<>();
        this.onDataLoaded = onDataLoaded;
    }

    public void loadData() {
        new LoadDataTask(this).execute();
    }

    @Override
    public void onDataLoaded(SparseArray<TeamDetails> detailsArray, boolean error) {
        this.detailsArray = detailsArray;
        dataLoaded = !error;
        onDataLoaded.callback(dataLoaded);
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setAttributeAndUpdateElements(int attributeNum) {
        currentAttributeType = attributeNum;
        elements.clear();
        for (int i = 0; i < detailsArray.size(); i++) {
            String teamNumber = detailsArray.keyAt(i) + "";
            double attribute = 0.0;

            TeamDetails teamDetails = detailsArray.valueAt(i);
            switch (attributeNum) {
                case AVG_CARGO:
                    attribute = teamDetails.getAverageCargo();
                    break;
                case AVG_HATCH_PANEL:
                    attribute = teamDetails.getAverageHatchPanels();
                    break;
                case AVG_CARGO_SHIP:
                    attribute = teamDetails.getAverageCargoShip();
                    break;
                case AVG_ROCKET_1:
                    attribute = teamDetails.getAverageRocket1();
                    break;
                case AVG_ROCKET_2:
                    attribute = teamDetails.getAverageRocket2();
                    break;
                case AVG_ROCKET_3:
                    attribute = teamDetails.getAverageRocket3();
                    break;
                case HAB_2_AMOUNT:
                    attribute = teamDetails.getHab2Num();
                    break;
                case HAB_3_AMOUNT:
                    attribute = teamDetails.getHab3Num();
                    break;
                case SUCCESSFUL_DEFENSE_AMOUNT:
                    attribute = teamDetails.getEffectiveDefenseNum();
                    break;
                case OPR:
                    attribute = teamDetails.getOPR();
                    break;
                case DPR:
                    attribute = teamDetails.getDPR();
                    break;
            }

            elements.add(new AnalysisElement(teamNumber, attribute));
        }
        sortElements();
    }

    private void sortElements() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            elements.sort((o1, o2) -> Double.compare(o2.getAttribute(), o1.getAttribute()));
        }
    }

    public ArrayList<AnalysisElement> getElements() {
        return elements;
    }

    public int getCurrentAttributeType() {
        return currentAttributeType;
    }

    public class AnalysisElement {
        private String teamNumber;
        private double attribute;

        public AnalysisElement(String teamNumber, double attribute) {
            if (teamNumber == null)
                throw new IllegalArgumentException("Team number cannot be null");

            this.teamNumber = teamNumber;
            this.attribute = attribute;
        }

        public String getTeamNumber() {
            return teamNumber;
        }

        public double getAttribute() {
            return attribute;
        }
    }
}
