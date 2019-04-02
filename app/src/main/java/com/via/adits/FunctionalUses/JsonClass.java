package com.via.adits.FunctionalUses;

import android.content.Context;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;

public class JsonClass {

    public void sendData(String name, String tcId, String age, String health, Integer level, Context context){
        try {
            Jsoup.connect("http://192.168.4.1/buffer").data("u_name", String.valueOf(name)).data("u_tcno", String.valueOf(tcId)).data("u_age", String.valueOf(age)).data("u_healts", String.valueOf(health)).data("u_level", String.valueOf(level)).post();
            Toast.makeText(context, "Veriler başarıyla güncellendi !", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Veri göderimi başarısız lütfen tekrar deneyin !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
        return level;
    }

}
