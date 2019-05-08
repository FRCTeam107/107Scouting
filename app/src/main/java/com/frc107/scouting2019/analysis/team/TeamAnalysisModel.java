package com.frc107.scouting2019.analysis.team;

import android.util.SparseArray;

import com.frc107.scouting2019.analysis.IAnalysisListener;
import com.frc107.scouting2019.ui.IUIListener;
import com.frc107.scouting2019.analysis.LoadDataTask;
import com.frc107.scouting2019.analysis.TeamDetails;

public class TeamAnalysisModel implements IAnalysisListener {
    private SparseArray<TeamDetails> teamDetailsSparseArray;
    private int teamNumber;
    private double averageCargo;
    private double averageHatchPanels;
    private int defenseCount;
    private int effectiveDefenseAmount;
    private boolean rocket1, rocket2, rocket3;
    private boolean hab1, hab2, hab3;
    private double opr;
    private double dpr;
    private IUIListener listener; // TODO: THIS SUCKS. FIX THIS. FIGURE OUT HOW TO DO PROPER MVVM.
    private String[] teamNumbers;

    public TeamAnalysisModel(IUIListener listener) {
        this.listener = listener;
        teamDetailsSparseArray = new SparseArray<>();
    }

    public void loadData() {
        new LoadDataTask(this).execute();
    }

    public void setTeamNumber(String newTeamNumber) {
        this.teamNumber = Integer.parseInt(newTeamNumber);
        TeamDetails teamDetails = teamDetailsSparseArray.get(teamNumber);
        if (teamDetails == null) {
            averageCargo = 0;
            averageHatchPanels = 0;
            defenseCount = 0;
            effectiveDefenseAmount = 0;
            hab1 = false;
            hab2 = false;
            hab3 = false;
            rocket1 = false;
            rocket2 = false;
            rocket3 = false;
            opr = 0;
            dpr = 0;
            return;
        }

        averageCargo = teamDetails.getAverageCargo();
        averageHatchPanels = teamDetails.getAverageHatchPanels();
        defenseCount = teamDetails.getDefenseCount();
        effectiveDefenseAmount = teamDetails.getEffectiveDefenseNum();
        hab1 = teamDetails.canClimbHabOne();
        hab2 = teamDetails.canClimbHabTwo();
        hab3 = teamDetails.canClimbHabThree();
        rocket1 = teamDetails.canAccessRocketOne();
        rocket2 = teamDetails.canAccessRocketTwo();
        rocket3 = teamDetails.canAccessRocketThree();
        opr = teamDetails.getOPR();
        dpr = teamDetails.getDPR();
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public double getAverageCargo() {
        return averageCargo;
    }

    public double getAverageHatchPanels() {
        return averageHatchPanels;
    }

    public int getDefenseCount() {
        return defenseCount;
    }

    public int getEffectiveDefenseCount() {
        return effectiveDefenseAmount;
    }

    public boolean canAccessRocketOne() {
        return rocket1;
    }

    public boolean canAccessRocketTwo() {
        return rocket2;
    }

    public boolean canAccessRocketThree() {
        return rocket3;
    }

    public boolean canClimbHabOne() {
        return hab1;
    }

    public boolean canClimbHabTwo() {
        return hab2;
    }

    public boolean canClimbHabThree() {
        return hab3;
    }

    public double getOPR() {
        return opr;
    }

    public double getDPR() {
        return dpr;
    }

    @Override
    public void onDataLoaded(SparseArray<TeamDetails> detailsArray, boolean error) {
        teamDetailsSparseArray = detailsArray;
        listener.callback(error);
        loadTeamNumbers();
    }

    private void loadTeamNumbers() {
        teamNumbers = new String[teamDetailsSparseArray.size()];
        for (int i = 0; i < teamDetailsSparseArray.size(); i++) {
            int key = teamDetailsSparseArray.keyAt(i);
            teamNumbers[i] = key + "";
        }
    }

    public String[] getTeamNumbers() {
        return teamNumbers;
    }
}
