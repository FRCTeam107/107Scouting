package com.frc107.vanguard.core.analysis;

import androidx.lifecycle.ViewModel;

import com.frc107.vanguard.core.utils.callbacks.ICallbackWithParam;
import com.frc107.vanguard.deepspace.match.MatchAnalysisManager;

import java.util.ArrayList;

public class AnalysisModel extends ViewModel {
    private String filePath;
    private ArrayList<AnalysisElement> elements = new ArrayList<>();

    private int currentAttributeTypeIndex = -1;
    private int currentTeamNumberIndex = -1;

    private IAnalysisManager attributeManager = new MatchAnalysisManager();
    private ICallbackWithParam<Boolean> onDataLoaded;

    void setCallbacks(ICallbackWithParam<Boolean> onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }

    void loadData() {
        new LoadDataTask(this::onDataLoaded, attributeManager, filePath).execute();
    }

    private boolean hasDataBeenLoaded;

    private void onDataLoaded(Boolean result) {
        attributeManager.makeFinalCalculations();
        teamNumbers = attributeManager.getTeamNumbers(); // Set the team numbers so we don't crash
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
            teamNumbers = attributeManager.getTeamNumbers();
        } else {
            int teamNumber = attributeManager.getTeamNumbers()[which - 1];
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
        attributeManager.setAttribute(attributeIndex);

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
        return attributeManager.getAttributeNames();
    }

    void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
