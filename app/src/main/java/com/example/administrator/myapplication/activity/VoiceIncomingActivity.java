package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class VoiceIncomingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_listen,img_break;//接听 挂断按钮
    public static SipAudioCall sipAudioCall;
    public String username;
    private TextView tv_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_incoming);
        IncomingCallReceiver incomingCallReceiver = new IncomingCallReceiver();

        sipAudioCall = IncomingCallReceiver.getSipAudioCall();//获取sipAudioCall
        Intent intent = getIntent();
        username = intent.getStringExtra("userID");//获取来电ID
        initView();
    }
    //初始化控件
    private void initView(){
        tv_name = (TextView)findViewById(R.id.voice_name);
        tv_name.setText(username);
        img_break = (ImageView) findViewById(R.id.refuse_btn);
        img_listen = (ImageView)findViewById(R.id.receive_btn);
        img_listen.setOnClickListener(this);//接听事件的监听
        img_break.setOnClickListener(this);//挂断事件的监听
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //接听事件
            case R.id.receive_btn:{
                try {
                    sipAudioCall.answerCall(30);
                    sipAudioCall.startAudio();
                    sipAudioCall.setSpeakerMode(true);
                    if (sipAudioCall.isMuted()){
                        sipAudioCall.toggleMute();
                    }
                } catch (SipException e) {
                    e.printStackTrace();
                }
                setIntent();
                finish();
                break;
            }
            //挂断事件
            case R.id.refuse_btn:{
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
    //电话接听跳转
    public void setIntent(){
        Intent intent = new Intent(VoiceIncomingActivity.this,AnswerCallActivity.class);
        intent.putExtra("usernameID",username);
        startActivity(intent);
    }
    public static SipAudioCall getSipAndiocall(){
        return sipAudioCall;
    }

}
