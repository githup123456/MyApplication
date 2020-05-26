package com.example.administrator.myapplication.fragment;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.View;
import android.view.animation.Animation;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.workgroup.packet.MonitorPacket;

//消息
public class MessageFragment extends Fragment {
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }

}
