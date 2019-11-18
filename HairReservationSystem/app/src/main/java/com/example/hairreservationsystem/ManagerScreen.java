package com.example.hairreservationsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerScreen extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_screen);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
