package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.myapplication.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class SipAnswerActivity extends Activity {


    private EditText ed_username,ed_password,ed_domain;
    private Button btn_sip_login;

    private static final String[] VIDEO_PERMISSIONS = {Manifest.permission.USE_SIP,Manifest.permission.RECORD_AUDIO,Manifest.permission_group.PHONE,Manifest.permission.CAMERA};
    //申请权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_SIP) != PackageManager.PERMISSION_GRANTED) {
                // 检查权限状态
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.USE_SIP)) {
                    //  用户彻底拒绝授予权限，一般会提示用户进入设置权限界面
                } else {
                    //  用户未彻底拒绝授予权限
                    ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, 1);
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    // 申请成功
                } else {
                    // 申请失败
                }
            }
        }
    }
    public static  void state(Context context) {
        if (SipManager.isApiSupported(context)) {
        }

    }
    public static SipManager sipManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sip_answer_activity);
        //权限
        requestPermission();
        ed_username = (EditText)findViewById(R.id.sip_ed_username);
        ed_password = (EditText)findViewById(R.id.sip_ed_password);
        ed_domain   = (EditText)findViewById(R.id.sip_ed_domain);
        SharedPreferences shared = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
        if(shared.getString("namePref","")!=null){
            ed_username.setText(shared.getString("namePref",""));
            ed_password.setText(shared.getString("passPref",""));
            ed_domain.setText(shared.getString("domainPref",""));
        }
        btn_sip_login = (Button)findViewById(R.id.sip_btnn_login);
        btn_sip_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  username, password, domain;
                username = ed_username.getText().toString();
                password = ed_password.getText().toString();
                domain  =  ed_domain .getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("namePref",username);
                editor.putString("passPref",password);
                editor.putString("domainPref",domain);
                editor.commit();
                SipProfile sipProfile=null;
                sipManager = SipManager.newInstance(getApplicationContext());
                MainActivity.showZhuce(SipAnswerActivity.this,sipProfile,username,domain,password,sipManager);
                Intent intent = new Intent(SipAnswerActivity.this,MenuActivity.class);
                intent.putExtra("string_url",username+"@"+domain);
                startActivity(intent);
            }
        });
    }
    //获取SipManager
    public SipManager getSipManager(){
        return sipManager;
    }
}
