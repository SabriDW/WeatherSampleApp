package com.sabrimonaf.json;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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

public class SearchActivity extends AppCompatActivity {

    EditText searchQuery;
    ImageButton searchButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQuery = (EditText) findViewById(R.id.search_query);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list_view);

        // set a click listener for the search button.
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchAsyncTask task = new SearchAsyncTask();
                // create an AsyncTask and execute it while passing the text from the SearchEditText to the task
                task.execute(searchQuery.getText().toString());
            }
        });


    }

    private void updateUI(String s) {

        // wrap the JSON parsing logic inside try/catch
        try {

            // create a new JSONArray from the string s, because the root of the response is an array
            JSONArray root = new JSONArray(s);

            // create an empty ArrayList which will hold the search results
            final ArrayList<SearchResult> results = new ArrayList<>();

            // loop the JSON array to get the individual objects inside it
            for (int i = 0; i < root.length(); i++) {

                // get an object from the array using the for loop counter (i)
                JSONObject object = root.getJSONObject(i);

                // get the relevant information
                // the woeid which we'll use in the DefailsActivity
                int id = object.getInt("woeid");
                // the title which we'll show in the list
                String city = object.getString("title");

                // create a new object from the information we just got from the JSONObject
                SearchResult searchResult = new SearchResult(id, city);

                // add it to the ArrayList that will hold the search results
                results.add(searchResult);

                // this process will repeat until it reaches the end of the array and put all the items inside our ArrayList
            }

            // create a SearchResultAdapter, pass the context, resId, and the list
            SearchResultAdapter adapter = new SearchResultAdapter(this, R.layout.item_search, results);

            // set the adapter to the list view
            listView.setAdapter(adapter);

            // set an onItemClickListener for the listView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // get the city that corresponds with the clicked item, using position
                    SearchResult clickedItem = results.get(position);
                    // get the ID to pass it to the DetailsActivity
                    int woeid = clickedItem.getId();

                    // create intent, putExtra and StartActivity
                    Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                    intent.putExtra("ID", woeid);
                    startActivity(intent);

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class SearchAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            // strings are passed from the the task.execute() methods.
            StringBuilder JsonData = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {

                // this is the URL for the API, we add the query which was passed in the searchButton onClickListener
                // to make the API dynamic
                String urlString = "https://www.metaweather.com/api/location/search/?query=" + strings[0];
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

            if (s != null && !s.isEmpty())
                updateUI(s);
            else
                Toast.makeText(SearchActivity.this, "No Internet!!!", Toast.LENGTH_LONG).show();
        }
    }

}
