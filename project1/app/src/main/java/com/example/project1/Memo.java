package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Closet;
import com.example.project1.R;

public class Memo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo);
        Toast.makeText(getApplicationContext(), "메모장입니다.", Toast.LENGTH_SHORT).show();

        final EditText editText = (EditText)this.findViewById(R.id.edit_memo);

        Button btn_save = (Button)this.findViewById(R.id.btn_save);

        Intent i = getIntent();
        final int position = i.getIntExtra("position",0);
        final int id = i.getIntExtra("id",0);
        ImageView iv = this.findViewById(R.id.memo_image);
        iv.setImageResource(id);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memo = editText.getText().toString();
                // edittext에 뭘 썼으면
                if(memo.length()>0){
                    Intent intent = new Intent(Memo.this, Closet.class);
                    intent.putExtra("edited_memo", memo);
                    intent.putExtra("position",position);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

            }
        });

    }
}
