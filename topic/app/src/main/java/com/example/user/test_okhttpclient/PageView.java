package com.example.user.test_okhttpclient;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class PageView extends LinearLayout {
    public PageView(Context context){
        super(context);
    }

    public abstract void refreshView();
}
