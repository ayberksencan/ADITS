package com.via.adits.FunctionalUses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import java.util.List;

public class WifiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //A controller object has been created to control progress through the activity.
        ControlClass controller = new ControlClass();

        List<ScanResult> wifiList;
        wifiList = controller.getWifiManager().getScanResults();

    }
}
