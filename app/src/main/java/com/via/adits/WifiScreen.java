package com.via.adits;

//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.via.adits.Adapters.BilgiAdapter;
import com.via.adits.Adapters.CustomAdapter;
import com.via.adits.Adapters.HttpHandler;
import com.via.adits.FunctionalUses.Item;
import com.via.adits.FunctionalUses.JsonClass;
import com.via.adits.FunctionalUses.People;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.via.adits.FunctionalUses.ControlClass.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.via.adits.R.color.colorRed;

public class WifiScreen extends AppCompatActivity {

    public ListView listView;
    List<Item> wifiAddresses;
    SwipeRefreshLayout pullToRefresh;
    CustomAdapter wifiAdapter;
    public WifiManager wifiManager;
    public LocationManager lm;
    public CustomAdapter customAdapter;
    public int con_pos;
    List<ScanResult> wifiList;
    public int clickFlag;
    public View ConnectedView;
    public String ssidConnected;
    public ProgressDialog progressDialog1;
    String ssid;
    String key;
    public TextView nameInfo;
    public TextView tcInfo;
    public TextView ageInfo;
    public TextView healthInfo;
    public TextView levelInfo;
    public ImageView person;
    public int flag;
    public int itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //No title will be shown
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Bottom navigation bar of Android will not be shown.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //No Status Bar will be shown
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //welcome_screen layout will be loaded as this classes layout
        setContentView(R.layout.wifi_screen);
        //This screen will always shown vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Variable definitions will be made under this block.
        nameInfo = (TextView) findViewById(R.id.name_info);
        tcInfo = (TextView) findViewById(R.id.tc_info);
        ageInfo = (TextView) findViewById(R.id.age_info);
        healthInfo = (TextView) findViewById(R.id.health_info);
        levelInfo = (TextView) findViewById(R.id.level_info);
        person = (ImageView) findViewById(R.id.person);
        final ListView wifiList = (ListView) findViewById(R.id.wifi_list);
        ImageView warning = (ImageView) findViewById(R.id.warning);
        TextView infoWifiScreen = (TextView) findViewById(R.id.info);
        final TextView connected;
        View convertView;
        final ArrayList itemList = null;

