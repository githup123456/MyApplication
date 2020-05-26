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
    private ImageView img_right_top;


    public CommunicateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_communicate, container, false);
        initView(inflate);
        tv_top.setText("通话");
        return inflate;
    }

    //初始化view
    private void initView(View view){
        tv_top = view.findViewById(R.id.top_moudle);

    }
}
