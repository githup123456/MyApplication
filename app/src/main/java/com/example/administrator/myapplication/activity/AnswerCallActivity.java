package com.example.administrator.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipSession;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

import java.util.logging.Logger;

/**
 * 接听界面，实现电话功能
 */
public class AnswerCallActivity extends AppCompatActivity implements View.OnClickListener {
    private SipAudioCall sipAudioCall;
    private ImageView img_break;//挂断电话按钮
    private String userID;
    private SipManager sipManager;
    public String close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_call);
        sipAudioCall = VoiceIncomingActivity.getSipAndiocall();//获取sipAudioCall
        if (sipAudioCall == null){
            sipAudioCall = SendVoicePhoneActivity.getSipAudioCall();
        }
        close = null;
        Log.d("SipMain","1233"+close);
        sipManager = SipManager.newInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");//获取来电ID
        close = intent.getStringExtra("close");
        SharedPreferences sharedPreferences = getSharedPreferences("close_call",Context.MODE_PRIVATE);

        close = sharedPreferences.getString("close","");
        if (close!=null){
            Log.d("SipMain","1234"+close);
            if (close.equals("通话中断")){
                AnswerCallActivity.this.finish();
            }
        }else {
            Log.d("SipMain","12345"+close);
        }

        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnswerCallActivity.this.finish();
    }

    //初始化控件
    private void initView(){
        img_break = (ImageView)findViewById(R.id.hang_up_btn);
        img_break.setOnClickListener(this);//挂断事件监听

    }
    public static void close(){
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hang_up_btn:{
                try {
                    sipAudioCall.endCall();
                    sipAudioCall.close();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            }
        }
    }
}
