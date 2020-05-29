package com.example.administrator.myapplication.Window;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.WalkieTalkieActivity;

import java.util.logging.Logger;

public class SendVoicePhoneWindow extends PopupWindow {
    public SendVoicePhoneWindow sendVoicePhoneWindow;
    private View rootView;
    private Context context;
    private String name;
    private String local_url;
    public  SipAudioCall sipAudioCall;
    private ConstraintLayout constraintLayout_1,constraintLayout_2;//底部布局
    private TextView tv_name,call_invite;
    private Chronometer call_time;
    private ImageView img_hang_up, img_cancel_call;//挂断
    private String number;
    public SendVoicePhoneWindow(Context context, String url,String name,String number) {
        this.context = context;
        this.local_url = url;
        this.number =number;
        sendVoicePhoneWindow = SendVoicePhoneWindow.this;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.activity_send_voice_phone, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        WalkieTalkieActivity sipAnswerActivity = new WalkieTalkieActivity();
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
        tv_name.setText(number);
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
        //初始化电话状态信息
        call_invite = (TextView)view.findViewById(R.id.call_invite);
        //call_time 初始化时间
        call_time = view.findViewById(R.id.call_time);

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
    //获取当前sipAudioCall
    private void setSipAudioCall(SipAudioCall sipAudioCall){
        this.sipAudioCall = sipAudioCall;
    }
    //切换主线程
    private Handler handler = new Handler();
    //打电话
    public  void initiateCall(String sipAddress, SipAudioCall sipAudioCall, SipManager sipManager, String url) {

        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                //Todo 接听电话
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
                            call_invite.setVisibility(View.GONE);
                            constraintLayout_2.setVisibility(View.VISIBLE);
                        }
                    });

                    //添加通话时间
                    //计时器清零
                    call_time.setBase(SystemClock.elapsedRealtime());
                    call_time.start();
                }
                //Todo 通话结束
                @Override
                public void onCallEnded(SipAudioCall call) {
                    super.onCallEnded(call);
                    Log.d("TAG", "Ready:");
                    Log.d("SipMainActivity1", "ready");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            sendVoicePhoneWindow.dismiss();//挂断电话，结束当前Window
                        }
                    });

                    //通话结束
                    call_time.stop();
                }

                @Override  //todo 通话等待
                public void onCalling(SipAudioCall call) {
                    super.onCalling(call);
                    if (!call.isMuted()) {
                        call.toggleMute();
                    }
                    Log.d("SipMainActivity1", "3");
                }
            };
            Log.d("SipMainActivity1", "s " + sipAddress);
            sipAudioCall = sipManager.makeAudioCall(url, sipAddress, listener, 30); // 打电话
            setSipAudioCall(sipAudioCall); //获取当前sipAudioCall
        } catch (Exception e) {
            Log.d("SipMainActivity1", "s " + e);
            if (url != null) {
                try {
                    sipManager.close(url);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            if (sipAudioCall != null) {
                sipAudioCall.close();
            }
        }
    }
}
