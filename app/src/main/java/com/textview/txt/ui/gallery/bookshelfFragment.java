package com.textview.txt.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.textview.txt.OpenPopupActivity;
import com.textview.txt.R;

public class bookshelfFragment extends Fragment {

    private bookshelfViewModel bookshelfViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookshelfViewModel =
                ViewModelProviders.of(this).get(bookshelfViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        bookshelfViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);


            }
        });
        /*

                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(this, OpenPopupActivity.class);
                intent.putExtra("open", "openfile");
                startActivityForResult(intent, 1);
                //readTxt();
                Toast.makeText(this, "open_열기", Toast.LENGTH_SHORT).show();
        */
        /*
        Button btn1= (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "로옹터치중이다!!!!!!!!!!!!");

            }
        });
        */

        /**/
        return root;
    }


}
