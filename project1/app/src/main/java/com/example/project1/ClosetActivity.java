
package com.example.project1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project1.db.ClosetDatabase;
import com.example.project1.R;

import java.io.File;

import static java.lang.Integer.parseInt;

public class ClosetActivity extends Activity {
    public static final String TAG="MClosetActivity";
    ListView ClosetListView;
    com.example.project1.ClosetListAdapter ClosetListAdapter1;
    int Count=0;
    public static ClosetDatabase Database = null;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closet_memo);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //sd카드를 체크한다.
            Toast.makeText(this, "SD 카드 없음. SD 카드를 넣은 후 다시 실행하십시오.", Toast.LENGTH_LONG).show();
            return;
        } else { //sd 카드가 있는 경우
            String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!com.example.project1.Information.ExternalChecked && externalPath != null) {
                com.example.project1.Information.ExternalPath = externalPath + File.separator;
                com.example.project1.Information.ExternalChecked = true;
                com.example.project1.Information.FOLDER_PHOTO = com.example.project1.Information.ExternalPath + com.example.project1.Information.FOLDER_PHOTO;
                com.example.project1.Information.DATABASE_NAME = com.example.project1.Information.ExternalPath + com.example.project1.Information.DATABASE_NAME;
            }
        }
        //리스트뷰 처리해주고 아답터를 설정해서 연결해주는 역할을 한다
        ClosetListView = (ListView)findViewById(R.id.memoList);
        ClosetListAdapter1 = new com.example.project1.ClosetListAdapter(this);
        ClosetListView.setAdapter(ClosetListAdapter1); //setAdapter 메서드로 아답터를 설정한다
        ClosetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) { //리스트뷰 클릭 이벤트를 설정해준다
                viewMemo(position);
            }
        });
        //새옷 추가 버튼 설정함
        Button newMemoBtn = (Button)findViewById(R.id.newCloset);
        newMemoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //새옷 추가 버튼 클릭 리스너 지정
                //버튼을 누르면 ClosetInsertActivity.java 파일로 이동한다.
                Intent intent = new Intent(getApplicationContext(), com.example.project1.ClosetInsertActivity.class);
                intent.putExtra(com.example.project1.Information.CLOSET_MEMO_MODE, com.example.project1.Information.MODE_INSERT);
                startActivityForResult(intent, com.example.project1.Information.REQ_INSERT_ACTIVITY);
            }
        });
        // 닫기 버튼을 설정한다.
        Button closeBtn = (Button)findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        }); //버튼을 누르면 finish()가 실행된다.
        Permissions(); //권한을 체크한다.
    }

    private void Permissions() {
        String[] permissions = { //퍼미션 형식을 문자열 형태로 지정한다.
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else { Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else { ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }
    //상위 클래스의 메서드를 재정의한다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    protected void onStart() { //각 메서드를 차례대로 수행한다. 역할은 아래에 작성된 주석 참고하기
        openthedb();
        Listdata();
        super.onStart();
    }
    public void openthedb() { //데이터베이스를 여는 역할을 한다.
        if (Database != null) {
            Database.close();
            Database = null;
        }
        Database = ClosetDatabase.getInstance(this);
        boolean isOpen = Database.open();
        if (isOpen) { //isOpen이 참일 경우
            Log.d(TAG, "closetmemo database is open.");
        } else {
            Log.d(TAG, "closetmemo database is not open.");
        }
    }
    //리스트뷰에 기록된 옷장 정보를 뿌려준다.
    public int Listdata() {
        String SQL = "select _id, INPUT_DATE, CONTENT_TEXT, ID_PHOTO, BOOL from MEMO order by INPUT_DATE desc"; //SQL문이 필요하다.
        int Count = -1;
        if (ClosetActivity.Database != null) {
            Cursor dbCursor = ClosetActivity.Database.rawQuery(SQL);
            Count = dbCursor.getCount(); //커서가 참조할 수 있는 해당 테이블의 행(Row)의 갯수를 얻어 온다.
            ClosetListAdapter1.clear();
            Resources res = getResources();
            for (int i = 0; i < Count; i++) {
                dbCursor.moveToNext(); //cursor를 다음 행(Row)으로 이동시킨다.
                String memoId = dbCursor.getString(0); //getString은 db 테이블의 실제 데이타를 가지고 오는 역할을 한다.
                String dateStr = dbCursor.getString(1); //getString은 db 테이블의 실제 데이타를 가지고 오는 역할을 한다.
                if (dateStr.length() > 10) {
                    dateStr = dateStr.substring(0, 10);
                }
                String memoStr = dbCursor.getString(2);
                String photoId = dbCursor.getString(3);
                String photoUriStr = getPhotoUriStr(photoId);
                //boolean b = dbCursor.getInt(4)>0; //작동은 함
                //boolean b = Boolean.parseBoolean(dbCursor.getString(4)); //이것도 작동은 함

                int k = dbCursor.getInt(4);
                //boolean b = parseInt(dbCursor.getString(4))>0;
                //boolean b = false;
                ClosetListAdapter1.addItem(new com.example.project1.ClosetListItem(memoId, dateStr, memoStr, photoId, photoUriStr,k)); //순서 중요
            }
            dbCursor.close(); //cursor를 종료시킨다.
            /*
            notifyDataSetChanged 메서드는 리스트뷰에서 데이터를 추가 또는 변경하였을 때 리스트뷰를 갱신해주는 역할을 한다.
             */
            ClosetListAdapter1.notifyDataSetChanged();
        }
        return Count;
    }

    public String getPhotoUriStr(String id_photo) { //사진 데이터의 uri 경로를 가져온다.
        String photoUriStr = null;
        if (id_photo != null && !id_photo.equals("-1")) {
            String SQL = "select URI from " + ClosetDatabase.TABLE_PHOTO + " where _ID = " + id_photo + ""; //sql 문을 수행한다.
            Cursor closetCursor1 = ClosetActivity.Database.rawQuery(SQL); //cursor를 지정한다.
            if (closetCursor1.moveToNext()) { //cursor를 다음 행(Row)으로 이동시킨다.
                photoUriStr = closetCursor1.getString(0);
            }
            closetCursor1.close(); //cursor를 종료시킨다.
        } else if(id_photo == null || id_photo.equals("-1")) {
            photoUriStr = "";
        }
        return photoUriStr; //String 형식으로 return 한다.
    }


    private void viewMemo(int position) {
        com.example.project1.ClosetListItem item = (com.example.project1.ClosetListItem)ClosetListAdapter1.getItem(position);
        Intent intent = new Intent(getApplicationContext(), com.example.project1.ClosetInsertActivity.class); //인텐트를 이용하여 새 액티비티를 띄운다.
        intent.putExtra(com.example.project1.Information.CLOSET_MEMO_DATE, item.getData(0));
        intent.putExtra(com.example.project1.Information.CLOSET_MEMO_TEXT, item.getData(1));
        intent.putExtra(com.example.project1.Information.CLOSET_ID_PHOTO, item.getData(2));
        intent.putExtra(com.example.project1.Information.CLOSET_URI_PHOTO, item.getData(3));
        //Information.java에 있는 정보를 이용
        intent.putExtra(com.example.project1.Information.CLOSET_MEMO_MODE, com.example.project1.Information.MODE_VIEW);
        intent.putExtra(com.example.project1.Information.CLOSET_MEMO_ID, item.getId());
        intent.putExtra(com.example.project1.Information.CLOSET_BOOL, item.getInt());

        startActivityForResult(intent, com.example.project1.Information.REQ_VIEW_ACTIVITY);
    }

    //Information.java에 있는 액티비티의 응답 처리
    //Listdata() 메서드를 수행한다
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case com.example.project1.Information.REQ_INSERT_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    Listdata();
                }
                break;
            case com.example.project1.Information.REQ_VIEW_ACTIVITY:
                Listdata();
                break;
        }
    }
}
