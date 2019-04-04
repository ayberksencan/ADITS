package com.via.adits.FunctionalUses;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;

public class JsonClass extends AsyncTask<Void, Void, Void>{

    String Name;
    String TcId;
    String Age;
    String Health;
    Integer Level;
    Context Context;

    public boolean sendData(String name, String tcId, String age, String health, Integer level, Context context){
        Name = name;
        TcId = tcId;
        Age = age;
        Health = health;
        Context = context;
        new JsonClass().execute();
        return true;
    }

    public Integer calculateLevel(String age, String health, Context c){

        Integer ageStatus = 0;
        Integer healthStatus = 0;
        Integer level = 0;

        if (Integer.valueOf(age) >= 80 ){
            ageStatus = 5;
        }
        else if (Integer.valueOf(age) >= 60 && Integer.valueOf(age) < 80 ){
            ageStatus = 4;
        }
        else if(Integer.valueOf(age) >= 40 && Integer.valueOf(age) < 60){
            ageStatus = 3;
        }
        else if (Integer.valueOf(age) >= 25 && Integer.valueOf(age) < 40){
            ageStatus = 2;
        }
        else if (Integer.valueOf(age) >= 18 && Integer.valueOf(age) < 25){
            ageStatus = 1;
        }
        else if(Integer.valueOf(age) >= 10 && Integer.valueOf(age) < 18){
            ageStatus = 3;
        }
        else if (Integer.valueOf(age) >= 0 && Integer.valueOf(age) <10){
            ageStatus = 5;
        }

        if (health.equalsIgnoreCase("Good")){
            healthStatus = 1;
        }
        else if (health.equalsIgnoreCase("Moderate")){
            healthStatus = 3;
        }
        else if (health.equalsIgnoreCase("Poor")){
            healthStatus = 5;
        }

        level = ageStatus + healthStatus;
        Level = level;
        return level;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Jsoup.connect("http://192.168.4.1/buffer").data("u_name", String.valueOf(Name)).data("u_tcno", String.valueOf(TcId)).data("u_age", String.valueOf(Age)).data("u_healts", String.valueOf(Health)).data("u_level", String.valueOf(Level)).post();
            Toast.makeText(Context, "Veriler başarıyla güncellendi !", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(Context, "Data update failed! Please check network connection.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }
}
