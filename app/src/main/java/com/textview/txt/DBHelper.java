package com.textview.txt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //생성자 - database 파일을 생성한다.
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //DB 처음 만들때 호출. - 테이블 생성 등의 초기 처리.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE history (Tname TEXT, FilePath TEXT, Data DATA, ScrollPage INTEGER);");//열어본파일
        //Tname 제목, FilePath 파일위치, Data 파일연날짜, ScrollPage 마지막으로 열어본페이지위치,
        db.execSQL("CREATE TABLE bookmarks (Tname TEXT, FilePath TEXT, Data DATA, ScrollPage INTEGER);");//책갈피
    }

    //DB 업그레이드 필요 시 호출. (version값에 따라 반응)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS history");
        //db.execSQL("DROP TABLE IF EXISTS bookmarks");
        //onCreate(db);
    }
}

