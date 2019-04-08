package com.via.adits.Adapters;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//Wifi Activity'de belirtilen adrese bağlantının yapılmasını sağlayan Class.
public class HttpHandler {

    public HttpHandler(){}

    //Servis bağlantısının (HTTP) yapılmasını sağlayan fonksiyon.
    public String makeServiceCall(String requestUrl){
        String response = null;

        try {

            //Bağlantı adresini alır.
            URL url = new URL(requestUrl);
            //Adresle bağlantı kurar.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //"GET" ile adresten veri alacağını belirtir. (Göndermek için "POST" kullanılır.)
            connection.setRequestMethod("GET");
            BufferedInputStream in = new BufferedInputStream (connection.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (
                IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    //Alınan DATAların String olarak formatlanmasını sağlayan fonksiyon.
    private String convertStreamToString(InputStream is){

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String satir = "";

        try{
            //reader.readLine null olmadığı sürece (Okunabilir satır verisi var demektir.)
            // Yeni verileri başka bir satır olarak ekle.
            while((satir = reader.readLine()) != null){
                sb.append(satir).append("\n");
            }

        } catch (IOException e){

            e.printStackTrace();

        } finally {

            try{
                is.close();
            }
            catch (IOException e){

                e.printStackTrace();

            }
        }
        return sb.toString();
    }
}
