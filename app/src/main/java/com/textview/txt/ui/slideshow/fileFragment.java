package com.textview.txt.ui.slideshow;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.textview.txt.MainActivity;
import com.textview.txt.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileFragment extends Fragment{


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

        //---------------------------------------
        ArrayList<String> list = new ArrayList<String>();
        final ListView listview = (ListView)root.findViewById(R.id.file_list_view);
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        list.add("LIST-0001");
        list.add("LIST-0002");
        list.add("LIST-0003");
        adapter.notifyDataSetChanged();
       //----------------------------------------

        return root;


    }

}