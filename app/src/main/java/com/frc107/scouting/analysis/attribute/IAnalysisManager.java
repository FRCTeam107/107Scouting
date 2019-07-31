package com.frc107.scouting.analysis.attribute;

import com.frc107.scouting.form.eTable;

import java.io.File;

public interface IAnalysisManager {
    void makeCalculationsFromRows(Object[] rowValues);
    File getFile();
    void setAttribute(int attributeIndex);
    double getAttributeForTeam(int teamNumber);
    void makeFinalCalculations();
    String[] getAttributeNames();
    Integer[] getTeamNumbers(); // todo: move team number stuff to AttributeAnalysisModel. Team numbers are common to all analysis; each attribute manager shouldn't have to manage them.
    eTable getTableType();
}
