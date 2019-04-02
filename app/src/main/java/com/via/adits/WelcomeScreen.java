package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.FunctionalUses.JsonClass;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstaveState) {
        super.onCreate(savedInstaveState);

        //No title will be shown
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Bottom navigation bar of Android will not be shown.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //No Status Bar will be shown
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //welcome_screen layout will be loaded as this classes layout
        setContentView(R.layout.welcome_screen);
        //This screen will turn if sensor of the phone sense the phone has turned
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        //Variable definitions will be made under this block.
        ImageView appLogo = (ImageView) findViewById(R.id.appLogo);
        TextView adits = (TextView) findViewById(R.id.adits);
        TextView kayitText = (TextView) findViewById(R.id.kayitText);
        final EditText nameInput = (EditText) findViewById(R.id.name_input);
        final EditText tcInput = (EditText) findViewById(R.id.tc_input);
        final EditText ageInput = (EditText) findViewById(R.id.age_input);
        final Spinner healthInput = (Spinner) findViewById(R.id.health_input);
        Button submitButton = (Button) findViewById(R.id.submitBtn);
        TextView companyNameWelcome = (TextView) findViewById(R.id.companyNameWelcome);


        //A controller object has been created to control progress through the activity.
        final ControlClass controller = new ControlClass();

        //A JsonClass object has been created to send and get Json data.
        final JsonClass json = new JsonClass();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Checking if EditTexts and Spinner is null or not.
               controller.editTextNullCheck(nameInput, getApplicationContext());
               controller.editTextNullCheck(tcInput, getApplicationContext());
               controller.editTextNullCheck(ageInput, getApplicationContext());
               controller.spinnerNullCheck(healthInput, getApplicationContext());

               //gets the String values of editTexts
               String name_data = String.valueOf(nameInput.getText());
               String tc_data = String.valueOf(nameInput.getText());
               String age_data = String.valueOf(nameInput.getText());
               String health_data = String.valueOf(nameInput.getText());
               Integer level_data = json.calculateLevel(age_data,health_data,getApplicationContext());

               //Checking if EditTexts and Spinner is empty or not.
                boolean name = controller.editTextEmptyCheck(nameInput, getApplicationContext());
                boolean tc = controller.editTextEmptyCheck(tcInput, getApplicationContext());
                boolean age = controller.editTextEmptyCheck(ageInput, getApplicationContext());

                if(!name && !tc && !age){
                   boolean wifiState =  controller.isConnected(getApplicationContext());
                    if(wifiState){
                        json.sendData(name_data, tc_data, age_data, health_data, level_data, getApplicationContext());
                    }
                }
                else{
                    //do nothing.
                }


            }
        });

    }


}
