package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.via.adits.FunctionalUses.OnSwipeTouchListener;

public class RangeScreen extends AppCompatActivity {

    /*---------------------------Defining global variables to use in the processes of this class---------------------------------------*/
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;
    private WifiManager mainWifi;
    public int level = 0;
    public int leveldbm;
    private TextView label1;
    String portIp;
    ConnectivityManager connManagerr;
    String label;
    public RelativeLayout rangeLay;
    private GraphView lineChart;
    public TextView ssid;
    public TextView dbm;

    /*------------------------------Defines what will happen after creating the screen-----------------------------*/
    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //No title will be shown
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Bottom navigation bar of Android will not be shown.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //No Status Bar will be shown
        //View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);

        //welcome_screen layout will be loaded as this classes layout
        setContentView(R.layout.range_screen);
        //This screen will always shown horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        //Variable definitions will be made under this block.
        ImageView appLogoRange = (ImageView) findViewById(R.id.appLogoRange);
        ImageView rangeInfo = (ImageView) findViewById(R.id.range_info);
        rangeLay = (RelativeLayout) findViewById(R.id.rangeScreen);
        lineChart = (GraphView) findViewById(R.id.range_chart);
        connManagerr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mainWifi = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ssid = (TextView) findViewById(R.id.ssid);
        dbm = (TextView) findViewById(R.id.dbm);


        /*--------------------------Editing Visual Settings of the Range Chart-------------------------------------------------------------------*/
        GraphView graph = (GraphView) findViewById(R.id.range_chart);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        /*--------------------------Editing Visual settings of the line shown in the chart--------------------------------------------------------*/
        series.setColor(Color.GREEN);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(70,100, 255, 100));
        series.setDrawDataPoints(false);
        series.setThickness(20);
        series.setTitle("dBm");
        rangeLay = (RelativeLayout) findViewById(R.id.rangeScreen);
        /*-------------------------Editing Visual settings of the chart itself----------------------------------------------------------------------*/
        graph.getGridLabelRenderer().setHorizontalAxisTitle("dBm");
        graph.getGridLabelRenderer().setNumHorizontalLabels(0);
        graph.getGridLabelRenderer().setNumVerticalLabels(3);
        graph.setBackground(getDrawable(R.color.Transparent));


        /*-------------------------------------This function controls what will happen when swiping right(On Layout)--------------------------*/
        rangeLay.setOnTouchListener(new OnSwipeTouchListener(RangeScreen.this) {
            public void onSwipeRight() {
                startActivity(new Intent(RangeScreen.this,WifiScreen.class));
                finish();
            }
        });

        /*-------------------------------------This function controls what will happen when swiping right(On Graph)--------------------------*/
        graph.setOnTouchListener(new OnSwipeTouchListener(RangeScreen.this) {
            public void onSwipeRight() {
                startActivity(new Intent(RangeScreen.this,WifiScreen.class));
                finish();
            }
        });

        /*-------------------------------------This function controls what will happen when swiping Left(On Layout)--------------------------*/
        rangeLay.setOnTouchListener(new OnSwipeTouchListener(RangeScreen.this) {
            public void onSwipeLeft() {
                startActivity(new Intent(RangeScreen.this,MeterScreen.class));
                finish();
            }
        });

        /*-------------------------------------This function controls what will happen when swiping Left(On Graph)--------------------------*/
        graph.setOnTouchListener(new OnSwipeTouchListener(RangeScreen.this) {
            public void onSwipeLeft() {
                startActivity(new Intent(RangeScreen.this,MeterScreen.class));
                finish();
            }
        });


        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setPadding(3);
        graph.getGridLabelRenderer().setHumanRounding(false);

        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(500);
        viewport.setMinY(-100);
        viewport.setMaxY(-20);
        viewport.setYAxisBoundsManual(true);
        viewport.setScrollable(false);
    }

    /*------------------onResume function for adding new dBm data to chart------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mainWifi.getConnectionInfo().getSupplicantState().toString().equalsIgnoreCase("completed")){
                                addEntry();
                                ssid.setText("Connected Network : " + mainWifi.getConnectionInfo().getSSID());
                                dbm.setText("dBm : " + mainWifi.getConnectionInfo().getRssi());
                            }
                            else{
                                addEntryManual(-100);
                                ssid.setText("Connected Network : " + "No Connection");
                                dbm.setText("dBm : " + "No Connection");
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    /*----------------------------------This function adds the new dBm data to the series (Series is the line on the graph)------------------*/
    private void addEntry(){

        ConnectivityManager connManagerr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifii = connManagerr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifii.isConnected()) {
            level = mainWifi.getConnectionInfo().getRssi();
            leveldbm = mainWifi.getConnectionInfo().getRssi();
        }
        if(series.getHighestValueX() < 500){
            series.appendData(new DataPoint(lastX++, level), false, 1000000);
        }
        else{
            series.appendData(new DataPoint(lastX++, level), true, 1000000);
        }
    }

    private void addEntryManual(int Level){
        ConnectivityManager connManagerr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifii = connManagerr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifii.isConnected()) {
            level = mainWifi.getConnectionInfo().getRssi();
            leveldbm = mainWifi.getConnectionInfo().getRssi();
        }
        if(series.getHighestValueX() < 500){
            series.appendData(new DataPoint(lastX++, Level), false, 1000000);
        }
        else{
            series.appendData(new DataPoint(lastX++, Level), true, 1000000);
        }
    }
}
