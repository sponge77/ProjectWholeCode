package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
//그림으로 쉽게 설명하는 안드로이드 프로그래밍 책을 참고하여 작성하였습니다.
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myListener(View target){
        Intent intent = new Intent(getApplicationContext(),Start.class);
        startActivity(intent);
    }

    public void myListener1(View target){
        Intent intent = new Intent(getApplicationContext(),Guide.class);
        startActivity(intent);
    }
}
