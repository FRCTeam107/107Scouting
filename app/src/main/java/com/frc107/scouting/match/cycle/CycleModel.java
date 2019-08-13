package com.frc107.scouting.match.cycle;

import androidx.lifecycle.ViewModel;

public class CycleModel extends ViewModel {
    private CyclesData data = new CyclesData();
    private int pickupLocation = -1;
    private int itemPickedUp = -1;
    private int locationPlaced = -1;
    private boolean defense;
    private boolean defenseAllCycle;

    void setPickupLocation(Integer pickupLocationId) {
        this.pickupLocation = CycleAnswers.getAnswerFromId(pickupLocationId);
    }

    void setItemPickedUp(Integer itemPickedUpId) {
        this.itemPickedUp = CycleAnswers.getAnswerFromId(itemPickedUpId);
    }

    void setLocationPlaced(Integer locationPlacedId) {
        this.locationPlaced = CycleAnswers.getAnswerFromId(locationPlacedId);
    }

    void setDefense(boolean defense) {
        this.defense = defense;
    }

    void setDefenseAllCycle(boolean defenseAllCycle) {
        this.defenseAllCycle = defenseAllCycle;
    }

    void finishCycle() {
        data.addEntry(pickupLocation, itemPickedUp, locationPlaced, defense, defenseAllCycle);
        clear();
    }

    void clear() {
        pickupLocation = -1;
        itemPickedUp = -1;
        locationPlaced = -1;
        defense = false;
        defenseAllCycle = false;
    }

    boolean areAllQuestionsUnanswered() {
        if (defense)
            return false;

        if (defenseAllCycle)
            return false;

        if (!answerIsEmpty(pickupLocation))
            return false;

        if (!answerIsEmpty(itemPickedUp))
            return false;

        return answerIsEmpty(locationPlaced);
    }

    int getUnfinishedQuestionId() {
        if (defenseAllCycle)
            return -1;

        if (answerIsEmpty(pickupLocation))
            return CycleIDs.PICKUP_LOCATION;

        if (answerIsEmpty(itemPickedUp))
            return CycleIDs.ITEM_PICKED_UP;

        if (answerIsEmpty(locationPlaced))
            return CycleIDs.LOCATION_PLACED;

        return -1;
    }

    private boolean answerIsEmpty(Integer answer) {
        return answer == null || answer == -1;
    }

    CyclesData getData() {
        return data;
    }
}
