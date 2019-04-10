package com.via.adits.Adapters;
//Author: Ömer Ayberk ŞENCAN
//Position: Intern
//Company: Via Computer Systems Limited Company
//Start Date of Project: 13/02/2019
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.via.adits.FunctionalUses.People;
import com.via.adits.R;
import java.util.ArrayList;

//JSON verilerinin depolanması ve gösterilmesi için kullanılan Adapter Class.
public class BilgiAdapter extends BaseAdapter {


    public BilgiAdapter(Activity activity, ArrayList<People> bilgiArrayList){

        //Class'ta daha sonra kullanılacak olan değişkenlerin belirtildiği blok.
        this.context = activity;
        this.bilgiArrayList = bilgiArrayList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

    }

    //Class içerisindeki fonksiyonlarda kullanılacak değişkenlerin tanımlandığı blok.
    Context context;
    ArrayList<People> bilgiArrayList;
    LayoutInflater layoutInflater;

    //Nesne sayısını döndüren fonksiyon.
    @Override
    public int getCount() {
        return bilgiArrayList.size();
    }

    //Nesnenin kendisini döndüren fonksiyon.
    @Override
    public People getItem(int position) {
        return bilgiArrayList.get(position);
    }

    //Nesnenin ID bilgisini döndüren fonksiyon.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Nesnenin View bilgisini döndüren fonksiyon.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View'in tanımlandığı ve View'e layout atanan blok
        View view = layoutInflater.inflate(R.layout.wifi_screen,null);

        //View İçerisindeki değişkenlerin tanımlandığı ve
        // Layout içerisindeki ID'ler ile eşleştirildiği blok.
        TextView name = (TextView) view.findViewById(R.id.name_info);
        TextView TCNo = (TextView) view.findViewById(R.id.tc_info);
        TextView Age = (TextView) view.findViewById(R.id.age_info);
        TextView Health = (TextView) view.findViewById(R.id.health_info);
        TextView Level = (TextView) view.findViewById(R.id.level_info);


        //Değişkenleri ekranda gösterilecekleri formatlarla set eden ve
        // Alınan JSON bilgilerini bu değişkenlere atayan blok.
        name.setText("Name: " + bilgiArrayList.get(position).getName());
        TCNo.setText("TC No: " +bilgiArrayList.get(position).getTCNo());
        Age.setText("Age: " + bilgiArrayList.get(position).getAge());
        Health.setText("Health Status: " + bilgiArrayList.get(position).getHealth());
        Level.setText("Level: " + bilgiArrayList.get(position).getLevel());

        //View'in geri döndürülmesini sağlayan betik.
        return view;

    }
}
