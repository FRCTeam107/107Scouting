package com.frc107.scouting.deepspace.match;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import com.frc107.scouting.Scouting;
import com.frc107.scouting.core.Logger;
import com.frc107.scouting.core.analysis.IAnalysisManager;
import com.frc107.scouting.core.file.FileDefinition;
import com.frc107.scouting.eTableType;
import com.frc107.scouting.deepspace.match.cycle.CycleAnswers;
import com.frc107.scouting.deepspace.match.endgame.EndgameAnswers;
import com.frc107.scouting.deepspace.match.sandstorm.SandstormAnswers;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private static final int CARGO = 0;
    private static final int HATCH_PANEL = 1;
    private static final int CARGO_SHIP = 2;
    private static final int ROCKET_1 = 3;
    private static final int ROCKET_2 = 4;
    private static final int ROCKET_3 = 5;
    private static final int HAB_2_AMOUNT = 6;
    private static final int HAB_3_AMOUNT = 7;
    private static final int SUCCESSFUL_DEFENSE_AMOUNT = 8;

    private SparseArray<TeamDetails> teamDetailsArray = new SparseArray<>();
    private List<Integer> teamNumberList = new ArrayList<>();
    private Integer[] teamNumbers;

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

        TeamDetails teamDetails = teamDetailsArray.get(teamNumber);
        if (teamDetails == null) {
            teamDetailsArray.put(teamNumber, new TeamDetails());
            teamNumberList.add(teamNumber);
            teamDetails = teamDetailsArray.get(teamNumber);
        }

        if (!teamDetails.hasMatch(matchNumber)) {
            teamDetails.addMatch(matchNumber);

            // These things reoccur across multiple rows, so only do them once.
            handleSandstormGamePiece(teamDetails, matchNumber, startingPlacedLocation, startingItem);
            handleMatchDefense(teamDetails, matchNumber, defense);
        }

        handleCycleGamePieces(teamDetails, matchNumber, cyclePlacedLocation, itemPickedUp);
        handleHabLevels(teamDetails, matchNumber, habLevel);
        handleCargoShipAndRocketLevels(teamDetails, matchNumber, startingPlacedLocation, cyclePlacedLocation);

        return true;
    }

    private void handleSandstormGamePiece(TeamDetails teamDetails, int matchNumber, int startingPlacedLocation, int startingItem) {
        if (startingPlacedLocation != SandstormAnswers.FLOOR && startingPlacedLocation != SandstormAnswers.NOT_PLACED) {
            switch (startingItem) {
                case SandstormAnswers.CARGO:
                    teamDetails.incrementCargoNum(matchNumber);
                    break;
                case SandstormAnswers.HATCH_PANEL:
                    teamDetails.incrementHatchNum(matchNumber);
                    break;
                default:
                    break;
            }
        }
    }
    private void handleMatchDefense(TeamDetails teamDetails, int matchNumber, int defense) {
        if (defense == EndgameAnswers.EFFECTIVE_DEFENSE) {
            teamDetails.incrementEffectiveDefenseNum(matchNumber);
        }
    }
    private void handleCycleGamePieces(TeamDetails teamDetails, int matchNumber, int cyclePlacedLocation, int itemPickedUp) {
        if (cyclePlacedLocation != CycleAnswers.FLOOR && cyclePlacedLocation != CycleAnswers.NOT_PLACED) {
            switch (itemPickedUp) {
                case CycleAnswers.CARGO:
                    teamDetails.incrementCargoNum(matchNumber);
                    break;
                case CycleAnswers.HATCH_PANEL:
                    teamDetails.incrementHatchNum(matchNumber);
                    break;
                default:
                    break;
            }
        }
    }
    private void handleHabLevels(TeamDetails teamDetails, int matchNumber, int habLevel) {
        switch (habLevel) {
            case EndgameAnswers.HAB_TWO:
                teamDetails.incrementHabTwoAmount(matchNumber);
                break;
            case EndgameAnswers.HAB_THREE:
                teamDetails.incrementHabThreeAmount(matchNumber);
                break;
            default:
                break;
        }
    }
    private void handleCargoShipAndRocketLevels(TeamDetails teamDetails, int matchNumber, int startingPlacedLocation, int cyclePlacedLocation) {
        switch (startingPlacedLocation) {
            case SandstormAnswers.BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount(matchNumber);
                break;
            case SandstormAnswers.MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount(matchNumber);
                break;
            case SandstormAnswers.TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount(matchNumber);
                break;
            case SandstormAnswers.CARGO_SHIP:
                teamDetails.incrementCargoShipAmount(matchNumber);
                break;
            default:
                break;
        }

        switch (cyclePlacedLocation) {
            case CycleAnswers.BOTTOM_ROCKET:
                teamDetails.incrementRocketOneAmount(matchNumber);
                break;
            case CycleAnswers.MIDDLE_ROCKET:
                teamDetails.incrementRocketTwoAmount(matchNumber);
                break;
            case CycleAnswers.TOP_ROCKET:
                teamDetails.incrementRocketThreeAmount(matchNumber);
                break;
            case CycleAnswers.CARGO_SHIP:
                teamDetails.incrementCargoShipAmount(matchNumber);
                break;
            default:
                break;
        }
    }

    @Override
    public File getFile() {
        FileDefinition fileDef = Scouting.getFileService().getMostRecentFileDefinition(eTableType.MATCH, true, Scouting.getInstance().getUserInitials());
        return fileDef.getFile();
    }

    private int attributeIndex;

    @Override
    public void setAttribute(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    @Override
    public double getAttributeValueForTeam(int teamNumber) {
        TeamDetails teamDetails = teamDetailsArray.get(teamNumber);
        double attribute;
        switch (attributeIndex) {
            case CARGO:
                attribute = teamDetails.getAverageCargo();
                break;
            case HATCH_PANEL:
                attribute = teamDetails.getAverageHatchPanels();
                break;
            case CARGO_SHIP:
                attribute = teamDetails.getAverageCargoShip();
                break;
            case ROCKET_1:
                attribute = teamDetails.getAverageRocket1();
                break;
            case ROCKET_2:
                attribute = teamDetails.getAverageRocket2();
                break;
            case ROCKET_3:
                attribute = teamDetails.getAverageRocket3();
                break;
            case HAB_2_AMOUNT:
                attribute = teamDetails.getTotalHab2Climbs();
                break;
            case HAB_3_AMOUNT:
                attribute = teamDetails.getTotalHab3Climbs();
                break;
            case SUCCESSFUL_DEFENSE_AMOUNT:
                attribute = teamDetails.getTotalEffectiveDefenseNum();
                break;
            default:
                attribute = 999; // If we end up at a different index than the ones above, there's a problem somewhere, so return a large number to point it out.
                break;
        }
        return attribute;
    }

    @Override
    public int getAttributeForTeamAtMatch(int teamNumber, int matchNumber) {
        TeamDetails teamDetails = teamDetailsArray.get(teamNumber);
        int attribute;
        switch (attributeIndex) {
            case CARGO:
                attribute = teamDetails.getCargoDeliveriesAtMatch(matchNumber);
                break;
            case HATCH_PANEL:
                attribute = teamDetails.getHatchDeliveriesAtMatch(matchNumber);
                break;
            case CARGO_SHIP:
                attribute = teamDetails.getCargoShipDropoffsPerMatch(matchNumber);
                break;
            case ROCKET_1:
                attribute = teamDetails.getRocket1DropoffsAtMatch(matchNumber);
                break;
            case ROCKET_2:
                attribute = teamDetails.getRocket2DropoffsAtMatch(matchNumber);
                break;
            case ROCKET_3:
                attribute = teamDetails.getRocket3DropoffsAtMatch(matchNumber);
                break;
            case HAB_2_AMOUNT:
                attribute = teamDetails.didClimbHab2AtMatch(matchNumber) ? 1 : 0;
                break;
            case HAB_3_AMOUNT:
                attribute = teamDetails.didClimbHab3AtMatch(matchNumber) ? 1 : 0;
                break;
            case SUCCESSFUL_DEFENSE_AMOUNT:
                attribute = teamDetails.getEffectiveDefensesAtMatch(matchNumber);
                break;
            default:
                attribute = 999; // If we end up at a different index than the ones above, there's a problem somewhere, so return a large number to point it out.
                break;
        }
        return attribute;
    }

    @Override
    public void makeFinalCalculations() {
        for (int i = 0; i < teamDetailsArray.size(); i++) {
            TeamDetails teamDetails = teamDetailsArray.valueAt(i);
            teamDetails.calculateAverages();
            teamDetails.sortMatchNumbers();
        }

        teamNumbers = teamNumberList.toArray(new Integer[0]);
        Arrays.sort(teamNumbers);
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

    @Override
    public Integer[] getTeamNumbers() {
        return teamNumbers;
    }

    @Override
    public Integer[] getMatchNumbersForTeam(int teamNumber) {
        TeamDetails teamDetails = teamDetailsArray.get(teamNumber);
        Integer[] matchNumbers = teamDetails.getMatchNumbers().toArray(new Integer[0]);
        return matchNumbers;
    }

    @Override
    public eTableType getTableType() {
        return eTableType.MATCH;
    }

    @Override
    public void clear() {
        teamNumbers = null;
        teamNumberList.clear();
        teamDetailsArray.clear();
    }

    private class TeamDetails {
        private List<Integer> matchNumbers = new ArrayList<>();
        private SparseArray<Double[]> matchAttributesArray = new SparseArray<>();

        private SparseIntArray cargoDeliveriesPerMatch = new SparseIntArray();
        private SparseIntArray hatchDeliveriesPerMatch = new SparseIntArray();
        private SparseIntArray cargoShipDropoffsPerMatch = new SparseIntArray();
        private SparseIntArray rocket1DropoffsPerMatch = new SparseIntArray();
        private SparseIntArray rocket2DropoffsPerMatch = new SparseIntArray();
        private SparseIntArray rocket3DropoffsPerMatch = new SparseIntArray();
        private SparseBooleanArray hab2ClimbsPerMatch = new SparseBooleanArray();
        private SparseBooleanArray hab3ClimbsPerMatch = new SparseBooleanArray();
        private SparseIntArray effectiveDefensesPerMatch = new SparseIntArray();

        private int totalCargoDeliveries;
        private double averageCargo;
        private int totalHatchDeliveries;
        private double averageHatchPanels;
        private int effectiveDefenseNum;
        private double averageCargoShip;
        private int totalCargoShipDropoffs;
        private double averageRocket1;
        private double averageRocket2;
        private double averageRocket3;
        private int totalRocket1Dropoffs;
        private int totalRocket2Dropoffs;
        private int totalRocket3Dropoffs;
        private int totalHab2Climbs;
        private int totalHab3Climbs;

        void incrementCargoNum(int matchNumber) {
            totalCargoDeliveries++;

            if (cargoDeliveriesPerMatch.indexOfKey(matchNumber) == -1)
                cargoDeliveriesPerMatch.put(matchNumber, 0);

            int matchValue = cargoDeliveriesPerMatch.get(matchNumber);
            cargoDeliveriesPerMatch.put(matchNumber, matchValue + 1);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[CARGO]++;
        }
        void incrementHatchNum(int matchNumber) {
            totalHatchDeliveries++;

            int matchValue = hatchDeliveriesPerMatch.get(matchNumber);
            hatchDeliveriesPerMatch.put(matchNumber, matchValue + 1);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[HATCH_PANEL]++;
        }
        void incrementEffectiveDefenseNum(int matchNumber) {
            effectiveDefenseNum++;

            int matchValue = effectiveDefensesPerMatch.get(matchNumber);
            effectiveDefensesPerMatch.put(matchNumber, matchValue + 1);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[SUCCESSFUL_DEFENSE_AMOUNT]++;
        }
        void incrementRocketOneAmount(int matchNumber) {
            totalRocket1Dropoffs++;

            int matchValue = rocket1DropoffsPerMatch.get(matchNumber);
            rocket1DropoffsPerMatch.put(matchNumber, matchValue + 1);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[ROCKET_1]++;
        }
        void incrementRocketTwoAmount(int matchNumber) {
            totalRocket2Dropoffs++;

            int matchValue = rocket2DropoffsPerMatch.get(matchNumber);
            rocket2DropoffsPerMatch.put(matchNumber, matchValue + 1);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[ROCKET_2]++;
        }
        void incrementRocketThreeAmount(int matchNumber) {
            totalRocket3Dropoffs++;

            int matchValue = rocket3DropoffsPerMatch.get(matchNumber);
            rocket3DropoffsPerMatch.put(matchNumber, matchValue + 1);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[ROCKET_3]++;
        }
        void incrementHabTwoAmount(int matchNumber) {
            totalHab2Climbs++;

            hab2ClimbsPerMatch.put(matchNumber, true);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[HAB_2_AMOUNT]++;
        }
        void incrementHabThreeAmount(int matchNumber) {
            totalHab3Climbs++;

            hab3ClimbsPerMatch.put(matchNumber, true);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[HAB_3_AMOUNT]++;
        }
        void incrementCargoShipAmount(int matchNumber) {
            totalCargoShipDropoffs++;

            int matchValue = cargoShipDropoffsPerMatch.get(matchNumber);
            cargoShipDropoffsPerMatch.put(matchNumber, matchValue);

            //Double[] matchAttributes = matchAttributesArray.get(matchNumber);
            //matchAttributes[CARGO_SHIP]++;
        }

        double getAverageCargo() {
            return averageCargo;
        }
        double getAverageHatchPanels() {
            return averageHatchPanels;
        }
        double getAverageCargoShip() {
            return averageCargoShip;
        }
        int getTotalEffectiveDefenseNum() {
            return effectiveDefenseNum;
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
        int getTotalHab2Climbs() {
            return totalHab2Climbs;
        }
        int getTotalHab3Climbs() {
            return totalHab3Climbs;
        }

        int getCargoDeliveriesAtMatch(int matchNumber) {
            return cargoDeliveriesPerMatch.get(matchNumber);
        }
        int getHatchDeliveriesAtMatch(int matchNumber) {
            return hatchDeliveriesPerMatch.get(matchNumber);
        }
        int getCargoShipDropoffsPerMatch(int matchNumber) {
            return cargoShipDropoffsPerMatch.get(matchNumber);
        }
        int getRocket1DropoffsAtMatch(int matchNumber) {
            return rocket1DropoffsPerMatch.get(matchNumber);
        }
        int getRocket2DropoffsAtMatch(int matchNumber) {
            return rocket2DropoffsPerMatch.get(matchNumber);
        }
        int getRocket3DropoffsAtMatch(int matchNumber) {
            return rocket3DropoffsPerMatch.get(matchNumber);
        }
        boolean didClimbHab2AtMatch(int matchNumber) {
            return hab2ClimbsPerMatch.get(matchNumber);
        }
        boolean didClimbHab3AtMatch(int matchNumber) {
            return hab3ClimbsPerMatch.get(matchNumber);
        }
        int getEffectiveDefensesAtMatch(int matchNumber) {
            return effectiveDefensesPerMatch.get(matchNumber);
        }

        void calculateAverages() {
            averageCargo = getAveragePerMatch(totalCargoDeliveries);
            averageHatchPanels = getAveragePerMatch(totalHatchDeliveries);
            averageCargoShip = getAveragePerMatch(totalCargoShipDropoffs);
            averageRocket1 = getAveragePerMatch(totalRocket1Dropoffs);
            averageRocket2 = getAveragePerMatch(totalRocket2Dropoffs);
            averageRocket3 = getAveragePerMatch(totalRocket3Dropoffs);
        }

        void sortMatchNumbers() {
            Collections.sort(matchNumbers);
            matchNumbers = Collections.unmodifiableList(matchNumbers);
        }

        private double getAveragePerMatch(int value) {
            BigDecimal bd = BigDecimal.valueOf((double) value / (double) matchNumbers.size());
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }

        void addMatch(int matchNum) {
            if (matchNumbers.contains(matchNum))
                return;

            matchNumbers.add(matchNum);
            //matchAttributesArray.put(matchNum, new Double[ATTRIBUTE_AMOUNT]);

            cargoDeliveriesPerMatch.put(matchNum, 0);
            hatchDeliveriesPerMatch.put(matchNum, 0);
            cargoShipDropoffsPerMatch.put(matchNum, 0);
            rocket1DropoffsPerMatch.put(matchNum, 0);
            rocket2DropoffsPerMatch.put(matchNum, 0);
            rocket3DropoffsPerMatch.put(matchNum, 0);
            hab2ClimbsPerMatch.put(matchNum, false);
            hab3ClimbsPerMatch.put(matchNum, false);
            effectiveDefensesPerMatch.put(matchNum, 0);
        }
        boolean hasMatch(int matchNum) {
            return matchAttributesArray.indexOfKey(matchNum) != -1;
        }
        List<Integer> getMatchNumbers() {
            return matchNumbers;
        }
    }
}
