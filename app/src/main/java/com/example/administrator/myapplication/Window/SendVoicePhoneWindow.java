package com.example.administrator.myapplication.Window;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.SipAnswerActivity;

import java.util.logging.Logger;

public class SendVoicePhoneWindow extends ClassWindow{
    public SendVoicePhoneWindow sendVoicePhoneWindow;
    private View rootView;
    private Context context;
    private String name;
    private String local_url;
    public  SipAudioCall sipAudioCall;
    private ConstraintLayout constraintLayout_1,constraintLayout_2;
    private TextView tv_name;
    private ImageView img_hang_up, img_cancel_call;//挂断
    public SendVoicePhoneWindow(Context context, String url,String name) {
        super(context);
        this.context = context;
        this.local_url = url;
        this.name =name;
        sendVoicePhoneWindow = SendVoicePhoneWindow.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.activity_send_voice_phone, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        SipAnswerActivity sipAnswerActivity = new SipAnswerActivity();
        sipAudioCall =null;
        SipManager sipManager = sipAnswerActivity.getSipManager();
        initiateCall(name,sipAudioCall,sipManager,url);
        initView(rootView);

    }
    //初始化控件
    private void initView(View view){
        constraintLayout_1 = (ConstraintLayout)view.findViewById(R.id.close_cancel);
        constraintLayout_2 = (ConstraintLayout)view.findViewById(R.id.answer_call);
        tv_name =(TextView)view.findViewById(R.id.send_name);
        tv_name.setText(name);
        img_cancel_call = (ImageView)view.findViewById(R.id.send_cancel_btn);
        img_cancel_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sipAudioCall.endCall();
                    sipAudioCall.close();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        img_hang_up = (ImageView)view.findViewById(R.id.hang_up_btn);
        img_hang_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sipAudioCall.endCall();
                    sipAudioCall.close();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
    }
    private void setSipAudioCall(SipAudioCall sipAudioCall){
        this.sipAudioCall = sipAudioCall;
    }
    private Handler handler = new Handler();
    //打电话
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logger.getLogger("onCallEnded");
                            constraintLayout_1.setVisibility(View.GONE);
                            constraintLayout_2.setVisibility(View.VISIBLE);
                        }
                    });


                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    super.onCallEnded(call);
                    Log.d("TAG", "Ready:");
                    Log.d("SipMainActivity1", "ready");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            sendVoicePhoneWindow.dismiss();
                        }
                    });

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
