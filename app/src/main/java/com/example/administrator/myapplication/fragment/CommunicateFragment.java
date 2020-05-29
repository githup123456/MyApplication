package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Window.SendVoicePhoneWindow;

/**
 * 通话
 */
public class CommunicateFragment extends Fragment {

    private ImageView img_call_audio;
    private EditText ed_ph_number;
    private TextView tv_top;
    Button  phone_fg_btn_select;
    RelativeLayout  search_layout;
    ConstraintLayout video_layout;
    ImageView  top_cancel;

    public CommunicateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_communicate, container, false);
        initView(inflate);
        tv_top.setText("通话");
        search_layout.setVisibility(View.VISIBLE);
        initListener();
        return inflate;
    }

    //点击button切换通话
    private void initListener() {
        //实现点击按钮切换通话界面
        phone_fg_btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_layout.setVisibility(View.VISIBLE);
                search_layout.setVisibility(View.GONE);
                top_cancel.setVisibility(View.VISIBLE);
            }
        });

        //todo 点击左箭头，返回输入手机号页面
        top_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_layout.setVisibility(View.GONE);
                search_layout.setVisibility(View.VISIBLE);
                top_cancel.setVisibility(View.GONE);
            }
        });
        //语音的监听事件
        img_call_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = ed_ph_number.getText().toString();
                String phone_number = ed_ph_number.getText().toString()+"@192.168.1.210";
                Intent intent2 = getActivity().getIntent();
                String url = intent2.getStringExtra("string_url");
                SendVoicePhoneWindow sendVoicePhoneWindow = new SendVoicePhoneWindow(getActivity(),url,phone_number,number);
                sendVoicePhoneWindow.showAtLocation(img_call_audio, Gravity.CENTER,0,0);
            }
        });
    }

    //todo 初始化view
    private void initView(View view){
        tv_top = view.findViewById(R.id.top_moudle);
        phone_fg_btn_select = view.findViewById(R.id.phone_fg_btn_select);
        search_layout = view.findViewById(R.id.search_layout);
        video_layout = view.findViewById(R.id.video_layout);
        top_cancel  =  view.findViewById(R.id.top_cancel);
        img_call_audio = (ImageView)view.findViewById(R.id.img_call_audio);
        ed_ph_number = (EditText)view.findViewById(R.id.phone_number);
    }
}
