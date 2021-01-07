// https://android.camposha.info/android-sqlite-gridview/ 를 참고하여 작성했습니다.

package com.example.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClosetDataBaseAdapter {

    // 테이블 열: id, 이미지 resource id, title
    static final String ROW_ID ="id";
    static final String IMG_RES_ID ="imgResId";
    static final String TITLE = "title";
    static final String MEMO = "memo";
    static final String TAG = "DBAdapter";

    //DB PROPERTIES
    static final String DB_NAME ="g_DB";
    static final String TB_NAME ="g_TB";
    static final int DB_VERSION ='1';


    final Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public ClosetDataBaseAdapter(Context c) {
        this.c=c;
        helper=new DBHelper(c);
    }

    // db 오픈. 이미 존재하면 있는 걸 오픈하고, 없으면 새로 만든 후 오픈한다.
    public ClosetDataBaseAdapter openDB()
    {
        try
        {
            db=helper.getWritableDatabase();
            System.out.println("******************************\n");
            System.out.println("열렸다\n");
            System.out.println("******************************\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isOpen() { return db.isOpen(); }

    // db 닫기
    public void close()
    {
        helper.close();
    }

    // 테이블에 데이터를 추가
    public long addToTable(Integer imgResId, String title)
    {
        try
        {
            ContentValues contentValues=new ContentValues();
            contentValues.put(IMG_RES_ID, imgResId);
            contentValues.put(TITLE, title);
            return db.insert(TB_NAME, ROW_ID, contentValues);

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 테이블에서 아이템 삭제
    public void delete(Integer id){
        try {
            /* 쿼리를 사용해서 delete --- 아니면 내장된 함수 써도 됨 */
            db.execSQL("DELETE FROM " + TB_NAME + " WHERE imgResId = " + id);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // 메모 업데이트
    public void update(Integer id, String memo_input){

        try {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
            System.out.println(memo_input);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
            db.execSQL("UPDATE " + TB_NAME + " SET memo = '" + memo_input + "' WHERE imgResId = " + id);

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    // db에서 데이터 다 가져오기 -> 그리드뷰에 뿌리기 위해 필요한 함수
    public Cursor getAllFromDB()
    {
        String[] columns={ROW_ID, IMG_RES_ID,TITLE,MEMO};
        return db.query(TB_NAME, columns, null, null, null, null, null);
    }


    // DBHelper가 필요. 이 class는 onCreate와 onUpgrade가 필수 함수이다.
    private static class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);

        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try
            {
                // 테이블 생성
                String CREATE_TB="CREATE TABLE g_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "imgResId INTEGER ,title TEXT NOT NULL ,memo TEXT DEFAULT ' 내용없음 ');";
                db.execSQL(CREATE_TB);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS g_TB");
            onCreate(db);
        }
    }


}