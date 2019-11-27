package com.example.hairreservationsystem;

import android.widget.TextView;

public class TableManager {
    private static final TableManager Instance = new TableManager();

    private TableManager(){}
    public static TableManager getInstance() {
        return Instance;
    }

    private int max;
    private int now;

    private TextView table1;
    private TextView table2;
    private TextView table3;
    private TextView table4;

    public TextView getTable1() {
        return table1;
    }
    public void setTable1(TextView table1) {
        this.table1 = table1;
    }

    public TextView getTable2() {
        return table2;
    }
    public void setTable2(TextView table2) {
        this.table2 = table2;
    }

    public TextView getTable3() {
        return table3;
    }
    public void setTable3(TextView table3) {
        this.table3 = table3;
    }

    public TextView getTable4() {
        return table4;
    }
    public void setTable4(TextView table4) {
        this.table4 = table4;
    }

    public int getMax(){
        return max;
    }
    public int getNow(){
        return now;
    }

    public void setMax(int a){
        this.max=a;
    }
    public void setNow(int a){
        this.now=a;
    }

}

