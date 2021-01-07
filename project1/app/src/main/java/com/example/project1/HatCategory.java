package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HatCategory extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hat);

        findViewById(R.id.btn_cap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HatList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_bucket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.project1.BucketList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.btn_beanie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.project1.BeanieList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });

    }
}
