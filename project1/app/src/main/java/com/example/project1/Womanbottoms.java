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

public class Womanbottoms extends AppCompatActivity {
    ArrayList<Clothes4> al = new ArrayList<Clothes4>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.womanbottoms_main);

        al.add(new Clothes4("NIKE",R.drawable.womanbr1,"키즈 드라이핏 아카데미 반바지"));
        al.add(new Clothes4("LANGNLU",R.drawable.womanbr2,"20 S LUCY PANTS (루시)"));
        al.add(new Clothes4("LANGNLU",R.drawable.womanbr3,"20 S HAILEY PANTS (헤일리)"));
        al.add(new Clothes4("PROWORLDCUP",R.drawable.womanbr4,"여성 여름 9부 캐주얼 팬츠"));
        al.add(new Clothes4("PROWORLDCUP",R.drawable.womanbr5,"여성 여름 캐주얼 팬츠"));


        MyAdapter4 adapter = new MyAdapter4(getApplicationContext(),R.layout.womanbottoms_row,al);

        ListView lv = (ListView)findViewById(R.id.womanbottomslistView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(),WomanbottomsDress.class);

                intent.putExtra("Nubmer",al.get(position).Number);
                intent.putExtra("img",al.get(position).img);
                intent.putExtra("name",al.get(position).name);

                startActivity(intent);
            }
        });
    }
}

class MyAdapter4 extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Clothes4> al;
    LayoutInflater inf;
    public MyAdapter4(Context context, int layout, ArrayList<Clothes4> al){
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
        ImageView iv = (ImageView)convertView.findViewById(R.id.womanbottomsimageView1);
        TextView tvName = (TextView)convertView.findViewById(R.id.womanbottomstextView1);
        TextView tvInfo = (TextView)convertView.findViewById(R.id.womanbottomstextView2);

        Clothes4 m = al.get(position);
        iv.setImageResource(m.img);
        tvName.setText(m.Number);
        tvInfo.setText(m.name);

        return convertView;
    }
}

class Clothes4{
    String Number="";
    int img;
    String name ="";
    public Clothes4(String Number,int img, String name){
        super();
        this.Number = Number;
        this.img = img;
        this.name = name;
    }
    public Clothes4() {}
}
