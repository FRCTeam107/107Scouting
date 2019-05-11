package com.frc107.scouting.analysis.attribute;

import android.os.Build;
import android.util.SparseArray;

import androidx.lifecycle.MutableLiveData;

import com.frc107.scouting.analysis.IAnalysisListener;
import com.frc107.scouting.ui.IUIListener;
import com.frc107.scouting.analysis.LoadDataTask;
import com.frc107.scouting.analysis.TeamDetails;

import java.util.ArrayList;

public class AttributeAnalysisModel implements IAnalysisListener {
    private ArrayList<AnalysisElement> elements;
    private MutableLiveData<ArrayList<AnalysisElement>> elementsLiveData;
    private SparseArray<TeamDetails> detailsArray;

    private static final String[] ATTRIBUTE_TYPES = new String[]{
            "Average Cargo",
            "Average Hatch Panel",
            "Average Cargo Ship",
            "Average Rocket Level 1",
            "Average Rocket Level 2",
            "Average Rocket Level 3",
            "Hab 2 Climb Amount",
            "Hab 3 Climb Amount",
            "Successful Defense Amount",
            "OPR",
            "DPR"
    };

    private static final int AVG_CARGO = 0,
                             AVG_HATCH_PANEL = 1,
                             AVG_CARGO_SHIP = 2,
                             AVG_ROCKET_1 = 3,
                             AVG_ROCKET_2 = 4,
                             AVG_ROCKET_3 = 5,
                             HAB_2_AMOUNT = 6,
                             HAB_3_AMOUNT = 7,
                             SUCCESSFUL_DEFENSE_AMOUNT = 8,
                             OPR = 9,
                             DPR = 10;

    private int currentAttributeType;

    public AttributeAnalysisModel() {
        elements = new ArrayList<>();
        elementsLiveData = new MutableLiveData<>();
    }

    public void loadData() {
        new LoadDataTask(this).execute();
    }

    @Override
    public void onDataLoaded(SparseArray<TeamDetails> detailsArray, boolean error) {
        this.detailsArray = detailsArray;
        if (!error)
            loadTeamNumsAndAttributes();
    }

    private void loadTeamNumsAndAttributes() {
        for (int i = 0; i < detailsArray.size(); i++) {
            String teamNumber = detailsArray.keyAt(i) + "";
            double attribute = 0.0;
            elements.add(new AnalysisElement(teamNumber, attribute));
        }
        elementsLiveData.setValue(elements);
    }

    public void setAttribute(int attributeNum) {
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
        elementsLiveData.setValue(elements);
    }

    private void sortElements() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            elements.sort((o1, o2) -> Double.compare(o2.getAttribute(), o1.getAttribute()));
        }
    }

    public MutableLiveData<ArrayList<AnalysisElement>> getElementsLiveData() {
        return elementsLiveData;
    }

    public String[] getAttributeTypes() {
        return ATTRIBUTE_TYPES;
    }

    public String getCurrentAttributeTypeName() {
        return ATTRIBUTE_TYPES[currentAttributeType];
    }

    public int getCurrentAttributeType() {
        return currentAttributeType;
    }
}
