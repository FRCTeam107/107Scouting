package com.frc107.scouting.analysis.attribute;

import com.frc107.scouting.analysis.LoadDataTask;
import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.callbacks.ICallbackWithParam;

import java.util.ArrayList;

public class AttributeAnalysisModel {
    private ArrayList<AnalysisElement> elements;

    private int currentAttributeType = -1;

    private IAnalysisManager attributeManager = new MatchAnalysisManagerOld();
    private ICallback onDataLoaded;
    private ICallback refreshUI;
    private ICallbackWithParam<String> onError;

    AttributeAnalysisModel(ICallback onDataLoaded, ICallback refreshUI, ICallbackWithParam<String> onError) {
        elements = new ArrayList<>();
        this.onDataLoaded = onDataLoaded;
        this.refreshUI = refreshUI;
        this.onError = onError;
    }

    void loadData() {
        new LoadDataTask(this::onDataLoaded, this::onDataLoadError).execute(attributeManager);
    }

    private boolean hasDataBeenLoaded;

    private void onDataLoaded() {
        attributeManager.makeFinalCalculations();
        teamNumbers = attributeManager.getTeamNumbers(); // Set the team numbers so we don't crash
        hasDataBeenLoaded = true;
        onDataLoaded.call(); // This should be the only call. Do not call this again.
    }

    private void onDataLoadError(String error) {
        onError.call(error);
    }

    boolean hasDataBeenLoaded() {
        return hasDataBeenLoaded;
    }

    private static final int ALL_TEAMS_INDEX = 0;
    private Integer[] teamNumbers;
    void setTeamNumberAndUpdateElements(int which) {
        if (which == ALL_TEAMS_INDEX) {
            teamNumbers = attributeManager.getTeamNumbers();
        } else {
            int teamNumber = attributeManager.getTeamNumbers()[which - 1];
            teamNumbers = new Integer[] { teamNumber };
        }

        updateElements();
    }

    void setAttributeAndUpdateElements(int attributeNum) {
        if (currentAttributeType == attributeNum)
            return;

        currentAttributeType = attributeNum;
        attributeManager.setAttribute(attributeNum);

        updateElements();
    }

    private void updateElements() {
        elements.clear();

        if (teamNumbers == null)
            teamNumbers = attributeManager.getTeamNumbers();

        for (int teamNumber : teamNumbers) {
            double attribute = attributeManager.getAttributeValueForTeam(teamNumber);
            elements.add(new AnalysisElement(teamNumber + "", attribute));
        }
        refreshUI.call();
    }

    ArrayList<AnalysisElement> getElements() {
        return elements;
    }

    int getCurrentAttributeType() {
        return currentAttributeType;
    }

    private String[] teamNumberStrings;
    String[] getTeamNumbers() {
        // This method just converts the team numbers into strings.
        if (teamNumberStrings == null) {
            teamNumberStrings = new String[teamNumbers.length];
            for (int i = 0; i < teamNumberStrings.length; i++) {
                teamNumberStrings[i] = teamNumbers[i] + "";
            }
        }

        return teamNumberStrings;
    }

    String[] getAttributeNames() {
        return attributeManager.getAttributeNames();
    }

    public class AnalysisElement {
        private String teamNumber;
        private double attribute;

        AnalysisElement(String teamNumber, double attribute) {
            if (teamNumber == null)
                throw new IllegalArgumentException("Team number cannot be null");

            this.teamNumber = teamNumber;
            this.attribute = attribute;
        }

        String getTeamNumber() {
            return teamNumber;
        }

        public double getAttribute() {
            return attribute;
        }
    }
}
