package com.example.skateparkapp;

import static com.example.skateparkapp.SkateparksActivity.MY_FAV_PARK_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RatingActivity extends AppCompatActivity {

    private String parkID, parkRating, noOfFavs, noOfRatings;
    private String myRating;
    private RatingBar parkRatingSB;
    private SwitchCompat parkIsFavSW;
    String updatedNoOfFavs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        TextView parkNameTV = findViewById(R.id.ratingPage_name_et);
        TextView parkVicinityTV = findViewById(R.id.ratingPage_vicinity_et);
        parkRatingSB = findViewById(R.id.ratingPage_rating_sb);
        parkIsFavSW = findViewById(R.id.ratingPage_isfav_sw);
        Button submitBtn = findViewById(R.id.ratingPage_submit_btn);
        Button cancelBtn = findViewById(R.id.ratingPage_cancel_btn);

        Bundle bundle = getIntent().getExtras();
        parkID = bundle.getString("parkid");
        parkRating = bundle.getString("rating");
        noOfFavs = bundle.getString("nofavs");
        noOfRatings = bundle.getString("noratings");
        parkNameTV.setText(bundle.getString("parkname"));
        parkVicinityTV.setText(bundle.getString("parkvicinity"));
        parkIsFavSW.setChecked(parkID.equalsIgnoreCase(MY_FAV_PARK_ID));

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent();
                setResult(3,cancelIntent);
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRatings();
            }
        });

    }

    private void saveRatings() {
        //Initialize firebase objects
        FirebaseAuth authFb = FirebaseAuth.getInstance();
        FirebaseFirestore dbFb = FirebaseFirestore.getInstance();

        String userEmail = authFb.getCurrentUser().getEmail();

        myRating = String.valueOf(parkRatingSB.getRating());
        String myFav = String.valueOf(parkIsFavSW.isChecked());
        //Log.d("ADEDO", "myFav = "+myFav);

        if(parkRating == null){
            parkRating = "0";
        }

        String updatedAvgRating = String.valueOf(Math.round(((Double.parseDouble(parkRating)*Double.parseDouble(noOfRatings))
                + Double.parseDouble(myRating))
                / (Double.parseDouble(noOfRatings)+1.0) * 10.0)/10.0);

        updatedNoOfFavs = noOfFavs;
        DocumentReference userRef = dbFb.collection("skaters").document(userEmail);
        if(myFav.equalsIgnoreCase("true") && !parkID.equalsIgnoreCase(MY_FAV_PARK_ID)){
            updatedNoOfFavs = String.valueOf(Double.parseDouble(noOfFavs) + 1.0);
            userRef.update("skaterFavPark",parkID);
            MY_FAV_PARK_ID = parkID;
        }
        else if ( parkID.equalsIgnoreCase(MY_FAV_PARK_ID) && myFav.equalsIgnoreCase("false")) {
            updatedNoOfFavs = String.valueOf(Double.parseDouble(noOfFavs) - 1.0);
            userRef.update("skaterFavPark",null);
            MY_FAV_PARK_ID = null;
        }

        String updatedNumOfRatings = String.valueOf(Double.parseDouble(noOfRatings) + 1.0);

        DocumentReference parkRef = dbFb.collection("skateparks").document(parkID);
        parkRef.update("avgRating", updatedAvgRating,
                "numOfFavs", updatedNoOfFavs,
                "numOfRatings", updatedNumOfRatings).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent();
                    intent.putExtra("parkID",parkID);
                    intent.putExtra("avgRating",updatedAvgRating);
                    intent.putExtra("numOfFavs",updatedNoOfFavs);
                    intent.putExtra("numOfRatings",updatedNumOfRatings);
                    setResult(2,intent);
                    finish();
                    //Toast.makeText(RatingActivity.this, "Park Rating updated in database", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RatingActivity.this, "Failed to update park's rating", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}