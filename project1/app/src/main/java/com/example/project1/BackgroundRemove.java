package com.example.project1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BackgroundRemove extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backgroundremove);
    }

    public void removebutton(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.remove.bg/")); //경로를 지정해준다.
        startActivity(intent);
    }

}