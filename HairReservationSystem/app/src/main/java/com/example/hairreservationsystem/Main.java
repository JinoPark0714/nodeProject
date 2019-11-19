package com.example.hairreservationsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * 기본화면이다.
 * 시작하면 로그인을 하거나 ID가 없으면 회원가입을 하는 버튼이 나온다.
 */
public class Main extends AppCompatActivity {
    private HttpConnectionManager hcManager; //통신 시도
    private Button btnLogin, btnMakeAccount, btnFindAccount;
    private EditText CID; //ID 입력
    private EditText CPassword; //PW입력
    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this); //2번 클릭 시 종료
        setVarID();
        onClickLogin();
        onClickMakeAccount();
        onClickFindAccount();
    }

    /**
     * ID값을 세팅한다. (완료)
     */
    void setVarID(){
        CID = (EditText)findViewById(R.id.CID);
        CPassword = (EditText)findViewById(R.id.CPassword);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnMakeAccount = (Button)findViewById(R.id.buttonMakeAccount);
        btnFindAccount = (Button)findViewById(R.id.buttonFindAccount);
    }

    /**
     * 클릭 시, 계정 찾기 화면으로 넘어간다.
     */
    void onClickFindAccount(){
        btnFindAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindAccount.class);
                startActivity(intent);
            }
        });
    }


    /**
     * 클릭 시, 회원가입 화면으로 넘어간다. (*완료)
     */
    void onClickMakeAccount(){
        btnMakeAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAccount.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 버튼을 누를 시, 로그인을 시도한다. (완료)
     */
    void onClickLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String myID = CID.getText().toString(); //ID값을 string으로 전환
                final String myPW = CPassword.getText().toString(); //PW값을 string으로 전환
                try {
                    hcManager.getInstance().login(myID, myPW, new JsonHttpResponseHandler() {
                        /*
                        * resonse는 서버에서 응답을 받아온 결과를 JSON형태로 받아온다.
                        * JSON이기 때문에 toString을 통하여 데이터 값을 String으로 전환해야 한다.
                        * */
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try{
                                AlertDialog.Builder dlg = new AlertDialog.Builder(Main.this);
                                int success = response.getInt("success");
                                if (success == 1) { //응답에 성공했습니까??
                                    int myGrade = response.getInt("Grade");
                                    Log.d("myGrade", String.valueOf(myGrade));
                                    Data.getInstance().setGrade(myGrade); //등급 값을 받아온다.
                                    /*등급 값에 따라 0 = 손님, 1 = 미용사, 2 = 관리자*/
                                    if(Data.getInstance().getGrade() == 0){//손님
                                        Log.d("확인", "손님 로그인");
                                        dlg.setMessage("일반회원 화면으로 이동합니다.");
                                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(getApplicationContext(), ClientReservation.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                    else if(Data.getInstance().getGrade() == 1){//미용사
                                        dlg.setMessage("미용사 화면으로 이동합니다.");
                                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(getApplicationContext(), HairDresser.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                    else if(Data.getInstance().getGrade() == 2){ //관리자
                                        dlg.setMessage("관리자 화면으로 이동합니다.");
                                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(getApplicationContext(), ManagerScreen.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                    dlg.show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "로그인 실 패, 니계정 봉 쇄 !", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(JSONException e){e.printStackTrace();}
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("Fail", "Oops, response fail");
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}