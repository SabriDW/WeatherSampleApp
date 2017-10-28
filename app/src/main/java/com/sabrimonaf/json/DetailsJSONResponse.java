package com.sabrimonaf.json;

import java.util.ArrayList;

/**
 * Created by Sabri on 10/22/17.
 */

public class DetailsJSONResponse {

    private ArrayList<WeatherUpdate> consolidated_weather;
    private String title;

    public ArrayList<WeatherUpdate> getConsolidated_weather() {
        return consolidated_weather;
    }

    public void setConsolidated_weather(ArrayList<WeatherUpdate> consolidated_weather) {
        this.consolidated_weather = consolidated_weather;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
