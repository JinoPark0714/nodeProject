package com.example.hairreservationsystem;

import android.app.Activity;
import android.widget.Toast;

/**
 * 뒤로가기를 2번 더 누르면 종료되는 클래스
 * 일일이 각 액티비티별로 구현하면 코드가 길어져서 지저분하다.
 * 그렇기에 객체화를 하여 이 기능이 필요한 액티비티만 받아오면 된다. (싱글톤 할 필요 x)
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    public BackPressCloseHandler(Activity activity){
        this.activity = activity;
    }

    public void onBackPressed(){
        //버튼을 누를 시, 토스트 메시지를 띄움
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        //빠르게 2번 누르면 액티비티가 종료됨
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }

    public void showGuide(){
        toast = Toast.makeText(activity, " \"뒤로\"버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT);
        toast.show();
    }



}
