package com.example.skateparkapp;

public class Skatepark {
    private String parkID;
    private String parkName;
    private String parkVicinity;
    private String numOfRatings;
    private String avgRating;
    private String numOfFavs;

    public Skatepark() {
    }

    public Skatepark(String parkID, String parkName, String parkVicinity, String numOfRatings, String avgRating, String numOfFavs) {
        this.parkID = parkID;
        this.parkName = parkName;
        this.parkVicinity = parkVicinity;
        this.numOfRatings = numOfRatings;
        this.avgRating = avgRating;
        this.numOfFavs = numOfFavs;
    }

    public String getParkID() {
        return parkID;
    }

    public void setParkID(String parkID) {
        this.parkID = parkID;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkVicinity() {
        return parkVicinity;
    }

    public void setParkVicinity(String parkVicinity) {
        this.parkVicinity = parkVicinity;
    }

    public String getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(String numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getNumOfFavs() {
        return numOfFavs;
    }

    public void setNumOfFavs(String numOfFavs) {
        this.numOfFavs = numOfFavs;
    }
}
