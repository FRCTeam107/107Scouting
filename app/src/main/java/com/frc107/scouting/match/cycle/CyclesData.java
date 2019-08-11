package com.frc107.scouting.match.cycle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CyclesData implements Parcelable {
    private int cycleNum = 1;

    private List<Integer> pickupLocationList = new ArrayList<>();
    private List<Integer> itemPickedUpList = new ArrayList<>();
    private List<Integer> locationPlacedList = new ArrayList<>();
    private List<Boolean> defenseList = new ArrayList<>();
    private List<Boolean> onlyDefenseList = new ArrayList<>();

    CyclesData() { }

    public int getCycleNum() {
        return cycleNum;
    }

    public int getPickupLocation(int index) {
        return pickupLocationList.get(index);
    }

    public int getItemPickedUp(int index) {
        return itemPickedUpList.get(index);
    }

    public int getLocationPlaced(int index) {
        return locationPlacedList.get(index);
    }

    public boolean getDefense(int index) {
        return defenseList.get(index);
    }

    public boolean getOnlyDefense(int index) {
        return onlyDefenseList.get(index);
    }

    public void addEntry(int pickupLocation, int itemPickedUp, int locationPlaced, boolean defense, boolean onlyDefense) {
        pickupLocationList.add(pickupLocation);
        itemPickedUpList.add(itemPickedUp);
        locationPlacedList.add(locationPlaced);
        defenseList.add(defense);
        onlyDefenseList.add(onlyDefense);
        cycleNum++;
    }

    public CyclesData(Parcel in) {
        // One might think that one should instead call readValue because we're looking at
        // objects of type Integer. However, at this point, none of these should be null.
        // If one of them is null, we have a bug when validating sandstorm, as we need to have
        // ALL questions answered when exiting.
        // That said, if it crashes here when trying to readInt, then you have a bug when creating the parcel.
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
