package com.frc107.scouting.form;

import com.frc107.scouting.Scouting;

/**
 * This is where you'll define your own table types.
 *
 * For example, for 2019, we would have had 2 different tables:
 * - Match
 * - Pit
 *
 * So I would have had my new eTable enum values be:
 *  - PIT
 *  - MATCH
 *
 * I would have had four String constants:
 *  private static final String PIT_PREFIX = "Pit";
 *  private static final String CONCAT_PIT_PREFIX = "ConcatPit";
 *  private static final String MATCH_PREFIX = "Match";
 *  private static final String CONCAT_MATCH_PREFIX = "ConcatMatch";
 *
 * Finally I would update the 4 methods in this file to match the enum values that I just added.
 */
public enum eTable {
    PIT,
    MATCH;

    private static final String PIT_PREFIX = "Pit";
    private static final String MATCH_PREFIX = "Match";
    private static final String CONCAT_PIT_PREFIX = "ConcatPit";
    private static final String CONCAT_MATCH_PREFIX = "ConcatMatch";

    public String getPrefix(boolean concat) {
        switch (this) {
            case PIT:
                return concat ? CONCAT_PIT_PREFIX : PIT_PREFIX;
            case MATCH:
                return concat ? CONCAT_MATCH_PREFIX : MATCH_PREFIX;
            default:
                return null;
        }
    }

    public String getHeader() {
        switch (this) {
            case PIT:
                return Scouting.getInstance().getPitHeader();
            case MATCH:
                return Scouting.getInstance().getMatchHeader();
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case PIT:
                return "Pit";
            case MATCH:
                return "Match";
            default:
                return "None";
        }
    }

    public String toStringConcat() {
        switch (this) {
            case PIT:
                return "Concatenated Pit";
            case MATCH:
                return "Concatenated Match";
            default:
                return "None";
        }
    }

    /**
     * Use this to convert a prefix to an eTable.
     */
    public static eTable getTableTypeFromPrefix(String prefix) {
        switch (prefix) {
            case PIT_PREFIX:
            case CONCAT_PIT_PREFIX:
                return PIT;
            case MATCH_PREFIX:
            case CONCAT_MATCH_PREFIX:
                return MATCH;
            default:
                return null;
        }
    }
}