package com.via.adits.FunctionalUses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.via.adits.WelcomeScreen;
import com.via.adits.WifiScreen;

import static android.content.Context.MODE_PRIVATE;

public class ControlClass extends AppCompatActivity {


    //This function controls if the app launched for first time or not and chooses which screen
    //will be opened.
    public void firstRun(){
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            //If the app launched for first time.
            //Setting first run flag false.
            prefs.edit().putBoolean("firstrun", false).apply();
            startActivity(new Intent(getApplicationContext(), WelcomeScreen.class));
            finish();
        }
        //If the app has launched before.
        else {
            startActivity(new Intent(getApplicationContext() , WifiScreen.class));
            finish();
        }
    }

}
