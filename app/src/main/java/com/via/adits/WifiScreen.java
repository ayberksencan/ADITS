package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.via.adits.FunctionalUses.ControlClass;

public class WifiScreen extends Activity {

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
        setContentView(R.layout.wifi_screen);
        //This screen will always shown vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ControlClass controller = new ControlClass();

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





    }
}