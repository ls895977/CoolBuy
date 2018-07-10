package com.lykj.coolbuy.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lykj.coolbuy.constants.Constant
import com.lykj.library_lykj.utils.Debug
import org.greenrobot.eventbus.EventBus

class CustomBroadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //广播接受
        if (intent.action == ACTION) {
            val url = intent.getStringExtra("url")
            Debug.e("-----$url")
            Constant.Url = url;
            //        发布消息：
            EventBus.getDefault().postSticky(StickyEvent(url))
        }
    }

    companion object {
        private val ACTION = "updateUrl"
    }
}