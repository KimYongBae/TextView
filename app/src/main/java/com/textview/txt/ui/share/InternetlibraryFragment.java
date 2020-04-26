package com.textview.txt.ui.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.textview.txt.R;

public class InternetlibraryFragment extends Fragment implements View.OnClickListener {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_internetlibrary, container, false);

        setHasOptionsMenu(true);//인터넷도서관은 메뉴바를 새롭게하기위해서 추가함

        Button btnN = (Button) root.findViewById(R.id.btnN);
        Button btnD = (Button) root.findViewById(R.id.btnDaum);
        Button btnK = (Button) root.findViewById(R.id.btnkako);
        Button btnL = (Button) root.findViewById(R.id.btnLagnz);
        Button btnS = (Button) root.findViewById(R.id.btnSearch);

        btnN.setOnClickListener(this);
        btnD.setOnClickListener(this);
        btnK.setOnClickListener(this);
        btnL.setOnClickListener(this);
        btnS.setOnClickListener(this);

        getActivity().invalidateOptionsMenu();

        return  root;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        //inflater.inflate(R.menu.inmenu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    public  void onClick(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (v.getId())
        {
            case R.id.btnN :
                intent.setData(Uri.parse("https://comic.naver.com/webtoon/weekdayList.nhn?week="));
                startActivity(intent);
                break;
            case R.id.btnDaum :
                intent.setData(Uri.parse("http://m.webtoon.daum.net/m/"));
                startActivity(intent);
                break;
            case R.id.btnkako :
                intent.setData(Uri.parse("https://page.kakao.com/main"));
                startActivity(intent);
                break;
            case R.id.btnLagnz :
                intent.setData(Uri.parse("https://www.lezhin.com/ko"));
                startActivity(intent);
                break;
            case R.id.btnSearch :
                intent.setData(Uri.parse("https://m.search.naver.com/search.naver?query=%EC%9B%B9%ED%88%B0+%EB%A7%8C%ED%99%94"));
                startActivity(intent);
                break;
        }
    }



}