package com.frc107.scouting.core.analysis;

import androidx.lifecycle.ViewModel;

import com.frc107.scouting.core.thebluealliance.LoadDataTask;
import com.frc107.scouting.core.utils.callbacks.ICallback;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;
import com.frc107.scouting.match.MatchAnalysisManager;

import java.util.ArrayList;

public class AnalysisModel extends ViewModel {
    private ArrayList<AnalysisElement> elements;

    private int currentAttributeTypeIndex = -1;
    private int currentTeamNumberIndex = -1;

    private IAnalysisManager attributeManager = new MatchAnalysisManager();
    private ICallback onDataLoaded;
    private ICallbackWithParam<String> onError;

    AnalysisModel() {
        elements = new ArrayList<>();
    }

    void setCallbacks(ICallback onDataLoaded, ICallbackWithParam<String> onError) {
        this.onDataLoaded = onDataLoaded;
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
}
