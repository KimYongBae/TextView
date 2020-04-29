package com.textview.txt.ui.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.textview.txt.DBHelper;
import com.textview.txt.R;
import com.textview.txt.ReadView;

import java.util.ArrayList;


public class RecentlistFragment extends Fragment {
    public static HistoryRecyclerAdapter myAdapter;
    public static RecyclerView mRecyclerView;
    public ArrayList<String> list = new ArrayList<>();
    private AdView mAdView;

    DBHelper db;
    final static String dbName = "TextView.db";
    final static int dbVersion = 1;
    DBHelper dbHelper;
    String sql;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Historyinfo> HistoryinfoArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_recentlist, container, false);

        mRecyclerView = root.findViewById(R.id.hirecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        HistoryinfoArrayList = new ArrayList<>();
        dbHelper = new DBHelper(getActivity(), dbName, null, dbVersion);

        //데이터 조회 (전체 긁어오기)
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();

        sql = "SELECT * FROM history ORDER BY Data DESC;";
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
                Log.e("제목", HistoryinfoArrayList.get(pos).t1);
                Log.e("경로", HistoryinfoArrayList.get(pos).t2);
                Intent intent = new Intent(getActivity(), ReadView.class);
                intent.putExtra("openfilePath",HistoryinfoArrayList.get(pos).t2);//파일이있는 경로 보내기
                intent.putExtra("openfileName", HistoryinfoArrayList.get(pos).t1);//파일이있는 이름 보내기
                int i =  Integer.parseInt(HistoryinfoArrayList.get(pos).t4);
                intent.putExtra("Spage",i);//현재페이지보내기
                startActivity(intent);

            }
        });
        myAdapter.notifyDataSetChanged();



        /* 광고 */
        MobileAds.initialize(getActivity(), getString(R.string.admob_app_id));
        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드.
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
                Log.d("@@@", "onAdLoaded");
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d("@@@", "onAdFailedToLoad " + errorCode);
            }
            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }
            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }
            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        /**/


        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            myAdapter.notifyDataSetChanged();
        }
    }
}
