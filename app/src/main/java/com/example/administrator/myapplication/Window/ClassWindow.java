package com.example.administrator.myapplication.Window;

import android.content.Context;
import android.widget.PopupWindow;

public class ClassWindow extends PopupWindow {
    public ClassWindow(Context context){
        super(context);
    }


    public void setDismiss(PopupWindow popupWindow) {
        if (popupWindow!=null){
            popupWindow.dismiss();
        }

    }
}
