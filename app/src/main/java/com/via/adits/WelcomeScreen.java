package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.FunctionalUses.JsonSetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WelcomeScreen extends AppCompatActivity{

    public String healtInfo;
    public int Position;
    public boolean nameBoolean;
    public boolean tcBoolean;
    public boolean ageBoolean;
    public boolean healthBoolean;
    String[] health = {"Health Status","Good","Moderate","Poor"};

    @Override
    protected void onCreate(Bundle savedInstaveState) {
        super.onCreate(savedInstaveState);

        WelcomeScreen welcomeScreen = new WelcomeScreen();
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

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        final Spinner healthInput = (Spinner) findViewById(R.id.health_input);
        final List<String> healthList = new ArrayList<>(Arrays.asList(health));

        //Creating a control class object to control processes.
        final ControlClass controller = new ControlClass();

        //Creating a JsonClass object to send Json data.
        final JsonSetter json = new JsonSetter();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.editTextNullCheck(nameInput, WelcomeScreen.this);
                controller.editTextNullCheck(tcInput, WelcomeScreen.this);
                controller.editTextNullCheck(ageInput, WelcomeScreen.this);
                if(nameInput == null || tcInput==null || ageInput == null ){
                    Log.d("Error", "Something is null");
                }

                nameBoolean = controller.editTextEmptyCheck(nameInput, WelcomeScreen.this);
                tcBoolean = controller.editTextEmptyCheck(tcInput, WelcomeScreen.this);
                ageBoolean = controller.editTextEmptyCheck(ageInput, WelcomeScreen.this);
                healthBoolean = controller.spinnerEmptyCheck(Position, WelcomeScreen.this);

                controlEditTexts(nameInput, tcInput, ageInput, nameBoolean, tcBoolean,ageBoolean);

                if(!nameBoolean && !tcBoolean && !ageBoolean && !healthBoolean){
                    if (controller.isConnected(WelcomeScreen.this)){
                            WifiConfiguration conf = getConf();
                            controlNetwork(conf, WelcomeScreen.this);
                            assert nameInput != null;
                            String name = nameInput.getText().toString();
                            assert tcInput != null;
                            String tc = tcInput.getText().toString();
                            assert ageInput != null;
                            String age = ageInput.getText().toString();
                            String health = healtInfo;
                            Integer level = json.calculateLevel(age, health, WelcomeScreen.this);
                            json.sendData(name, tc, age, health, level);
                            new JsonSetter().execute();
                            sleep(500);
                            int flag = json.getFlag();
                            if(flag == 1){
                                showMessage("Data has been updated successfully !");
                            }
                            else{showMessage("Data couldn't updated, please be sure your are connected to adits !");}
                        }
                    }
                }
        });


        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,healthList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
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
                if(position > 0){
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

    public void controlEditTexts(EditText name, EditText tc, EditText age, boolean nameB, boolean tcB, boolean ageB){

        if(nameB){
            name.setBackground(getDrawable(R.drawable.edittext_bg_red));
        }

        else if (!nameB){
            name.setBackground(getDrawable(R.drawable.edittext_bg));
        }

        if(tcB){
            tc.setBackground(getDrawable(R.drawable.edittext_bg_red));
        }

        else if (!tcB){
            tc.setBackground(getDrawable(R.drawable.edittext_bg));
        }

        if(ageB){
            age.setBackground(getDrawable(R.drawable.edittext_bg_red));
        }

        else if (!ageB){
            age.setBackground(getDrawable(R.drawable.edittext_bg));
        }

    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public WifiConfiguration getConf(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration conf = new WifiConfiguration();
        for(int i = 0; i<wifiManager.getConfiguredNetworks().size(); i++){
            if (wifiManager.getConnectionInfo().getSSID().equals(wifiManager.getConfiguredNetworks().get(i).SSID)){
                conf.SSID = wifiManager.getConfiguredNetworks().get(i).SSID;
                conf.preSharedKey = wifiManager.getConfiguredNetworks().get(i).preSharedKey;
            }
        }
        return conf;
    }


    public void controlNetwork(WifiConfiguration config, Context context){
        WifiConfiguration conf = new WifiConfiguration();
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Fragment'dan alınan bilgilerin konfigürasyonlarının yapıldığı blok.
        conf.SSID = "\"" + config.SSID + "\"";
        conf.preSharedKey = "\"" + config.preSharedKey + "\"";

        //Verilen bilgilere göre ağa bağlanma isteğini gönderen ve bağlanma işlemini gerçekleştiren blok.
        int netId = wifiManager.addNetwork(conf);
        if (netId != -1) {
            wifiManager.enableNetwork(netId, true);
            wifiManager.saveConfiguration();
        }
        else{
            for(int i = 0; i<wifiManager.getConfiguredNetworks().size(); i++){
                if(conf.SSID.equals(wifiManager.getConfiguredNetworks().get(i).SSID)){
                    wifiManager.getConfiguredNetworks().remove(i);
                    netId = wifiManager.addNetwork(conf);
                    wifiManager.saveConfiguration();
                }
            }
        }
    }

    public void sleep(int time){
        try{
            Thread.sleep(time);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}