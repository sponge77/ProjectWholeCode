package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Closet;
import com.example.project1.R;

/* 옷장 카테고리 페이지이다. 상의, 하의, 모자로 구성됨 */
public class MyClosetIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_closet);

        /* 옷장 바로가기를 누르면 옷장으로 이동 */
        ImageView iv = (ImageView)findViewById(R.id.go_to_closet);

        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Closet.class);
                Toast.makeText(getApplicationContext(), "옷장입니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }
}
