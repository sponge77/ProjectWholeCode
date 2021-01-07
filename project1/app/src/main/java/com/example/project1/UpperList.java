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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class UpperList extends AppCompatActivity {

    private com.example.project1.CustomAdapter adapter;
    private ListView listview;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothes_list);

        adapter = new com.example.project1.CustomAdapter();
        listview = (ListView)this.findViewById(R.id.listView);
        editText = (EditText)this.findViewById(R.id.edit_text);

        // 다 세팅
        setData();

        /* editText 요소에 포커스가 가도록 해야 키보드로 입력할 수 있게된다. */
//        editText.requestFocus();

        /* inputMethodManager 객체로 디바이스의 키보드를 제어할 수 있다.
        getSystemService는 디바이스나 안드로이드 프레임워크 내의 다른 기능을
        이 어플리케이션에서 가동하는게 가능하도록 하는 함수이다.  */
//        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드 보이기
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 옷 선택 후 토스트 메시지
                Toast.makeText(getApplicationContext(), "옷을 선택했습니다.", Toast.LENGTH_SHORT).show();
                int imgRes = ((com.example.project1.ClothesItem)adapter.getItem(position)).getResId();   // 이 위치의 imgRes 가져오기

                /* test page로 옷 사진을 인텐트에 담아서 보내기

                 * startActivityForResult로 넘어왔기 때문에, intent에 정보를 담은 뒤 종료시킬 수 있다.
                 * putExtra함수를 사용해서 옷의 image resource Id를 imgRes이라는 이름을 붙여서 담는다. (string : value)
                 * 성공적으로 수행했다는 RESULT_OK 코드를 담고 종료하면, 다시 합성 페이지로 넘어간다. */
                Intent intent = new Intent(UpperList.this, Test.class);
                intent.putExtra("imgRes", imgRes);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        /* alert 창 */
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이 옷을 옷장에 추가하시겠습니까?");

        // 아니오를 눌렀을 때
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"취소했습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int position_ = position; // alert 창에서 위치를 넘겨받을 수 있도록 final 변수로 설정

                /* "예" 버튼은 해당 아이템의 주소를 받아야하기 때문에  item click listener 안에 처리해줬다.  */
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /* 해당 위치 아이템의 id, 설명 가져오기 */
                        int imgRes = ((com.example.project1.ClothesItem)adapter.getItem(position_)).getResId();
                        String ex = ((com.example.project1.ClothesItem)adapter.getItem(position_)).getExplain();


                        /* 이미지 id와 옷 설명을 옷장으로 보내기 */
                        Intent intent = new Intent(UpperList.this, com.example.project1.Closet.class);
                        intent.putExtra("imgRes", imgRes);
                        intent.putExtra("explains",ex);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });

                /* alert 창 띄우기 */
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;    // 이걸 true로 해야 일반 클릭이 작동된다고 한다.
            }

        });

        /* editText 필수 함수 */
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
//                editText.setText(changed);

                /* 아직 아무것도 입력하지 않은 상태라면? listview의 목록이 하나도 뜨지 않도록 한다. */
                if(changed.length()==0){
                    listview.clearTextFilter();
                }

                /* 무언가 입력하면 바로 filtering하여 목록을 보여준다. */
                else {
                    ((com.example.project1.CustomAdapter)listview.getAdapter()).getFilter().filter(changed);
//                    listview.setFilterText(changed);
                }
            }
        });


    }

    // 카테고리 리스트에 옷 정보를 넘겨주면 리스트뷰에 하나씩 추가된다.
    private void setData(){
        TypedArray arrResId = getResources().obtainTypedArray(R.array.resId);
        String[]explains = getResources().getStringArray(R.array.explain);

        for(int i=0; i<arrResId.length(); i++){
            com.example.project1.ClothesItem clothesitem = new com.example.project1.ClothesItem();
            clothesitem.setResId(arrResId.getResourceId(i,0));
            clothesitem.setExplain(explains[i]);

            adapter.addItem(clothesitem);
        }
    }

}


