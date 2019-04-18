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


public class HttpHandler {

    public HttpHandler(){}

    /*-----------------------------------------Function for connecting via HTTP----------------------------------------------------------*/
    public String makeServiceCall(String requestUrl){
        String response = null;

        try {
            /*---------------------------------Creating an object, type of URL to use while trying to connect----------------------------*/
            URL url = new URL(requestUrl);
            /*---------------------------------Making a HTTP connection with the website at the given URL--------------------------------*/
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /*--------------------------Defining Request Method as "GET" to get information. "POST" can be used for sending information----*/
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

    /*----------------------------------------Function for converting the data from server to String format--------------------------------*/
    private String convertStreamToString(InputStream is){

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String satir = "";

        try{
            /*If reader.redLine is not null, there still is lines to read. Function for adding the new data as a new line-------------------*/
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
