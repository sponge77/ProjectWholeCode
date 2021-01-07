package com.example.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.BottomCategory;
import com.example.project1.ClosetDataBaseAdapter;
import com.example.project1.ClothesItem;
import com.example.project1.GridViewCustomAdapter;
import com.example.project1.HatCategory;
import com.example.project1.Memo;
import com.example.project1.R;
import com.example.project1.UpperCategory;

/* 사용자가 담은 상의 옷 이미지가 보이는 페이지를 작업 */
public class Closet extends AppCompatActivity {

    private GridView gridView;
    private GridViewCustomAdapter gridViewCustomAdapter;
    private final int IMAGE_TO_UPPER_CLOSET = 450;
    private final int IMAGE_TO_BOTTOM_CLOSET = 550;
    private final int IMAGE_TO_HAT_CLOSET = 650;
    private final int EDITED_MEMO = 750;
    final ClosetDataBaseAdapter db = new ClosetDataBaseAdapter(this);

    @Override
    protected void onResume() {
        super.onResume();
        gridViewCustomAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closet);

        /* 그리드뷰 아답터를 설정해준다. */
        gridViewCustomAdapter = new GridViewCustomAdapter();
        gridView = (GridView) this.findViewById(R.id.grid_view);

        gridView.setAdapter(gridViewCustomAdapter);
        Button go_to_upper = this.findViewById(R.id.go_to_upper);
        Button go_to_bottom = this.findViewById(R.id.go_to_bottom);
        Button go_to_hat = this.findViewById(R.id.go_to_hat);

//        final DBAdapter db = new DBAdapter(this);
        /* 상의 옷장 페이지에 들어가면, db 오픈하고, db에 저장된 값을 다 읽어와서 그리드뷰에 추가하고 보여줌 */

        db.openDB();

        Cursor c = db.getAllFromDB();

        /* 이미지 id, 옷 설명, 메모를 읽어와서 그리드뷰에 아이템 추가 */
        while(c.moveToNext()){
            ClothesItem clothesItem = new ClothesItem();
            Integer ids = c.getInt(1);
            String title = c.getString(2);
            String memo = c.getString(3);
            clothesItem.setResId(ids);
            clothesItem.setExplain(title);
            clothesItem.setMemo(memo);
            gridViewCustomAdapter.addItem(clothesItem);

        }

        /* 아이템을 누르면 옷의 설명란이 뜬다. */
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("옷의 정보");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int position_ = position;
                final int imgRes = ((ClothesItem)gridViewCustomAdapter.getItem(position)).getResId();
                String explain = ((ClothesItem)gridViewCustomAdapter.getItem(position)).getExplain();
                String memo = ((ClothesItem)gridViewCustomAdapter.getItem(position)).getMemo();
                builder.setMessage(explain + '\n' + '\n' + memo);   // 옷 설명 + 메모 표시
                builder.setPositiveButton("OK",null);   // 아무 작업 안함

