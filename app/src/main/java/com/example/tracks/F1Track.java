package com.example.tracks;

import android.os.Parcel;
import android.os.Parcelable;

public class F1Track implements Parcelable {

    private String trackName;
    private String raceDistance;
    private String numberOfLaps;
    private String firstGrandPrix;
    private String imageUrl;

    // details
    private String circuitType;
    private String trackDirection;
    private String trackWidth;
    private String tyreWear;
    private String weatherConditions;
    private String elevation;
    private String drivingDifficulty;
    private String location;

    // 🔹 Firebase يحتاج constructor فاضي
    public F1Track() {}

    public F1Track(String trackName, String raceDistance, String numberOfLaps,
                   String firstGrandPrix, String imageUrl,
                   String circuitType, String trackDirection,
                   String trackWidth, String tyreWear,
                   String weatherConditions, String elevation,
                   String drivingDifficulty, String location) {

        this.trackName = trackName;
        this.raceDistance = raceDistance;
        this.numberOfLaps = numberOfLaps;
        this.firstGrandPrix = firstGrandPrix;
        this.imageUrl = imageUrl;
        this.circuitType = circuitType;
        this.trackDirection = trackDirection;
        this.trackWidth = trackWidth;
        this.tyreWear = tyreWear;
        this.weatherConditions = weatherConditions;
        this.elevation = elevation;
        this.drivingDifficulty = drivingDifficulty;
        this.location = location;
    }

    // 🔹 Parcelable constructor
    protected F1Track(Parcel in) {
        trackName = in.readString();
        raceDistance = in.readString();
        numberOfLaps = in.readString();
        firstGrandPrix = in.readString();
        imageUrl = in.readString();
        circuitType = in.readString();
        trackDirection = in.readString();
        trackWidth = in.readString();
        tyreWear = in.readString();
        weatherConditions = in.readString();
        elevation = in.readString();
        drivingDifficulty = in.readString();
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trackName);
        dest.writeString(raceDistance);
        dest.writeString(numberOfLaps);
        dest.writeString(firstGrandPrix);
        dest.writeString(imageUrl);
        dest.writeString(circuitType);
        dest.writeString(trackDirection);
        dest.writeString(trackWidth);
        dest.writeString(tyreWear);
        dest.writeString(weatherConditions);
        dest.writeString(elevation);
        dest.writeString(drivingDifficulty);
        dest.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<F1Track> CREATOR = new Creator<F1Track>() {
        @Override
        public F1Track createFromParcel(Parcel in) {
            return new F1Track(in);
        }

        @Override
        public F1Track[] newArray(int size) {
            return new F1Track[size];
        }
    };

    // getters & setters

    public String getTrackName() { return trackName; }
    public void setTrackName(String trackName) { this.trackName = trackName; }

    public String getRaceDistance() { return raceDistance; }
    public void setRaceDistance(String raceDistance) { this.raceDistance = raceDistance; }

    public String getNumberOfLaps() { return numberOfLaps; }
    public void setNumberOfLaps(String numberOfLaps) { this.numberOfLaps = numberOfLaps; }

    public String getFirstGrandPrix() { return firstGrandPrix; }
    public void setFirstGrandPrix(String firstGrandPrix) { this.firstGrandPrix = firstGrandPrix; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCircuitType() { return circuitType; }
    public void setCircuitType(String circuitType) { this.circuitType = circuitType; }

    public String getTrackDirection() { return trackDirection; }
    public void setTrackDirection(String trackDirection) { this.trackDirection = trackDirection; }

    public String getTrackWidth() { return trackWidth; }
    public void setTrackWidth(String trackWidth) { this.trackWidth = trackWidth; }

    public String getTyreWear() { return tyreWear; }
    public void setTyreWear(String tyreWear) { this.tyreWear = tyreWear; }

    public String getWeatherConditions() { return weatherConditions; }
    public void setWeatherConditions(String weatherConditions) { this.weatherConditions = weatherConditions; }

    public String getElevation() { return elevation; }
    public void setElevation(String elevation) { this.elevation = elevation; }

    public String getDrivingDifficulty() { return drivingDifficulty; }
    public void setDrivingDifficulty(String drivingDifficulty) { this.drivingDifficulty = drivingDifficulty; }

    public String getLocation1() { return location; }
    public void setLocation1(String location) { this.location = location; }
}