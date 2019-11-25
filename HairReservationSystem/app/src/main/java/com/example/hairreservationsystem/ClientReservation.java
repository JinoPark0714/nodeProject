package com.example.hairreservationsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 손님이 미용실을 찾고 예약 하는 화면
 */
public class ClientReservation extends AppCompatActivity {
    private EditText searchEditHairRoom;
    private Button searchBtnHairRoom;
    private TextView searchedTextHairRoom;
    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_reservation);
        setID();
        backPressCloseHandler = new BackPressCloseHandler(this);

        //여기서부터 onCreate 작성해야 힘 (위에 건들지 마라)
        onClickSearchHairRoom();

    }

    void setID(){
        searchEditHairRoom = (EditText)findViewById(R.id.searchHairRoom);
        searchBtnHairRoom = (Button)findViewById(R.id.searchBtnHairRoom);
        searchedTextHairRoom = (TextView)findViewById(R.id.searchedHairRoom);
    }



    public void onClickSearchHairRoom(){

        searchBtnHairRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = searchEditHairRoom.getText().toString();
                if(str != null){
                    Toast.makeText(getApplicationContext(), "정보 : " + str, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(), "This is Test", Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * 뒤로가기 버튼을 누를 시, 종료유무를 다시 한번 더 확인한다.
     */
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
