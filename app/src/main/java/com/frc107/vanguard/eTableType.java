package com.frc107.vanguard;

import com.frc107.vanguard.core.NotImplementedException;

/**
 * This is where you'll define your own table types.
 *
 * For example, for 2019, we would have had 2 different tables:
 * - Match
 * - PitAnswers
 *
 * So I would have had my new eTableType enum values be:
 *  - PIT
 *  - MATCH
 *
 * I would have had four String constants:
 *  private static final String PIT_PREFIX = "PitAnswers";
 *  private static final String CONCAT_PIT_PREFIX = "ConcatPit";
 *  private static final String MATCH_PREFIX = "Match";
 *  private static final String CONCAT_MATCH_PREFIX = "ConcatMatch";
 *
 * Finally I would update the 4 methods in this file to match the enum values that I just added.
 */
public enum eTableType {
    ; // Put your values before this semicolon.

    /**
     * Get the file name prefix for an eTableType.
     * For example, if I had an eTableType MATCH and called this method with concat = true, then
     * MATCH.getPrefix(true) might return "ConcatMatch", or whatever you define.
     * @param concat Whether you want the concatenated version of the prefix or not.
     * @return The file name prefix for this eTableType.
     */
    public String getPrefix(boolean concat) {
        throw new NotImplementedException("getPrefix");
    }

    /**
     * Get the full name of the eTableType,
     * If I had eTableType.MATCH, I might return "Match" here.
     * @return The user-friendly name of the table.
     */
    @Override
    public String toString() {
        throw new NotImplementedException("toString");
    }

    /**
     * The full name of the eTableType, but concatenated.
     * If I had eTableType.MATCH, I might return "Concatenated Match" here.
     * @return The user-friendly concatenated name of the table.
     */
    public String toStringConcat() {
        throw new NotImplementedException("toStringConcat");
    }

    /**
     * Use this to convert a file name prefix to an eTableType.
     * @param prefix The file name prefix.
     * @return The eTableType associated with the inputted table type.
     */
    public static eTableType getTableTypeFromPrefix(String prefix) {
        throw new NotImplementedException("getTableTypeFromPrefix");
    }
}
