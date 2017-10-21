package com.sabrimonaf.json;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sabri on 10/21/17.
 */

public class SearchResultAdapter extends ArrayAdapter<SearchResult> {

    // global variables to make them accessible
    private Context context;
    private int resource;
    private ArrayList<SearchResult> objects;

    // public constructor
    public SearchResultAdapter(Context context, int resource, ArrayList<SearchResult> objects) {
        // pass the parameters to the super class
        super(context, resource, objects);
        // set the values to the global variables
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // if the convertView is null, inflate the layout and set it to the convertView
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);
        }

        // get the corresponding data object using position
        SearchResult result = objects.get(position);

        // bind views by calling findViewById on the convertView (because we're not in an Activity, so we need to call findViewById on the root view)
        TextView city = convertView.findViewById(R.id.city);

        // set texts using data from the data object
        city.setText(result.getName());

        // return the inflated convertView
        return convertView;
    }
}
