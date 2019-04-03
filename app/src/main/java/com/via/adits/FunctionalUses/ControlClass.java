package com.via.adits.FunctionalUses;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.via.adits.R;
import com.via.adits.WelcomeScreen;
import com.via.adits.WifiScreen;

import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class ControlClass extends AppCompatActivity {

    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
    WifiConfiguration conf = new WifiConfiguration();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    List<String> listPermissionsNeeded = new ArrayList<>();
    LocationManager lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);

    //This function controls if the app launched for first time or not and chooses which screen
    //will be opened.
    public void firstRun(){
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

    //This function controls if given EditText is null or not.
    public boolean editTextNullCheck(EditText e, Context c){
        if(e == null){
            Log.d("This is null: ", String.valueOf(e));
            return true;
        }
        else {
            return false;
        }
    }

    //This function controls if the given Spinner is null or not.
    public boolean spinnerNullCheck(Spinner s, Context c) {
        if (s == null) {
            Log.d("This is null: ", String.valueOf(s));
            return true;
        } else {
            return false;
        }
    }

    //This function controls if the given EditText is empty or not.
    public boolean editTextEmptyCheck(EditText e, Context c){
        if (e.getText().toString().isEmpty()){
            e.setBackground(getDrawable(R.drawable.edittext_bg_red));
            Toast.makeText(c, e.getHint()+ "Cannot be empty !", Toast.LENGTH_SHORT).show();
            return true;
        }
        else return false;
    }

    //This function controls if the device is connected to a network or not.
    public boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
            return true;
        }
        else{
            Toast.makeText(context, "Please try again after connected to a network !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //This function shows the given mesaage at the given Context(Can be thought as screen)
    public void showMessage(String s, Context c){
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }

    //This function controls if the given network conf has already registered to configured networks
    //If so, deletes the old one and saves the new one.
    public int deleteNetwork(WifiConfiguration conf){

        int networkConfiguration = 0;

        for(int i=0 ; i<wifiManager.getConfiguredNetworks().size(); i++){
            if(conf.SSID.equals(wifiManager.getConfiguredNetworks().get(i).SSID)){
                wifiManager.getConfiguredNetworks().remove(i);
                networkConfiguration = wifiManager.addNetwork(conf);
                wifiManager.saveConfiguration();
            }
        }
        return networkConfiguration;
    }

    public WifiConfiguration getConf(){

        for(int i = 0; i<wifiManager.getConfiguredNetworks().size(); i++){
            if (wifiManager.getConnectionInfo().getSSID().equals(wifiManager.getConfiguredNetworks().get(i).SSID)){
                conf.SSID = wifiManager.getConfiguredNetworks().get(i).SSID;
                conf.preSharedKey = wifiManager.getConfiguredNetworks().get(i).preSharedKey;
            }
        }
        return conf;
    }

    public void connectWifi(int netId){

        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    public void disconnectWifi(){

        wifiManager.disconnect();

    }

    public boolean isAdits(){
        if(wifiManager.getConnectionInfo().getSSID().equalsIgnoreCase("00adits00")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkBuildVersion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return true;
        }
        else{
            showMessage("Build version is incompatible, terminating the app!", getApplicationContext());
            return false;
        }
    }

    public boolean checkAndRequestPermissions(Context c){
        final int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        //Checks if the location request has been granted or not.
        if (permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showMessage("Please turn your location service on !", c);
        }
        //Checks if request list is empty or not.
        //If not request listed permissions.
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            checkAndRequestPermissions(c);
        }
        return true;
    }

    public void enableWifi(Context c){
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
            showMessage("Enabling wifi...", c);
        }
    }

    public void searchWifi(Context c){


    }

    public WifiManager getWifiManager(){
        return wifiManager;
    }

}