        progressDialog1 = new ProgressDialog(WifiScreen.this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        listView = wifiList;
        wifiAddresses = new ArrayList<Item>();
        wifiAdapter = new CustomAdapter(this, wifiAddresses);
        customAdapter = new CustomAdapter(this, wifiAddresses);
        clickFlag = 0;


        isPermissionsGet();


        wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(flag == 1){
                    setDisconnected(itemPos);
                }
                ssidConnected = wifiManager.getConnectionInfo().getSSID();
                ConnectedView = listView.getChildAt(con_pos).findViewById(R.id.connected);

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(650);
                view.startAnimation(animation1);

                //ListView'de seçili olan item'ın pozisyonunu depolayan değişken. (con_pos) [Daha sonra kullanılacak]
                con_pos = position;
                Log.d("Ssid Clicked", wifiAddresses.get(con_pos).getSsid());
                Connect(wifiAddresses.get(con_pos).getSsid().substring(6));
                setConnected();
                flag = 1;
                itemPos = position;
            }
        });

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDialog("Refreshing", 1500);
                scanWifi();
                scanResults();
                //Refresh işlemini sonlandıran blok.
                pullToRefresh.setRefreshing(false);
                for(int i = 0 ; i<wifiAddresses.size(); i++){
                    if(ssidConnected != null){
                        if (ssidConnected.equalsIgnoreCase(wifiAddresses.get(i).getSsid())){
                            con_pos = i;
                            setConnected();
                        }
                    }
                }
            }
        });
    }

    public void scanWifi() {
        wifiManager.startScan();
    }

    public void Connect(String ssid) {
        //YANLIŞ SSID DÖNÜYOR ! ssid = wifiList.get(con_pos).SSID;


        Log.d("ssid from Connect();", ssid);
        if(ssid.equalsIgnoreCase("00ADITS00")){
            key = "12345678";
        }
        else if (ssid.equalsIgnoreCase("Via")){
            key = "viA.Via_2018";
        }
        else {
            Log.d("NO MATCHED NETWORKS", key);
            key="viA.Via_2018";
        }

        Log.d("Password from Connect()", key );

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        conf.preSharedKey = "\"" + key + "\"";

        int netId = wifiManager.addNetwork(conf);
        if (netId != -1) {
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
            //progressDialog("Trying to connect", 3500);
            ssidConnected = ssid;
            new getInfo().execute();
        }
        else {
            for(int i = 0 ; i< wifiManager.getConfiguredNetworks().size(); i++){
                if (conf.SSID.equalsIgnoreCase(wifiManager.getConfiguredNetworks().get(i).SSID)){
                    netId = wifiManager.getConfiguredNetworks().get(i).networkId;
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netId, true);
                    wifiManager.reconnect();
                    //progressDialog("Trying to connect", 3500);
                    ssidConnected = ssid;
                    new getInfo().execute();
                    break;
                }
            }
        }
    }

    public void progressDialog(String message, int timeMillis){
        progressDialog1.show();
        progressDialog1.setMessage(message);
        progressDialog1.setCancelable(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog1.dismiss();
            }
        }, timeMillis);
    }

    public void isPermissionsGet(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions();
                displayMessage("Please refresh after giving requested permissions !");
            }
            else {
                //Eğer GPS izni vermilmemişse Ekrana yazı basan blok.
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    displayMessage("Please, turn on Location Service!");
                } else {
                    //Wifi'ın açık olma durumunu kontrol eden, değilse açan blok.
                    if (wifiManager != null) {
                        if (!wifiManager.isWifiEnabled()) {
                            wifiManager.setWifiEnabled(true);
                            progressDialog("Scanning..", 2000);
                            scanWifi();
                            scanResults();
                        }
                        progressDialog("Scanning...", 2000);
                        scanWifi();
                        scanResults();
                        wifiManager.disconnect();
                    }
                }
            }
        }
    }

    //Ekranda Mesajları görüntülemek için kullanılan blok ---------------------------------------------------------------------------------------------------
    public void displayMessage(String message) {
        Toast.makeText(WifiScreen.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkAndRequestPermissions() {
        final int permissionAccessCoarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        //Lokasyon izninin varlığını kontrol eden blok.
        if (permissionAccessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        //Diğer izinlerin varlığını kontrol eden yoksa izin isteyen blok.
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            displayMessage("Please Refresh!");
            return false;
        }
        return true;
    }

    public void controlConfigured(List<Item> wifiAddresses) {
        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
        StringBuilder ntworks_ssid = new StringBuilder();
        for (int i = 0 ; i< wifiConfigurations.size(); i++){
            ntworks_ssid.append(wifiConfigurations.get(i).SSID);
        }
        Log.d("wifiConfigBefore", String.valueOf(ntworks_ssid));

        for (int i = 0; i < wifiManager.getConfiguredNetworks().size(); i++) {
            for(int k = 0; k < wifiAddresses.size(); k++){
                if (wifiAddresses.get(k).getSsid().equalsIgnoreCase(wifiManager.getConfiguredNetworks().get(i).SSID)){
                    int netId = wifiManager.getConfiguredNetworks().get(i).networkId;
                    wifiManager.removeNetwork(netId);
                }
            }
        }

        Log.d("WifiConfigAfter", String.valueOf(ntworks_ssid));

    }

    public void setConnected(){
        //View v = listView.getChildAt(con_pos);
        TextView connected = ConnectedView.findViewById(R.id.connected);
        connected.setVisibility(View.VISIBLE);
        connected.setText("Connected");

    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    public void setDisconnected(int position){
        TextView connected = ConnectedView.findViewById(R.id.connected);
        connected.setVisibility(View.VISIBLE);
        connected.setText("");
    }

    public void scanResults(){
        wifiList = wifiManager.getScanResults();

        //Wifi Sonuçlarının bir liste halinde depolanmasını sağlayan blok ---------------------------------------------------------------------------
        StringBuilder networks_ssid = new StringBuilder();
        StringBuilder networks_bssid = new StringBuilder();
        StringBuilder networks_rssi = new StringBuilder();
        StringBuilder networks_sp = new StringBuilder();
        int strengthInPercentage = 0;

        for (int i = 0; i < wifiList.size(); i++) {
            //Bulunan ağların bilgilerini bir StringBuilder'a aktaran blok
            networks_ssid.append(",SSID: ").append(wifiList.get(i).SSID);
            networks_bssid.append(",BSSID: ").append(wifiList.get(i).BSSID);
            networks_rssi.append(",RSSI: ").append(wifiList.get(i).level).append(" dBm");
            strengthInPercentage = WifiManager.calculateSignalLevel(wifiList.get(i).level, 100);
            networks_sp.append(",").append(strengthInPercentage);
        }

        //Aktarılan bilgileri "," e göre ayıran yani her bi ağın bilgilerinin birbirinden ayrılmasını
        //sağlayan blok.
        String[] aSSid = networks_ssid.toString().split(",");
        String[] aBssid = networks_bssid.toString().split(",");
        String[] aRssi = networks_rssi.toString().split(",");
        String[] aSp = networks_sp.toString().split(",");


        //Bulunan sonuçların bir ArrayList'te depolanmasını sağlayan blok
        //(ArrayList kullanma amacımız verileri ListView üzerinde gösterebilmek)
        wifiAddresses = new ArrayList<>();
        for (int i = 1; i <= wifiList.size(); i++) {
            //Wifi Filter İşlemini gerçekleştiren fonksiyon (Wifi Filtresi)
            if (aSSid[i].toLowerCase().contains("via") || aSSid[i].contains("ADITS")) {
                wifiAddresses.add(new Item(aSSid[i], aBssid[i], aRssi[i], aSp[i], ""));
            }
        }

        //Kaç Adet Wifi Bulunduğunu Listeleyen Fonksiyon
        displayMessage("Bulunan Wifi Sayısı: " + wifiAddresses.size());

        //RSSI(DBM) Değerlerine göre sonuçları sıralayan fonksiyon
        Collections.sort(wifiAddresses, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getRssi().compareToIgnoreCase(o2.getRssi());
            }
        });

        customAdapter = new CustomAdapter(this, wifiAddresses);
        listView.setAdapter(customAdapter);

    }

    public void setNotFound(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                nameInfo.setText("Name: Not Found !");
                tcInfo.setText("Tc Id: Not Found !");
                ageInfo.setText("Age: Not Found !");
                healthInfo.setText("Health Status: Not Found !");
                levelInfo.setText("Level: Not Found !");
            }
        });
    }

    private class getInfo extends AsyncTask<Void, Void, Void> {

        ArrayList<People> bilgiArraylist = new ArrayList<>();
        private String server_url = "http://192.168.4.1/json";


        //İşlem Başlamadan Önce Yapılacaklar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //İşlem Bittikten Sonra Yapılacaklar
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        //İşlem Sırasında Yapılacaklar
        @Override
        protected Void doInBackground(Void... voids) {

            //HttpHandler'ın tanımlandığı blok.
            HttpHandler httpHandler = new HttpHandler();
            String jsonString = httpHandler.makeServiceCall(server_url);

            if(jsonString != null){

                //JSON'dan cevap olarak alınan verilerin Log olarak terminalde gösterildiği blok.
                Log.d("JSON_RESPONSE", jsonString);

                try{
                    JSONObject jsonObject = new JSONObject(jsonString);
                    //İnternet adresine bağlanıp, HTML içerisinden People başlıklı JSON
                    //objelerini çeken blok
                    JSONArray kisiler = jsonObject.getJSONArray("people");

                    for(int i = 0; i<kisiler.length(); i++){

                        //Elde edilen bilgileri, başlıklara göre parse eden blok.
                        JSONObject kisi = kisiler.getJSONObject(i);

                        String Name = kisi.getString("Name");
                        String TCNo = kisi.getString("TC No");
                        String Age = kisi.getString("Age");
                        String Health = kisi.getString("Healt Status");
                        String Level = kisi.getString("Level");

                        //People Class'ından yeni bir nesne oluşturarak, JSON tarafından sağlanan
                        //bilgileri oluşturulan nesnede depolayan blok.
                        People people = new People(Name,TCNo,Age,Health,Level);
                        bilgiArraylist.add(people);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            else{
                //Hata olması durumunda terminale log basan blok.
                Log.d("JSON_RESPONSE", "Sayfa Kaynağı Boş");
                setNotFound();
            }
            return null;
        }
    }

}

