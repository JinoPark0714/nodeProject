package com.example.hairreservationsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HairDresser extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hair_dresser);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
