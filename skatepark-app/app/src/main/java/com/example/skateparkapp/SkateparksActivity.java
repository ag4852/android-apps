package com.example.skateparkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

public class SkateparksActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 100;
    public static final int INTENT_REQUEST_CODE = 2;
    public static String MY_FAV_PARK_ID;
    private LocationManager lm;

    private double currentLat = 0, currentLon = 0;
    private FirebaseFirestore dbFb;
    public ArrayList<Skatepark> parkList = new ArrayList<>();
    private CustomSkateparksAdapter adapter;
    private Skatepark skatepark;

    private FirebaseAuth authFb;

    Button logoutBtn;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE && resultCode == INTENT_REQUEST_CODE) {
            //String updatedParkID = data.getStringExtra("parkID");
            String updatedAvgRating = data.getStringExtra("avgRating");
            String updatedNumOfFavs = data.getStringExtra("numOfFavs");
            String updatedNumOfRatings = data.getStringExtra("numOfRatings");
            skatepark.setAvgRating(updatedAvgRating);
            skatepark.setNumOfFavs(updatedNumOfFavs);
            skatepark.setNumOfRatings(updatedNumOfRatings);
            adapter.notifyDataSetChanged();
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skateparks);

        logoutBtn = findViewById(R.id.skateparksPage_logout_btn);

        //initialize firebase instance
        authFb = FirebaseAuth.getInstance();
        dbFb = FirebaseFirestore.getInstance();
        getFavPark();

        //check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            //Log.d("ADEDO", "fine location asked");
            return;
        }
        getCurrentLocation();

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLon +
                "&radius=15000" + //nearby radius
                "&keyword=skate%20park" + //placeType
                "&sensor=true" +
                "&key=" + getResources().getString(R.string.google_map_key);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authFb.signOut();
                startActivity(new Intent(SkateparksActivity.this, MainActivity.class));
            }
        });

        adapter = new CustomSkateparksAdapter(SkateparksActivity.this, parkList);
        // Attach the adapter to a ListView
        ListView parkListView = (ListView) findViewById(R.id.skateparksPage_parks_lv);
        parkListView.setAdapter(adapter);

        parkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                skatepark = parkList.get(i);
                Intent intent = new Intent(SkateparksActivity.this, RatingActivity.class);
                intent.putExtra("parkid", skatepark.getParkID());
                intent.putExtra("parkname", skatepark.getParkName());
                intent.putExtra("parkvicinity", skatepark.getParkVicinity());
                intent.putExtra("rating", skatepark.getAvgRating());
                intent.putExtra("nofavs", skatepark.getNumOfFavs());
                intent.putExtra("noratings", skatepark.getNumOfRatings());
                //startActivity(intent);
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });

        new SkateParkTask().execute(url);
    }

    private void getFavPark() {
        FirebaseAuth authFb = FirebaseAuth.getInstance();
        String currentUserEmail = authFb.getCurrentUser().getEmail();
        DocumentReference userRef = dbFb.collection("skaters").document(currentUserEmail);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot skaterSnapshot = task.getResult();
                    Skater currentSkater = skaterSnapshot.toObject(Skater.class);
                    SkateparksActivity.MY_FAV_PARK_ID = currentSkater.getSkaterFavPark();
                    //Log.d("ADEDO","Get MY_FAV_PARK_ID from db: "+SkateparksActivity.MY_FAV_PARK_ID);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission was granted", Toast.LENGTH_LONG).show();
                getCurrentLocation();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getCurrentLocation() {
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastLocation != null) {
            currentLat = lastLocation.getLatitude();
            currentLon = lastLocation.getLongitude();
        }
    }

    private class SkateParkTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String parkString = null;
            try {
                parkString = downloadUrl(strings[0]);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SkateparksActivity.this, "downloadURL error "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //Log.d("ADEDO", "after doInBackground");
            return parkString;
        }

        private String downloadUrl(String sUrl) throws IOException {
            // define URL
            URL url = new URL(sUrl);
            // define connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // make connection
            connection.connect();
            //init input stream
            InputStream stream = connection.getInputStream();
            //init buffer reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            //init string builder
            StringBuilder sb = new StringBuilder();
            String line = "";
            //concantinate all lines into sb
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String data = sb.toString();
            return data;
        }

        @Override
        protected void onPostExecute(String parkStr) {
            //Log.d("ADEDO", "before execute ParserTask.execute");
            new ParserTask().execute(parkStr);
        }
    } //SkateParkTask

    private class ParserTask extends AsyncTask<String,Integer,ArrayList<Skatepark>> {

        @Override
        protected ArrayList<Skatepark> doInBackground(String... strings) {
            JSONArray parkJsonArray = null;
            parkList.clear();

            try {
                JSONObject allParksJson = new JSONObject(strings[0]);
                parkJsonArray = allParksJson.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // only take top 10 parks
            int maxLoop = Math.min(10, parkJsonArray.length());
            for (int i = 0; i < maxLoop; i++) {
                try {
                    JSONObject parkObject = (JSONObject) parkJsonArray.get(i);

                    String parkID = parkObject.getString("place_id");
                    String parkName = parkObject.getString("name");
                    String parkVicinity = parkObject.getString("vicinity");

                    DocumentReference parkIdRef = dbFb.collection("skateparks").document(parkID);
                    parkIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot parkSnapshot = task.getResult();
                                if (parkSnapshot.exists()) {
                                    Skatepark skatepark = parkSnapshot.toObject(Skatepark.class);
                                    //Log.d("ADEDO", "exists in DB ="+skatepark.getParkName());
                                    parkList.add(skatepark);
                                } else {
                                    Skatepark newPark = new Skatepark(parkID, parkName, parkVicinity, "0", null, "0");
                                    //Log.d("ADEDO", "new in DB =" + parkID);
                                    parkIdRef.set(newPark);
                                    parkList.add(newPark);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("ADEDO", "Failed to read park Id=" + parkID);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return parkList;
        }

    }//private class ParserTask

}