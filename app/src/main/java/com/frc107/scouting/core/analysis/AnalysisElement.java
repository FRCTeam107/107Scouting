package com.frc107.scouting.core.analysis;

class AnalysisElement {
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

    double getAttribute() {
        return attribute;
    }
}
