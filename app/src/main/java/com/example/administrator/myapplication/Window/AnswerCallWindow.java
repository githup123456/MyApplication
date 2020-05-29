package com.example.administrator.myapplication.Window;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;

public class AnswerCallWindow extends ClassWindow implements View.OnClickListener {
    private Context context;
    private View rootView;
    private SipAudioCall sipAudioCall;
    private ImageView img_break;//挂断电话按钮
    private String userID;
    public AnswerCallWindow(Context context,String userID,SipAudioCall sipAudioCall) {
        super(context);
        this.context = context;
        this.userID = userID;
        this.sipAudioCall = sipAudioCall;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.activity_answer_call, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        initView(rootView);
    }
    //初始化控件
    private void initView(View view){
        img_break = (ImageView)view.findViewById(R.id.hang_up_btn);
        img_break.setOnClickListener(this);//挂断事件监听

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
                dismiss();
                break;
            }
        }
    }
}
