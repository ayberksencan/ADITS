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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.via.adits.Adapters.CustomAdapter;
import com.via.adits.FunctionalUses.Item;

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
        TextView nameInfo = (TextView) findViewById(R.id.name_info);
        TextView tcInfo = (TextView) findViewById(R.id.tc_info);
        TextView ageInfo = (TextView) findViewById(R.id.age_info);
        TextView healthInfo = (TextView) findViewById(R.id.health_info);
        TextView levelInfo = (TextView) findViewById(R.id.level_info);
        ImageView person = (ImageView) findViewById(R.id.person);
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

                ssidConnected = wifiManager.getConnectionInfo().getSSID();
                ConnectedView = listView.getChildAt(con_pos).findViewById(R.id.connected);

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(650);
                view.startAnimation(animation1);

                //ListView'de seçili olan item'ın pozisyonunu depolayan değişken. (con_pos) [Daha sonra kullanılacak]
                con_pos = position;
                Connect();
            }
        });

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDialog("Refreshing");
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

    public void Connect() {
        ssid = wifiAddresses.get(con_pos).getSsid();
        Log.d("ssid",ssid);
        /*if(ssid.contains("ADITS")){
            key = "12345678";
        }
        else if (ssid.equals("Via")){
            key = "viA.Via_2018";
        }*/
        key="viA.Via_2018";


        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + ssid + "\"";
        conf.preSharedKey = "\"" + key + "\"";

        int netId = wifiManager.addNetwork(conf);
        if (netId != -1) {
            wifiManager.enableNetwork(netId, true);
            Log.d("+++++++++++++++++", ssid);
            setConnected();
        } else {
            Toast.makeText(WifiScreen.this, "Couldn't connect ! Please try again !", Toast.LENGTH_LONG).show();
        }
    }

    public void progressDialog(String message){
        progressDialog1.show();
        progressDialog1.setMessage(message);
        progressDialog1.setCancelable(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog1.dismiss();
            }
        }, 1500);

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
                            progressDialog("Scanning..");
                            scanWifi();
                            scanResults();
                            //wifiManager.disconnect();
                            controlConfigured(wifiAddresses);
                        }
                        progressDialog("Scanning...");
                        scanWifi();
                        scanResults();
                        //wifiManager.disconnect();
                        controlConfigured(wifiAddresses);
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
        for (int i = 0; i < wifiManager.getConfiguredNetworks().size(); i++) {
            for(int k = 0; k < wifiAddresses.size(); k++){
                if (wifiAddresses.get(k).getSsid().equalsIgnoreCase(wifiManager.getConfiguredNetworks().get(i).SSID)){
                    wifiManager.removeNetwork(i);
                }
            }
        }
    }

    public void setConnected(){
        //View v = listView.getChildAt(con_pos);
        TextView connected = ConnectedView.findViewById(R.id.connected);
        connected.setVisibility(View.VISIBLE);
        connected.setText("Connected");

    }

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.M)
    public void setDisconnected(int position){
        View v = listView.getChildAt(position);
        if (v == null){return;}
        TextView connected = (TextView) findViewById(R.id.connected);
        //connected.setText("Disconnected");
        //connected.setTextColor(getColor(R.color.colorRed));
        connected.setVisibility(View.VISIBLE);
    }

    public void scanResults(){
        wifiList = wifiManager.getScanResults();
        customAdapter = new CustomAdapter(this, wifiAddresses);

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

        listView.setAdapter(customAdapter);

        //Kaç Adet Wifi Bulunduğunu Listeleyen Fonksiyon
        displayMessage("Bulunan Wifi Sayısı: " + wifiAddresses.size());

        //RSSI(DBM) Değerlerine göre sonuçları sıralayan fonksiyon
        Collections.sort(wifiAddresses, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getRssi().compareToIgnoreCase(o2.getRssi());
            }
        });

    }

}

