package com.textview.txt.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.textview.txt.R;
import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView t1, t2, t3, t4, text;

        MyViewHolder(View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.textView1) ;    //제목
            t2 = itemView.findViewById(R.id.textView2) ;    //경로
            t3 = itemView.findViewById(R.id.textView3) ;    //날짜
            t4 = itemView.findViewById(R.id.textView4) ;    //페이지
            // 아이템 클릭 이벤트 처리.

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // -- Log.d("클릭이벤트", v.toString());
                        mListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }
    // 커스텀 리스너 인터페이스
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historyrecyclerview_item, parent, false);
        return new MyViewHolder(v);
    }
    private ArrayList<Historyinfo> HistoryinfoArrayList;
    public HistoryRecyclerAdapter(ArrayList<Historyinfo> HistoryinfoArrayList){
        this.HistoryinfoArrayList = HistoryinfoArrayList;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.t1.setText(HistoryinfoArrayList.get(position).t1);
        myViewHolder.t2.setText(HistoryinfoArrayList.get(position).t2);
        myViewHolder.t3.setText(HistoryinfoArrayList.get(position).t3);
        myViewHolder.t4.setText(HistoryinfoArrayList.get(position).t4);
    }
    @Override
    public int getItemCount() {  return HistoryinfoArrayList.size();   }
}
