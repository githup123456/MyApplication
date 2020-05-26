package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * 通话
 */
public class CommunicateFragment extends Fragment {

    private TextView tv_top;
    private ImageView img_right_top,img_left_top;


    public CommunicateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_communicate, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //初始化view
    private void initView(View view){
        tv_top = (TextView)view.findViewById(R.id.top_moudle);
        img_left_top = (ImageView)view.findViewById(R.id.top_cancel);
        img_right_top = (ImageView)view.findViewById(R.id.top_right);
        tv_top.setText("通话");
        img_right_top.setBackgroundResource(R.drawable.call_jilu);
        img_left_top.setBackgroundResource(R.drawable.cancel);

    }
}
