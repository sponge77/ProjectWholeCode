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

public class Hat extends AppCompatActivity {
    ArrayList<Clothes5> al = new ArrayList<Clothes5>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hat_main);

        al.add(new Clothes5("아디다스",R.drawable.hat1rm,"야구 3S 캡 CT"));
        al.add(new Clothes5("아디다스",R.drawable.hat2rm,"AF 멜란지 캡"));
        al.add(new Clothes5("DESCENTE",R.drawable.hat3rm,"독일 봅슬레이팀 스냅백"));
        al.add(new Clothes5("노스페이스",R.drawable.hat4rm,"화이트라밸 코튼 볼 캡"));
        al.add(new Clothes5("MLB",R.drawable.hat5rm,"루키 볼캡 NY(BLUE)"));
        al.add(new Clothes5("MLB",R.drawable.hat6rm,"루키 볼캡 P(WINE)"));
        al.add(new Clothes5("MLB",R.drawable.hat7rm,"코듀로이 N-COVER 볼캡 NY(PINK)"));
        al.add(new Clothes5("SWELLMOB",R.drawable.hat8rm,"8 panel poplin beret beige"));
        al.add(new Clothes5("JADEGOLDNINE",R.drawable.hat9rm,"피치베레(주황)"));
        al.add(new Clothes5("MLB",R.drawable.hat10rm,"루키 볼캡 LA(BLUE)"));
        al.add(new Clothes5("MSKN2ND",R.drawable.hat11rm,"스마일 라피아햇"));
        al.add(new Clothes5("에잇세컨즈",R.drawable.hat12rm,"베이지 케이블 니트 비니"));


        MyAdapter5 adapter = new MyAdapter5(getApplicationContext(),R.layout.hat_row,al);

        ListView lv = (ListView)findViewById(R.id.hatlistView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(),HatDress.class);

                intent.putExtra("Nubmer",al.get(position).Number);
                intent.putExtra("img",al.get(position).img);
                intent.putExtra("name",al.get(position).name);

                startActivity(intent);
            }
        });
    }

}

class MyAdapter5 extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Clothes5> al;
    LayoutInflater inf;
    public MyAdapter5(Context context,int layout, ArrayList<Clothes5> al){
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
        ImageView iv = (ImageView)convertView.findViewById(R.id.hatimageView1);
        TextView tvName = (TextView)convertView.findViewById(R.id.hattextView1);
        TextView tvInfo = (TextView)convertView.findViewById(R.id.hattextView2);

        Clothes5 m = al.get(position);
        iv.setImageResource(m.img);
        tvName.setText(m.Number);
        tvInfo.setText(m.name);

        return convertView;
    }
}

class Clothes5{
    String Number="";
    int img;
    String name ="";
    public Clothes5(String Number,int img, String name){
        super();
        this.Number = Number;
        this.img = img;
        this.name = name;
    }
    public Clothes5() {}
}

