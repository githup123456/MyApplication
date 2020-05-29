package com.example.administrator.myapplication.Window;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class VoiceIncomingWindow extends ClassWindow implements View.OnClickListener {
    private View rootView;
    private Context context;
    private ImageView img_listen,img_break,img_hang_up;//接听 挂断按钮
    private ConstraintLayout constraintLayout_1,constraintLayout_2;
    public  SipAudioCall sipAudioCall;
    public String username;
    private TextView tv_name;
    public VoiceIncomingWindow(Context context,String username,SipAudioCall sipAudioCall) {
        super(context);
        this.context = context;
        this.username = username;
        this.sipAudioCall = sipAudioCall;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.voice_incoming, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        initView(rootView);

    }


    //初始化控件
    private void initView(View view){
        constraintLayout_1 = (ConstraintLayout)view.findViewById(R.id.close_receiver);
        constraintLayout_2 = (ConstraintLayout)view.findViewById(R.id.answer_call) ;
        img_hang_up = (ImageView)view.findViewById(R.id.hang_up_btn);
        img_hang_up.setOnClickListener(this);
        tv_name = (TextView)view.findViewById(R.id.voice_name);
        tv_name.setText(username);
        img_break = (ImageView) view.findViewById(R.id.refuse_btn);
        img_listen = (ImageView)view.findViewById(R.id.receive_btn);
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
                constraintLayout_1.setVisibility(View.GONE);
                constraintLayout_2.setVisibility(View.VISIBLE);
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
                dismiss();
                break;
            }
            //接听挂断事件
            case R.id.hang_up_btn:{
                try {
                    sipAudioCall.endCall();
                    sipAudioCall.close();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        }
    }

}