                /* 수정하기를 누르면? 해당 아이템의 위치를 가지고 Memo로 넘어간다.*/
                builder.setNeutralButton("수정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), Memo.class);
                        intent.putExtra("position", position_);
                        intent.putExtra("id",imgRes);
                        startActivityForResult(intent, EDITED_MEMO);
                    }
                });

                builder.create().show();
            }
        });

        /* 아이템 롱 클릭시 삭제 */
        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("옷을 삭제하시겠습니까?");
        builder2.setNegativeButton("아니오",null); // 아니오 누르면 아무 작업도 하지 않음

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int position_ = position; // alert 창에서 위치를 넘겨받을 수 있도록 final 변수로 설정

                /* "예" 버튼은 해당 아이템의 주소를 받아야하기 때문에  item click listener 안에 처리해줬다.  */
                builder2.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        /* 해당 위치 아이템의 id, 설명 가져오기 */
                        int imgRes = ((ClothesItem)gridViewCustomAdapter.getItem(position_)).getResId();

                        gridViewCustomAdapter.deleteItem(position_);
                        gridViewCustomAdapter.notifyDataSetChanged();

                        // 그리드뷰에서 먼저 삭제하고 db에서 삭제
                        db.delete(imgRes);
                        Toast.makeText(getApplicationContext(), "옷이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                /* alert 창 띄우기 */
                AlertDialog alertDialog = builder2.create();
                alertDialog.show();

                return true;    // 이걸 true로 해야 일반 클릭이 작동된다고 한다.
            }

        });


        /* 상의 담으러가기 버튼을 누르면 상의 카테고리 페이지로 이동한다. */
        go_to_upper.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "상의 옷을 추가해보세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UpperCategory.class);
                startActivityForResult(intent, IMAGE_TO_UPPER_CLOSET);
            }
        });

        go_to_bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "하의 옷을 추가해보세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BottomCategory.class);
                startActivityForResult(intent, IMAGE_TO_BOTTOM_CLOSET);
            }
        });

        go_to_hat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "모자를 추가해보세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HatCategory.class);
                startActivityForResult(intent, IMAGE_TO_HAT_CLOSET);
            }
        });

    }

    /* 이미지 id를 받아오고, 그리드뷰에 추가 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_TO_UPPER_CLOSET) {
            if(resultCode == Activity.RESULT_OK) {
//                db.openDB();

                /* db에 insert */
                int id = data.getIntExtra("imgRes",0);
                String exs = data.getStringExtra("explains");

                ClothesItem clothesItem = new ClothesItem();
                clothesItem.setResId(id);
                clothesItem.setExplain(exs);

                long result = db.addToTable(id,exs);

                if(result != 0)
                {
                    gridViewCustomAdapter.addItem(clothesItem);
                    gridViewCustomAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"옷이 정상적으로 추가되었습니다.",Toast.LENGTH_SHORT).show();

                } else
                {
                    Toast.makeText(getApplicationContext(), "옷을 추가하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }

                db.close();

            }
        }

        if (requestCode == IMAGE_TO_BOTTOM_CLOSET) {
            if(resultCode == Activity.RESULT_OK) {

                /* db에 insert */
                int id = data.getIntExtra("imgRes",0);
                String exs = data.getStringExtra("explains");

                ClothesItem clothesItem = new ClothesItem();
                clothesItem.setResId(id);
                clothesItem.setExplain(exs);

                long result = db.addToTable(id,exs);

                if(result != 0)
                {
                    gridViewCustomAdapter.addItem(clothesItem);
                    gridViewCustomAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"옷이 정상적으로 추가되었습니다.",Toast.LENGTH_SHORT).show();


                } else
                {
                    Toast.makeText(getApplicationContext(), "옷을 추가하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        }

        /* 모자 */
        if (requestCode == IMAGE_TO_HAT_CLOSET) {
            if(resultCode == Activity.RESULT_OK) {

                /* db에 insert */
                int id = data.getIntExtra("imgRes",0);
                String exs = data.getStringExtra("explains");

                ClothesItem clothesItem = new ClothesItem();
                clothesItem.setResId(id);
                clothesItem.setExplain(exs);

                long result = db.addToTable(id,exs);

                if(result != 0)
                {
                    gridViewCustomAdapter.addItem(clothesItem);
                    gridViewCustomAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),"옷이 정상적으로 추가되었습니다.",Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(getApplicationContext(), "옷을 추가하지 못했습니다.", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        }
        if (requestCode == EDITED_MEMO) {


            if (resultCode == Activity.RESULT_OK) {
                String memo = data.getStringExtra("edited_memo");
                int position = data.getIntExtra("position",0);

                /* position의 아이템의 메모를 수정 */
                ClothesItem clothesItem = (ClothesItem)gridViewCustomAdapter.getItem(position);
                clothesItem.setMemo(memo);

                int id = ((ClothesItem)gridViewCustomAdapter.getItem(position)).getResId();
                db.update(id, memo);    // 여기서 id는 이미지 id여야함.... position 아님주의
                Toast.makeText(getApplicationContext(), "메모를 성공적으로 저장했습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }


}
