package com.example.administrator.myapplication.activity;

import android.content.IntentFilter;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    public SipManager sipManager;
    private ViewPager fist_viewPager;
    private RadioButton btn_commuite;
    private RadioButton btn_lanuage;
    private RadioButton btn_mode;
    private RadioButton btn_message;
    private RadioButton btn_my;
    private RadioGroup rg;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    //数据
    private ArrayList<Fragment> fragment_list=new ArrayList<>();
    //适配器
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    public IncomingCallReceiver mReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        SipAnswerActivity sipAnswerActivity = new SipAnswerActivity();
        sipManager = sipAnswerActivity.getSipManager();
        //广播
        IntentFilter intentFilter = new IntentFilter("android.SipTest.INCOMING_CALL");
        mReceiver = new IncomingCallReceiver();
        registerReceiver(mReceiver, intentFilter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView(); //初始化
        initData(); //添加数据
        initListener(); //初始监听器
        initAdapter();  //添加适配器
    }

    @Override
    protected void onResume() {
        super.onResume();
        fist_viewPager.setCurrentItem(2);
        btn_mode.setChecked(true);
        setDialog();
    }

    //显示对话框
    private void setDialog(){
        LayoutInflater inflater = LayoutInflater.from(MenuActivity.this);
        View view = inflater.inflate(R.layout.home_dialog,null);
        Button button = (Button) view.findViewById(R.id.dialog_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog!=null){
                    alertDialog.cancel();
                    alertDialog.dismiss();//对话框关闭
                }
            }
        });
        showDialog(view);

    }
    //建立对话框
    public void showDialog(View view){
        builder = new AlertDialog.Builder(MenuActivity.this);
        alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        alertDialog.getWindow().setAttributes(p);     //设置生效
    }
    private void initListener() {
        //RadioGroup的点击事件
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(btn_commuite.getId()==checkedId){
                    fist_viewPager.setCurrentItem(0);
                }
                if(btn_lanuage.getId()==checkedId){
                    fist_viewPager.setCurrentItem(1);
                }
                if(btn_mode.getId()==checkedId){
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
                    btn_commuite.setChecked(true);
                }else  if(i==1){
                    btn_lanuage.setChecked(true);
                }else  if(i==2){
                    btn_mode.setChecked(true);
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
