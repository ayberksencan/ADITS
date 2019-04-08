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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.via.adits.FunctionalUses.ControlClass.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class WifiScreen extends AppCompatActivity {

    ListView listView;
    List<Item> wifiAddresses;
    SwipeRefreshLayout pullToRefresh;
    CustomAdapter wifiAdapter;
    public WifiManager wifiManager;
    public WifiReceiver receiverWifi;
    public LocationManager lm;
    public CustomAdapter customAdapter;
    private int con_pos;

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
        ListView wifiList = (ListView) findViewById(R.id.wifi_list);
        ImageView warning = (ImageView) findViewById(R.id.warning);
        TextView infoWifiScreen = (TextView) findViewById(R.id.info);
        TextView connected;
        View convertView;
        final ArrayList itemList = null;

        final ProgressDialog progressDialog1 = new ProgressDialog(WifiScreen.this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        listView = wifiList;
        wifiAddresses = new ArrayList<Item>();
        wifiAdapter = new CustomAdapter(this, wifiAddresses);
        listView.setAdapter(wifiAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions();
                displayMessage("Please refresh after giving requested permissions !");
                wifiManager.setWifiEnabled(true);
                progressDialog1.show();
                progressDialog1.setMessage("Scanning...");
                progressDialog1.setCancelable(false);
                scanWifi();
                controlConfigured(wifiAddresses);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog1.dismiss();
                    }
                }, 3000);
            } else {
                //Eğer GPS izni vermilmemişse Ekrana yazı basan blok.
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    displayMessage("Please, turn on Location Service!");
                } else {
                    //Wifi'ın açık olma durumunu kontrol eden, değilse açan blok.
                    if (wifiManager != null) {
                        if (!wifiManager.isWifiEnabled()) {
                            wifiManager.setWifiEnabled(true);
                            progressDialog1.show();
                            progressDialog1.setMessage("Scanning...");
                            progressDialog1.setCancelable(false);
                            scanWifi();
                            controlConfigured(wifiAddresses);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog1.dismiss();
                                }
                            }, 3000);
                        }
                        progressDialog1.show();
                        progressDialog1.setMessage("Scanning...");
                        progressDialog1.setCancelable(false);
                        scanWifi();
                        controlConfigured(wifiAddresses);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog1.dismiss();
                            }
                        }, 3000);
                    }
                }
            }
        }


            wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SupplicantState info = wifiManager.getConnectionInfo().getSupplicantState();
                //Item üzerine tıklanması halinde geçekleşecek olan animasyonu tanımlayan fonksiyon.
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(650);
                view.startAnimation(animation1);
                try{
                    Thread.sleep(500);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                //ListView'de seçili olan item'ın pozisyonunu depolayan değişken. (con_pos) [Daha sonra kullanılacak]
                con_pos = position;

                String ssid = wifiAddresses.get(con_pos).getSsid();
                String key = "12345678";

                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", ssid);
                wifiConfig.preSharedKey = String.format("\"%s\"", key);

                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();

                if (wifiManager.getConnectionInfo().getSupplicantState().toString().equalsIgnoreCase("completed")){
                    if (wifiManager.getConnectionInfo().getSSID().equalsIgnoreCase(ssid)){
                        // BURADA KALDIN !
                    }
                }
                else{
                    customAdapter.setDisconnected(con_pos);
                }



            }
        });

                pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
                pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        progressDialog1.show();
                        progressDialog1.setMessage("Refreshing...");
                        progressDialog1.setCancelable(false);
                        scanWifi();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog1.dismiss();
                            }
                        }, 1500);
                        scanWifi();
                        //Refresh işlemini sonlandıran blok.
                        pullToRefresh.setRefreshing(false);
                    }
                });
            }

            public void scanWifi () {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        receiverWifi = new WifiReceiver();
                        registerReceiver(receiverWifi, new IntentFilter(
                                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                        wifiManager.startScan();
                    }
                }, 0);
            }

            public void Connect (String ssid, String password){
                WifiConfiguration conf = new WifiConfiguration();
                conf.SSID = "\"" + ssid + "\"";
                conf.preSharedKey = "\"" + password + "\"";

                int netId = wifiManager.addNetwork(conf);
                if (netId != -1) {
                    wifiManager.enableNetwork(netId, true);
                } else {
                    Toast.makeText(WifiScreen.this, "Couldn't connect ! Please try again !", Toast.LENGTH_LONG).show();
                }
            }

            class WifiReceiver extends BroadcastReceiver {
                public void onReceive(Context c, Intent intent) {

                    List<ScanResult> wifiList;
                    wifiList = wifiManager.getScanResults();
                    WifiScreen wifiScreen = new WifiScreen();

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
                            wifiAddresses.add(new Item(aSSid[i], aBssid[i], aRssi[i], aSp[i]));
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

                    CustomAdapter customAdapter = new CustomAdapter(WifiScreen.this, wifiAddresses);
                    listView.setAdapter(customAdapter);
                }
            }

            //Ekranda Mesajları görüntülemek için kullanılan blok ---------------------------------------------------------------------------------------------------
            public void displayMessage (String message){
                Toast.makeText(WifiScreen.this, message, Toast.LENGTH_SHORT).show();
            }

            private boolean checkAndRequestPermissions () {
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

            public void controlConfigured(List<Item> wifiAddresses){
        for(int i = 0 ; i< wifiAddresses.size(); i++){
            if(wifiManager.getConfiguredNetworks().get(i).SSID.equalsIgnoreCase(wifiAddresses.get(i).getSsid())){
                wifiManager.removeNetwork(i);
            }
        }
            }

        }

