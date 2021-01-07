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

public class Womantop extends AppCompatActivity {
    ArrayList<Clothes2> al = new ArrayList<Clothes2>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.womantop_main);

        al.add(new Clothes2("SCULPTOR",R.drawable.womanr1,"Bubble Gum Boxy Tee WHITE"));
        al.add(new Clothes2("CALVIN KLEIN JEANS",R.drawable.womanr2,"블라썸 로그 슬림 핏 긴팔 티셔츠"));
        al.add(new Clothes2("NIKE",R.drawable.womanr3,"NSW 퓨추라 아이콘 TD 반팔티셔츠"));
        al.add(new Clothes2("LANG GN LU",R.drawable.womanr4,"FLORA BLOUSE 플로라_02"));
        al.add(new Clothes2("LANG GN LU",R.drawable.womanr5,"DELIA BLOUSE 델리아_02"));
        al.add(new Clothes2("ALL KANG DESIGN STUDIO",R.drawable.womanr6,"절개 스트라이프 셔츠"));
        al.add(new Clothes2("NIKE",R.drawable.womanr7,"브러시트 클럽 맨투맨 블랙"));
        al.add(new Clothes2("MIXXO",R.drawable.womanr8,"R넥 여유핏 굵은 케이블 풀오버"));


        MyAdapter2 adapter = new MyAdapter2(getApplicationContext(),R.layout.womantop_row,al);

        ListView lv = (ListView)findViewById(R.id.womantoplistView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getApplicationContext(),WomantopDress.class);

                intent.putExtra("Nubmer",al.get(position).Number);
                intent.putExtra("img",al.get(position).img);
                intent.putExtra("name",al.get(position).name);

                startActivity(intent);
            }
        });
    }


}
class MyAdapter2 extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Clothes2> al;
    LayoutInflater inf;
    public MyAdapter2(Context context,int layout, ArrayList<Clothes2> al){
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
        ImageView iv = (ImageView)convertView.findViewById(R.id.womantopimageView1);
        TextView tvName = (TextView)convertView.findViewById(R.id.womantoptextView1);
        TextView tvInfo = (TextView)convertView.findViewById(R.id.womantoptextView2);

        Clothes2 m = al.get(position);
        iv.setImageResource(m.img);
        tvName.setText(m.Number);
        tvInfo.setText(m.name);

        return convertView;
    }
}

class Clothes2{
    String Number="";
    int img;
    String name ="";
    public Clothes2(String Number,int img, String name){
        super();
        this.Number = Number;
        this.img = img;
        this.name = name;
    }
    public Clothes2() {}
}
