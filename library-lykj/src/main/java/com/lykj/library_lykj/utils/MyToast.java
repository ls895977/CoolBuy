package com.lykj.library_lykj.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lykj.library_lykj.R;

public class MyToast {

    public static void show(Context context, String msg) {
        Toast toast = new Toast(context);
        View v = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView txtMsg = (TextView) v.findViewById(R.id.txtMsg);
        txtMsg.setText(msg == null ? "错误" : msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(v);
        toast.show();
    }

    public static void show(Context context, View v) {
        Toast toast = new Toast(context);
        toast.setView(v);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(v);
        toast.show();
    }

    public static void showBar(View container, String text){
        Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }
}