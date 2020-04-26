package com.textview.txt.ui.send;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.textview.txt.DBHelper;
import com.textview.txt.MainActivity;
import com.textview.txt.R;
import com.textview.txt.ReadView;
import com.textview.txt.ui.home.HistoryRecyclerAdapter;
import com.textview.txt.ui.home.Historyinfo;
import java.util.ArrayList;

public class bookmarkFragment extends Fragment {

    private LinearLayout bookmarkFragment;



    public static HistoryRecyclerAdapter myAdapter;
    public static RecyclerView mRecyclerView;
    public ArrayList<String> list = new ArrayList<>();


    boolean chDB;
    DBHelper db;
    final static String dbName = "TextView.db";
    final static int dbVersion = 1;
    DBHelper dbHelper;
    String sql;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Historyinfo> HistoryinfoArrayList;
    String resultName, resultPath;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        setHasOptionsMenu(true);//책갈피는 메뉴바를 새롭게하기위해서
        mRecyclerView = root.findViewById(R.id.bookmark);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        HistoryinfoArrayList = new ArrayList<>();

        dbHelper = new DBHelper(getActivity(), dbName, null, dbVersion);

        //데이터 조회 (전체 긁어오기)
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        sql = "SELECT * FROM bookmarks;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                HistoryinfoArrayList.add(new Historyinfo(cursor.getString(0),cursor.getString(1),cursor.getString(2) ,cursor.getString(3)));
            }
        } else {
            list.add("\n조회결과가 없습니다.");
        }
        cursor.close();
        myAdapter = new HistoryRecyclerAdapter(HistoryinfoArrayList);
        mRecyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new HistoryRecyclerAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View v, int pos)
            {
                Intent intent = new Intent(getActivity(), ReadView.class);
                resultName =HistoryinfoArrayList.get(pos).t1;
                resultPath =HistoryinfoArrayList.get(pos).t2;
                intent.putExtra("openfilePath",HistoryinfoArrayList.get(pos).t2);//파일이있는 경로 보내기
                intent.putExtra("openfileName", HistoryinfoArrayList.get(pos).t1);//파일이있는 이름 보내기
                int i =  Integer.parseInt(HistoryinfoArrayList.get(pos).t4);//저장한 페이지
                intent.putExtra("Spage",i);//현재페이지보내기
                startActivity(intent);
                //데이터 조회 (전체 긁어오기)
                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();

                if (checkDB() == false) { //신규
                    //파일이름, 경로, 현재시간, 스크롤위치
                    db.execSQL(String.format("INSERT INTO history(Tname, FilePath, Data, ScrollPage) VALUES('%s', '%s', datetime('now','localtime'), 0);", resultName, resultPath));
                } else if (checkDB() == true) {//데이터 있을때
                    db.execSQL(String.format("UPDATE history SET Data = datetime('now','localtime') WHERE Tname = '%s';", resultName));
                }


            }



        });
        myAdapter.notifyDataSetChanged();

        getActivity().invalidateOptionsMenu();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.bookmain, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {

            case R.id.action_bookdelete:
                dbHelper = new DBHelper(getActivity(), dbName, null, dbVersion);
                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();

                String strAllDelsql="";
                strAllDelsql +="delete from bookmarks";
                db.execSQL(strAllDelsql);
                Toast.makeText(getActivity(), "책갈피를 전부삭제합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public boolean checkDB() {


        try {

            dbHelper = new DBHelper(getActivity(), dbName, null, dbVersion);
            String sql = String.format("SELECT Tname FROM history WHERE  Tname = '%s'", resultName);
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);

            while (cursor.moveToNext()) {
                if ((cursor.getString(0)).equals(" ") || cursor.getString(0).length()==0 ) {//신규일경우
                    chDB = false;
                    break;
                }else if (cursor.getString(0).length()!=0){//신규가 아닐경우

                    chDB = true;
                    break;
                }

            }
        }catch (Exception e){

        }
        return chDB;
    }




}