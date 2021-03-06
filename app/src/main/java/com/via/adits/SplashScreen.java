package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/*-----------------------------------This class has been created for Splash Screen-----------------------------------------------------*/
public class SplashScreen extends AppCompatActivity {


    /*--------------------------------onCreate method defines what will happen when created the screen-----------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No title will be shown
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Bottom navigation bar of Android will not be shown.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //No Status Bar will be shown
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //welcome_screen layout will be loaded as this classes layout
        setContentView(R.layout.splash_screen);
        //This screen will always shown vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Variable definitions will be made under this block.
        ImageView appLogo = (ImageView) findViewById(R.id.appLogoSplash);
        TextView aditsSplash = (TextView) findViewById(R.id.adits2);
        TextView aditsLong = (TextView) findViewById(R.id.adits3);
        TextView companyName = (TextView) findViewById(R.id.companyName);


        Animation animation_i = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        //Setting the duration of the animation as 3000 milliseconds = 3 seconds
        animation_i.setDuration(3000);
        appLogo.setAnimation(animation_i);


        animation_i.setAnimationListener(new Animation.AnimationListener() {
            /*---------------------------------Defines what will happen on the start of the animation-----------------------*/
            @Override
            public void onAnimationStart(Animation animation) {
            }

            /*----------------------------------Defines what will happen on the end of the animation------------------------*/
            @Override
            public void onAnimationEnd(Animation animation) {
                //Ends the animation
                finish();


                //If the app runned for first time open WelcomeScreen
                SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
                if (prefs.getBoolean("firstrun", true)) {
                    prefs.edit().putBoolean("firstrun", false).apply();
                    startActivity(new Intent(getApplicationContext(), WifiScreen.class));
                    finish();
                }
                //Else open WifiScreen.
                else {
                    startActivity(new Intent(getApplicationContext() , WifiScreen.class));
                    finish();
                }
            }

            /*------------------------------Defines what will happen on animation repeat-------------------------------------------*/
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    /*--------------------------------------Defines what will happen during the animation-------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
    }

    }
