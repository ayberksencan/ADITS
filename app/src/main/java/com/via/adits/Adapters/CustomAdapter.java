package com.via.adits.Adapters;
//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.via.adits.FunctionalUses.Item;
import com.via.adits.R;
import java.util.List;

import static com.via.adits.R.color.*;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Item> mItemList;

    public CustomAdapter(Activity activity, List<Item> items) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mItemList = items;
    }

    //Returns item count.
    @Override
    public int getCount() {
        return mItemList.size();
    }

    //Returns item.
    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    //Returns items ID.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Returns items view.
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View initialized.
        convertView = mInflater.inflate(R.layout.row, null);

        //Initializing variables from view.
        TextView tvSsid = (TextView) convertView.findViewById(R.id.ssid);
        TextView tvBssid = (TextView) convertView.findViewById(R.id.mac);
        TextView tvRssi = (TextView) convertView.findViewById(R.id.dbm);
        TextView signalP = (TextView) convertView.findViewById(R.id.signal);

        //Creating an Item object.
        Item item = mItemList.get(position);

        //Setting textviews with network informations.
        tvSsid.setText(item.getSsid());
        tvBssid.setText(item.getBssid());
        tvRssi.setText(item.getRssi());
        signalP.setText(item.getSignalP());

        //Calculating signal rate as percentage and storing it.
        signalP.setText(item.getSignalP());
        int myRssi = Integer.parseInt(item.getRssi().replaceAll("[\\D]", ""));
        return convertView;
    }

    public void setConnected(int position){
        //View initialized.
        View convertView = mInflater.inflate(R.layout.row, null);

        TextView connected = (TextView) convertView.findViewById(R.id.connected);
        connected.setText("Connected");

        //Creating an Item object.
        Item item = mItemList.get(position);
        connected.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ResourceAsColor")
    public void setDisconnected(int position){
        //View initialized.
        View convertView = mInflater.inflate(R.layout.row, null);

        TextView connected = (TextView) convertView.findViewById(R.id.connected);

        //Creating an Item object.
        Item item = mItemList.get(position);
        connected.setText(R.string.disconnected);
        connected.setTextColor(colorRed);
        connected.setVisibility(View.VISIBLE);
    }

    public String getSsid(int position){
        View convertView = mInflater.inflate(R.layout.row, null);

        TextView ssid = (TextView) convertView.findViewById(R.id.ssid);

        //Creating an Item object.
        Item item = mItemList.get(position);
        String ssid1 = item.getSsid().substring(6);
        return ssid1;
    }

}
