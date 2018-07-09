package com.lykj.coolbuy.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.lykj.coolbuy.MainActivity
import com.lykj.coolbuy.constants.Constant

import com.lykj.library_lykj.utils.Debug

class CustomBroadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //广播接受
        if (intent.action == ACTION) {
            val url = intent.getStringExtra("url")
            Debug.e("-----$url")
            Constant.Url = url;
            Toast.makeText(context, "接到一个广播！！！", Toast.LENGTH_LONG).show()

            val intent1 = Intent()
            intent1.action = "ll"
            context.sendBroadcast(intent1)
        }
    }

    companion object {
        private val ACTION = "updateUrl"
    }
}