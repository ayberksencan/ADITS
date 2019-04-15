package com.via.adits.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.via.adits.FunctionalUses.Item;
import com.via.adits.FunctionalUses.OnSwipeTouchListener;
import com.via.adits.R;
import com.via.adits.RangeScreen;
import com.via.adits.WelcomeScreen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.via.adits.FunctionalUses.ControlClass.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class OldWifiScreen extends AppCompatActivity {

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
    public TextView nameInfo;
    public TextView tcInfo;
    public TextView ageInfo;
    public TextView healthInfo;
    public TextView levelInfo;
    public ImageView person;
    public int flag;
    public int itemPos;
    public JsonGetter jsonGetter;
    public RelativeLayout relativeLayout;

    public String SSID;
    public String Password;
    public String key;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        relativeLayout = (RelativeLayout) findViewById(R.id.wifiScreen);

        progressDialog1 = new ProgressDialog(OldWifiScreen.this);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        listView = wifiList;
        wifiAddresses = new ArrayList<Item>();
        wifiAdapter = new CustomAdapter(this, wifiAddresses);
        customAdapter = new CustomAdapter(this, wifiAddresses);
        clickFlag = 0;
        itemPos = -1;
        jsonGetter = new JsonGetter();


        isPermissionsGet();


        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(OldWifiScreen.this) {
            @Override
            public void onSwipeRight() {
                startActivity(new Intent(OldWifiScreen.this, WelcomeScreen.class));
                finish();
            }

            @Override
            public void onSwipeLeft() {
                startActivity(new Intent(OldWifiScreen.this, RangeScreen.class));
                finish();
            }
        });

        wifiList.setOnTouchListener(new OnSwipeTouchListener(OldWifiScreen.this) {
            @Override
            public void onSwipeRight() {
                startActivity(new Intent(OldWifiScreen.this, WelcomeScreen.class));
                finish();
            }

            @Override
            public void onSwipeLeft() {
                startActivity(new Intent(OldWifiScreen.this, RangeScreen.class));
                finish();
            }
        });

        wifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(650);
                view.startAnimation(animation1);
                SSID = wifiAddresses.get(con_pos).getSsid().substring(6);
                if (SSID.equalsIgnoreCase("00ADITS00")) {
                    Password = "12345678";
                    new Connect().execute();
                    new JsonGetter().execute();
                } else {
                    Password = "viA.Via_2018";
                    new Connect().execute();
                    setNotFound();
                }
                con_pos = position;
                flag = 1;
            }
        });

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressDialog("Refreshing", 1500);
                scanWifi();
                scanResults();
                if (Password.equalsIgnoreCase("12345678")) {
                    new JsonGetter().execute();
                }
                //Refresh işlemini sonlandıran blok.
                pullToRefresh.setRefreshing(false);
                for (int i = 0; i < wifiAddresses.size(); i++) {
                    if (ssidConnected != null) {
                        if (ssidConnected.equalsIgnoreCase(wifiAddresses.get(i).getSsid())) {
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


    public void progressDialog(String message, int timeMillis) {
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

    public void isPermissionsGet() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkAndRequestPermissions();
                displayMessage("Please refresh after giving requested permissions !");
            } else {
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
                    }
                }
            }
        }
    }


    public void displayMessage(String message) {
        Toast.makeText(OldWifiScreen.this, message, Toast.LENGTH_SHORT).show();
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

    public void setConnected() {
        //View v = listView.getChildAt(con_pos);
        TextView connected = ConnectedView.findViewById(R.id.connected);
        connected.setVisibility(View.VISIBLE);
        connected.setText("Connected");

    }

    public void setDisconnected(int position) {
        TextView connected = ConnectedView.findViewById(R.id.connected);
        connected.setVisibility(View.VISIBLE);
        connected.setText("");
    }

    public void scanResults() {
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

    public void setNotFound() {
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

    public void setFound(final String name, final String tcno, final String age, final String health, final String level) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                nameInfo.setText("Name: " + name);
                tcInfo.setText("Tc Id: " + tcno);
                ageInfo.setText("Age: " + age);
                healthInfo.setText("Health Status: " + health);
                levelInfo.setText("Level: " + level);
            }
        });
    }


    public void connect() {
        List<WifiConfiguration> configurationList = wifiManager.getConfiguredNetworks();
        WifiConfiguration newCon = new WifiConfiguration();
        newCon.SSID = "\"" + SSID + "\"";
        newCon.preSharedKey = "\"" + Password + "\"";

        int netId = wifiManager.addNetwork(newCon);

        if (netId != -1) {
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
        } else {
            for (WifiConfiguration i : configurationList) {
                if (i.SSID.equals(newCon.SSID)) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    break;
                }
            }
        }
    }

    public class Connect extends AsyncTask<String, Void, Void> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(OldWifiScreen.this, "Connecting...",
                    "Please wait...", true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
        }

        @Override
        protected Void doInBackground(String... strings) {

            connect();
            String ssid = "";
            ssid = strings[0];

            if (ssid.equalsIgnoreCase("00ADITS00")) {
                key = "12345678";
            } else if (ssid.equalsIgnoreCase("Via")) {
                key = "viA.Via_2018";
            } else {
                Log.d("NO MATCHED NETWORKS", key);
                key = "viA.Via_2018";
            }

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + ssid + "\"";
            conf.preSharedKey = "\"" + key + "\"";

            int netId = wifiManager.addNetwork(conf);
            if (netId != -1) {
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
            } else {
                for (int i = 0; i < wifiManager.getConfiguredNetworks().size(); i++) {
                    if (conf.SSID.equalsIgnoreCase(wifiManager.getConfiguredNetworks().get(i).SSID)) {
                        netId = wifiManager.getConfiguredNetworks().get(i).networkId;
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(netId, true);
                        wifiManager.reconnect();
                        break;
                    }
                }
            }
            return null;
        }
    }


    public class JsonGetter extends AsyncTask<Void, Void, Void> {

        private String server_url = "http://192.168.4.1/json";
        public int setFlag;
        String Name;
        String TCNo;
        String Age;
        String Health;
        String Level;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setFlag = 0;
            progress = ProgressDialog.show(OldWifiScreen.this, "Getting...",
                    "JSON Information ", true);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            setFlag = 1;
            if (Name != null && TCNo != null && Age != null && Health != null && Level != null) {
                setFound(Name, TCNo, Age, Health, Level);
            } else {
                setNotFound();
            }
            progress.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //HttpHandler'ın tanımlandığı blok.
            HttpHandler httpHandler = new HttpHandler();
            String jsonString = httpHandler.makeServiceCall(server_url);

            if (jsonString != null) {

                Log.d("JSON_RESPONSE", jsonString);

                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray kisiler = jsonObject.getJSONArray("people");

                    for (int i = 0; i < kisiler.length(); i++) {

                        //Elde edilen bilgileri, başlıklara göre parse eden blok.
                        JSONObject kisi = kisiler.getJSONObject(i);
                        Name = kisi.getString("Name");
                        Log.d("Name", Name);
                        TCNo = kisi.getString("TC No");
                        Log.d("Tcno", TCNo);
                        Age = kisi.getString("Age");
                        Log.d("Age", Age);
                        Health = kisi.getString("Healt Status");
                        Log.d("Health", Health);
                        Level = kisi.getString("Level");
                        Log.d("Level", Level);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //Hata olması durumunda terminale log basan blok.
                Log.d("JSON_RESPONSE", "Sayfa Kaynağı Boş");
            }
            return null;
        }
    }
}