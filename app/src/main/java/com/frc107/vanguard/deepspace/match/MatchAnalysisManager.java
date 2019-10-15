package com.frc107.vanguard.deepspace.match;

import android.util.SparseArray;

import com.frc107.vanguard.Vanguard;
import com.frc107.vanguard.core.Logger;
import com.frc107.vanguard.core.analysis.IAnalysisManager;
import com.frc107.vanguard.core.file.FileDefinition;
import com.frc107.vanguard.eTableType;
import com.frc107.vanguard.deepspace.match.cycle.CycleAnswers;
import com.frc107.vanguard.deepspace.match.endgame.EndgameAnswers;
import com.frc107.vanguard.deepspace.match.sandstorm.SandstormAnswers;

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

    private SparseArray<TeamDetails> teamDetailsSparseArray = new SparseArray<>();
    private ArrayList<Integer> teamNumberList = new ArrayList<>();

    @Override
    public boolean handleRow(Object[] rowValues) {
        int matchNumber;
        int teamNumber;
        int startingItem;
        int startingPlacedLocation;
        int itemPickedUp;
        int cyclePlacedLocation;
        int defense;
        int habLevel;
        try {
            matchNumber = (int) rowValues[COL_MATCH_NUM];
            teamNumber = (int) rowValues[COL_TEAM_NUM];
            startingItem = (int) rowValues[COL_STARTING_ITEM];
            startingPlacedLocation = (int) rowValues[COL_STARTING_PLACED_LOCATION];
            itemPickedUp = (int) rowValues[COL_ITEM_PICKED_UP];
            cyclePlacedLocation = (int) rowValues[COL_ITEM_PLACED_LOCATION];
            defense = (int) rowValues[COL_DEFENSE];
            habLevel = (int) rowValues[COL_HAB];
        } catch (IndexOutOfBoundsException e) {
            Logger.log(e.getLocalizedMessage());
            return false;
        }

        TeamDetails teamDetails = teamDetailsSparseArray.get(teamNumber);
        if (teamDetails == null) {
            teamDetailsSparseArray.put(teamNumber, new TeamDetails());
            teamNumberList.add(teamNumber);
            teamDetails = teamDetailsSparseArray.get(teamNumber);
        }
        
        if (!teamDetails.hasMatch(matchNumber)) {
            handleSandstormGamePiece(teamDetails, startingPlacedLocation, startingItem);
            handleMatchDefense(teamDetails, defense);
            teamDetails.addMatch(matchNumber);
        }

        handleCycleGamePieces(teamDetails, cyclePlacedLocation, itemPickedUp);
        handleHabLevels(teamDetails, habLevel);
        handleCargoShipAndRocketLevels(teamDetails, startingPlacedLocation, cyclePlacedLocation);

        return true;
    }

    private void handleSandstormGamePiece(TeamDetails teamDetails, int startingPlacedLocation, int startingItem) {
        if (startingPlacedLocation != SandstormAnswers.FLOOR && startingPlacedLocation != SandstormAnswers.NOT_PLACED) {
            switch (startingItem) {
                case SandstormAnswers.CARGO:
                    teamDetails.incrementCargoNum();
                    break;
                case SandstormAnswers.HATCH_PANEL:
                    teamDetails.incrementHatchNum();
                    break;
                default:
                    break;
            }
        }
    }
    private void handleMatchDefense(TeamDetails teamDetails, int defense) {
        if (defense == EndgameAnswers.EFFECTIVE_DEFENSE) {
            teamDetails.incrementEffectiveDefenseNum();
        }
    }
    private void handleCycleGamePieces(TeamDetails teamDetails, int cyclePlacedLocation, int itemPickedUp) {
        if (cyclePlacedLocation != CycleAnswers.FLOOR && cyclePlacedLocation != CycleAnswers.NOT_PLACED) {
            switch (itemPickedUp) {
                case CycleAnswers.CARGO:
                    teamDetails.incrementCargoNum();
                    break;
                case CycleAnswers.HATCH_PANEL:
                    teamDetails.incrementHatchNum();
                    break;
                default:
                    break;
            }
        }
    }
    private void handleHabLevels(TeamDetails teamDetails, int habLevel) {
        switch (habLevel) {
            case EndgameAnswers.HAB_TWO:
                teamDetails.incrementHabTwoAmount();
                break;
            case EndgameAnswers.HAB_THREE:
                teamDetails.incrementHabThreeAmount();
                break;
            default:
                break;
        }
    }
    private void handleCargoShipAndRocketLevels(TeamDetails teamDetails, int startingPlacedLocation, int cyclePlacedLocation) {
        switch (startingPlacedLocation) {
            case SandstormAnswers.BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount();
                break;
            case SandstormAnswers.MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount();
                break;
            case SandstormAnswers.TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount();
                break;
            case SandstormAnswers.CARGO_SHIP:
                teamDetails.incrementCargoShipAmount();
                break;
            default:
                break;
        }

        switch (cyclePlacedLocation) {
            case CycleAnswers.BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount();
                break;
            case CycleAnswers.MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount();
                break;
            case CycleAnswers.TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount();
                break;
            case CycleAnswers.CARGO_SHIP:
                teamDetails.incrementCargoShipAmount();
                break;
            default:
                break;
        }
    }

    @Override
    public File getFile() {
        FileDefinition fileDef = Vanguard.getFileService().getMostRecentFileDefinition(eTableType.MATCH, true, Vanguard.getInstance().getUserInitials());
        return fileDef.getFile();
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
            default:
                attribute = 999; // If we end up at a different index than the ones above, there's a problem somewhere, so I return a large number to point it out.
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
    public eTableType getTableType() {
        return eTableType.MATCH;
    }

    @Override
    public void clear() {
        teamNumbers = null;
        teamNumberList.clear();
        teamDetailsSparseArray.clear();
    }

    private class TeamDetails {
        private ArrayList<Integer> matchNums = new ArrayList<>();
        private int cargoNum;
        private double averageCargo;
        private int hatchNum;
        private double averageHatchPanels;
        private int effectiveDefenseNum;
        private double averageCargoShip;
        private int cargoShipNum;
        private double averageRocket1;
        private double averageRocket2;
        private double averageRocket3;
        private int rocket1Num;
        private int rocket2Num;
        private int rocket3Num;
        private int hab2Num;
        private int hab3Num;

        void incrementCargoNum() {
            cargoNum++;
        }
        double getAverageCargo() {
            return averageCargo;
        }

        void incrementHatchNum() {
            hatchNum++;
        }
        double getAverageHatchPanels() {
            return averageHatchPanels;
        }

        int getEffectiveDefenseNum() {
            return effectiveDefenseNum;
        }
        void incrementEffectiveDefenseNum() {
            effectiveDefenseNum++;
        }

        void incrementRocketOneAmount() {
            rocket1Num++;
        }
        void incrementRocketTwoAmount() {
            rocket2Num++;
        }
        void incrementRocketThreeAmount() {
            rocket3Num++;
        }

        void incrementHabTwoAmount() {
            hab2Num++;
        }
        void incrementHabThreeAmount() {
            hab3Num++;
        }

        double getAverageCargoShip() {
            return averageCargoShip;
        }

        void incrementCargoShipAmount() {
            cargoShipNum++;
        }

        double getAverageRocket1() {
            return averageRocket1;
        }

        double getAverageRocket2() {
            return averageRocket2;
        }

        double getAverageRocket3() {
            return averageRocket3;
        }

        int getHab2Num() {
            return hab2Num;
        }

        int getHab3Num() {
            return hab3Num;
        }

        void calculateAverages() {
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

        void addMatch(int matchNum) {
            matchNums.add(matchNum);
        }
        boolean hasMatch(int matchNum) {
            return matchNums.contains(matchNum);
        }
    }
}
