package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Upload1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload2);
    }

    public void onClick(View target){
        Toast.makeText(getApplicationContext(),"남성 옷을 업로드하세요",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ManUpload.class);
        startActivity(intent);
    }

    public void onClick2(View target){
        Toast.makeText(getApplicationContext(),"여성 옷을 업로드하세요",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), WomanUpload.class);
        startActivity(intent);
    }
}
