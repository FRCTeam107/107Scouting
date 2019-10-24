package com.frc107.scouting.core.analysis;

public class MatchSummary {
    public double[] attributes;

    public void setAttribute(int index, double attribute) {
        attributes[index] = attribute;
    }

    public double getAttribute(int index) {
        return attributes[index];
    }
}
