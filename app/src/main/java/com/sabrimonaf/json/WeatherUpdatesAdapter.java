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

    Context context;
    int resource;
    ArrayList<WeatherUpdate> updates;

    public WeatherUpdatesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<WeatherUpdate> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.updates = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, null);
        }

        WeatherUpdate object = updates.get(position);

        TextView applicable_date = convertView.findViewById(R.id.applicable_date);
        TextView minTemp = convertView.findViewById(R.id.min_temp);
        TextView maxTemp = convertView.findViewById(R.id.max_temp);
        TextView theTemp = convertView.findViewById(R.id.the_temp);


        applicable_date.setText(object.getApplicableDate());
        minTemp.setText(object.getMinTemp() + " C");
        maxTemp.setText(object.getMaxTemp() + " C");
        theTemp.setText(object.getTheTemp() + " C");


        return convertView;

    }
}
