package com.frc107.vanguard;

import com.frc107.vanguard.core.Logger;
import com.frc107.vanguard.core.analysis.IAnalysisManager;
import com.frc107.vanguard.core.file.FileDefinition;
import com.frc107.vanguard.core.file.FileService;
import com.frc107.vanguard.core.table.Table;
import com.frc107.vanguard.core.table.column.Column;
import com.frc107.vanguard.core.table.column.eColumnType;
import com.frc107.vanguard.deepspace.match.MatchAnalysisManager;
import com.frc107.vanguard.deepspace.match.MatchColumnIDs;
import com.frc107.vanguard.deepspace.pit.PitIDs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vanguard {
    private static Vanguard vanguard;
    public static Vanguard getInstance() {
        return vanguard;
    }

    private List<Table> tables = new ArrayList<>();
    private Table pitTable;
    private Table matchTable;

    private IAnalysisManager analysisManager;

    static {
        vanguard = new Vanguard();
        vanguard.reloadFileData();
    }

    private Vanguard() {
        pitTable = new Table("Pit",
                new Column(PitIDs.TEAM_NUM, "Team number", eColumnType.INT),
                new Column(PitIDs.SANDSTORM_OP, "Sandstorm op", eColumnType.INT),
                new Column(PitIDs.SANDSTORM_PREF, "Sandstorm preference", eColumnType.INT),
                new Column(PitIDs.SANDSTORM_HIGHEST_ROCKET_LEVEL, "Sandstorm highest rocket level", eColumnType.INT),
                new Column(PitIDs.HIGHEST_HAB, "Highest habitat", eColumnType.INT),
                new Column(PitIDs.HAB_TIME, "Habitat climb time", eColumnType.STRING),
                new Column(PitIDs.PROGRAMMING_LANG, "Programming language", eColumnType.STRING),
                new Column(PitIDs.BONUS, "Bonus question", eColumnType.STRING),
                new Column(PitIDs.COMMENTS, "Comments", eColumnType.STRING));
        tables.add(pitTable);

        matchTable = new Table("Match",
                new Column(MatchColumnIDs.MATCH_NUM, "Match number", eColumnType.INT),
                new Column(MatchColumnIDs.TEAM_NUM, "Team number", eColumnType.INT),
                new Column(MatchColumnIDs.STARTING_LOCATION, "Starting location", eColumnType.INT),
                new Column(MatchColumnIDs.STARTING_ITEM, "Starting item", eColumnType.INT),
                new Column(MatchColumnIDs.STARTING_ITEM_PLACED, "Starting item placed", eColumnType.INT),
                new Column(MatchColumnIDs.CROSSED_BASELINE, "Crossed baseline", eColumnType.INT),
                new Column(MatchColumnIDs.CYCLE_NUM, "Cycle number", eColumnType.INT),
                new Column(MatchColumnIDs.PICKUP_LOCATION, "Pick up location", eColumnType.INT),
                new Column(MatchColumnIDs.ITEM_PICKED_UP, "Item picked up", eColumnType.INT),
                new Column(MatchColumnIDs.ITEM_PLACED, "Item placed", eColumnType.INT),
                new Column(MatchColumnIDs.DEFENSE, "Defense", eColumnType.INT),
                new Column(MatchColumnIDs.HAB_LEVEL, "Hab level", eColumnType.INT),
                new Column(MatchColumnIDs.ALL_MATCH, "All match", eColumnType.INT),
                new Column(MatchColumnIDs.MATCH_DEFENSE, "Match defense", eColumnType.INT),
                new Column(MatchColumnIDs.FOULS, "Fouls", eColumnType.INT),
                new Column(MatchColumnIDs.MAX_CYCLES, "Max Cycles", eColumnType.INT),
                new Column(MatchColumnIDs.SCOUTER_INITIALS, "Scouter Initials", eColumnType.STRING));
        tables.add(matchTable);

        analysisManager = new MatchAnalysisManager();
    }

    public Table getTable(eTableType table) {
        switch (table) {
            case PIT:
                return pitTable;
            case MATCH:
                return matchTable;
            default:
                return null;
        }
    }

    public IAnalysisManager getAnalysisManager() {
        return analysisManager;
    }

    /**
     * Reload the file data. Call this when you create or delete files, or if you move vanguard files to the Vanguard directory.
     */
    public void reloadFileData() {
        pitTable.clear();

        fileService.clearFileDefinitions();
        fileService.loadFileDefinitions();

        FileDefinition fileDef = fileService.getMostRecentFileDefinition(eTableType.PIT, false, userInitials);
        if (fileDef != null) {
            String fileData;
            try {
                fileData = fileService.getFileData(fileDef.getFile());
            } catch (IOException e) {
                Logzger.log(e.getLocalizedMessage());
                return;
            }
            pitTable.importData(fileData, row -> true);
        }
    }

    public List<Table> getTables() {
        return tables;
    }

    private FileService fileService = new FileService();

    public static FileService getFileService() {
        return getInstance().fileService;
    }

    private String userInitials;

    public void setUserInitials(String userInitials) {
        this.userInitials = userInitials;
    }

    public String getUserInitials() {
        return userInitials;
    }

    private Integer matchNumber;

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    private String eventKey;

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getEventKey() {
        return eventKey;
    }
}
