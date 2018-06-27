package com.lykj.coolbuy.ui

import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.luck.picture.lib.rxbus2.RxBus
import com.lykj.coolbuy.R
import com.lykj.coolbuy.constants.Constant
import com.lykj.library_lykj.common.BaseFgt
import com.lykj.library_lykj.utils.MyToast

/**
 *<pre>
 *     author: Fan
 *     time  : 2018/5/8 下午1:40
 *     desc  : 便民服务
 *</pre>
 */
class ServiceDetailFgt : BaseFgt() {
    var webView: AgentWeb ?= null

    override fun initLayoutId(): Int {
        return R.layout.fgt_service_detail
    }

    override fun init() {
        hideHeader()
        getViewAndClick<ImageView>(R.id.service_back)

        val flag = arguments?.getInt("flag", 0)

        var url = "https://p.10086.cn/jf/publicutilitieZhtygj/zhtygjIndexhbgzhhtml5.htm?Token=cddf662897d2a88fb46b968e03f18c32"

        when(flag){
            0 -> {//电
                url = "https://p.10086.cn/jf/publicutilitieZhtygj/zhtygjinputnumber.htm?pversion=hbgzhhtml5&areaid=280&area=280&projectid=002"
            }
            1 -> {//气
                url = "https://p.10086.cn/jf/publicutilitieZhtygj/zhtygjinputnumber.htm?pversion=hbgzhhtml5&areaid=280&area=280&projectid=003"
            }
            2 -> {//水
                url = "https://p.10086.cn/jf/publicutilitieZhtygj/zhtygjinputnumber.htm?pversion=hbgzhhtml5&areaid=280&area=280&projectid=001"
            }
        }

        webView = AgentWeb.with(this)//
                .setAgentWebParent(getView(R.id.layout), LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                //                .setReceivedTitleCallback(mCallback)
                //                .setWebChromeClient(mWebChromeClient)
                //                .setWebViewClient(mWebViewClient)
                .createAgentWeb()//
                .ready()
                .go(url)

        val webView = webView?.webCreator?.webView
        webView?.setOnTouchListener({ _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> RxBus.getDefault().send(Constant.RX_CODE_RESET_TIME, "-1")
            }
            false })

    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.service_back) {
            val webView1 = webView!!.webCreator.webView
            if (webView1.canGoBack()) webView1.goBack()
            else RxBus.getDefault().send(Constant.RX_CODE_JUMP, "2back")
        }
    }


}