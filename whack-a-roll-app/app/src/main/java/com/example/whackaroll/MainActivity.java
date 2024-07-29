package com.example.whackaroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView tv_counter;
    TextView tv_score;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;
    ImageView img7;
    ImageView img8;
    ImageView img9;
    ConstraintLayout c_layout;
    TableLayout t_layout;
    float vbias = (float) 0.05;
    float hbias = (float) 0.05;
    int whacked = 0;
    int counter;
    int random_pos;
    int random_time;
    int random_roll;
    int[] rolls = {R.drawable.roll1,R.drawable.roll2,R.drawable.roll3,R.drawable.roll4,R.drawable.roll5,R.drawable.roll6,R.drawable.roll7,R.drawable.roll8,R.drawable.wasabi};
    boolean zoomingOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_counter = findViewById(R.id.id_tv_counter);
        tv_score = findViewById(R.id.id_tv_score);
        img1 = findViewById(R.id.id_img1);
        img2 = findViewById(R.id.id_img2);
        img3 = findViewById(R.id.id_img3);
        img4 = findViewById(R.id.id_img4);
        img5 = findViewById(R.id.id_img5);
        img6 = findViewById(R.id.id_img6);
        img7 = findViewById(R.id.id_img7);
        img8 = findViewById(R.id.id_img8);
        img9 = findViewById(R.id.id_img9);
        c_layout = findViewById(R.id.id_c_layout);
        t_layout = findViewById(R.id.id_t_layout);

        c_layout.setBackgroundResource(R.drawable.pixelwood);

        counter = 60;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_counter.setText(""+counter);
                        if(counter%1==0 && counter!=0){
                            random_pos = (int)(Math.random()*9)+1;
                            random_roll = (int)(Math.random()*9);
                            //random_pos = 1;
                            switch(random_pos){
                                case 1:
                                    popUpRoll(img1);
                                    break;
                                case 2:
                                    popUpRoll(img2);
                                    break;
                                case 3:
                                    popUpRoll(img3);
                                    break;
                                case 4:
                                    popUpRoll(img4);
                                    break;
                                case 5:
                                    popUpRoll(img5);
                                    break;
                                case 6:
                                    popUpRoll(img6);
                                    break;
                                case 7:
                                    popUpRoll(img7);
                                    break;
                                case 8:
                                    popUpRoll(img8);
                                    break;
                                case 9:
                                    popUpRoll(img9);
                                    break;
                                default:
                            }
                        }
                        if(counter==0){
                            tv_score.setText("Score: "+whacked);
                            t_layout.setClickable(false);
                        }
                    }
                });
                if(counter>0)
                    counter--;
                else
                    timer.cancel();
            }
        },1000,1000);
        
    }
    public void onClickGo(View v){
        ScaleAnimation clickOutAnimation = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        clickOutAnimation.setDuration(700);
        clickOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
                v.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.INVISIBLE);
                v.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(((ImageView)v).getDrawable().getConstantState() == getResources().getDrawable(R.drawable.wasabi).getConstantState()){
            if(counter>=5)
                counter -= 5;
            else
                counter = 0;
            Toast.makeText(this,"You just lost 5 seconds!",Toast.LENGTH_SHORT).show();
        }else{
            whacked++;
            addTallyMark();
        }

        //If it is doing the zoom out animation already make it not clickable?? So it does not repeat the beginning
        //if(!zoomingOut)
        v.startAnimation(clickOutAnimation);
    }

    public void popUpRoll(ImageView img){
        ScaleAnimation zoomInAnimation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        zoomInAnimation.setDuration(700); //Duration is in ms
        ScaleAnimation zoomOutAnimation = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        zoomOutAnimation.setDuration(700);
        //Stays on screen random amount of time between 2 and 4 seconds
        random_time = (int)(Math.random()*2001)+2000;
        zoomOutAnimation.setStartOffset(random_time);

        /*zoomOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img.setClickable(false);
                    }
                }, random_time);
                //zoomingOut = true;
                //Log.d("ADEDO","In zoomOutAnimation START");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.setClickable(true);
                //zoomingOut = false;
                //Log.d("ADEDO","In zoomOutAnimation END");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(zoomInAnimation);
        animationSet.addAnimation(zoomOutAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                img.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(img.getVisibility()==View.INVISIBLE){
            img.setImageResource(rolls[random_roll]);
            img.startAnimation(animationSet);
        }
    }

    public void addTallyMark(){
        ImageView img_tally = new ImageView(this);
        img_tally.setId(View.generateViewId());
        img_tally.setImageResource(R.drawable.stick);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        img_tally.setLayoutParams(params);

        c_layout.addView(img_tally);

        ConstraintSet cSet = new ConstraintSet();
        cSet.clone(c_layout);

        cSet.connect(img_tally.getId(),ConstraintSet.TOP,c_layout.getId(),ConstraintSet.TOP);
        cSet.connect(img_tally.getId(),ConstraintSet.BOTTOM,c_layout.getId(),ConstraintSet.BOTTOM);
        cSet.connect(img_tally.getId(),ConstraintSet.LEFT,c_layout.getId(),ConstraintSet.LEFT);
        cSet.connect(img_tally.getId(),ConstraintSet.RIGHT,c_layout.getId(),ConstraintSet.RIGHT);

        cSet.setHorizontalBias(img_tally.getId(),vbias);
        cSet.setVerticalBias(img_tally.getId(),hbias);

        cSet.applyTo(c_layout);

        vbias+=0.02;
    }
}