package com.example.hairreservationsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 사업자가 일정관리, 스케줄 조회, 자신의 사업장 설정을 할 수 있는 곳이다.
 *
 */
public class ManagerScreen extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    private Button setPlaceBusiness, businessCalendar;
    private SetHairRoom setHairRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_screen);
        setID();
        backPressCloseHandler = new BackPressCloseHandler(this);
        onClickSetPlaceBusiness();
        onClickSetBusinessCalendar();
    }
    void setID(){
        setPlaceBusiness = (Button)findViewById(R.id.setPlaceBusiness); //사업장 설정
        businessCalendar = (Button)findViewById(R.id.businessCalendar); //일정관리
    }

    void onClickSetBusinessCalendar(){
        businessCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowCalendar.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 클릭 시, 사업장 설정 화면으로 넘어간다.
     * 이 때, 회원가입 후 최초 로그인이면 사업장 정보가 없으므로
     * 정보 입력란에는 예시가 나오게 되고, 이미 사업장 정보를 기입하였으면
     * 해당 입력란에는 입력한 정보가 바로 나오게 된다.
     */
    void onClickSetPlaceBusiness(){
        setPlaceBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetHairRoom.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
