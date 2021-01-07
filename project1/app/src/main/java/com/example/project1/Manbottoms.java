package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Manbottoms extends AppCompatActivity {
    ArrayList<Clothes3> al = new ArrayList<Clothes3>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manbottoms_main);

        al.add(new Clothes3("NIKE",R.drawable.manbr1,"NSW 우븐 플로우 반바지"));
        al.add(new Clothes3("HELIKONTEX",R.drawable.manbr2,"아웃도어 택티컬 팬츠_네이비블루"));
        al.add(new Clothes3("NIKE",R.drawable.manbr3,"프로 롱 쇼츠 반바지"));
        al.add(new Clothes3("PIECE WORKER",R.drawable.manbr4,"Stone Worker DS Greyish"));
        al.add(new Clothes3("GROOVE RHYME",R.drawable.manbr5,"NYLON TASLAN BUCKLE PANTS"));


        MyAdapter3 adapter = new MyAdapter3(getApplicationContext(),R.layout.manbottoms_row,al);

        ListView lv = (ListView)findViewById(R.id.manbottomslistView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(),ManbottomsDress.class);

                intent.putExtra("Nubmer",al.get(position).Number);
                intent.putExtra("img",al.get(position).img);
                intent.putExtra("name",al.get(position).name);

                startActivity(intent);
            }
        });
    }


}

class MyAdapter3 extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Clothes3> al;
    LayoutInflater inf;
    public MyAdapter3(Context context, int layout, ArrayList<Clothes3> al){
        this.context = context;
        this.layout = layout;
        this.al = al;
        inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return al.size();
    }
    @Override
    public Object getItem(int position) {
        return al.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = inf.inflate(layout, null);
        }
        ImageView iv = (ImageView)convertView.findViewById(R.id.manbottomsimageView1);
        TextView tvName = (TextView)convertView.findViewById(R.id.manbottomstextView1);
        TextView tvInfo = (TextView)convertView.findViewById(R.id.manbottomstextView2);

        Clothes3 m = al.get(position);
        iv.setImageResource(m.img);
        tvName.setText(m.Number);
        tvInfo.setText(m.name);

        return convertView;
    }
}

class Clothes3{
    String Number="";
    int img;
    String name ="";
    public Clothes3(String Number,int img, String name){
        super();
        this.Number = Number;
        this.img = img;
        this.name = name;
    }
    public Clothes3() {}
}

