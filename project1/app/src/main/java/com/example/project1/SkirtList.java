package com.example.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.Closet;
import com.example.project1.ClothesItem;
import com.example.project1.CustomAdapter;
import com.example.project1.R;


public class SkirtList extends AppCompatActivity {
    private final int IMAGE_FROM_GALLERY2 = 400;
    private CustomAdapter adapter;
    private ListView listview;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothes_list);

        adapter = new CustomAdapter();
        listview = (ListView)this.findViewById(R.id.listView);
        editText = (EditText)this.findViewById(R.id.edit_text);

        setData();

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "옷을 선택했습니다.", Toast.LENGTH_SHORT).show();
                int imgRes = ((ClothesItem)adapter.getItem(position)).getResId();   // 이 위치의 imgRes 가져오기

                Intent intent = new Intent();
                Bundle extra;
                extra=new Bundle();
                extra.putInt("imgRes2",imgRes);
                intent.putExtras(extra);

                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이 옷을 옷장에 추가하시겠습니까?");

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"취소했습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int position_ = position;

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int imgRes = ((ClothesItem)adapter.getItem(position_)).getResId();
                        String ex = ((ClothesItem)adapter.getItem(position_)).getExplain();


                        Intent intent = new Intent(SkirtList.this, Closet.class);
                        intent.putExtra("imgRes", imgRes);
                        intent.putExtra("explains",ex);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return true;
            }

        });

        editText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String changed = s.toString();
                System.out.println(changed);

                if(changed.length()==0){
                    listview.clearTextFilter();
                }

                else {
                    ((CustomAdapter)listview.getAdapter()).getFilter().filter(changed);
                }
            }
        });

    }

    // 카테고리 리스트에 옷 정보를 넘겨주면 리스트뷰에 하나씩 추가된다.
    private void setData(){

        TypedArray arrResId = getResources().obtainTypedArray(R.array.resId6);
        String[]explains = getResources().getStringArray(R.array.explain6);

        for(int i=0; i<arrResId.length(); i++){
            ClothesItem clothesitem = new ClothesItem();
            clothesitem.setResId(arrResId.getResourceId(i,0));
            clothesitem.setExplain(explains[i]);

            adapter.addItem(clothesitem);
        }
    }
}

