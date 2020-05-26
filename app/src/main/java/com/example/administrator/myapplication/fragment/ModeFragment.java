package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModeFragment extends Fragment implements View.OnClickListener{
    private Button btn_hostory;
    private LinearLayout lly_b_list,lly_w_list;
    public ModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mode, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initView(View view){
        btn_hostory = (Button)view.findViewById(R.id.bw_btn_problem);
        lly_w_list = (LinearLayout) view.findViewById(R.id.bw_lly_white_list);
        lly_b_list = (LinearLayout)view.findViewById(R.id.bw_lly_black_list);
        btn_hostory.setOnClickListener(this);
        lly_b_list.setOnClickListener(this);
        lly_w_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bw_lly_black_list:{
                break;
            }
            case R.id.bw_lly_white_list:{
                break;
            }
            case R.id.bw_btn_problem:{
                break;
            }
        }
    }
}
