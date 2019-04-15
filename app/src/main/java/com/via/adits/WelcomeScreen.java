package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.via.adits.FunctionalUses.OnSwipeTouchListener;
import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.FunctionalUses.JsonSetter;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeScreen extends AppCompatActivity {

    public String healtInfo;
    public int Position;
    public boolean nameBoolean;
    public boolean tcBoolean;
    public boolean ageBoolean;
    public boolean healthBoolean;
    String[] health = {"Health Status", "Good", "Moderate", "Poor"};
    String name;
    String tc;
    String age;
    String health1;
    Integer level;
    public RelativeLayout relativeLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstaveState) {
        super.onCreate(savedInstaveState);

        //No title will be shown
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Bottom navigation bar of Android will not be shown.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
        TextView companyNameWelcome = (TextView) findViewById(R.id.companyNameWelcome);
        final EditText nameInput = (EditText) findViewById(R.id.name_input);
        final EditText tcInput = (EditText) findViewById(R.id.tc_input);
        final EditText ageInput = (EditText) findViewById(R.id.age_input);
        Button submitButton = (Button) findViewById(R.id.submitBtn);
        WifiManager wifiManager = (WifiManager) getBaseContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        relativeLayout = (RelativeLayout) findViewById(R.id.welcomeScreen);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        final Spinner healthInput = (Spinner) findViewById(R.id.health_input);
        final List<String> healthList = new ArrayList<>(Arrays.asList(health));

        //Creating a control class object to control processes.
        final ControlClass controller = new ControlClass();

        //Creating a JsonClass object to send Json data.
        final JsonSetter json = new JsonSetter();

        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(WelcomeScreen.this){
            @Override
            public void onSwipeLeft() {
                startActivity(new Intent(WelcomeScreen.this,WifiScreen.class));
                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameBoolean = controller.editTextEmptyCheck(nameInput, WelcomeScreen.this);
                tcBoolean = controller.editTextEmptyCheck(tcInput, WelcomeScreen.this);
                ageBoolean = controller.editTextEmptyCheck(ageInput, WelcomeScreen.this);
                healthBoolean = controller.spinnerEmptyCheck(Position, WelcomeScreen.this);

                controlEditTexts(nameInput, tcInput, ageInput, nameBoolean, tcBoolean, ageBoolean);

                if (!nameBoolean && !tcBoolean && !ageBoolean && !healthBoolean) {
                        name = nameInput.getText().toString();
                        tc = tcInput.getText().toString();
                        age = ageInput.getText().toString();
                        health1 = healtInfo.toString();
                        level = json.calculateLevel(age, health1, WelcomeScreen.this);
                        Log.d("Name", name);
                        Log.d("tcNo", tc);
                        Log.d("Age", age);
                        Log.d("Health", health1);
                        Log.d("Level", String.valueOf(level));
                        new sendData().execute();
                }
            }
        });


        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, healthList) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        healthInput.setAdapter(spinnerArrayAdapter);

        healthInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position == 1){
                    healthInput.setBackground(getDrawable(R.drawable.health_green));
                }
                else if(position == 2){
                    healthInput.setBackground(getDrawable(R.drawable.health_yellow));
                }
                else if(position == 3){
                    healthInput.setBackground(getDrawable(R.drawable.health_red));
                }
                if (position > 0) {
                    // Notify the selected item text
                    healtInfo = selectedItemText;
                    Position = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void controlEditTexts(EditText name, EditText tc, EditText age, boolean nameB, boolean tcB, boolean ageB) {

        if (nameB) {
            name.setBackground(getDrawable(R.drawable.edittext_bg_red));
        } else if (!nameB) {
            name.setBackground(getDrawable(R.drawable.edittext_bg));
        }

        if (tcB) {
            tc.setBackground(getDrawable(R.drawable.edittext_bg_red));
        } else if (!tcB) {
            tc.setBackground(getDrawable(R.drawable.edittext_bg));
        }

        if (ageB) {
            age.setBackground(getDrawable(R.drawable.edittext_bg_red));
        } else if (!ageB) {
            age.setBackground(getDrawable(R.drawable.edittext_bg));
        }

    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    class sendData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress;
        private boolean isSended;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(WelcomeScreen.this, "Sending...",
                    "JSON Information ", true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            if (isSended){
                showMessage("Data sended succesfully");
            }
            else{
                showMessage("Couldn't send the Json data, please try again !");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Jsoup.connect("http://192.168.4.1/buffer").data("u_name", String.valueOf(name)).data("u_tcno", String.valueOf(" "+tc)).data("u_age", String.valueOf(age)).data("u_healts", String.valueOf(health1)).data("u_level", String.valueOf(level)).post();
                isSended = true;
            } catch (IOException e) {
                isSended = false;
            }
            return null;
        }
    }
}
