package com.sabrimonaf.json;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sabri on 10/25/17.
 */

public class SearchWeatherLoader extends AsyncTaskLoader<String> {

    String query;

    public SearchWeatherLoader(Context context, String query){
        super(context);
        this.query = query;
    }

    @Override
    public String loadInBackground() {
        // strings are passed from the the task.execute() methods.
        StringBuilder JsonData = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {

            // this is the URL for the API, we add the query which was passed in the searchButton onClickListener
            // to make the API dynamic
            String urlString = "https://www.metaweather.com/api/location/search/?query=" + query;
            Log.v("NETWORK_URL", urlString);

            // the same code in all requests
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                JsonData.append(line);
                line = reader.readLine();
            }
            Log.v("AsyncTask", "Connected" + httpURLConnection.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("AsyncTask", e.getMessage());
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }


        // return the result (json string)


        return JsonData.toString();

    }
}
