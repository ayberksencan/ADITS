package com.via.adits.WifiUses;

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
    public LayoutInflater layoutInflater;
    public List<WifiAddress> wifiAddressList;
    public Activity activity;


    public WifiAdapter(Activity activity, List<WifiAddress> mList){
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wifiAddressList = mList;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return wifiAddressList.size();
    }

    @Override
    public Object getItem(int position) {
        return wifiAddressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        rowView = layoutInflater.inflate(R.layout.row, null);

        TextView ssid = (TextView) rowView.findViewById(R.id.ssid);
        TextView bssid = (TextView) rowView.findViewById(R.id.mac);
        TextView dbm = (TextView) rowView.findViewById(R.id.dbm);
        TextView connected = (TextView) rowView.findViewById(R.id.connected);

        final WifiAddress wifiAddress = wifiAddressList.get(position);

        ssid.setText("SSID : " + wifiAddress.getSSID().toString());
        bssid.setText("BSSID : " + wifiAddress.getBSSID().toString());
        dbm.setText("" + wifiAddress.getDBM());

        return rowView;
    }
}
