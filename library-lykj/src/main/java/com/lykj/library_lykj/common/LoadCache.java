package com.lykj.library_lykj.common;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lykj.library_lykj.R;


public class LoadCache {
    public LinearLayout llyLoad;
    public TextView txtMessage;
    public ImageView imgMessage;
    public ProgressBar proLoading;

    public void showLoading(String msg) {
        llyLoad.setVisibility(View.VISIBLE);
        proLoading.setVisibility(View.VISIBLE);
        imgMessage.setVisibility(View.GONE);
        txtMessage.setVisibility(View.VISIBLE);
        txtMessage.setText(msg);
    }

    public void showNoData(String msg, int drawId) {
        llyLoad.setVisibility(View.VISIBLE);
        proLoading.setVisibility(View.GONE);
        imgMessage.setVisibility(View.VISIBLE);
        txtMessage.setVisibility(View.VISIBLE);
        if (msg == null) txtMessage.setText(R.string.hint_nodata);
        else txtMessage.setText(msg);
        imgMessage.setImageResource(drawId);
    }

    public void showDynamicData(String msg, int drawId, Context context) {
        llyLoad.setVisibility(View.VISIBLE);
        proLoading.setVisibility(View.GONE);
        imgMessage.setVisibility(View.VISIBLE);
        txtMessage.setVisibility(View.VISIBLE);
        txtMessage.setText(msg);
        txtMessage.setTextSize(14);
//        Drawable drawable = context.getResources().getDrawable(drawId);
        imgMessage.setBackgroundResource(drawId);
        AnimationDrawable drawable1 = (AnimationDrawable) imgMessage.getBackground();
        drawable1.start();
    }

}