package com.lykj.coolbuy.ui

import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.just.agentweb.AgentWeb
import com.luck.picture.lib.rxbus2.RxBus
import com.lykj.coolbuy.R
import com.lykj.coolbuy.constants.Constant
import com.lykj.library_lykj.common.BaseFgt
import com.lykj.library_lykj.utils.MyToast

/**
 *<pre>
 *     author: Fan
 *     time  : 2018/5/8 下午4:12
 *     desc  : 政府公告/办事指南/办事进度/公积金查询
 *</pre>
 */
class WebViewFgt : BaseFgt() {
    private var webView: AgentWeb?= null

    override fun initLayoutId(): Int {
        return R.layout.fgt_web_view
    }

    override fun init() {
        hideHeader()
        val type = arguments?.getInt("type")
        getViewAndClick<ImageView>(R.id.web_back)
        val textView = getView<TextView>(R.id.web_title)

        var url = ""

        when(type){
            0 ->{//政府公告
                url = "http://dzzwdt.chengdu.gov.cn/portal/index_pc10000.jsp"
                textView.text = "政府公告"
                getView<View>(R.id.web_cover).visibility = View.GONE
                val layout: RelativeLayout = getView(R.id.layout)
                val params = RelativeLayout.LayoutParams(-1, -1)
                params.topMargin = 60
                layout.layoutParams = params
            }
            1 ->{//办事指南
                url = "http://egov.chengdu.gov.cn/weizhan/QueryProceedingTypeAction.form"
                textView.text = "办事指南"
            }
            2 ->{//办事进度
                url = "http://egov.chengdu.gov.cn/weizhan/QueryDotransactionAction.form"
                textView.text = "办事进度"
            }
            3 ->{//公积金查询
                url = "http://scsjgjj.com/wx/User/PersonalUserLogin"
                textView.text = "公积金查询"
                getView<View>(R.id.web_cover).visibility = View.GONE
                val layout: RelativeLayout = getView(R.id.layout)
                val params = RelativeLayout.LayoutParams(-1, -1)
                params.topMargin = 150
                layout.layoutParams = params
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

        val settings = webView!!.webCreator.webView.settings
        //支持javascript
        settings.setJavaScriptEnabled(true)
        // 设置可以支持缩放
//        settings.setSupportZoom(true)
        // 设置出现缩放工具
//        settings.builtInZoomControls = true
        //扩大比例的缩放
        settings.useWideViewPort = true
        //自适应屏幕
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        settings.loadWithOverviewMode = true

        val webView = webView?.webCreator?.webView
        webView?.setOnTouchListener({ _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    RxBus.getDefault().send(Constant.RX_CODE_RESET_TIME, "-1")
                }
            }
            false })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.web_back) {
            val webView1 = webView!!.webCreator.webView
            if (webView1.canGoBack()) webView1.goBack()
            else RxBus.getDefault().send(Constant.RX_CODE_JUMP, "0back")
        }
    }
}