package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class WalkieTalkieActivity extends Activity {
    private EditText ed_username, ed_password;
    private Button btn_sip_login;
    private static SipProfile sip_Profile;
    private static final String[] VIDEO_PERMISSIONS = {Manifest.permission.USE_SIP, Manifest.permission.RECORD_AUDIO, Manifest.permission_group.PHONE, Manifest.permission.CAMERA};

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

    public static void state(Context context) {
        if (SipManager.isApiSupported(context)) {
        }

    }

    public static SipManager sipManager;
    public SipProfile sipProfile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sip_answer_activity);
        //权限
        requestPermission();
        ed_username = (EditText) findViewById(R.id.sip_ed_username);
        ed_password = (EditText) findViewById(R.id.sip_ed_password);
        SharedPreferences shared = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
        if (shared.getString("namePref", "") != null) {
            ed_username.setText(shared.getString("namePref", ""));
            ed_password.setText(shared.getString("passPref", ""));
        }
        btn_sip_login = (Button) findViewById(R.id.sip_btnn_login);
        btn_sip_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, domain;
                username = ed_username.getText().toString();
                password = ed_password.getText().toString();
                domain = "192.168.1.210";
                SharedPreferences sharedPreferences = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("namePref", username);
                editor.putString("passPref", password);
                editor.putString("domainPref", domain);
                editor.commit();
                SipProfile sipProfile = null;
                sipManager = SipManager.newInstance(getApplicationContext());
                showZhuce(WalkieTalkieActivity.this);
            }
        });
    }

    public void showZhuce(final Context context) {
        if (sipManager == null) {
            sipManager = SipManager.newInstance(context);
        }
        if (sipManager == null) {
            Toast.makeText(WalkieTalkieActivity.this,"手机不支持Sip协议",Toast.LENGTH_SHORT).show();
            return;
        }
        if (sipProfile != null) {
            closeLocalProfile(sipManager, sipProfile);
        }
        SharedPreferences preferences = getSharedPreferences("sip_inmation", Context.MODE_PRIVATE);
        final String userName,passWord,domain;
        userName = preferences.getString("namePref","");
        passWord = preferences.getString("passPref","");
        domain  = preferences.getString("domainPref","");

            Log.d("SipMainActivity", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
        if (userName.length() == 0 && domain.length() == 0 && passWord.length() == 0) {
            //showDialog(Update_Setting_Dialog);
            return;
        }
        try {
            //添加一个本地的过滤器，用于接受电话
            //构造一个PendingIntent对象,这样当sip Service收到一个通话请求时,
            //SipService会调用PendingIntent的send方法发送相应广播消息给调用者,也就是当前的SipProfile对象.
            Log.d("SipMainActivity1", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
            SipProfile.Builder sip_profile = new SipProfile.Builder(userName, domain);
            sip_profile.setPassword(passWord);
            sipProfile = sip_profile.build();
            Log.d("SipMainActivity2", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
            Intent i = new Intent();
            i.setAction("android.SipTest.INCOMING_CALL");
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, Intent.FILL_IN_DATA);
            SipRegistrationListener registrationListener = new SipRegistrationListener() {
                @Override
                public void onRegistering(String localProfileUri) {
                    Log.d("SipMainActivity123", "" + userName + ";  ;" + domain + " ;  ;" + passWord);

                }

                @Override
                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    Log.d("SipMainActivity_log+", ";" + localProfileUri + "; ; ;" + expiryTime + ";  ;");
                    Intent intent_register = new Intent(context, MenuActivity.class);
                    intent_register.putExtra("string_url", userName + "@" + domain);
                    context.startActivity(intent_register);
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                    Log.d("SipMainActivity_log=", ";" + localProfileUri + "; ; ;" + errorCode + ";  ;" + errorMessage);

                    Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();

                }
            };
            Log.d("SipMainActivity3", "" + ";  ;" + domain + " ;  ;" + passWord);
            setSip_Profile(sipProfile);
            sipManager.open(sipProfile, pi, null);
            sipManager.setRegistrationListener(sipProfile.getUriString(), registrationListener);
            setSipManager(sipManager);
            Log.d("SipMainActivity12", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
        } catch (ParseException pe) {
            Log.d("SipMainActivity+erro", "" + pe);
        } catch (SipException se) {
            Log.d("SipMainActivity+erro", "" + se);
        } catch (java.text.ParseException e) {
            Log.d("SipMainActivity+erro3", "" + e);
            e.printStackTrace();
        }
    }
    public void setSip_Profile(SipProfile sipProfile){
        this.sip_Profile = sipProfile;
    }


    public static SipProfile getSip_Profile(){
        return sip_Profile;
    }
    public void setSipManager(SipManager sipManager){
        this.sipManager = sipManager;
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeLocalProfile(sipManager, sip_Profile);
        WalkieTalkieActivity.this.finish();
    }

    public void closeLocalProfile(SipManager sipManager, SipProfile sipProfile) {
        if (sipManager == null) {
            return;
        }
        try {
            if (sipProfile != null) {
                sipManager.close(sipProfile.getUriString());
            }
        } catch (Exception ee) {
            Log.d("MainActivity/onDestroy", "Failed to close local profile.", ee);
        }
    }

    //获取SipManager
    public SipManager getSipManager() {
        return sipManager;
    }
}
