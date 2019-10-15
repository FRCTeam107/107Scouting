package com.frc107.scouting.core.analysis;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

import java.util.ArrayList;

public class AnalysisModel extends ViewModel {
    private String filePath;
    private ArrayList<AnalysisElement> elements = new ArrayList<>();

    private int currentAttributeTypeIndex = -1;
    private int currentTeamNumberIndex = -1;

    private IAnalysisManager analysisManager;
    private ICallbackWithParam<Boolean> onDataLoaded;

    void initialize(ICallbackWithParam<Boolean> onDataLoaded) {
        this.analysisManager = Scouting.getInstance().getAnalysisManager();
        this.onDataLoaded = onDataLoaded;
    }

    boolean tryToLoadData() {
        if (analysisManager == null)
            return false;

        analysisManager.clear();
        new LoadDataTask(this::onDataLoaded, analysisManager, filePath).execute();
        return true;
    }

    private boolean hasDataBeenLoaded;

    private void onDataLoaded(Boolean result) {
        analysisManager.makeFinalCalculations();
        teamNumbers = analysisManager.getTeamNumbers(); // Set the team numbers so we don't crash
        hasDataBeenLoaded = true;
        onDataLoaded.call(result); // This should be the only call. Do not call this again.
    }

    boolean hasDataBeenLoaded() {
        return hasDataBeenLoaded;
    }

    private static final int ALL_TEAMS_INDEX = 0;
    private Integer[] teamNumbers;
    void setTeamNumberAndUpdateElements(int which) {
        currentTeamNumberIndex = which;
        if (which == ALL_TEAMS_INDEX) {
            teamNumbers = analysisManager.getTeamNumbers();
        } else {
            int teamNumber = analysisManager.getTeamNumbers()[which - 1];
            teamNumbers = new Integer[] { teamNumber };
        }

        updateElements();
    }

    int getCurrentTeamNumberIndex() {
        return currentTeamNumberIndex;
    }

    void setAttributeAndUpdateElements(int attributeIndex) {
        if (currentAttributeTypeIndex == attributeIndex)
            return;

        currentAttributeTypeIndex = attributeIndex;
        analysisManager.setAttribute(attributeIndex);

        updateElements();
    }

    private void updateElements() {
        elements.clear();

        if (teamNumbers == null)
            teamNumbers = analysisManager.getTeamNumbers();

        for (int teamNumber : teamNumbers) {
            double attribute = analysisManager.getAttributeValueForTeam(teamNumber);
            elements.add(new AnalysisElement(teamNumber + "", attribute));
        }
    }

    ArrayList<AnalysisElement> getElements() {
        return elements;
    }

    int getCurrentAttributeTypeIndex() {
        return currentAttributeTypeIndex;
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
        return analysisManager.getAttributeNames();
    }

    void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
