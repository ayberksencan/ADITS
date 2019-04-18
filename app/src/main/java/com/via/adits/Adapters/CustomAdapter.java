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

import org.w3c.dom.Text;

import java.util.List;

import static com.via.adits.R.color.*;

public class CustomAdapter extends BaseAdapter {

    /*------------------------------------Defining global variables which will be used inside of the processes of this class--------------------------*/
    private LayoutInflater mInflater;
    private List<Item> mItemList;

    /*------------------------------------Constructor Method for initializing an object from this class-------------------------------------------------*/
    public CustomAdapter(Activity activity, List<Item> items) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mItemList = items;
    }


    /*-------------------------------------Function for returning item count----------------------------------------------------------------------------*/
    @Override
    public int getCount() {
        return mItemList.size();
    }

    /*--------------------------------------Function for returning the item------------------------------------------------------------------------------*/
    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    /*--------------------------------------Function for returning the item ID----------------------------------------------------------------------------*/
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*--------------------------------------Function for returning the view of the item--------------------------------------------------------------------*/
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
        TextView connected = (TextView) convertView.findViewById(R.id.connected);

        //Creating an Item object.
        Item item = mItemList.get(position);

        //Setting textviews with network informations.
        tvSsid.setText(item.getSsid());
        tvBssid.setText(item.getBssid());
        tvRssi.setText(item.getRssi());
        signalP.setText(item.getSignalP());
        connected.setText(item.getConnected());

        //Calculating signal rate as percentage and storing it.
        signalP.setText(item.getSignalP());
        int myRssi = Integer.parseInt(item.getRssi().replaceAll("[\\D]", ""));
        return convertView;
    }
}
