package com.frc107.scouting.match.sandstorm;

import android.os.Parcel;
import android.os.Parcelable;

public class SandstormData implements Parcelable {
    private Integer matchNumber;
    private Integer teamNumber;
    private Integer startingLocation;
    private Integer startingGamePiece;
    private Integer placedLocation;
    private Boolean crossedBaseline;

    SandstormData() { }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Integer getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(Integer startingLocation) {
        this.startingLocation = startingLocation;
    }

    public Integer getStartingGamePiece() {
        return startingGamePiece;
    }

    public void setStartingGamePiece(Integer startingGamePiece) {
        this.startingGamePiece = startingGamePiece;
    }

    public Integer getPlacedLocation() {
        return placedLocation;
    }

    public void setPlacedLocation(Integer placedLocation) {
        this.placedLocation = placedLocation;
    }

    public Boolean getCrossedBaseline() {
        return crossedBaseline;
    }

    public void setCrossedBaseline(Boolean crossedBaseline) {
        this.crossedBaseline = crossedBaseline;
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
