package com.frc107.vanguard;

import com.frc107.vanguard.core.analysis.IAnalysisManager;
import com.frc107.vanguard.core.file.FileService;
import com.frc107.vanguard.core.table.Table;

import java.util.ArrayList;
import java.util.List;

public class Vanguard {
    private static Vanguard vanguard;
    public static Vanguard getInstance() {
        return vanguard;
    }

    private List<Table> tables = new ArrayList<>();

    static {
        vanguard = new Vanguard();
        vanguard.reloadFileData();
    }

    private Vanguard() {
        // Set up your tables and analysis manager here, as well as whatever else you want to set up at the start of the app.
    }

    public Table getTable(eTableType table) {
        // Vanguard needs a way to identify tables, so implement this.
        return null;
    }

    public IAnalysisManager getAnalysisManager() {
        // Return your analysis manager here.
        // TODO: allow multiple analysis managers so one could be able to analyze different table types.
        return null;
    }

    /**
     * Reload the file data. Call this when you create or delete files, or if you move vanguard files to the Vanguard directory.
     */
    public void reloadFileData() {
        fileService.clearFileDefinitions();
        fileService.loadFileDefinitions();
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
