package com.example.project1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;

public class ClothesItemView extends LinearLayout {
    TextView clothesName;
    ImageView iv;

    public ClothesItemView(Context context){
        super(context);
        init(context);
    }

    public ClothesItemView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_card,this,true);

        // list_card xml 에서 요소 가져오기 - 옷 설명이랑 옷 이미지
        clothesName = findViewById(R.id.explain);
        iv = findViewById(R.id.iconItem);
    }

    public void setName(String name){
        clothesName.setText(name);
    }


    public void setImage(int resId){
        iv.setImageResource(resId);
    }
}
