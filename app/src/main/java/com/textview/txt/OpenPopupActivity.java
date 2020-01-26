package com.textview.txt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenPopupActivity extends AppCompatActivity {
    File[] files;
    private TextView item_textview;
    /* item은 탐색기에 표시될 내용, path는 item 클릭시 이동할 경로이다. */
    private List<String> item = null;
    private List<String> path = null;
    /* 루트 디렉토리 설정 */
    String root=Environment.getExternalStorageDirectory().toString();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_openpopup);
        myPath = (TextView)findViewById(R.id.path);
        //데이터 가져오기///////////////////////
        Intent intent = getIntent();
        String data = intent.getStringExtra("open");
        //txtText.setText(data);
        listview = (ListView)findViewById(R.id.file_list_view);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        file();

        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //클릭한 아이템의 문자열을 가져옴

                String selected_item = (String)adapterView.getItemAtPosition(position);
                Log.i("selected_item ::: ", selected_item);
                try {
                    if(selected_item == "../") { //뒤로가기
                        int end = MPath.lastIndexOf("/");///가 나오는 마지막 인덱스를 찾고
                        String uppath = MPath.substring(0, end);//그부분을 짤라버림 즉 위로가게됨
                        MPath = uppath;
                        myPath.setText(MPath);
                        refreshFiles();//리프레쉬
                    }
                    else {
                        String dire=selected_item;

                        if(dircke(dire)==true)//디렉토리 클릭 할때 파일오픈
                        {

                            myPath.append(selected_item);//최종현재경로에 파일이름 붙이기
                            MPath = myPath.getText().toString();//최종현재경로 백업
                            refreshFiles();//리프레쉬
                        }else{//그외에 경우 -> 텍스트 열기
                            MPath = myPath.getText().toString();//최종현재경로 백업
                            Intent intent = new Intent();
                            intent.putExtra("openfilePath", MPath);//파일이있는 경로 보내기
                            intent.putExtra("openfileName", selected_item);//파일이있는 이름 보내기
                            //응답코드
                            setResult(RESULT_OK, intent);
                            //액티비티(팝업) 닫기
                            finish();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "더이상 뒤로갈수없습니다", Toast.LENGTH_SHORT).show();
                    file();
                return;
                }

                }
        });
        /* EMD listview.setOnItemClickListener */
    }
    /**/
    void  file(){ //파일 목록 읽어오기

        listview.setAdapter(adapter);//리스트 추가

        final String path = Environment.getExternalStorageDirectory().toString()+"/KakaoTalkDownload";
        //final String path = Environment.getExternalStorageDirectory().toString();///////////////// 이걸로 바꿀것.

        myPath.setText(path); //초기경로설정
        Log.i("Files", path);
        //목록추가
        File directory = new File(path);//경로
        files = directory.listFiles();//디렉토리
        list.add("../");
        for (int i = 0; i < files.length; i++)
        {
            Dname=files[i].getName();
            Log.i("Files", files[i].getName());
            if(files[i].isDirectory() == true)
            {
                Dname="/"+files[i].getName();
                //list.add(files[i].getName());
                list.add(Dname);
            }else if(files[i].isFile() == true && Dname.endsWith(".txt")==true ){
                list.add(files[i].getName());
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**/

    /**/
    void refreshFiles(){
        list.clear();
        try{
            int count = adapter.getCount();

            for (int i = 0; i < count; i++) {

                adapter.remove(adapter.getItem(i));
                adapter.clear();
            }
            listview.setAdapter(adapter);//리스트 추가
            Log.i("Files", MPath);
            //목록추가
            File directory = new File(MPath);//경로
            files = directory.listFiles();//디렉토리
            list.add("../");
            for (int i = 0; i < files.length; i++)
            {
                Dname=files[i].getName();
                Log.i("Files", files[i].getName());
                if(files[i].isDirectory() == true)
                {
                    Dname="/"+files[i].getName();
                    //list.add(files[i].getName());
                    list.add(Dname);
                }else if(files[i].isFile() == true && Dname.endsWith(".txt")==true ){
                    list.add(files[i].getName());
                }
            }

        }catch (Exception e){
            Toast.makeText(this, "해당파일의 경로에 이상이 있습니다.", Toast.LENGTH_SHORT).show();
            list.clear();
            file();
            return;
        }

        adapter.notifyDataSetChanged();
    }

    /**/


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    public boolean dircke(String dire){

        boolean dir=false;

        String up;
        try{

            int end = dire.lastIndexOf("/");//디렉토리 구분
            up = dire.substring(end, 1);
            if(up.contentEquals("/")==true)//디렉토리 클릭 할때 파일오픈
            {
                return dir=true;
            }
        }catch (Exception e){//오류일시 그것은 텍스트파일

            return dir=false;

        }
        return dir;
    }
/*
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

 */
}

