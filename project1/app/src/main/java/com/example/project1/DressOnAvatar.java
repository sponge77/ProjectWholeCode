package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DressOnAvatar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dressonavatar);
    }

    public void clothes(View target){
        Intent intent = new Intent(getApplicationContext(),IntroClothes.class);
        startActivity(intent);
    }

    public void Accesory(View target){
        Intent intent = new Intent(getApplicationContext(),Hat.class);
        startActivity(intent);
    }
}
