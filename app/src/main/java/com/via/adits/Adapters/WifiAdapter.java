package com.via.adits.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.via.adits.R;

import java.util.List;

public class WifiAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<WifiAddress> wifiAddressList;
    Activity activity;


    /*------------------------------------Constructor Method for initializing an object from this class-------------------------------------------------*/
    public WifiAdapter(Activity activity, List<WifiAddress> mList){
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wifiAddressList = mList;
        this.activity = activity;
    }

    /*-------------------------------------Function for returning the item count-------------------------------------------------------------------------*/
    @Override
    public int getCount() {
        return wifiAddressList.size();
    }

    /*-------------------------------------Function for returning the item--------------------------------------------------------------------------------*/
    @Override
    public Object getItem(int position) {
        return wifiAddressList.get(position);
    }

    /*-------------------------------------Function for returning the ID of the item-----------------------------------------------------------------------*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*-------------------------------------Function for returning the View of the item---------------------------------------------------------------------*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        rowView = layoutInflater.inflate(R.layout.row, null);

        TextView ssid = (TextView) rowView.findViewById(R.id.ssid);
        TextView bssid = (TextView) rowView.findViewById(R.id.mac);
        TextView dbm = (TextView) rowView.findViewById(R.id.dbm);
        TextView signal = (TextView) rowView.findViewById(R.id.signal);

        final WifiAddress wifiAddress = wifiAddressList.get(position);

        /*---------------------------------Editing this items TextViews with the information of this network-------------------------------------------------*/
        ssid.setText("SSID : " + wifiAddress.getSSID().toString());
        bssid.setText("BSSID : " + wifiAddress.getBSSID().toString());
        dbm.setText("DBM : " + wifiAddress.getDBM());
        signal.setText(wifiAddress.getSIGNAL());

        /*---------------------------------This block converts the dBm data to signal percentage-------------------------------------------------------------*/
        signal.setText(wifiAddress.getSIGNAL());
        int mySignal = Integer.parseInt(wifiAddress.getSIGNAL().replaceAll("[\\D]]",""));

        return rowView;
    }
}