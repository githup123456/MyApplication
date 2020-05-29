package com.example.administrator.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipProfile;
import android.net.sip.SipSession;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Window.VoiceIncomingWindow;

import java.util.logging.Logger;

public class IncomingCallReceiver extends BroadcastReceiver {
    public static SipAudioCall sipAudioCall;
    public String userID;
    @Override
    public void onReceive(final Context context, Intent intent) {
        SipAudioCall incomingCall = null;
        Log.d("TAGqw", "onReceive: 1");
        final MenuActivity answerActivity = (MenuActivity) context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.menu_activity,null);
        try {

            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    super.onRinging(call, caller);
                    try {
                        Log.d("TAGqw", "onReceive: 1");
                        userID = caller.getUserName();
                        call.answerCall(30);
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCalling(SipAudioCall call) {
                    super.onCalling(call);
                    Log.d("TAGqw", "onReceive: 2");

                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    super.onCallEnded(call);
                    Log.d("TAGqw", "onReceive: 3");
                }
            };

            incomingCall = answerActivity.sipManager.takeAudioCall(intent, listener);
            setSipAudioCall(incomingCall);
            setIntent(context,userID,incomingCall,view);
            //showDialog(context,incomingCall);
            initGetSipSession(context,answerActivity,intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //设置sipAudioCall
    private  void setSipAudioCall(SipAudioCall sipAudioCall){
        this.sipAudioCall = sipAudioCall;
        if (sipAudioCall == null)
        Log.d("SipManager",""+sipAudioCall);
    }
    //获取sipAudioCall
    public static SipAudioCall  getSipAudioCall(){
        return sipAudioCall;
    }
    private void setIntent(Context context,String userID,SipAudioCall sipAudioCall,View view){
        VoiceIncomingWindow voiceIncomingWindow = new VoiceIncomingWindow(context,userID,sipAudioCall);
        Activity activity = (Activity ) context;
        voiceIncomingWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }
    private Handler handler = new Handler();
    private void initGetSipSession(final Context context, MenuActivity mainActivity, final Intent intent) {
        try {
            SipSession sessionFor = mainActivity.sipManager.getSessionFor(intent);

            SipSession.Listener listener = new SipSession.Listener(){
                @Override
                public void onCalling(SipSession session) {
                }
                @Override
                public void onCallEnded(final SipSession session) {
                    Logger.getLogger("onCallEnded");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"通话中断",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            };
            sessionFor.setListener(listener);
        } catch (SipException e) {
            e.printStackTrace();
        }
    }
    public void showDialog(Context context,SipAudioCall call){
        final SipAudioCall sipAudioCall = call;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("New Call")
                .setMessage("From " + sipAudioCall.getPeerProfile().getUserName())
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            sipAudioCall.answerCall(30);
                            sipAudioCall.startAudio();
                            sipAudioCall.setSpeakerMode(true);
                            if (sipAudioCall.isMuted()){
                                sipAudioCall.toggleMute();
                            }
                        } catch (SipException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            sipAudioCall.endCall();
                            sipAudioCall.close();
                        } catch (SipException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.create().show();
    }


}
