package com.via.adits.FunctionalUses;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.via.adits.SplashScreen;
import com.via.adits.WelcomeScreen;

import java.util.ArrayList;
import java.util.List;

/*------This class has been created to control some variables outside where they used. So, we can decrease the load of the variables' class------*/
public class ControlClass extends AppCompatActivity {

    /*------------------------------------------Defining global variables to use in the processes of this class----------------------------------*/
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    List<String> listPermissionsNeeded = new ArrayList<>();
    ControlClass controlClass;
    WelcomeScreen welcomeScreen = new WelcomeScreen();
    SplashScreen splashScreen = new SplashScreen();
    public int position;
    public Context context;


    /*-----------------------------------------This function controls if given EditText is null or not--------------------------------------------*/
    public boolean editTextNullCheck(EditText e, Context c){
        if(e == null){
            Log.d("This is null: ", String.valueOf(e));
            return true;
        }
        else {
            return false;
        }
    }

    /*--------------------------------------------This function controls if the given Spinner is null or not--------------------------------------*/
    public boolean spinnerNullCheck(Spinner s, Context c) {
        if (s == null) {
            Log.d("This is null: ", String.valueOf(s));
            return true;
        } else {
            return false;
        }
    }

    /*-------------------------------------------This function controls if the given EditText is empty or not----------------------------------------*/
    public boolean editTextEmptyCheck(EditText e, Context c){
        if (e.getText().toString().isEmpty()){
            Toast.makeText(c, e.getHint()+ " CAN'T BE EMPTY !", Toast.LENGTH_SHORT).show();
            return true;
        }
        else return false;
    }

    /*------------------------------------------This function controls if the given spinner is empty or not-------------------------------------------*/
    public boolean spinnerEmptyCheck(int position, Context c){
        if (position == 0){
            Toast.makeText(c, "Please choose a health status !", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }

    /*--------------------------------------------This function controls if the device is connected to a network or not---------------------------------*/
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

    /*-----------------------------------This function shows the given mesaage at the given Context(Can be thought as screen)----------------------------*/
    public void showMessage(String s, Context c){
        Toast.makeText(c, s, Toast.LENGTH_LONG).show();
    }

    /*-----------------------------------This function controls if the connected network is an ADITS network or not--------------------------------------*/
    public boolean isAdits(Context context){
        WifiManager wifiManager = (WifiManager)  context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        if(wifiManager.getConnectionInfo().getSSID().equalsIgnoreCase("00adits00")){
            return true;
        }
        else{
            Toast.makeText(context, "Please connect to an ADITS network !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
