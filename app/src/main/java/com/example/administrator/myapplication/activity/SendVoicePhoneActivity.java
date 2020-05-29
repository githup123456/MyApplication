package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * 实现发送语音电话功能
 */
public class SendVoicePhoneActivity extends AppCompatActivity {
    private String name;
    public static SipAudioCall sipAudioCall;
    private TextView tv_name;
    private ImageView img_hang_up;//挂断
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_voice_phone);
        SipAnswerActivity sipAnswerActivity = new SipAnswerActivity();
        SipAudioCall sipAudioCall =null;
        SipManager sipManager = sipAnswerActivity.getSipManager();
        SipProfile sipProfile = null;
        Intent intent = getIntent();
        String url = intent.getStringExtra("local_url");
        name = intent.getStringExtra("phone_name");
        initiateCall(name,sipAudioCall,sipManager,url);
        initView();
    }
    //初始化控件
    private void initView(){
        tv_name =(TextView)findViewById(R.id.send_name);
        tv_name.setText(name);
        img_hang_up = (ImageView)findViewById(R.id.send_cancel_btn);
        img_hang_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sipAudioCall.endCall();
                    sipAudioCall.close();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setSipAudioCall(SipAudioCall sipAudioCall){
        this.sipAudioCall = sipAudioCall;
    }
    public static SipAudioCall getSipAudioCall(){
        return sipAudioCall;
    }
    public  void initiateCall(String sipAddress, SipAudioCall sipAudioCall, SipManager sipManager, String url) {

        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onCallEstablished(SipAudioCall call) {
                    super.onCallEstablished(call);
                    Log.d("SipMain","1");
                    call.startAudio();
                    call.setSpeakerMode(true);
                    call.toggleMute();
                    Intent intent = new Intent(SendVoicePhoneActivity.this,AnswerCallActivity.class);
                    intent.putExtra("userID",name);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    super.onCallEnded(call);
                    Log.d("TAG", "Ready:");
                    Log.d("SipMainActivity1", "ready");
                    SendVoicePhoneActivity.this.finish();
                }

                @Override
                public void onCalling(SipAudioCall call) {
                    super.onCalling(call);
                    if (!call.isMuted()) {
                        call.toggleMute();
                    }
                    Log.d("SipMainActivity1", "3");
                }
            };
            Log.d("SipMainActivity1", "s " + sipAddress);
            sipAudioCall = sipManager.makeAudioCall(url, sipAddress, listener, 30);
            setSipAudioCall(sipAudioCall);
        } catch (Exception e) {

            Log.d("SipMainActivity1", "s " + e);
            if (url != null) {
                try {
                    Log.d("SipMainActivity1", "s " + sipAddress);
                    sipManager.close(url);
                } catch (Exception ee) {

                    Log.d("SipMainActivity1", "s " + sipAddress);
                    ee.printStackTrace();
                }
            }
            if (sipAudioCall != null) {

                Log.d("SipMainActivity1", "s " + sipAddress);
                sipAudioCall.close();
            }

        }
    }
}
