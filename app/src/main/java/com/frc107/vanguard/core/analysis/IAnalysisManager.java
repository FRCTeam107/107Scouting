package com.frc107.vanguard.core.analysis;

import com.frc107.vanguard.eTableType;

import java.io.File;

public interface IAnalysisManager {
    /**
     * Instead of looping through each row yourself, just perform your operations in this method.
     * This runs once for every single row of data.
     * @param rowValues All of the values in the row.
     * @return If it was successful.
     */
    boolean handleRow(Object[] rowValues);

    /**
     * Use this method to define what file is associated with this kind of analysis.
     * @return A File object, non-null.
     */
    File getFile();

    /**
     * Set the current attribute index. It's up to you to write code handling storage and management of attributes.
     * For context, an attribute would be one of the analysis options that shows up in the UI. For example,
     * you might have things like "Average cargo" or whatever data points you want to analyze. You would then use
     * attributeIndex to locate the attribute in question in your collection of attributes.
     * @param attributeIndex The position of the desired attribute.
     */
    void setAttribute(int attributeIndex);

    /**
     * @param teamNumber A team number.
     * @return The value of the current attribute for that team.
     */
    double getAttributeValueForTeam(int teamNumber);

    /**
     *  If you need to make any calculations, such as finding averages, then do that here.
     *  This will run after analyzing every row, so this is where you make calculations based on
     *  your whole dataset if you need to.
     */
    void makeFinalCalculations();

    /**
     * Get the names of each attribute. This is used by the UI.
     * @return The attribute names.
     */
    String[] getAttributeNames();

    /**
     * Get all the team numbers that were in the data.
     * @return The team numbers.
     */
    Integer[] getTeamNumbers();

    /**
     * Define the associated table here.
     * @return The eTableType that you want to analyze data as.
     */
    eTableType getTableType();

    /**
     * Remove all current values.
     */
    void clear();
}
