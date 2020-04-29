package com.textview.txt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;

import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.WindowManager;

import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ReadView extends AppCompatActivity {
    String dbName = "TextView.db";
    int dbVersion = 1;
    DBHelper dbHelper;
    boolean chDB=false;

    TextView txtRead;//메인뷰텍스트
    String filePath;
    String line; // 한줄씩 읽기

    ArrayList<String> list = new ArrayList<>();

    int width;
    int height;
    ActionBar ab;//액션바

    boolean scr = true;//보이기 액션바
    ScrollView scrview;
    RecyclerView recyclerView;
    String resultName;
    String resultPath;
    int Spage;
    int tru=0;
    LinearLayoutManager mlayoutManager;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readview);
        scrview = (ScrollView) findViewById(R.id.ScrView);
        ab = getSupportActionBar();//액션바
        // Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); //디스플레이 설정얻기?
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //즉각
        mlayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mlayoutManager.scrollToPositionWithOffset(0, 0);
        //부드럽게 올라가는거
        recyclerView.smoothScrollToPosition(0);
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        RecyclerAdapter adapter = new RecyclerAdapter(list);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        resultName = intent.getStringExtra("openfileName");
        resultPath = intent.getStringExtra("openfilePath");
        Spage =intent.getIntExtra("Spage", -1);
        filePath = String.format("%s/%s", resultPath, resultName);


        ab.setTitle(resultName) ;  //액션타이틀바 이름 현재 파일이름으로 바꾸기

        BackThread thread = new BackThread();  // 작업스레드 생성
        thread.setDaemon(true);  // 메인스레드와 종료 동기화
        thread.start();     // 작업스레드 시작 -> run() 이 작업스레드로 실행됨

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y - 80;
        recyclerView.setVerticalScrollBarEnabled(scr);
        //화면꺼짐 방지 ON
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(Spage > -1){
            recyclerView.scrollToPosition(Spage);
        }
        sava();

        /* 광고 */
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
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

    }
    //액션 메뉴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open://열기
                Intent openintent = new Intent(this, MainActivity.class);
                startActivity(openintent);
                return true;
            case R.id.bookmark://열기
              //  long ss = recyclerView.getAdapter().onBindViewHolder(item);
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int FirsTextIndex = layoutManager.findFirstVisibleItemPosition(); //보이는레이아웃의 첫번째 아이템
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();//보이는레이아웃의 마지막 아이템

                //scrview.scrollTo(300,200);
                Toast.makeText(this,  String.format("현재 위치(%s)에 책갈피를 합니다",FirsTextIndex)  , Toast.LENGTH_SHORT).show();
                dbHelper = new DBHelper(this, dbName, null, dbVersion);
                SQLiteDatabase db;
                db = dbHelper.getWritableDatabase();
                //db.execSQL("CREATE TABLE bookmarks (Tname TEXT, FilePath TEXT, Data DATA, ScrollPage INTEGER);");//책갈피
                db.execSQL(String.format("INSERT INTO bookmarks(Tname, FilePath, Data, ScrollPage ) VALUES('%s', '%s', datetime('now','localtime'), %s);", resultName, resultPath,FirsTextIndex));
                return true;
            case R.id.Help://열기
                //데이터 담아서 팝업(액티비티) 호출
                Intent Helpintent = new Intent(this, HelpActivity.class);
                startActivityForResult(Helpintent, 404);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String x = Float.toString(event.getX());
        String y = Float.toString(event.getY());
        return super.onTouchEvent(event);
    }

    class BackThread extends Thread {  // Thread 를 상속받은 작업스레드 생성
        @Override
        public void run() {
            try {
                BufferedReader buf = new BufferedReader(new FileReader(filePath));
                while ((line = buf.readLine()) != null) {
                       list.add(line);
                }
                buf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // end class BackThread
    //   /**/

    private String readTxt(final String resultPath, final String resultName) throws FileNotFoundException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = String.format("%s/%s", resultPath, resultName);
                String line = null; // 한줄씩 읽기
                try {
                    BufferedReader buf = new BufferedReader(new FileReader(filePath));
                    while ((line = buf.readLine()) != null) {
                        txtRead.append(new String(line));
                        txtRead.append("\n");
                    }
                    buf.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
       return null;
    }
    public static int getLength(String string) {
        if (string == null) {
            return 0;
        }
        int length = string.length();
        int charLength = 0;
        for (int i = 0; i < length; i++) {
            charLength += string.codePointAt(i) > 0x00ff ? 2 : 1;
        }
        return charLength;
    }
    @Override
    public void onBackPressed() {
        sava();
        //화면꺼짐 방지 off
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
        super.onBackPressed();
    }
    public boolean checkDB() {
        try {
            dbHelper = new DBHelper(this, dbName, null, dbVersion);
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
    public String sava() {
        LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
        int FirsTextIndex = layoutManager.findFirstVisibleItemPosition(); //보이는레이아웃의 첫번째 아이템

        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        //데이터 조회 (전체 긁어오기)
        SQLiteDatabase db;
        db = dbHelper.getWritableDatabase();
        String Deldata="";
        String strSql = "";
        String totalCnt = "0";
        int nTotalCnt = 0;
        strSql += "SELECT COUNT(*) FROM history";
        Cursor cntRow = dbHelper.getReadableDatabase().rawQuery(strSql, null);
        while (cntRow.moveToNext()) {
            totalCnt = cntRow.getString(0);
        }
        nTotalCnt = Integer.parseInt(totalCnt);
        if (nTotalCnt >= 10) {  this.tru = 1; }//10개를 초과할시
        else if (checkDB() == false) {  this.tru = 2;  }//10개가 넘지 않고 신규파일일시
        else if (checkDB() == true) {   this.tru = 3;  }//10개가 넘지 않고 기존파일일시

        if (tru == 1) {
            String strDelsql="SELECT MIN(Data) FROM history ";
            Cursor DelRow = dbHelper.getReadableDatabase().rawQuery(strDelsql, null);
            while (DelRow.moveToNext()) {
                Deldata = DelRow.getString(0);
            }
            strDelsql ="";
            strDelsql +="delete from history where data = '" + Deldata +"';";
            Toast.makeText(this,  String.format(" %s를 지움  ",Deldata)  , Toast.LENGTH_SHORT).show();
            db.execSQL(strDelsql);
            if (checkDB() == false) { //신규
                //파일이름, 경로, 현재시간, 스크롤위치
                db.execSQL(String.format("INSERT INTO history(Tname, FilePath, Data, ScrollPage) VALUES('%s', '%s', datetime('now','localtime'), %s);", resultName, resultPath, FirsTextIndex));
            } else if (checkDB() == true) {//데이터 있을때
                db.execSQL(String.format("UPDATE history SET Data = datetime('now','localtime'),ScrollPage = %s WHERE Tname = '%s';",  FirsTextIndex,resultName));
            }
        } else if (tru == 2) {//false : 파일이 신규 파일일시
            db.execSQL(String.format("INSERT INTO history(Tname, FilePath, Data, ScrollPage) VALUES('%s', '%s', datetime('now','localtime'), %s);", resultName, resultPath, FirsTextIndex));

        } else if (tru == 3) {//파일이 신규파일이 아닐시
            db.execSQL(String.format("UPDATE history SET Data = datetime('now','localtime'),ScrollPage = %s WHERE Tname = '%s';",  FirsTextIndex,resultName));
        }
        return null;
    }
}