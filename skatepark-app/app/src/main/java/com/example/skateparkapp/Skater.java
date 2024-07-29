package com.example.skateparkapp;

public class Skater {
    private String skaterEmail;
    private String skaterName;
    private String skaterPassword;
    private String skaterAge;
    private String skaterGender;
    private String skaterFavPark;

    public Skater() {
    }
    public Skater(String skaterEmail, String skaterName, String skaterPassword, String skaterAge, String skaterGender, String skaterFavPark) {
        this.skaterEmail = skaterEmail;
        this.skaterName = skaterName;
        this.skaterPassword = skaterPassword;
        this.skaterAge = skaterAge;
        this.skaterGender = skaterGender;
        this.skaterFavPark = skaterFavPark;
    }

    public String getSkaterEmail() {
        return skaterEmail;
    }

    public void setSkaterEmail(String skaterEmail) {
        this.skaterEmail = skaterEmail;
    }

    public String getSkaterName() {
        return skaterName;
    }

    public void setSkaterName(String skaterName) {
        this.skaterName = skaterName;
    }

    public String getSkaterPassword() {
        return skaterPassword;
    }

    public void setSkaterPassword(String skaterPassword) {
        this.skaterPassword = skaterPassword;
    }

    public String getSkaterAge() {
        return skaterAge;
    }

    public void setSkaterAge(String skaterAge) {
        this.skaterAge = skaterAge;
    }

    public String getSkaterGender() {
        return skaterGender;
    }

    public void setSkaterGender(String skaterGender) {
        this.skaterGender = skaterGender;
    }

    public String getSkaterFavPark() {
        return skaterFavPark;
    }

    public void setSkaterFavParks(String skaterFavPark) {
        this.skaterFavPark = skaterFavPark;
    }
}
