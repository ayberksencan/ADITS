package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.FunctionalUses.CustomAdapter;
import com.via.adits.FunctionalUses.Item;
import com.via.adits.WifiUses.WifiReceiver;
import com.via.adits.WifiUses.WifiStatusHandler;

import java.util.ArrayList;

public class WifiScreen extends Activity {

    //A controller object has been created to control progress through the activity.
    public ControlClass controller = new ControlClass();

    //WifiManager object has been called from ControlClass.
    public WifiManager wifiManager = controller.getWifiManager();

    //A WifiReceiver object has been created.
    public WifiReceiver wifiReceiver = new WifiReceiver();

    //A WifiStatusHandler has been created to control wifi states.
    public WifiStatusHandler wifiStatusHandler = new WifiStatusHandler();

    //An ArrayList has been created to show wifi information on the screen.
    public ArrayList<Item> itemList;

    public LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

    //Variable definitions will be made under this block.
    TextView nameInfo = (TextView) findViewById(R.id.name_info);
    TextView tcInfo = (TextView) findViewById(R.id.tc_info);
    TextView ageInfo = (TextView) findViewById(R.id.age_info);
    TextView healthInfo = (TextView) findViewById(R.id.health_info);
    TextView levelInfo = (TextView) findViewById(R.id.level_info);
    ImageView person = (ImageView) findViewById(R.id.person);
    ListView wifiList = (ListView) findViewById(R.id.wifi_list);
    ImageView warning = (ImageView) findViewById(R.id.warning);
    TextView infoWifiScreen = (TextView) findViewById(R.id.info);
    TextView connected;
    View convertView;



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

        //Checks if Build version is compatible with target SDK version
        if(controller.checkBuildVersion()){
            //Checks if the requested permissions given or not.
            //If not requests needed permissions.
            controller.checkAndRequestPermissions(getApplicationContext());
            //Checks if Wifi enabled or not. If not enables wifi.
            controller.enableWifi(getApplicationContext());

            itemList = controller.searchWifi(getApplicationContext());

            //A CustomAdapter has been created to show wifi info on list.
            CustomAdapter wifiAdapter = new CustomAdapter(WifiScreen.this, itemList);

            convertView = inflater.inflate(R.layout.row, null);

            //Using CustomAdapter on wifiList to show scan results on list.
            wifiList.setAdapter(wifiAdapter);

            wifiStatusHandler.start(getApplicationContext());

        }

    }

    public ListView getListView(){
        return wifiList;
    }

    public CustomAdapter getAdapter(){
        //A CustomAdapter has been created to show wifi info on list.
        CustomAdapter wifiAdapter = new CustomAdapter(WifiScreen.this, itemList);
        return wifiAdapter;
    }

    public View getConvertView() {
        convertView = inflater.inflate(R.layout.row, null);
        return convertView;
    }
}
