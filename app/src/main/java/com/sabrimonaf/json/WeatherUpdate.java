package com.sabrimonaf.json;

/**
 * Created by Sabri on 10/17/17.
 */

public class WeatherUpdate {

    private String applicableDate;
    private double minTemp;
    private double maxTemp;
    private double theTemp;


    public WeatherUpdate(String applicableDate, double minTemp, double maxTemp, double theTemp) {
        this.applicableDate = applicableDate;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.theTemp = theTemp;
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getTheTemp() {
        return theTemp;
    }

    public void setTheTemp(double theTemp) {
        this.theTemp = theTemp;
    }
}
