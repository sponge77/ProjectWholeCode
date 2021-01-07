package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class IntroClothes extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothes);
    }

    public void dressavatarclick1(View target){
        Intent intent = new Intent(getApplicationContext(),Mantop.class);
        startActivity(intent);
    }
    public void dressavatarclick2(View target){
        Intent intent = new Intent(getApplicationContext(),Manbottoms.class);
        startActivity(intent);
    }
    public void dressavatarclick3(View target){
        Intent intent = new Intent(getApplicationContext(),Womantop.class);
        startActivity(intent);
    }

    public void dressavatarclick4(View target){
        Intent intent = new Intent(getApplicationContext(),Womanbottoms.class);
        startActivity(intent);
    }
}
