package com.example.hairreservationsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * 사업자 정보를 입력하는 곳이다.
 * 여기서는 미용실 이름, 미용실 주소
 */
public class SetHairRoom extends AppCompatActivity {
    private EditText setHairRoomName, setHairRoomCallNum, setHairRoomAddress, setHairRoomHairDresserNum;
    private Button setHairRoomInfo;
    private HttpConnectionManager hcManager; //통신 시도
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_hair_room);
        setID();
        onClickSetHairRoomInfo();
        Log.d("CID", Data.getInstance().getCID());
    }
    public void setID(){
        setHairRoomName = (EditText)findViewById(R.id.setHairRoomName);
        setHairRoomCallNum = (EditText)findViewById(R.id.setHairRoomCallNum);
        setHairRoomAddress = (EditText)findViewById(R.id.setHairRoomAddress);
        setHairRoomHairDresserNum = (EditText)findViewById(R.id.setHairRoomHairDresserNum);
        setHairRoomInfo = (Button)findViewById(R.id.setHairRoomInfo);
    }


    public void onClickSetHairRoomInfo(){
        setHairRoomInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String hairRoomName = setHairRoomName.getText().toString();
                final String hairRoomCallNum = setHairRoomCallNum.getText().toString();
                final String hairRoomAddress = setHairRoomAddress.getText().toString();
                final String hairRoomHairDresserNum = setHairRoomHairDresserNum.getText().toString();
                hcManager = HttpConnectionManager.getInstance();
                try{
                    hcManager.setHairRoomInfo(hairRoomName, hairRoomCallNum, hairRoomAddress, hairRoomHairDresserNum, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("F ", "응답에 실패했습니까?");
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                catch(JSONException e){e.printStackTrace();}
                catch(UnsupportedEncodingException e){e.printStackTrace();}

            }
        });
    }
}
