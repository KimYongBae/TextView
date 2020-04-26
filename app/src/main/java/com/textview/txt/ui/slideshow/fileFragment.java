package com.textview.txt.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.textview.txt.MainActivity;
import com.textview.txt.R;
import com.textview.txt.ReadView;
import java.io.File;
import java.util.ArrayList;

public class fileFragment extends Fragment {
    String m_keyPath;
    ArrayList<String> m_list = new ArrayList<>();
    TextView m_path;
    private String MPath;
    ArrayAdapter adapter;

    ListView listview;
    File[] files;//디렉토리
    String Dname;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        View root = inflater.inflate(R.layout.fragment_file, container, false);
        m_path = (TextView) root.findViewById(R.id.m_path);

        /*파일 불러오기*/
        m_list = file();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, m_list);
        listview = (ListView) root.findViewById(R.id.file_list_view);
        listview.setAdapter(adapter);

        // Retrieving the external storage state
        m_keyPath = Environment.getExternalStorageDirectory().toString();

        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selected_item = (String) adapterView.getItemAtPosition(position);

                try {
                    if(dircke(selected_item) == true) {
                        m_path.append(selected_item);                    //최종현재경로에 파일이름 붙이기
                        MPath = m_path.getText().toString();            //최종현재경로 백업
                        refreshFiles();//리프레쉬
                    }else if (selected_item == "../") {                 //뒤로가기
                        if(MPath != null) {
                            if(!m_keyPath.equals(MPath)) {
                                int end = MPath.lastIndexOf("/");          //가 나오는 마지막 인덱스를 찾고
                                String uppath = MPath.substring(0, end);         //그부분을 짤라버림 즉 위로가게됨
                                MPath = uppath;
                                m_path.setText(MPath);
                                refreshFiles();
                            }
                        }
                    } else if (selected_item != "../") {
                        Log.i("selected_item ::: ", selected_item);
                        MPath = m_path.getText().toString();//최종현재경로 백업
                        Intent intent = new Intent(getActivity(), ReadView.class);
                        intent.putExtra("openfilePath", MPath);//파일이있는 경로 보내기
                        intent.putExtra("openfileName", selected_item);//파일이있는 이름 보내기
                        startActivity(intent);
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "해당파일의 경로에 이상이 있습니다.", Toast.LENGTH_SHORT).show();
                    file();
                }
            }

        });
        /* EMD listview.setOnItemClickListener */
        return root;
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

    private ArrayList<String> file() { //파일 목록 읽어오기
        String Dname = null;
        //final String path = Environment.getExternalStorageDirectory().toString() + "/KakaoTalkDownload";
        final String path = Environment.getExternalStorageDirectory().toString();
        Log.i("Files", path);

        //목록추가
        File directory = new File(path);//경로
        files = directory.listFiles();//디렉토리
        m_path.setText(path); //초기경로설정
        int count = files.length;

        for (int i = 0; i < count; i++) {
            Dname = files[i].getName();
            Log.i("Files", files[i].getName());
            if (files[i].isDirectory() == true) {
                Dname = "/" + files[i].getName();
                m_list.add(Dname);
            } else if (files[i].isFile() == true && Dname.endsWith(".txt") == true) {
                m_list.add(files[i].getName());
            }
        }
        return m_list;
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

    void refreshFiles() {
        m_list.clear();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            adapter.remove(adapter.getItem(i));
            adapter.clear();
        }
        listview.setAdapter(adapter);
        File directory = new File(MPath);//경로
        files = directory.listFiles();//디렉토리
        if(!m_keyPath.equals(MPath))
            m_list.add("../");

        for (int i = 0; i < files.length; i++) {
            Dname = files[i].getName();
            Log.i("Files", files[i].getName());
            if (files[i].isDirectory() == true) {
                Dname = "/" + files[i].getName();
                m_list.add(Dname);
            } else if (files[i].isFile() == true && Dname.endsWith(".txt") == true) {
                m_list.add(files[i].getName());
            }
            adapter.notifyDataSetChanged();
        }
    }
}


