package com.via.adits.WifiUses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.FunctionalUses.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
public class WifiReceiver extends BroadcastReceiver {

    public ArrayList<Item> itemList;

    @Override
    public void onReceive(Context context, Intent intent) {

        WifiReceiver wifiReceiver = new WifiReceiver();

        //A controller object has been created to control progress through the activity.
        ControlClass controller = new ControlClass(null, null, wifiReceiver, null, null);

            List<ScanResult> wifiList;
            wifiList = controller.getWifiManager().getScanResults();

        //Initializing the StringBuilders to store ScanResults.
        StringBuilder networks_ssid = new StringBuilder();
        StringBuilder networks_bssid = new StringBuilder();
        StringBuilder networks_rssi = new StringBuilder();
        StringBuilder networks_frequency = new StringBuilder();
        StringBuilder networks_sp = new StringBuilder();
        StringBuilder connected = new StringBuilder();
        int strengthInPercentage = 0;

        for (int i = 0; i < wifiList.size(); i++) {
            //Storing wifi found wifi information at StringBuilders.
            networks_ssid.append(",SSID: ").append(wifiList.get(i).SSID);
            networks_bssid.append(",BSSID: ").append(wifiList.get(i).BSSID);
            networks_rssi.append(",RSSI: ").append(wifiList.get(i).level).append(" dBm");
            strengthInPercentage = WifiManager.calculateSignalLevel(wifiList.get(i).level, 100);
            networks_sp.append(",").append(strengthInPercentage);
            connected.append(",Connected");

            //Splits the data from "," to split found wifi information from each other.
            String[] aSSid = networks_ssid.toString().split(",");
            String[] aBssid = networks_bssid.toString().split(",");
            String[] aRssi = networks_rssi.toString().split(",");
            String[] aSp = networks_sp.toString().split(",");
            String[] aConnected = connected.toString().split(",");

            //Stores StringBuilders in an ArrayList
            //We are doing this to show found wifi info at listView
            itemList = new ArrayList<>();
            for (int k = 1; k <= wifiList.size(); k++) {
                //Filtering found wifi networks via their SSID's.
                if (aSSid[k].toLowerCase().contains("via") || aSSid[k].contains("ADITS")) {
                    itemList.add(new Item(aSSid[k], aBssid[k], aRssi[k], aSp[k], aConnected[k]));
                }
            }
        }

        //RSSI(DBM) Değerlerine göre sonuçları sıralayan fonksiyon
        Collections.sort(itemList, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getRssi().compareToIgnoreCase(o2.getRssi());
            }
        });
    }

    public ArrayList<Item> getItemList(){
        return itemList;
    }
}
*/