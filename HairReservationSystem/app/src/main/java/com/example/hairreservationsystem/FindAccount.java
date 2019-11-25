package com.example.hairreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FindAccount extends AppCompatActivity {
    private HttpConnectionManager hcManager; //통신 시도
    private EditText findName, findPhoneNum_1, findPhoneNum_2, findPhoneNum_3, findID;
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
                findName = (EditText)findViewById(R.id.IDFindName);
                findPhoneNum_1 = (EditText)findViewById(R.id.IDFindPhoneNum_1);
                findPhoneNum_2 = (EditText)findViewById(R.id.IDFindPhoneNum_2);
                findPhoneNum_3 = (EditText)findViewById(R.id.IDFindPhoneNum_3);
                final String CName = findName.getText().toString();
                final String CPhoneNum = findPhoneNum_1.getText().toString()
                        + '-' + findPhoneNum_2.getText().toString()
                        + '-' + findPhoneNum_3.getText().toString();
                hcManager = HttpConnectionManager.getInstance();
                try{
                    hcManager.findID(CName, CPhoneNum, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("E", "응답이 잘 되나요?");
                            try{
                                String findedID = response.getString("CID");
                                Toast.makeText(getApplicationContext(), "ID : " + findedID,
                                        Toast.LENGTH_SHORT).show();
                            }catch(Exception e){e.printStackTrace();}
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            Log.d("ERR", "응답이 없습니다.");
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }catch(Exception e){e.printStackTrace();}

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
                findName = (EditText)findViewById(R.id.PWFindName);
                findID = (EditText)findViewById(R.id.PWFindID);
                findPhoneNum_1 = (EditText)findViewById(R.id.PWFindPhoneNum_1);
                findPhoneNum_2 = (EditText)findViewById(R.id.PWFindPhoneNum_2);
                findPhoneNum_3 = (EditText)findViewById(R.id.PWFindPhoneNum_3);
                final String myID = findID.getText().toString();
                final String myName = findName.getText().toString();
                final String myPhoneNum = findPhoneNum_1.getText().toString()
                        + '-' + findPhoneNum_2.getText().toString()
                        + '-' + findPhoneNum_3.getText().toString();
                hcManager = HttpConnectionManager.getInstance();
                try{
                    hcManager.findPW(myID, myName, myPhoneNum, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try{
                                String findedPW = response.getString("CPassword");
                                Toast.makeText(getApplicationContext(), "PW : " + findedPW, Toast.LENGTH_SHORT).show();
                            }
                            catch(Exception e){e.printStackTrace();}
                            super.onSuccess(statusCode, headers, response);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                catch(Exception e ){e.printStackTrace();}
            }
        });
    }


}
