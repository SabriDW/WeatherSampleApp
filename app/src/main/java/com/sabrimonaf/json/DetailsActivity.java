package com.sabrimonaf.json;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {


    TextView city;
    ListView listView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // get the city id that was passed from the SearchActivity
        int id = getIntent().getIntExtra("ID", 0);

        city = (TextView) findViewById(R.id.city);
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // create and execute the AsyncTask, and pass the id to the task
        GetWeatherAsyncTask task = new GetWeatherAsyncTask();
        task.execute(id);


    }


    private void updateUI(String s) {

        // wrap the JSON parsing logic inside try/catch
        try {

            // create a new JSONObject from the string s, because the root of the response is an object
            JSONObject root = new JSONObject(s);

            // get title from the root which will contain the city's name
            String title = root.getString("title");

            // get weather updates array from the root
            JSONArray weatherUpdates = root.getJSONArray("consolidated_weather");

            // create an empty ArrayList which will hold the weather updates
            ArrayList<WeatherUpdate> updateArrayList = new ArrayList<>();

            // loop the JSON array to get the individual objects inside it
            for (int i = 0; i < weatherUpdates.length(); i++) {

                // get an object from the array using the for loop counter (i)
                JSONObject currentUpdate = weatherUpdates.getJSONObject(i);

                // get the relevant information
                String applicableDate = currentUpdate.getString("applicable_date");
                double minTemp = currentUpdate.getDouble("min_temp");
                double maxTemp = currentUpdate.getDouble("max_temp");
                double theTemp = currentUpdate.getDouble("the_temp");

                // create a new object from the information we just got from the JSONObject
                WeatherUpdate update = new WeatherUpdate(applicableDate, minTemp, maxTemp, theTemp);

                // add it to the ArrayList that will hold the weather updates
                updateArrayList.add(update);

                // this process will repeat until it reaches the end of the array and put all the items inside our ArrayList
            }

            city.setText(title);

            // create a WeatherUpdatesAdapter, pass the context, resId, and the list
            WeatherUpdatesAdapter adapter = new WeatherUpdatesAdapter(this, R.layout.item_weather_update, updateArrayList);

            // set the adapter to the list view
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // hide the progressBar after the loading is finished
        progressBar.setVisibility(View.GONE);
    }


    class GetWeatherAsyncTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... integers) {

            // strings are passed from the the task.execute() methods.
            StringBuilder JsonData = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {

                // this is the URL for the API, we add the city id which was passed in from the SearchActivity and which we retrieved from the intent
                // to make the API dynamic
                String urlString = "https://www.metaweather.com/api/location/"+ integers[0] + "/";
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

        @Override
        protected void onPostExecute(String s) {
            // call the updateUI() method and pass the json string to it (s)
            updateUI(s);
        }
    }

}
