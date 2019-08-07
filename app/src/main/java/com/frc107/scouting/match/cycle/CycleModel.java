package com.frc107.scouting.match.cycle;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class CycleModel extends ViewModel {
    private CycleData data = new CycleData();
    private Integer currentPickupLocation;
    private Integer currentItemPickedUp;
    private Integer currentLocationPlaced;
    private Boolean currentDefense;
    private Boolean currentOnlyDefense;

    void setWasStartingItemUsed(boolean wasStartingItemUsed) {
        data.setWasStartingItemUsed(wasStartingItemUsed);
    }

    void setCurrentPickupLocation(Integer currentPickupLocation) {
        this.currentPickupLocation = currentPickupLocation;
    }

    void setCurrentItemPickedUp(Integer currentItemPickedUp) {
        this.currentItemPickedUp = currentItemPickedUp;
    }

    void setCurrentLocationPlaced(Integer currentLocationPlaced) {
        this.currentLocationPlaced = currentLocationPlaced;
    }

    void setCurrentDefense(Boolean currentDefense) {
        this.currentDefense = currentDefense;
    }

    void setCurrentOnlyDefense(Boolean currentOnlyDefense) {
        this.currentOnlyDefense = currentOnlyDefense;
    }

    public void finishCycle() {
        data.incrementCycleNum();
        data.addEntry(currentPickupLocation, currentItemPickedUp, currentLocationPlaced, currentDefense, currentOnlyDefense);
        currentPickupLocation = null;
        currentItemPickedUp = null;
        currentLocationPlaced = null;
        currentDefense = null;
        currentOnlyDefense = null;
    }

    int getUnfinishedQuestionId() {
        if (data.getPickupLocation() == null)
            return CycleIDs.PICKUP_LOCATION;

        if (data.getItemPickedUp() == null)
            return CycleIDs.ITEM_PICKED_UP;

        if (data.getLocationPlaced() == null)
            return CycleIDs.LOCATION_PLACED;

        return -1;
    }

    CycleData getData() {
        return data;
    }
}
