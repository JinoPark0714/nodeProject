package com.example.hairreservationsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FindAccount extends AppCompatActivity {
    private HttpConnectionManager hcManager; //통신 시도
    private EditText findAccount_IDFindName, findAccount_IDFindPhoneNum;
    private EditText findAccount_PWFindName, findAccount_PWFindID, findAccount_PWFindPhoneNum;
    private String CID, CName, CPhoneNum;
    private Button btnFindID, btnFindPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_account);
        setID();
        onClickFindID();
        onClickFindPW();
    }
    void setID(){
        findAccount_IDFindName = (EditText)findViewById(R.id.FindAccount_IDFindName);
        findAccount_IDFindPhoneNum = (EditText)findViewById(R.id.FindAccount_IDFindPhoneNum);

        findAccount_PWFindID = (EditText)findViewById(R.id.FindAccount_PWFindID);
        findAccount_PWFindName = (EditText)findViewById(R.id.FindAccount_PWFindName);
        findAccount_PWFindPhoneNum = (EditText)findViewById(R.id.FindAccount_PWFindPhoneNum);

        btnFindID = (Button)findViewById(R.id.buttonFindID);
        btnFindPW = (Button)findViewById(R.id.buttonFindPW);
    }

    /**
     * 클릭 시 ID 찾기를 통해 ID를 조회한다.
     */
    void onClickFindID(){
        btnFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CName = findAccount_IDFindName.getText().toString();
                CPhoneNum = findAccount_IDFindPhoneNum.getText().toString();
                hcManager = HttpConnectionManager.getInstance();

            }
        });
    }

    /**
     * 클릭 시 PW 찾기를 통해 PW를 조회한다.
     */
    void onClickFindPW(){
        btnFindPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
