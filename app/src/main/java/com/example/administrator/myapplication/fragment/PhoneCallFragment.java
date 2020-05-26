package com.example.administrator.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class PhoneCallFragment extends Fragment {
    private TextView top_moudle;//fragment名称
    private ImageView top_right;//历史记录
    private EditText ed_number;//输入号码
    private Button btn_select;//搜索跳转
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_call,container,false);
        initView(view);
        return view;
    }
    //初始化控件
    private void initView(View view){
        top_moudle = (TextView)view.findViewById(R.id.top_moudle);
        top_moudle.setText("通话");
        top_right = (ImageView)view.findViewById(R.id.top_right);
        top_right.setBackgroundResource(R.drawable.call_jilu);
        ed_number = (EditText)view.findViewById(R.id.phone_call_ed_phone_number);
        btn_select = (Button)view.findViewById(R.id.phone_fg_btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = ed_number.getText().toString();
                getFragmentManager()   //所得到的是所在fragment 的父容器的管理器
                        .beginTransaction()
                        .addToBackStack(phone_number)   //添加到后退栈
                        //此处的id，是FrameLayout的id
                        .replace(R.id.rb1,new CommunicateFragment())   //anotherFragment()为新建的fragment的java文件，OnCreateView里面把.xml文件添加
                        .commit();
            }
        });
    }
}
