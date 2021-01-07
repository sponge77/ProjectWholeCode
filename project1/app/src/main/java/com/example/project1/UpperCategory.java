package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.R;
import com.example.project1.UpperList;

public class UpperCategory extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upper);

        findViewById(R.id.btn_coat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.project1.CoatList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_long_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpperList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_short_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.project1.UpperShortList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });

    }
}
