package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adpter.MyFragmentPagerAdapter;
import com.example.administrator.myapplication.fragment.CommunicateFragment;
import com.example.administrator.myapplication.fragment.MessageFragment;
import com.example.administrator.myapplication.fragment.ModeFragment;
import com.example.administrator.myapplication.fragment.MyFragment;
import com.example.administrator.myapplication.fragment.languageFragment;

import java.util.ArrayList;

//  主 界 面
public class MenuActivity extends AppCompatActivity {
    private ViewPager fist_viewPager;
    private RadioButton btn_commuite;
    private RadioButton btn_lanuage;
    private RadioButton btn_mode;
    private RadioButton btn_message;
    private RadioButton btn_my;
    private RadioGroup rg;
    //数据
    private ArrayList<Fragment> fragment_list=new ArrayList<>();
    //适配器
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        initView(); //初始化
        initData(); //添加数据
        initListener(); //初始监听器
        initAdapter();  //添加适配器
    }

    private void initListener() {
        //RadioGroup的点击事件
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(btn_commuite.getId()==checkedId){
                    Log.d("MenuActivity1","msg1");
                    fist_viewPager.setCurrentItem(0);
                }
                if(btn_lanuage.getId()==checkedId){
                    Log.d("MenuActivity1","msg2");
                    fist_viewPager.setCurrentItem(1);
                }
                if(btn_mode.getId()==checkedId){
                    Log.d("MenuActivity1","msg3");
                    fist_viewPager.setCurrentItem(2);
                }
                if(btn_message.getId()==checkedId){
                    fist_viewPager.setCurrentItem(3);
                }
                if(btn_my.getId()==checkedId){
                    fist_viewPager.setCurrentItem(4);
                }
            }
        });

        //viewPager的点击事件
        fist_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
                if(i==0){
                    Log.d("MenuActivity1","msg4");
                    btn_commuite.setChecked(true);
                }else  if(i==1){
                    btn_lanuage.setChecked(true);
                    Log.d("MenuActivity1","msg5");
                }else  if(i==2){
                    btn_mode.setChecked(true);
                    Log.d("MenuActivity1","msg6");
                }else  if(i==3){
                    btn_message.setChecked(true);
                }else if(i==4){
                    btn_my.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public  void  initAdapter() {
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragment_list);
        fist_viewPager.setAdapter(myFragmentPagerAdapter);
    }



    private void initData() {
        fragment_list.add(new CommunicateFragment());
        fragment_list.add(new languageFragment());
        fragment_list.add(new ModeFragment());
        fragment_list.add(new MessageFragment());
        fragment_list.add(new MyFragment());
    }

    private void initView() {
        fist_viewPager = (ViewPager) findViewById(R.id.fist_viewPager);
        btn_commuite = (RadioButton) findViewById(R.id.rb1);
        btn_lanuage = (RadioButton) findViewById(R.id.rb2);
        btn_mode = (RadioButton) findViewById(R.id.rb3);
        btn_message = (RadioButton) findViewById(R.id.rb4);
        btn_my = (RadioButton) findViewById(R.id.rb5);
        rg = (RadioGroup) findViewById(R.id.rg);
    }
}
