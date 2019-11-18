package com.example.hairreservationsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ClientReservation extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_reservation);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }


    /**
     * 뒤로가기 버튼을 누를 시, 종료유무를 다시 한번 더 확인한다.
     */
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
