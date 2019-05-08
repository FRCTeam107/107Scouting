package com.frc107.scouting.analysis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class TeamDetails {
    private int teamNumber;
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
    private double opr;
    private double dpr;
    private ArrayList<Integer> matches;

    public TeamDetails() {
        matches = new ArrayList<>();
    }

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

    public boolean canAccessRocketOne() {
        return rocket1Num > 0;
    }
    public void incrementRocketOneAmount() {
        rocket1Num++;
    }
    public boolean canAccessRocketTwo() {
        return rocket2Num > 0;
    }
    public void incrementRocketTwoAmount() {
        rocket2Num++;
    }
    public boolean canAccessRocketThree() {
        return rocket3Num > 0;
    }
    public void incrementRocketThreeAmount() {
        rocket3Num++;
    }

    public boolean canClimbHabOne() {
        return hab1Num > 0;
    }
    public void incrementHabOneAmount() {
        hab1Num++;
    }
    public boolean canClimbHabTwo() {
        return hab2Num > 0;
    }
    public void incrementHabTwoAmount() {
        hab2Num++;
    }
    public boolean canClimbHabThree() {
        return hab3Num > 0;
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

    public int getRocket1Num() {
        return rocket1Num;
    }

    public int getRocket2Num() {
        return rocket2Num;
    }

    public int getRocket3Num() {
        return rocket3Num;
    }

    public int getHab1Num() {
        return hab1Num;
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
        BigDecimal bd = new BigDecimal((double) value / (double) matches.size());
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void addMatch(int matchNum) {
        matches.add(matchNum);
    }
    public boolean hasMatch(int matchNum) {
        return matches.contains(matchNum);
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
