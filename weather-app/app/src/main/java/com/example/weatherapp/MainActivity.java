package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    BufferedReader br;
    Button getdata;
    EditText et;
    ListView lv;
    String tempUrl;

    public TextView latlon;
    public String lat;
    public String lon;
    public String city;

    final String DEGREE  = "\u00b0";

    public CustomAdapter adapter;

    public ArrayList<WeatherData> alw = new ArrayList<WeatherData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getdata = findViewById(R.id.id_b_getdata);
        et = findViewById(R.id.id_et);
        lv = findViewById(R.id.id_lv);
        latlon = findViewById(R.id.id_tv_latlon);

        adapter = new CustomAdapter(this,R.layout.weather_adapter,alw);
        lv.setAdapter(adapter);

        getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskThing myTask = new AsyncTaskThing();
                myTask.execute();
                et.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });

    }//onCreate

    private class AsyncTaskThing extends AsyncTask<String, Void, String> {
        @Override
            protected void onPreExecute() {

            }
            @Override
            protected String doInBackground(String... strings) {
                Log.d("ADEDO", "in doInBackground");
                tempUrl = "http://api.openweathermap.org/geo/1.0/zip?zip="+et.getText().toString()+",US&appid=0dacdea9ea045b62884225e0e92597c9";
                //tempUrl = "http://api.openweathermap.org/geo/1.0/zip?zip=08540,US&appid=0dacdea9ea045b62884225e0e92597c9";
                URL url = null;
                try {
                    url = new URL(tempUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                URLConnection urlConnection = null;
                try {
                    urlConnection = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream inputStream = null;
                try {
                    inputStream = urlConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                br = new BufferedReader(new InputStreamReader(inputStream));

                String response = new String();

                try {
                    for (String line; (line = br.readLine()) != null; response += line) ;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject location = null;
                try {
                    location = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    lat = location.getString("lat");
                    lon = location.getString("lon");
                    city = location.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    //tempUrl = "https://api.openweathermap.org/data/2.5/weather?lat="+location.getString("lat")+"&lon="+location.getString("lon")+"&appid=0dacdea9ea045b62884225e0e92597c9";
                    tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat="+location.getString("lat")+"&lon="+location.getString("lon")+"&exclude=current,minutely,daily,alerts&units=imperial&appid=0dacdea9ea045b62884225e0e92597c9";
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    url = new URL(tempUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    urlConnection = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    inputStream = urlConnection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                br = new BufferedReader(new InputStreamReader(inputStream));

                response = "";

                try {
                    for (String line; (line = br.readLine()) != null; response += line) ;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject resp_json = new JSONObject(response);
                    JSONArray hourly_array = resp_json.getJSONArray("hourly");
                    alw.clear();
                    for(int i=0; i<4; i++) {
                        JSONObject json_obj = hourly_array.getJSONObject(i);
                        JSONArray hourly_weather = json_obj.getJSONArray("weather");

                        WeatherData nw = new WeatherData(json_obj.getInt("dt"),
                                json_obj.getDouble("temp"),
                                hourly_weather.getJSONObject(0).getString("main"),
                                json_obj.getDouble("feels_like"),
                                json_obj.getDouble("wind_gust"),
                                json_obj.getInt("humidity"));
                        alw.add(nw);
                        //Log.d("ADEDO"," in doInBackground Loop Response i="+i+ ", desc=" + nw.getDesc());

                    }
                } catch (JSONException e) {
                    //Log.d("ADEDO", "in doInBackground Error ");
                    e.printStackTrace();
                }

                return response;
            }

            //alt + insert button to see method can be overwrite or implement
            @Override
            protected void onPostExecute(String output) {
                //super.onPostExecute(unused);
                //w.setText(output);
                //Log.d("ADEDO","in onPostExecute");
                adapter.notifyDataSetChanged();
                latlon.setText(city+"\n"+lat+DEGREE+", "+lon+DEGREE);
        }

    }
}