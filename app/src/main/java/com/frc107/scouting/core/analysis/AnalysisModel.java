package com.frc107.scouting.core.analysis;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.utils.callbacks.ICallback;
import com.frc107.scouting.core.utils.callbacks.ICallbackWithParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisModel extends ViewModel {
    private static final int ALL_TEAMS_INDEX = -1;

    private String[] teamNumberStrings;

    private ObservableList<ChartElement> chartElements = new ObservableArrayList<>();
    private int[] matchNumbers;
    private int[] matchAttributes;

    private int currentAttributeTypeIndex = 0;
    private int currentTeamNumberIndex = 0;

    private String filePath;

    private IAnalysisManager analysisManager;
    private ICallbackWithParam<Boolean> onDataLoaded;
    private List<ICallback> onElementsUpdatedCallbacks = new ArrayList<>();

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
        //allTeamNumbers = analysisManager.getTeamNumbers(); // Set the team numbers so we don't crash
        hasDataBeenLoaded = true;
        onDataLoaded.call(result); // This should be the only call. Do not call this again.
    }

    boolean hasDataBeenLoaded() {
        return hasDataBeenLoaded;
    }
    void setTeamNumberAndUpdateElements(int which) {
        currentTeamNumberIndex = which;

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
        updateChartElements();
        updateGraphElements();

        for (ICallback callback : onElementsUpdatedCallbacks) {
            callback.call();
        }
    }

    private void updateChartElements() {
        chartElements.clear();
        Integer[] teamNumbers = analysisManager.getTeamNumbers();
        if (currentTeamNumberIndex == ALL_TEAMS_INDEX) {
            for (int teamNumber : teamNumbers) {
                double attribute = analysisManager.getAttributeValueForTeam(teamNumber);
                chartElements.add(new ChartElement(teamNumber + "", attribute));
            }
            Collections.sort(chartElements, (e1, e2) -> Double.compare(e2.getAttribute(), e1.getAttribute()));
        } else {
            int teamNumber = teamNumbers[currentTeamNumberIndex];
            double attribute = analysisManager.getAttributeValueForTeam(teamNumber);
            chartElements.add(new ChartElement(teamNumber + "", attribute));
        }
    }

    private void updateGraphElements() {
        if (currentTeamNumberIndex == ALL_TEAMS_INDEX) {
            matchNumbers = matchAttributes = new int[0];
            return; // if we are looking at all the teams, the graph becomes pointless for now
        }

        Integer[] teamNumbers = analysisManager.getTeamNumbers();
        int teamNumber = teamNumbers[currentTeamNumberIndex];
        Integer[] teamMatchNumbers = analysisManager.getMatchNumbersForTeam(teamNumbers[currentTeamNumberIndex]);

        matchNumbers = new int[teamMatchNumbers.length];
        matchAttributes = new int[matchNumbers.length];

        for (int i = 0; i < teamMatchNumbers.length; i++) {
            int matchNumber = teamMatchNumbers[i];
            int attribute = analysisManager.getAttributeForTeamAtMatch(teamNumber, matchNumber);
            matchNumbers[i] = matchNumber;
            matchAttributes[i] = attribute;
        }
    }

    void addOnElementsUpdatedCallback(ICallback callback) {
        onElementsUpdatedCallbacks.add(callback);
    }

    ObservableList<ChartElement> getChartElements() {
        return chartElements;
    }

    int[] getMatchNumbers() {
        return matchNumbers;
    }

    int[] getMatchAttributes() {
        return matchAttributes;
    }

    int getCurrentAttributeTypeIndex() {
        return currentAttributeTypeIndex;
    }

    String[] getTeamNumberStrings() {
        // This method just converts the team numbers into strings.
        if (teamNumberStrings == null) {
            teamNumberStrings = new String[analysisManager.getTeamNumbers().length];
            for (int i = 0; i < teamNumberStrings.length; i++) {
                teamNumberStrings[i] = analysisManager.getTeamNumbers()[i] + "";
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
