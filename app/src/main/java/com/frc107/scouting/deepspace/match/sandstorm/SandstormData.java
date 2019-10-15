package com.frc107.scouting.deepspace.match.sandstorm;

import android.os.Parcel;
import android.os.Parcelable;

public class SandstormData implements Parcelable {
    /**
     * This class holds the backing data for Sandstorm.
     *
     * Use Parcelable to create a class whose instances can be transferred from activity to activity.
     */

    private int matchNumber = -1;
    private int teamNumber = -1;
    private int startingLocation = -1;
    private int startingGamePiece = -1;
    private int placedLocation = -1;
    private boolean crossedBaseline;

    SandstormData() { }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(int startingLocation) {
        this.startingLocation = startingLocation;
    }

    public int getStartingGamePiece() {
        return startingGamePiece;
    }

    public void setStartingGamePiece(int startingGamePiece) {
        this.startingGamePiece = startingGamePiece;
    }

    public int getPlacedLocation() {
        return placedLocation;
    }

    public void setPlacedLocation(int placedLocation) {
        this.placedLocation = placedLocation;
    }

    public boolean getCrossedBaseline() {
        return crossedBaseline;
    }

    public void setCrossedBaseline(boolean crossedBaseline) {
        this.crossedBaseline = crossedBaseline;
    }

    public void clear() {
        matchNumber = -1;
        teamNumber = -1;
        startingLocation = -1;
        startingGamePiece = -1;
        placedLocation = -1;
        crossedBaseline = false;
    }

    public SandstormData(Parcel in) {
        // One might think that one should instead call readValue because we're looking at
        // objects of type Integer. However, at this point, none of these should be null.
        // If one of them is null, we have a bug when validating sandstorm, as we need to have
        // ALL questions answered when exiting.
        // That said, if it crashes here when trying to readInt, then you have a bug earlier on.
        // Go fix it.
        matchNumber = in.readInt();
        teamNumber = in.readInt();
        startingLocation = in.readInt();
        startingGamePiece = in.readInt();
        placedLocation = in.readInt();

        // apparently there isn't a method to read booleans, wat
        crossedBaseline = (Boolean) in.readValue(null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(matchNumber);
        dest.writeInt(teamNumber);
        dest.writeInt(startingLocation);
        dest.writeInt(startingGamePiece);
        dest.writeInt(placedLocation);
        dest.writeValue(crossedBaseline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SandstormData> CREATOR = new Creator<SandstormData>() {
        @Override
        public SandstormData createFromParcel(Parcel in) {
            return new SandstormData(in);
        }

        @Override
        public SandstormData[] newArray(int size) {
            return new SandstormData[size];
        }
    };
}
