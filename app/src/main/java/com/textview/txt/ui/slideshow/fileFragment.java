package com.textview.txt.ui.slideshow;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.textview.txt.R;

import java.io.File;
import java.util.List;

public class fileFragment extends Fragment {
    /**/
    File[] files;
    private TextView item_textview;
    /* item은 탐색기에 표시될 내용, path는 item 클릭시 이동할 경로이다. */
    private List<String> item = null;
    private List<String> path = null;
    /* 루트 디렉토리 설정 */
    String root= Environment.getExternalStorageDirectory().toString();
    /* 현재 경로를 저장해주는 변수 MPath */
    private String MPath;
    /* 현재 경로를 보여줄 변수 myPath */
    private TextView myPath;
    /* 디렉토리 일경우 */
    String Dname;
    /* 파일 관련 변수*/
    ListView listview;
    List<String> list;
    ArrayAdapter<String> adapter;
    /**/
    private fileViewModel fileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fileViewModel =
                ViewModelProviders.of(this).get(fileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_file, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        fileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;


    }

}