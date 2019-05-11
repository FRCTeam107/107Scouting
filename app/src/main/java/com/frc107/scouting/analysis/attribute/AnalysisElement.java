package com.frc107.scouting.analysis.attribute;

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
