package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.via.adits.Adapters.WifiAdapter;
import com.via.adits.Adapters.WifiAddress;
import com.via.adits.FunctionalUses.OnSwipeTouchListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class WifiScreen extends AppCompatActivity {

    /*---------------------------Public Identfy------------------------------*/
    // Personel Information
    TextView nameTxt;
    TextView tcTxt;
    TextView ageTxt;
    TextView healthTxt;
    TextView levelTxt;
    TextView networkTxt;

    //Wifi List
    ListView wifiList;

    //İnfo Text
    ImageView warning;
    TextView warningTxt;

    //SwipeRefreshLayout
    SwipeRefreshLayout swipeRefreshLayout;

    // Managers
    WifiManager wifiManager;
    LocationManager locationManager;

    // Multiple Permissinons
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    // WifiClass
    List<WifiAddress> wifiAddresses = new ArrayList<WifiAddress>();

    // WİFİ scan than Scan Results
    List<ScanResult> scanResults = new ArrayList<ScanResult>();

    //Wifi Configuration Informations
    String SSID,Password;

    RelativeLayout relativeLayout;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_screen);
        //Bottom navigation bar of Android will not be shown.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //No Status Bar will be shown
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //This screen will always shown horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*---------------------------Variable Initialization------------------------------*/

        nameTxt = (TextView)findViewById(R.id.name_info);
        tcTxt = (TextView)findViewById(R.id.tc_info);
        ageTxt = (TextView)findViewById(R.id.age_info);
        healthTxt = (TextView)findViewById(R.id.health_info);
        levelTxt = (TextView)findViewById(R.id.level_info);
        networkTxt = (TextView) findViewById(R.id.network_info);

        wifiList = (ListView) findViewById(R.id.wifi_list);

        /*
        warning = (ImageView) findViewById(R.id.warning);
        warningTxt = (TextView) findViewById(R.id.warningTxt);
        */

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        relativeLayout = (RelativeLayout) findViewById(R.id.wifiScreen);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        final WifiAdapter wifiAdapter = new WifiAdapter(this, wifiAddresses);
        wifiList.setAdapter(wifiAdapter);



        /*---------------------------CODE START------------------------------*/

        if(checkAndRequestPermissions()){
            checkAndRequestPermissions();
        }
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }
        if(wifiManager.getConnectionInfo().getSupplicantState().toString().equalsIgnoreCase("completed")){
            networkTxt.setText("Network : " + wifiManager.getConnectionInfo().getSSID());
            getJSONdata();
        }
        else{
            networkTxt.setText("Network : Not Found");
        }
        scanWifi();

        /*---------------------------SwipePage to RegisterActivity(Wifi)------------------------------*/
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(WifiScreen.this){
            @Override
            public void onSwipeRight() {
                        startActivity(new Intent(WifiScreen.this,WelcomeScreen.class));
                        finish();
            }

            @Override
            public void onSwipeLeft() {
                        startActivity(new Intent(WifiScreen.this,RangeScreen.class));
                        finish();
            }
        });

        /*--------------------------- wifiList Click------------------------------*/
        wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                SSID = wifiAddresses.get(position).getSSID();
                Password = "12345678";
                if (SSID.equalsIgnoreCase("00ADITS00")){
                    Password = "12345678";
                }
                else if(SSID.equalsIgnoreCase("via")){
                    Password = "viA.Via_2018";
                }
                else{
                    Toast.makeText(WifiScreen.this, "Lütfen ADITS ağlarından birine bağlanın !", Toast.LENGTH_SHORT).show();
                }
                connect();
                /*--- Waiting for device to connect to network before getting Json data. ---*/
                try{
                    Thread.sleep(6000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                getJSONdata();
            }
        });


        /*-------------------------- swipeRefreshLayout Refresh ------------------------------*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(wifiManager.getConnectionInfo().getSupplicantState().toString().equalsIgnoreCase("completed")){
                    networkTxt.setText("Network : " + wifiManager.getConnectionInfo().getSSID());
                    getJSONdata();
                }
                else {
                    networkTxt.setText("Network : Not Found");
                }
                scanWifi();
                wifiAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    /*---------------------------CODE END------------------------------*/


    /*---------------------------scanWifi------------------------------*/
    public void scanWifi(){
        wifiAddresses.clear();
        Toast.makeText(this, "Scanning Available Networks...", Toast.LENGTH_SHORT).show();
        wifiManager.startScan();
        scanResults = wifiManager.getScanResults();
        for (ScanResult scanResult:scanResults){
                wifiAddresses.add(new WifiAddress(scanResult.SSID, scanResult.BSSID, Integer.toString(scanResult.level), String.valueOf(wifiManager.calculateSignalLevel(scanResult.level,100))));
        }
    }

    /*---------------------------connect ADITS------------------------------*/
    public void connect(){
        List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
        WifiConfiguration newCon =new WifiConfiguration();
        newCon.SSID = "\"" + SSID + "\"";
        newCon.preSharedKey = "\"" + Password + "\"";

        int netId = wifiManager.addNetwork(newCon);

        if (netId != -1)
        {
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
            networkTxt.setText("Network : " + SSID);
        }
        else
        {
            for( WifiConfiguration i : configurationList ) {
                if (i.SSID.equals(newCon.SSID)){
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    networkTxt.setText("Network : " + SSID);
                    break;
                }
            }
        }
    }

    /*---------------------------getJSONdata------------------------------*/

    public void getJSONdata (){
        new getJSON().execute("http://192.168.4.1/json");
    }

    /*-------------------------- checkAndRequestPermissions ------------------------------*/
    public boolean checkAndRequestPermissions() {
        final int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        final int permissionWifiState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_WIFI_STATE);
        final int permissionChangeWifiState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CHANGE_WIFI_STATE);
        final int permissionInternet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        final int permissionFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();


        //Lokasyon izninin varlığını kontrol eden blok.
        if (permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionChangeWifiState!= PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
        }

        if (permissionFineLocation!= PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionInternet!= PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

        if (permissionWifiState!= PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        //Diğer izinlerin varlığını kontrol eden yoksa izin isteyen blok.
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You should open your Location Service to search Wi-Fi networks. Do you want to open it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    /*-------------------------- getJSON - Json Data in the Webserver has parsed. ------------------------------*/
    class getJSON extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            // Frist Item is Host address.
            HttpURLConnection connection = null;
            BufferedReader br = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String satir;
                String dosya = "";
                while ((satir = br.readLine()) != null) {
                    Log.d("satir", satir);
                    dosya += satir;
                }
                connection.disconnect();
                return dosya;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "dataError";
        }
        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jo = new JSONObject(s);
                JSONArray information = jo.getJSONArray("people");
                for (int i=0; i < information.length(); i++)
                {
                    JSONObject kisi = information.getJSONObject(i);
                    nameTxt.setText("Name : " + kisi.getString("Name"));
                    tcTxt.setText("TC ID : " + kisi.getString("TC No"));
                    ageTxt.setText("Age : " + kisi.getString("Age"));
                    healthTxt.setText("Health Status : " + kisi.getString("Healt Status"));
                    levelTxt.setText("Level : " + kisi.getString("Level"));
                    networkTxt.setText("Network : " + wifiManager.getConnectionInfo().getSSID());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}