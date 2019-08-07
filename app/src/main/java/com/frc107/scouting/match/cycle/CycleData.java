package com.frc107.scouting.match.cycle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CycleData implements Parcelable {
    private int cycleNum;
    private boolean wasStartingItemUsed;

    private List<Integer> pickupLocationList = new ArrayList<>();
    private List<Integer> itemPickedUpList = new ArrayList<>();
    private List<Integer> locationPlacedList = new ArrayList<>();
    private List<Boolean> defenseList = new ArrayList<>();
    private List<Boolean> onlyDefenseList = new ArrayList<>();

    CycleData() { }

    public int getCycleNum() {
        return cycleNum;
    }

    public void incrementCycleNum() {
        cycleNum++;
    }

    public boolean wasStartingItemUsed() {
        return wasStartingItemUsed;
    }

    public void setWasStartingItemUsed(boolean wasStartingItemUsed) {
        this.wasStartingItemUsed = wasStartingItemUsed;
    }

    public List<Integer> getPickupLocationList() {
        return pickupLocationList;
    }

    public List<Integer> getItemPickedUpList() {
        return itemPickedUpList;
    }

    public List<Integer> getLocationPlacedList() {
        return locationPlacedList;
    }

    public List<Boolean> getDefenseList() {
        return defenseList;
    }

    public List<Boolean> getOnlyDefenseList() {
        return onlyDefenseList;
    }

    public void addEntry(int pickupLocation, int itemPickedUp, int locationPlaced, boolean defense, boolean onlyDefense) {
        pickupLocationList.add(pickupLocation);
        itemPickedUpList.add(itemPickedUp);
        locationPlacedList.add(locationPlaced);
        defenseList.add(defense);
        onlyDefenseList.add(onlyDefense);
    }

    public CycleData(Parcel in) {
        // One might think that one should instead call readValue because we're looking at
        // objects of type Integer. However, at this point, none of these should be null.
        // If one of them is null, we have a bug when validating sandstorm, as we need to have
        // ALL questions answered when exiting.
        // That said, if it crashes here when trying to readInt, then you have a bug earlier on.
        // Go fix it.

        cycleNum = in.readInt();

        // apparently there isn't a method to read booleans, wat
        wasStartingItemUsed = (Boolean) in.readValue(null);

        in.readList(pickupLocationList, Integer.class.getClassLoader());
        in.readList(itemPickedUpList, Integer.class.getClassLoader());
        in.readList(locationPlacedList, Integer.class.getClassLoader());
        in.readList(defenseList, Boolean.class.getClassLoader());
        in.readList(onlyDefenseList, Boolean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cycleNum);
        dest.writeValue(wasStartingItemUsed);
        dest.writeList(pickupLocationList);
        dest.writeList(itemPickedUpList);
        dest.writeList(locationPlacedList);
        dest.writeList(defenseList);
        dest.writeList(onlyDefenseList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CycleData> CREATOR = new Creator<CycleData>() {
        @Override
        public CycleData createFromParcel(Parcel in) {
            return new CycleData(in);
        }

        @Override
        public CycleData[] newArray(int size) {
            return new CycleData[size];
        }
    };
}
