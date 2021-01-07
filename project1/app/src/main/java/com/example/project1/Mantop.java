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

public class Mantop extends AppCompatActivity {
    ArrayList<Clothes> al = new ArrayList<Clothes>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mantop_main);

        al.add(new Clothes("마크곤잘레스",R.drawable.manr1,"M/G SIGN LOGO T-SHIRTS"));
        al.add(new Clothes("LAB TWELVE",R.drawable.manr2,"오버핏 피케티셔츠(브라운)"));
        al.add(new Clothes("Diamond Layla",R.drawable.manr3,"Stripe shirt S46 Sky Blue"));
        al.add(new Clothes("마크곤잘레스",R.drawable.manr4,"SMALL SIGN LOGO CREWNECK"));
        al.add(new Clothes("마크곤잘레스",R.drawable.manr5,"STRIPE LONG SLEEVE BLUE"));
        al.add(new Clothes("Partimento",R.drawable.manr6,"리넨 2PK 오픈 카라 셔츠"));
        al.add(new Clothes("PRINTSTAR",R.drawable.manr7,"무지 반팔티 라이트 핑크"));
        al.add(new Clothes("SPAO",R.drawable.manr8,"베이직 폴로 반팔 티셔츠"));

        //Adapter를 이요해 리스트뷰를 연결한다.
        MyAdapter adapter = new MyAdapter(getApplicationContext(),R.layout.mantop_row,al);

        ListView lv = (ListView)findViewById(R.id.mantoplistView1);
        lv.setAdapter(adapter); //아답터를 설정한다.

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){ //클릭 리스터 이벤트를 발생시킨다.
                Intent intent = new Intent(getApplicationContext(),MantopDress.class);
                //인텐트를 사용하여 값들을 넘긴다.
                intent.putExtra("Nubmer",al.get(position).Number);
                intent.putExtra("img",al.get(position).img);
                intent.putExtra("name",al.get(position).name);
                //인텐트를 실행시킨다.
                startActivity(intent);
            }
        });
    }
}
//아답터 설정 부분
class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Clothes> al;
    LayoutInflater inf;
    public MyAdapter(Context context,int layout, ArrayList<Clothes> al){
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
        ImageView iv = (ImageView)convertView.findViewById(R.id.mantopimageView1);
        TextView tvName = (TextView)convertView.findViewById(R.id.mantoptextView1);
        TextView tvInfo = (TextView)convertView.findViewById(R.id.mantoptextView2);

        Clothes m = al.get(position);
        iv.setImageResource(m.img);
        tvName.setText(m.Number);
        tvInfo.setText(m.name);

        return convertView;
    }
}
//클래스를 지정한다.
class Clothes{
    String Number="";
    int img;
    String name ="";
    public Clothes(String Number,int img, String name){
        super();
        this.Number = Number;
        this.img = img;
        this.name = name;
    }
    public Clothes() {}
}

