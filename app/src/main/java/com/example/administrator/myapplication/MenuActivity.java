package com.example.administrator.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import com.example.administrator.myapplication.fragment.BWListFragment;
import com.example.administrator.myapplication.fragment.FragmentView;
import com.example.administrator.myapplication.fragment.MessageFragment;
import com.example.administrator.myapplication.fragment.PersonFragment;

import java.util.ArrayList;
import java.util.List;
 //  主 界 面
public class MenuActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

    }

    private void createFragment(){
        FragmentView fragmentView = (FragmentView)findViewById(R.id.fragment_view);
        MessageFragment messageFragment = new MessageFragment();
        PersonFragment  personFragment  = new PersonFragment();
        BWListFragment  bwListFragment  = new BWListFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(messageFragment);
        fragmentList.add(personFragment);
        fragmentList.add(bwListFragment);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
