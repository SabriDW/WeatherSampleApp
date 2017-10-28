package com.sabrimonaf.json;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        DetailsJSONResponse response = new Gson().fromJson(s, DetailsJSONResponse.class);

        city.setText(response.getTitle());

        WeatherUpdatesAdapter adapter = new WeatherUpdatesAdapter(this, R.layout.item_weather_update, response.getConsolidated_weather());

        listView.setAdapter(adapter);

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
