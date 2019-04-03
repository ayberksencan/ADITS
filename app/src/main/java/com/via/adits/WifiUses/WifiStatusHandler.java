package com.via.adits.WifiUses;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.FunctionalUses.CustomAdapter;
import com.via.adits.FunctionalUses.Item;
import com.via.adits.WifiScreen;

import java.util.ArrayList;

public class WifiStatusHandler extends Handler {

    private boolean running = false;
    WifiManager mainWifi;
    public Context context;

    ControlClass controller = new ControlClass();
    WifiReceiver wifiReceiver = new WifiReceiver();
    WifiScreen wifiScreen = new WifiScreen();

    public synchronized void start(Context c) {
        running = true;
        removeMessages(0);
        sendMessageDelayed(obtainMessage(0), 0);
        context = c;
    }

    public void handleMessage(Message message, Context c) {

        if (running) {

            //Wifi information variables has been defined and initialized.
            mainWifi = controller.getWifiManager();
            int curWifiState = mainWifi.getWifiState();
            SupplicantState info = mainWifi.getConnectionInfo().getSupplicantState();
            final WifiInfo curWifi = mainWifi.getConnectionInfo();

            //Decides what happens on which wifi state.
            switch (info.toString()) {

                //In case of scanning.
                case "SCANNING":
                    controller.showMessage("Ağ bağlantısı kayboldu, lütfen tekrar deneyiniz !",c);
                    stop();
                    break;

                //In case of connecting.
                case "FOUR_WAY_HANDSHAKE":
                    controller.showMessage("Connecting to" + curWifi.getSSID(),c);
                    break;

                //In case of connected.
                case "COMPLETED":
                    ArrayList<Item> itemList = wifiReceiver.getItemList();
                    CustomAdapter adapter = wifiScreen.getAdapter();
                    View convertView = wifiScreen.getConvertView();
                    controller.showConnected(mainWifi.getConnectionInfo().getSSID(), adapter, convertView);
                    break;
                //In case of disconnected.
                case "DISCONNECTED":
                    ArrayList<Item> itemList1 = wifiReceiver.getItemList();
                    CustomAdapter adapter1 = wifiScreen.getAdapter();
                    View convertView1 = wifiScreen.getConvertView();
                    controller.showDisconnected(mainWifi.getConnectionInfo().getSSID(), adapter1, convertView1);
                    break;
            }
        }
    }

    public synchronized void stop() {
        running = false;
    }
}