package com.example.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<WeatherData> {
    Context mainActivityContext;
    List<WeatherData> wlist;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<WeatherData> objects) {
        super(context, resource, objects);
        mainActivityContext = context;
        wlist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView hour;
        ImageView img;
        TextView temperature;
        TextView desc;

        LayoutInflater li = (LayoutInflater) mainActivityContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.weather_adapter,null);

        hour = v.findViewById(R.id.id_w_tv_hour);
        img = v.findViewById(R.id.id_w_img);
        temperature = v.findViewById(R.id.id_w_tv_temp);
        desc = v.findViewById(R.id.id_w_tv_desc);

        //Need to make String
        hour.setText(wlist.get(position).getHour());
        img.setImageResource(wlist.get(position).getImageId());
        temperature.setText(wlist.get(position).getTemperature());
        desc.setText(wlist.get(position).getDesc());
        return v;
    }

}
