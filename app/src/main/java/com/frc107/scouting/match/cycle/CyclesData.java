package com.frc107.scouting.match.cycle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CyclesData implements Parcelable {
    private int cycleNum = 1;

    private List<Integer> pickupLocationList;
    private List<Integer> itemPickedUpList;
    private List<Integer> locationPlacedList;
    private List<Boolean> defenseList;
    private List<Boolean> onlyDefenseList;

    CyclesData() {
        pickupLocationList = new ArrayList<>();
        itemPickedUpList = new ArrayList<>();
        locationPlacedList = new ArrayList<>();
        defenseList = new ArrayList<>();
        onlyDefenseList = new ArrayList<>();
    }

    public int getCycleNum() {
        return cycleNum;
    }

    public void incrementCycleNum() {
        cycleNum++;
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

    public CyclesData(Parcel in) {
        // One might think that one should instead call readValue because we're looking at
        // objects of type Integer. However, at this point, none of these should be null.
        // If one of them is null, we have a bug when validating sandstorm, as we need to have
        // ALL questions answered when exiting.
        // That said, if it crashes here when trying to readInt, then you have a bug earlier on.
        // Go fix it.

        cycleNum = in.readInt();

        in.readList(pickupLocationList, Integer.class.getClassLoader());
        in.readList(itemPickedUpList, Integer.class.getClassLoader());
        in.readList(locationPlacedList, Integer.class.getClassLoader());
        in.readList(defenseList, Boolean.class.getClassLoader());
        in.readList(onlyDefenseList, Boolean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cycleNum);

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

    public static final Creator<CyclesData> CREATOR = new Creator<CyclesData>() {
        @Override
        public CyclesData createFromParcel(Parcel in) {
            return new CyclesData(in);
        }

        @Override
        public CyclesData[] newArray(int size) {
            return new CyclesData[size];
        }
    };
}
