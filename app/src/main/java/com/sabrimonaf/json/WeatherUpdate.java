package com.sabrimonaf.json;

/**
 * Created by Sabri on 10/17/17.
 */

public class WeatherUpdate {

    private String applicable_date;
    private double min_temp;
    private double max_temp;
    private double the_temp;


    public WeatherUpdate(String applicableDate, double minTemp, double maxTemp, double theTemp) {
        this.applicable_date = applicableDate;
        this.min_temp = minTemp;
        this.max_temp = maxTemp;
        this.the_temp = theTemp;
    }

    public String getApplicable_date() {
        return applicable_date;
    }

    public void setApplicable_date(String applicable_date) {
        this.applicable_date = applicable_date;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(double min_temp) {
        this.min_temp = min_temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(double max_temp) {
        this.max_temp = max_temp;
    }

    public double getThe_temp() {
        return the_temp;
    }

    public void setThe_temp(double the_temp) {
        this.the_temp = the_temp;
    }
}
