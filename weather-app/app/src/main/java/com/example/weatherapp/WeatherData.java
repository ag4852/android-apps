package com.example.weatherapp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WeatherData {
    private int time;
    private double temp;
    private String condition;
    private double feelsLike;
    private double windGust;
    private int humidity;
    //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hha");
    final String DEGREE  = "\u00b0";

    public WeatherData(int t, double te, String c, double fl, double wg, int  h){
        time = t;
        temp = te;
        condition = c;
        feelsLike = fl;
        windGust = wg;
        humidity = h;
    }

    public String getHour() {
        return Instant.ofEpochSecond(this.time).atZone(ZoneId.of("GMT-5")).format(formatter);
    }

    public double getTemp() {
        return temp;
    }
    public String getTemperature() { return temp + DEGREE + "F";}

    public String getCondition() {
        return condition;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getWindGust() {
        return windGust;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getDesc(){ return "It feels like "+feelsLike+DEGREE+" F with a wind gust of "+windGust+" mph and a humidity of "+humidity+"."; }

    public int getImageId() {
        switch(condition.toLowerCase()){
            case "clear":
                return R.drawable.clear;
            case "clouds":
                return R.drawable.clouds;
            case "drizzle":
                return R.drawable.drizzle;
            case "rain":
                return R.drawable.rain;
            case "snow":
                return R.drawable.snow;
            case "thunderstorm":
                return R.drawable.thunderstorm;
            default:
                return R.drawable.mist;
        }
    }
}
