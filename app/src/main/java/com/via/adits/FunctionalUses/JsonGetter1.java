package com.via.adits.FunctionalUses;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonGetter1 extends AsyncTask<String, String, String> {

    @Override
    protected void onPostExecute(String s) {
        Log.d("postExceutetangelen",s);
        try{
            JSONObject jo = new JSONObject(s);
            String object = jo.toString();
            Log.d("JSONOBJECT", object);



        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onPostExecute(s);
    }

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
            return dosya;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "dataError";
    }
}
