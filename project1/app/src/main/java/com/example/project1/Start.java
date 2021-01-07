package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Start extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
    }

    public void myListener4(View target){
        Intent intent = new Intent(getApplicationContext(),DressOnAvatar.class);
        startActivity(intent);
    }

    public void myListener3(View target){
        Intent intent = new Intent(getApplicationContext(),Upload1.class);
        startActivity(intent);
    }

    public void backgroundremove1(View target){
        Intent intent = new Intent(getApplicationContext(),BackgroundRemove.class);
        startActivity(intent);
    }

    public void camera(View target){
        Intent intent = new Intent(getApplicationContext(),Camera.class);
        startActivity(intent);
    }

    public void myListener2(View target){
        Intent intent = new Intent(getApplicationContext(),LaunchActivity.class);
        startActivity(intent);
    }

    public void closet2(View target){
        Intent intent = new Intent(getApplicationContext(),ClosetActivity.class);
        startActivity(intent);
    }

    public void closet(View target){
        Intent intent = new Intent(getApplicationContext(),MyClosetIntro.class);
        startActivity(intent);
    }

}