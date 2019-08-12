package com.frc107.scouting.core.analysis;

import com.frc107.scouting.core.table.eTableType;

import java.io.File;

public interface IAnalysisManager {
    void makeCalculationsFromRows(Object[] rowValues);
    File getFile();
    void setAttribute(int attributeIndex);
    double getAttributeValueForTeam(int teamNumber);
    void makeFinalCalculations();
    String[] getAttributeNames();
    Integer[] getTeamNumbers(); // todo: move team number stuff to AnalysisModel. Team numbers are common to all analysises(analysi?); each attribute manager shouldn't have to manage them.
    eTableType getTableType();
}
