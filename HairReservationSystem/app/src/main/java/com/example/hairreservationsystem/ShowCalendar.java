package com.example.hairreservationsystem;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowCalendar extends AppCompatActivity {
    RecyclerView mRecycler;
    RecyclerView.LayoutManager layoutManager;

    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;

    TableManager dm = TableManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_calendar);
        mRecycler = findViewById(R.id.rV);
        t1 = findViewById(R.id.table_txt1);
        t2 = findViewById(R.id.table_txt2);
        t3 = findViewById(R.id.table_txt3);
        t4 = findViewById(R.id.table_txt4);

        dm.setTable1(t1);
        dm.setTable2(t2);
        dm.setTable3(t3);
        dm.setTable4(t4);

        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));
        mRecycler.setHasFixedSize(true);

        //RecyclerView에 LayoutManager를 통해 GridLayout형태로 출력함 밑에 spanCount 2는 1행에 2열출력 한다는거
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecycler.setLayoutManager(layoutManager);

        ArrayList<DataClass> dataArray = new ArrayList<>();

        for(int i=0; i<14; i++){
            dataArray.add(new DataClass(plzDay(i),plzWeekday(i)));
        }

        //adapter클래스를 위에 for문에서 날짜의 값을 String으로 받은 dataArray로 생성함
        MyAdapter myAdapter = new MyAdapter(dataArray);

        //adapter의 내용을 mRecycler에 삽입
        mRecycler.setAdapter(myAdapter);
    }
    // +a 일의 날짜를 String 형으로 반환하는 함수
    public String plzDay(int a){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,a);
        Date date = calendar.getTime();
        String currentDate=null;

        try {
            //SimpleDateFormat이 불안정한 클래스라 에러뜬다고함.
            currentDate = new SimpleDateFormat("dd").format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return currentDate;
    }

    public int plzWeekday(int a){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,a);
        Date date = calendar.getTime();
        int weekday = calendar.get(calendar.DAY_OF_WEEK);

        return weekday;

    }
}
