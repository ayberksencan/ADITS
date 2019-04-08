package com.via.adits.WifiUses;
/*
import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import com.via.adits.FunctionalUses.ControlClass;
import com.via.adits.WifiScreen;


public class WifiStatusHandler extends Handler {

    private boolean running = false;
    WifiManager mainWifi;
    public Context context;


    WifiStatusHandler wifiStatusHandler;

    public synchronized void start(Context c) {
        running = true;
        removeMessages(0);
        sendMessageDelayed(obtainMessage(0), 0);
        context = c;
    }

    public void handleMessage(Message message, Context c) {

        if (running) {

            ControlClass controller = new ControlClass();
            //Wifi information variables has been defined and initialized.
            WifiManager mainWifi = (WifiManager) c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiScreen wifiScreen = new WifiScreen();
            int curWifiState = mainWifi.getWifiState();
            SupplicantState info = mainWifi.getConnectionInfo().getSupplicantState();
            final WifiInfo curWifi = mainWifi.getConnectionInfo();

            //Decides what happens on which wifi state.
            switch (info.toString()) {

                //In case of scanning.
                case "SCANNING":

                    break;

                //In case of connecting.
                case "FOUR_WAY_HANDSHAKE":

                    break;

                //In case of connected.
                case "COMPLETED":

                    break;
                //In case of disconnected.
                case "DISCONNECTED":

                    break;
            }
        }
    }

    public synchronized void stop() {
        running = false;
    }
}*/