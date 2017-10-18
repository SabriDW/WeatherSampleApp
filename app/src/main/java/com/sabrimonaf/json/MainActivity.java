package com.sabrimonaf.json;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {


    TextView city;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.city);
        listView = (ListView) findViewById(R.id.list_view);

        GetWeatherAsyncTask task = new GetWeatherAsyncTask();
        task.execute();
    }


    private void updateUI(String s) {
        try {

            JSONObject root = new JSONObject(s);


            JSONArray weatherUpdates = root.getJSONArray("consolidated_weather");
            String title = root.getString("title");

            ArrayList<WeatherUpdate> updateArrayList = new ArrayList<>();

            for (int i = 0; i < weatherUpdates.length(); i++) {

                JSONObject currentUpdate = weatherUpdates.getJSONObject(i);

                String applicableDate = currentUpdate.getString("applicable_date");
                double minTemp = currentUpdate.getDouble("min_temp");
                double maxTemp = currentUpdate.getDouble("max_temp");
                double theTemp = currentUpdate.getDouble("the_temp");

                WeatherUpdate update = new WeatherUpdate(applicableDate, minTemp, maxTemp, theTemp);

                updateArrayList.add(update);
            }

            city.setText(title);
            WeatherUpdatesAdapter adapter = new WeatherUpdatesAdapter(this, R.layout.item_weather_update, updateArrayList);

            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class GetWeatherAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            StringBuilder JsonData = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {

                // CHANGE THE ID TO TEST DIFFERENT CITIES
                URL url = new URL("https://www.metaweather.com/api/location/12586539/");
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

            Log.v("TIME", String.valueOf(System.currentTimeMillis()));

            return JsonData.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            updateUI(s);
        }
    }

}




/*
public class Async extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder JsonData = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(strings[0]);
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
        return JsonData.toString();

    }

    @Override
    protected void onPostExecute(String s) {
        updateUI(s);
    }
}
*/
