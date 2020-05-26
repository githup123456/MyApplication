package com.example.administrator.myapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallPhoneFragment extends Fragment {

    TextView  tv_moudle;
    public CallPhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_call_phone, container, false);
        initView(inflate);
        tv_moudle.setText("通话");
        return inflate;
    }

    private void initView(View inflate) {
        tv_moudle = inflate.findViewById(R.id.top_moudle);
    }
}
