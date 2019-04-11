package com.via.adits.FunctionalUses;
import android.os.AsyncTask;
import android.util.Log;
import com.via.adits.Adapters.HttpHandler;
import com.via.adits.WifiScreen;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonGetter extends AsyncTask<Void, Void, Void> {

    private String server_url = "http://192.168.4.1/json";
    public int setFlag;
    String Name;
    String TCNo;
    String Age;
    String Health;
    String Level;
    WifiScreen wifiScreen = new WifiScreen();



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        setFlag = 0;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(Name != null && TCNo != null && Age != null && Health != null && Level != null){
            setFlag = 1;
        }
        else{
            setFlag = 0;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //HttpHandler'ın tanımlandığı blok.
        HttpHandler httpHandler = new HttpHandler();
        String jsonString = httpHandler.makeServiceCall(server_url);

        if(jsonString != null){

            Log.d("JSON_RESPONSE", jsonString);

            try{
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray kisiler = jsonObject.getJSONArray("people");

                for(int i = 0; i<kisiler.length(); i++){

                    //Elde edilen bilgileri, başlıklara göre parse eden blok.
                    JSONObject kisi = kisiler.getJSONObject(i);
                    Name = kisi.getString("Name");
                    Log.d("JsonGetterName", Name);
                    TCNo = kisi.getString("TC No");
                    Age = kisi.getString("Age");
                    Health = kisi.getString("Healt Status");
                    Level = kisi.getString("Level");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            //Hata olması durumunda terminale log basan blok.
            Log.d("JSON_RESPONSE", "Sayfa Kaynağı Boş");
        }
        return null;
    }

    public int getFlag(){
        return setFlag;
    }

    public String getName(){
        return Name;
    }

    public String getTCNo(){
        return TCNo;
    }

    public String getAge(){
        return Age;
    }

    public String getHealth(){
        return Health;
    }

    public String getLevel(){
        return Level;
    }

}