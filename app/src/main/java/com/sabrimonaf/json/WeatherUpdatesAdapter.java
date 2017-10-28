package com.sabrimonaf.json;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sabri on 10/17/17.
 */

public class WeatherUpdatesAdapter extends ArrayAdapter<WeatherUpdate> {

    // global variables to make them accessible
    Context context;
    int resource;
    ArrayList<WeatherUpdate> updates;

    // public constructor
    public WeatherUpdatesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<WeatherUpdate> objects) {
        // pass the parameters to the super class
        super(context, resource, objects);
        // set the values to the global variables
        this.context = context;
        this.resource = resource;
        this.updates = objects;
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
        WeatherUpdate object = updates.get(position);

        // bind views by calling findViewById on the convertView (because we're not in an Activity, so we need to call findViewById on the root view)
        TextView applicableDate = convertView.findViewById(R.id.applicable_date);
        TextView minTemp = convertView.findViewById(R.id.min_temp);
        TextView maxTemp = convertView.findViewById(R.id.max_temp);
        TextView theTemp = convertView.findViewById(R.id.the_temp);


        // set texts using data from the data object
        applicableDate.setText(object.getApplicable_date());
        minTemp.setText(object.getMin_temp() + " C");
        maxTemp.setText(object.getMax_temp() + " C");
        theTemp.setText(object.getThe_temp() + " C");


        // return the inflated convertView
        return convertView;

    }
}
