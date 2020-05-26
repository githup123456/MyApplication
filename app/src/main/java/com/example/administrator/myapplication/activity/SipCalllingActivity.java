package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.MainActivity;

import java.io.Serializable;
import java.util.logging.Logger;

public class SipCalllingActivity extends Activity implements View.OnClickListener {
    private TextView tv_sip_phon_number;
    private Button btn_call_on, btn_call_off;
    private ToggleButton toggleButton;
    private PowerManager.WakeLock wakeLock;
    public MainActivity mainActivity;
    public SipAudioCall sipAudioCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sip_calling_activity);

        Intent call_intent = getIntent();
        String phone_number = call_intent.getStringExtra("phone_nuber");
        String panduan = call_intent.getStringExtra("panduan");
        Serializable serializable = call_intent.getSerializableExtra("sip_call");
        sipAudioCall = (SipAudioCall) serializable;
        btn_call_off = (Button) findViewById(R.id.sip_call_btn_call_off);
        btn_call_on = (Button) findViewById(R.id.sip_call_btn_call_on);
        toggleButton = (ToggleButton) findViewById(R.id.sip_call_btn_yunyin);
        if (panduan != null) {
            btn_call_on.setVisibility(View.GONE);
            toggleButton.setVisibility(View.VISIBLE);
        }
        tv_sip_phon_number = (TextView) findViewById(R.id.sip_call_tv_phone_number);
        tv_sip_phon_number.setText(phone_number);
        mainActivity = new MainActivity();
        int aaa = getDefaultEncodingFormat();
        Log.d("taggg","adasd:"+aaa);
    }
    public static final int getDefaultEncodingFormat() {
        return AudioFormat.ENCODING_PCM_16BIT;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        btn_call_on.setOnClickListener(this);
        btn_call_off.setOnClickListener(this);
        toggleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mainActivity.sipAudioCall == null) {
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && mainActivity.sipAudioCall != null && mainActivity.sipAudioCall.isMuted()) {
                    Log.d("togglebutton", "dakai");
                    toggleButton.setBackgroundResource(R.drawable.yuyin_open);
                    mainActivity.sipAudioCall.toggleMute();
                } else if (event.getAction() == MotionEvent.ACTION_UP && !mainActivity.sipAudioCall.isMuted()) {
                    toggleButton.setBackgroundResource(R.drawable.yunyin_close);
                    mainActivity.sipAudioCall.toggleMute();
                }
                return false;
            }
        });
    }
    private Handler handler = new Handler();
    private void initGetSipSession(final Context context, MainActivity mainActivity, Intent intent) {
        try {
            SipSession sessionFor =mainActivity.sipManager.getSessionFor(intent);

            SipSession.Listener listener = new SipSession.Listener(){
                @Override
                public void onCalling(SipSession session) {
                    Logger.getLogger("onCalling");
                }
                @Override
                public void onCallEnded(SipSession session) {
                    Logger.getLogger("onCallEnded");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"通话通断",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
            sessionFor.setListener(listener);
        } catch (SipException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sip_call_btn_call_on: {
                try {
                    sipAudioCall.answerCall(30);
                    sipAudioCall.startAudio();
                    sipAudioCall.setSpeakerMode(true);
                    if (sipAudioCall.isMuted()) {
                        sipAudioCall.toggleMute();
                    }
                } catch (SipException e) {
                    e.printStackTrace();
                }
                btn_call_on.setVisibility(View.GONE);
                toggleButton.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.sip_call_btn_call_off: {
                try {
                    sipAudioCall.endCall();
                    sipAudioCall.close();
                } catch (SipException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            }
            default:
                break;
        }
    }
}
