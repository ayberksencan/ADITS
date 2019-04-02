package com.via.adits.FunctionalUses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.via.adits.R;
import com.via.adits.WelcomeScreen;
import com.via.adits.WifiScreen;

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

    public boolean editTextNullCheck(EditText e, Context c){
        if(e == null){
            Log.d("This is null: ", String.valueOf(e));
            return true;
        }
        else {
            return false;
        }
    }

    public boolean spinnerNullCheck(Spinner s, Context c) {
        if (s == null) {
            Log.d("This is null: ", String.valueOf(s));
            return true;
        } else {
            return false;
        }
    }

    public boolean editTextEmptyCheck(EditText e, Context c){
        if (e.getText().toString().isEmpty()){
            e.setBackground(getDrawable(R.drawable.edittext_bg_red));
            Toast.makeText(c, e.getHint()+ "Boş Olamaz !", Toast.LENGTH_SHORT).show();
            return true;
        }
        else return false;
    }

    public boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
            return true;
        }
        else{
            Toast.makeText(context, "Lütfen bir ağa bağlanıp tekrar deneyin !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
