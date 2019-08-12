package com.frc107.scouting.core.thebluealliance;

import java.util.HashMap;

public class OPR {
    private HashMap<Integer, String> oprMap;
    private HashMap<Integer, String> dprMap;

    public OPR() {
        oprMap = new HashMap<>();
        dprMap = new HashMap<>();
    }

    public boolean containsTeam(int teamNum) {
        return oprMap.containsKey(teamNum);
    }

    public void setOPR(int teamNum, String opr) {
        oprMap.put(teamNum, opr);
    }

    public String getOPR(int teamNum) {
        if (!oprMap.containsKey(teamNum))
            throw new IllegalArgumentException("Invalid team number: " + teamNum);

        return oprMap.get(teamNum);
    }

    public void setDPR(int teamNum, String dpr) {
        dprMap.put(teamNum, dpr);
    }

    public String getDPR(int teamNum) {
        if (!dprMap.containsKey(teamNum))
            throw new IllegalArgumentException("Invalid team number: " + teamNum);

        return dprMap.get(teamNum);
    }
}
