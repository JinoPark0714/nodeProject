package com.example.hairreservationsystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHold> {
    TableManager dm = TableManager.getInstance();

    public class ViewHold extends RecyclerView.ViewHolder{

        //recycler_item1.xml에서 넣을 뷰에 아이디값 매칭함
        TextView week_txt;
        TextView day_txt;
        LinearLayout day_layout;

        ViewHold(View v){
            super(v);
            week_txt = v.findViewById(R.id.week_txt);
            day_txt = v.findViewById(R.id.day_txt);
            day_layout = v.findViewById(R.id.day_layout);
        }
    }
    ArrayList<DataClass> dataclass;

    //생성자
    MyAdapter(ArrayList<DataClass> d){
        this.dataclass = d;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item1,viewGroup,false);
        Log.d("whatisit","asdff");
        return new ViewHold(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, final int position) {
        final ViewHold viewHold = holder;
        viewHold.week_txt.setText(dataclass.get(position).getWeekday());
        viewHold.day_txt.setText(dataclass.get(position).getDay_txt());
        //추가할 데이터 있으면 viewHold.ID로받은값.setText(dataclass.get(position).private 자료형일경우 getter함수로 public일 경우 그냥변수이름);

        viewHold.day_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), position + "",Toast.LENGTH_LONG).show();
                //클릭시 setBackgroundResource로 선택 스타일로 바뀌게 해야함
                dm.getTable1().setText(position+"z");
                dm.getTable2().setText(position+"x");
                dm.getTable3().setText(position+"c");
                dm.getTable4().setText(position+"v");

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataclass.size();
    }

}
