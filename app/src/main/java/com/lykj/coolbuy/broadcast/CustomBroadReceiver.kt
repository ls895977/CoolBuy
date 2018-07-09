package com.lykj.coolbuy.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lykj.coolbuy.constants.Constant
import com.lykj.library_lykj.utils.Debug
class CustomBroadReceiver : BroadcastReceiver() {
    private var onBackMM: WordCallBack? = null
     fun initData(callBack : WordCallBack){
                 this.onBackMM = callBack
    }
    override fun onReceive(context: Context, intent: Intent) {
        //广播接受
        if (intent.action == ACTION) {
            val url = intent.getStringExtra("url")
            Debug.e("-----$url")
            Constant.Url = url;
            onBackMM?.onBack();
        }
    }

    companion object {
        private val ACTION = "updateUrl"
    }
}