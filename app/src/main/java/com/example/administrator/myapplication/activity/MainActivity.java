package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.net.ParseException;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.administrator.myapplication.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends Activity {
    public static SipManager sipManager;
    private static String sipAddress = null;
    public static SipAudioCall sipAudioCall;
    public static SipProfile sipProfile;

    private static final String[] VIDEO_PERMISSIONS = {Manifest.permission.USE_SIP,Manifest.permission.RECORD_AUDIO,Manifest.permission_group.PHONE};
    private IncomingCallReceiver incomingCallReceiver;
    private static String userName;
    private String passWord;
    private String domain;
    public static final String STATE_CONNECTED = "Connected";
    public static final String STATE_CONNECTED_FAILURE = "Register failed, try to refresh";
    public static final String STATE_CONNECTING = "Connecting...";
    public static final String STATE_CALLING = "Calling...";
    private static final int Call_Address = 1;
    private static final int Set_Auth_info = 2;
    private static final int Update_Setting_Dialog = 3;
    private static final int Hang_up = 4;
    private ToggleButton toggleButton;
    private Button button,y_b;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        sipManager = SipManager.newInstance(getApplicationContext());
        toggleButton = (ToggleButton) findViewById(R.id.toggle_btn);
        toggleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sipAudioCall == null) {
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && sipAudioCall != null && sipAudioCall.isMuted()) {
                    Log.d("togglebutton","dakai");
                    toggleButton.setBackgroundResource(R.drawable.yuyin_open);
                    sipAudioCall.toggleMute();
                } else if (event.getAction() == MotionEvent.ACTION_UP && !sipAudioCall.isMuted()) {
                    toggleButton.setBackgroundResource(R.drawable.yunyin_close);
                    sipAudioCall.toggleMute();
                }
                return false;
            }
        });
        button = (Button) findViewById(R.id.zhuce);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeManager();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.SipTest.INCOMING_CALL");//
        incomingCallReceiver = new IncomingCallReceiver();
        this.registerReceiver(incomingCallReceiver, intentFilter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int aaa = getDefaultEncodingFormat();
        Log.d("taggg","adasd:"+aaa);
    }
    public static final int getDefaultEncodingFormat() {
        return AudioFormat.ENCODING_PCM_16BIT;
    }

    protected void onResume() {
        super.onResume();
        initializeManager();
    }

    public  void initializeManager() {
        if (sipManager == null) {
            Log.d("", "sipManager.toString()");
            sipManager = SipManager.newInstance(getApplicationContext());
        }
        Log.d("SipMainActivity123456", "");
        initializeLocalProfile();
    }

    public void initializeLocalProfile() {
        if (sipManager == null) {
            Log.d("SipMainActivity23456", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
            return;
        }
        if (sipProfile != null) {
            closeLocalProfile(sipManager,sipProfile);
        }
        Log.d("SipMainActivity126", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
        SharedPreferences sharedPreferences = this.getSharedPreferences("sip_inmation",Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("namePref", "");
        domain = sharedPreferences.getString("domainPref", "");
        passWord = sharedPreferences.getString("passPref", "");
        Log.d("SipMainActivity556", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
        showZhuce(MainActivity.this,sipProfile,userName,domain,passWord,sipManager);
    }
    public  SipAudioCall getSipcall(){
        return sipAudioCall;
    }

    public static void showZhuce(Context context,SipProfile sipProfile,final String userName, final String domain, final String passWord,SipManager sipManager) {
        if (sipManager==null){
            sipManager = SipManager.newInstance(context);
        }
        if (sipManager==null){
            return;
        }
        if (sipProfile!=null){
            closeLocalProfile(sipManager,sipProfile);
        }

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
                    Log.d("SipMainActivity_log+",";"+localProfileUri+"; ; ;"+expiryTime+";  ;");
                }
                @Override
                public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                    Log.d("SipMainActivity_log=",";"+localProfileUri+"; ; ;"+errorCode+";  ;"+errorMessage);
                }
            };
            Log.d("SipMainActivity3", ""  + ";  ;" + domain + " ;  ;" + passWord);
            sipManager.open(sipProfile, pi, null);
            //sipManager.register(sipProfile,20,registrationListener);
            sipManager.setRegistrationListener(sipProfile.getUriString(),registrationListener);
            //createProxyConfigAndLeaveAssistant(true);
            Log.d("SipMainActivity12", "" + userName + ";  ;" + domain + " ;  ;" + passWord);
        } catch (ParseException pe) {
            updateStatus("Connection Error.1");
            Log.d("SipMainActivity+erro",""+pe);
        } catch (SipException se) {
            Log.d("SipMainActivity+erro",""+se);
            updateStatus("Connection error.2");
        } catch (java.text.ParseException e) {
            Log.d("SipMainActivity+erro3",""+e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Call_Address, 0, "Call someone");
        menu.add(0, Set_Auth_info, 0, "Edit your SIP Info.");
        menu.add(0, Hang_up, 0, "End Current Call.");
        return true;
    }

    @SuppressLint("LongLogTag")
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case Call_Address: {
                showDialog(Call_Address);
                break;
            }
            case Set_Auth_info: {
                updatePreferences();
                break;
            }
            case Hang_up: {
                if (sipAudioCall != null) {
                    try {
                        sipAudioCall.endCall();
                    } catch (SipException e) {
                        Log.d("MainActivity/onOptionsItemSelected",
                                "Error ending call.", e);
                        sipAudioCall.close();

                    }
                }
                break;
            }
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case Call_Address: {
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                final View view = layoutInflater.inflate(R.layout.call_dialog, null);
                return new AlertDialog.Builder(this)
                        .setTitle("Call Someone.")
                        .setView(view)
                        .setPositiveButton(
                                android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        EditText textField = (EditText)
                                                (view.findViewById(R.id.call_dialog_ed));
                                        sipAddress = textField.getText().toString();
                                        String status = null;
                                        initiateCall(sipAddress,sipAudioCall,sipManager,sipProfile.getUriString(),status);
                                    }
                                })
                        .setNegativeButton(
                                android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Noop.
                                    }
                                })
                        .create();
            }
            case Update_Setting_Dialog: {
                return new AlertDialog.Builder(this)
                        .setTitle("Please update your SIP Account Settings.")
                        .setPositiveButton(
                                android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        updatePreferences();
                                    }
                                })
                        .setNegativeButton(
                                android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // Noop.
                                    }
                                })
                        .create();
            }
        }
        return null;
    }
    @SuppressLint("LongLogTag")
    public static void initiateCall(String sipAddress, SipAudioCall sipAudioCall, SipManager sipManager, String url, final String status) {
        updateStatus(sipAddress);
        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onCallEstablished(SipAudioCall call) {
                    super.onCallEstablished(call);
                    call.startAudio();
                    call.setSpeakerMode(true);
                    call.toggleMute();
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    super.onCallEnded(call);
                    Log.d("TAG","Ready:");
                    Log.d("SipMainActivity1", "ready");
                }

                @Override
                public void onCalling(SipAudioCall call) {
                    super.onCalling(call);
                    if (!call.isMuted()){
                        call.toggleMute();
                    }
                }
            };

            Log.d("SipMainActivity1", "s "+sipAddress);
            sipAudioCall = sipManager.makeAudioCall(url, sipAddress, listener, 30);
        } catch (Exception e) {

            Log.d("SipMainActivity1", "s "+e);
            if (sipProfile != null) {
                try {
                    Log.d("SipMainActivity1", "s "+sipAddress);
                    sipManager.close(sipProfile.getUriString());
                } catch (Exception ee) {

                    Log.d("SipMainActivity1", "s "+sipAddress);
                    ee.printStackTrace();
                }
            }
            if (sipAudioCall != null) {

                Log.d("SipMainActivity1", "s "+sipAddress);
                sipAudioCall.close();
            }

        }
    }

    public static void updateStatus(final String status) {
        /*this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView lable_textview = (TextView) findViewById(R.id.tv_label);
                lable_textview.setText(status);
            }
        });*/
    }

    public void updateStatus(SipAudioCall call) {
        String useName = call.getPeerProfile().getDisplayName();
        if (useName == null) {
            useName = call.getPeerProfile().getUserName();
        }
        updateStatus(useName + "@" + call.getPeerProfile().getSipDomain());

    }

    public static void closeLocalProfile(SipManager sipManager, SipProfile sipProfile) {
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


    public void updatePreferences() {
        Intent settingsActivity = new Intent(getBaseContext(),
                SipAnswerActivity.class);
        startActivity(settingsActivity);
    }

    public  void createProxyConfigAndLeaveAssistant(boolean isGenericAccount) {
        String uri = "";

        SharedPreferences sharedPreferences = getSharedPreferences("Sip_BaoCun",Context.MODE_PRIVATE);
        boolean useLinphoneDefaultValues =
                getString(R.string.uri).equals(sharedPreferences.getString("domainPref",""));

        if (isGenericAccount) {
            if (useLinphoneDefaultValues) {
                Log.d(
                        "","[Assistant] Default domain found for generic connection, reloading configuration");

            } else {
            }
        }



        if (isGenericAccount) {
            if (useLinphoneDefaultValues) {
                // Restore default values

            } else {
                // If this isn't a sip.linphone.org account, disable push notifications and enable
                // service notification, otherwise incoming calls won't work (most probably)

                Log.w(
                        "sa", "[Assistant] Unknown domain used, push probably won't work, enable service mode");
            }
        }


        goToLinphoneActivity();

    }

    void goToLinphoneActivity() {
        boolean needsEchoCalibration =
                false;
        boolean echoCalibrationDone = false;
        try {
            needsEchoCalibration = SipManager.newInstance(this).isRegistered(sipProfile.getUriString());
            echoCalibrationDone = sipManager.isOpened(sipProfile.getUriString());
        } catch (SipException e) {
            e.printStackTrace();
        }

        Log.d("","[Assistant] Echo cancellation calibration required ? "+ needsEchoCalibration+ ", already done ? "+ echoCalibrationDone);

        Intent intent;
        if (needsEchoCalibration && echoCalibrationDone) {
            intent = new Intent(this, SipAnswerActivity.class);
            startActivity(intent);
        } else {
            /*boolean openH264 = LinphonePreferences.instance().isOpenH264CodecDownloadEnabled();
            boolean codecFound =
                    LinphoneManager.getInstance().getOpenH264DownloadHelper().isCodecFound();
            boolean abiSupported =
                    Version.getCpuAbis().contains("armeabi-v7a")
                            && !Version.getCpuAbis().contains("x86");
            boolean androidVersionOk = Version.sdkStrictlyBelow(Build.VERSION_CODES.M);

            if (openH264 && abiSupported && androidVersionOk && !codecFound) {
                intent = new Intent(this, OpenH264DownloadAssistantActivity.class);
            } else {*/
            Log.d("Sip","SipMainActivity00"+ needsEchoCalibration+";;;"+echoCalibrationDone);
            // }
        }
    }
}
