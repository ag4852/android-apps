package com.example.skateparkapp;

import static com.example.skateparkapp.SkateparksActivity.MY_FAV_PARK_ID;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomSkateparksAdapter extends ArrayAdapter<Skatepark> {

    public CustomSkateparksAdapter(Context context, ArrayList<Skatepark> skateparks) {
        super(context, R.layout.item_skatepark, skateparks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_skatepark, parent, false);
        }

        // Get the data item for this position
        Skatepark skatepark = getItem(position);

        // Lookup view for data population
        TextView parkNameTv = (TextView) convertView.findViewById(R.id.skateparksPage_parkname_tv);
        TextView parkRatingTv = (TextView) convertView.findViewById(R.id.skateparksPage_parkrating_tv);
        TextView noOfFavsTv = (TextView) convertView.findViewById(R.id.skateparksPage_nooffav_tv);
        TextView totalRatingsTv = (TextView) convertView.findViewById(R.id.skateparksPage_noofratings_tv);

        // Populate the data into the template view using the data object
        //Log.d("ADEDO","MY_FAV_PARK_ID: "+MY_FAV_PARK_ID);
        if(skatepark.getParkID().equals(MY_FAV_PARK_ID)) {
            parkNameTv.setText("★ " + skatepark.getParkName());
        } else {
            parkNameTv.setText(skatepark.getParkName());
        }

        parkRatingTv.setText(skatepark.getAvgRating());
        noOfFavsTv.setText(skatepark.getNumOfFavs());
        totalRatingsTv.setText(skatepark.getNumOfRatings());
        // Return the completed view to render on screen
        return convertView;
    }

}
