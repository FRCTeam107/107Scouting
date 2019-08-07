package com.frc107.scouting.match;

import android.util.SparseArray;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.analysis.attribute.IAnalysisManager;
import com.frc107.scouting.form.eTable;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class MatchAnalysisManager implements IAnalysisManager {
    // Table column indices
    private static final int COL_MATCH_NUM = 0;
    private static final int COL_TEAM_NUM = 1;
    private static final int COL_STARTING_ITEM = 3;
    private static final int COL_STARTING_PLACED_LOCATION = 4;
    private static final int COL_ITEM_PICKED_UP = 8;
    private static final int COL_ITEM_PLACED_LOCATION = 9;
    private static final int COL_DEFENSE = 13;
    private static final int COL_HAB = 11;
    private static final int COL_OPR = 17;
    private static final int COL_DPR = 18;

    // Attribute indices
    private static final int AVG_CARGO = 0;
    private static final int AVG_HATCH_PANEL = 1;
    private static final int AVG_CARGO_SHIP = 2;
    private static final int AVG_ROCKET_1 = 3;
    private static final int AVG_ROCKET_2 = 4;
    private static final int AVG_ROCKET_3 = 5;
    private static final int HAB_2_AMOUNT = 6;
    private static final int HAB_3_AMOUNT = 7;
    private static final int SUCCESSFUL_DEFENSE_AMOUNT = 8;
    private static final int OPR = 9;
    private static final int DPR = 10;

    public static final int SANDSTORM_HAB_ONE = 0;
    public static final int SANDSTORM_HAB_TWO = 1;
    public static final int SANDSTORM_CARGO = 1;
    public static final int SANDSTORM_PANEL = 2;
    public static final int SANDSTORM_NO_STARTING_PIECE = 0;
    public static final int SANDSTORM_TOP_ROCKET = 3;
    public static final int SANDSTORM_MIDDLE_ROCKET = 2;
    public static final int SANDSTORM_BOTTOM_ROCKET = 1;
    public static final int SANDSTORM_CARGO_SHIP = 0;
    public static final int SANDSTORM_FLOOR = 4;
    public static final int SANDSTORM_NOTHING_PLACED = 5;
    public static final int CYCLE_PORT_PICKUP = 0;
    public static final int CYCLE_FLOOR_PICKUP = 1;
    public static final int CYCLE_STARTED_WITH_ITEM = 2;
    public static final int CYCLE_CARGO = 0;
    public static final int CYCLE_PANEL = 1;
    public static final int CYCLE_TOP_ROCKET = 3;
    public static final int CYCLE_MIDDLE_ROCKET = 2;
    public static final int CYCLE_BOTTOM_ROCKET = 1;
    public static final int CYCLE_CARGO_SHIP = 0;
    public static final int CYCLE_FLOOR = 4;
    public static final int CYCLE_NOTHING_PLACED = 5;
    public static final int ENDGAME_HAB_ONE = 1;
    public static final int ENDGAME_HAB_TWO = 2;
    public static final int ENDGAME_HAB_THREE = 3;
    public static final int ENDGAME_HAB_NONE = 0;
    public static final int ENDGAME_DEFENSE_EFFECTIVE = 1;
    public static final int ENDGAME_DEFENSE_INEFFECTIVE = 2;
    public static final int ENDGAME_DEFENSE_NONE = 0;

    private SparseArray<TeamDetails> teamDetailsSparseArray = new SparseArray<>();
    private ArrayList<Integer> teamNumberList = new ArrayList<>();

    @Override
    public void makeCalculationsFromRows(Object[] rowValues) {
        int matchNumber = (int) rowValues[COL_MATCH_NUM];
        int teamNumber = (int) rowValues[COL_TEAM_NUM];
        int startingItem = (int) rowValues[COL_STARTING_ITEM];
        int startingPlacedLocation = (int) rowValues[COL_STARTING_PLACED_LOCATION];
        int itemPickedUp = (int) rowValues[COL_ITEM_PICKED_UP];
        int cyclePlacedLocation = (int) rowValues[COL_ITEM_PLACED_LOCATION];
        int defense = (int) rowValues[COL_DEFENSE];
        int habLevel = (int) rowValues[COL_HAB];
        double opr = Double.parseDouble((String) rowValues[COL_OPR]);
        double dpr = Double.parseDouble((String) rowValues[COL_DPR]);

        TeamDetails teamDetails = teamDetailsSparseArray.get(teamNumber);
        if (teamDetails == null) {
            teamDetailsSparseArray.put(teamNumber, new TeamDetails());
            teamNumberList.add(teamNumber);
            teamDetails = teamDetailsSparseArray.get(teamNumber);
        }

        teamDetails.setOPR(opr);
        teamDetails.setDPR(dpr);
        teamDetails.incrementCycleNum();

        if (!teamDetails.hasMatch(matchNumber)) {
            // Sandstorm game pieces
            if (startingPlacedLocation != SANDSTORM_FLOOR && startingPlacedLocation != SANDSTORM_NOTHING_PLACED) {
                switch (startingItem) {
                    case SANDSTORM_CARGO:
                        teamDetails.incrementCargoNum();
                        break;
                    case SANDSTORM_PANEL:
                        teamDetails.incrementHatchNum();
                        break;
                }
            }

            // Match defense
            switch (defense) {
                case ENDGAME_DEFENSE_EFFECTIVE:
                    teamDetails.incrementDefenseNum();
                    teamDetails.incrementEffectiveDefenseNum();
                    break;
                case ENDGAME_DEFENSE_INEFFECTIVE:
                    teamDetails.incrementDefenseNum();
                    break;
            }
            teamDetails.addMatch(matchNumber);
        }

        // Cycle game pieces
        if (cyclePlacedLocation != CYCLE_FLOOR && cyclePlacedLocation != CYCLE_NOTHING_PLACED) {
            switch (itemPickedUp) {
                case CYCLE_CARGO:
                    teamDetails.incrementCargoNum();
                    break;
                case CYCLE_PANEL:
                    teamDetails.incrementHatchNum();
                    break;
            }
        }

        // Habitat levels
        switch (habLevel) {
            case ENDGAME_HAB_ONE:
                teamDetails.incrementHabOneAmount();
                break;
            case ENDGAME_HAB_TWO:
                teamDetails.incrementHabTwoAmount();
                break;
            case ENDGAME_HAB_THREE:
                teamDetails.incrementHabThreeAmount();
                break;
        }

        // Rocket levels
        switch (startingPlacedLocation) {
            case SANDSTORM_BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount();
                break;
            case SANDSTORM_MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount();
                break;
            case SANDSTORM_TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount();
                break;
            case SANDSTORM_CARGO_SHIP:
                teamDetails.incrementCargoShipAmount();
                break;
        }
        switch (cyclePlacedLocation) {
            case CYCLE_BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount();
                break;
            case CYCLE_MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount();
                break;
            case CYCLE_TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount();
                break;
            case CYCLE_CARGO_SHIP:
                teamDetails.incrementCargoShipAmount();
                break;
        }
    }

    @Override
    public File getFile() {
        return Scouting.FILE_SERVICE.getFile("ConcatenatedMatch.csv");
    }

    private int attributeIndex;

    @Override
    public void setAttribute(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    @Override
    public double getAttributeValueForTeam(int teamNumber) {
        TeamDetails teamDetails = teamDetailsSparseArray.get(teamNumber);
        double attribute;
        switch (attributeIndex) {
            case AVG_CARGO:
                attribute = teamDetails.getAverageCargo();
                break;
            case AVG_HATCH_PANEL:
                attribute = teamDetails.getAverageHatchPanels();
                break;
            case AVG_CARGO_SHIP:
                attribute = teamDetails.getAverageCargoShip();
                break;
            case AVG_ROCKET_1:
                attribute = teamDetails.getAverageRocket1();
                break;
            case AVG_ROCKET_2:
                attribute = teamDetails.getAverageRocket2();
                break;
            case AVG_ROCKET_3:
                attribute = teamDetails.getAverageRocket3();
                break;
            case HAB_2_AMOUNT:
                attribute = teamDetails.getHab2Num();
                break;
            case HAB_3_AMOUNT:
                attribute = teamDetails.getHab3Num();
                break;
            case SUCCESSFUL_DEFENSE_AMOUNT:
                attribute = teamDetails.getEffectiveDefenseNum();
                break;
            case OPR:
                attribute = teamDetails.getOPR();
                break;
            case DPR:
                attribute = teamDetails.getDPR();
                break;
            default:
                attribute = 999999;
                break;
        }
        return attribute;
    }

    @Override
    public void makeFinalCalculations() {
        for (int i = 0; i < teamDetailsSparseArray.size(); i++) {
            TeamDetails teamDetails = teamDetailsSparseArray.valueAt(i);
            teamDetails.calculateAverages();
        }
    }

    private static final String[] ATTRIBUTE_NAMES = new String[] {
            "Average Cargo",
            "Average Hatch Panel",
            "Average Cargo Ship",
            "Average Rocket Level 1",
            "Average Rocket Level 2",
            "Average Rocket Level 3",
            "Hab 2 Climb Amount",
            "Hab 3 Climb Amount",
            "Successful Defense Amount",
            "OPR",
            "DPR"
    };

    @Override
    public String[] getAttributeNames() {
        return ATTRIBUTE_NAMES;
    }

    private Integer[] teamNumbers;
    @Override
    public Integer[] getTeamNumbers() {
        if (teamNumbers == null)
            teamNumbers = teamNumberList.toArray(new Integer[0]);

        Arrays.sort(teamNumbers);
        return teamNumbers;
    }

    @Override
    public eTable getTableType() {
        return eTable.MATCH;
    }

    private class TeamDetails {
        private ArrayList<Integer> matchNums = new ArrayList<>();
        private int teamNumber;
        private double opr;
        private double dpr;
        private int cargoNum;
        private double averageCargo;
        private int hatchNum;
        private double averageHatchPanels;
        private int defenseCount;
        private int effectiveDefenseNum;
        private double averageCargoShip;
        private int cargoShipNum;
        private double averageRocket1, averageRocket2, averageRocket3;
        private int rocket1Num, rocket2Num, rocket3Num;
        private int hab1Num, hab2Num, hab3Num;
        private int cycleNum;

        public int getTeamNumber() {
            return teamNumber;
        }
        public void setTeamNumber(int teamNumber) {
            this.teamNumber = teamNumber;
        }

        public void incrementCargoNum() {
            cargoNum++;
        }
        public double getAverageCargo() {
            return averageCargo;
        }

        public void incrementHatchNum() {
            hatchNum++;
        }
        public double getAverageHatchPanels() {
            return averageHatchPanels;
        }

        public int getDefenseCount() {
            return defenseCount;
        }
        public void incrementDefenseNum() {
            defenseCount++;
        }
        public int getEffectiveDefenseNum() {
            return effectiveDefenseNum;
        }
        public void incrementEffectiveDefenseNum() {
            effectiveDefenseNum++;
        }

        public void incrementRocketOneAmount() {
            rocket1Num++;
        }
        public void incrementRocketTwoAmount() {
            rocket2Num++;
        }
        public void incrementRocketThreeAmount() {
            rocket3Num++;
        }

        public void incrementHabOneAmount() {
            hab1Num++;
        }
        public void incrementHabTwoAmount() {
            hab2Num++;
        }
        public void incrementHabThreeAmount() {
            hab3Num++;
        }

        public double getAverageCargoShip() {
            return averageCargoShip;
        }

        public void incrementCargoShipAmount() {
            cargoShipNum++;
        }

        public double getAverageRocket1() {
            return averageRocket1;
        }

        public double getAverageRocket2() {
            return averageRocket2;
        }

        public double getAverageRocket3() {
            return averageRocket3;
        }

        public int getHab2Num() {
            return hab2Num;
        }

        public int getHab3Num() {
            return hab3Num;
        }

        public void incrementCycleNum() {
            cycleNum++;
        }

        public void calculateAverages() {
            averageCargo = getAveragePerMatch(cargoNum);
            averageHatchPanels = getAveragePerMatch(hatchNum);
            averageCargoShip = getAveragePerMatch(cargoShipNum);
            averageRocket1 = getAveragePerMatch(rocket1Num);
            averageRocket2 = getAveragePerMatch(rocket2Num);
            averageRocket3 = getAveragePerMatch(rocket3Num);
        }

        private double getAveragePerMatch(int value) {
            BigDecimal bd = BigDecimal.valueOf((double) value / (double) matchNums.size());
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }

        public void addMatch(int matchNum) {
            matchNums.add(matchNum);
        }
        public boolean hasMatch(int matchNum) {
            return matchNums.contains(matchNum);
        }

        public void setOPR(double opr) {
            this.opr = opr;
        }

        public double getOPR() {
            return opr;
        }

        public void setDPR(double dpr) {
            this.dpr = dpr;
        }

        public double getDPR() {
            return dpr;
        }
    }
}
