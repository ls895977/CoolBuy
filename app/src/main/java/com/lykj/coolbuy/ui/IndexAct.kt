package com.lykj.coolbuy.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.just.agentweb.AgentWeb
import com.lykj.coolbuy.MainActivity
import com.lykj.coolbuy.R
import com.lykj.coolbuy.broadcast.StickyEvent
import com.lykj.coolbuy.constants.Constant
import com.lykj.coolbuy.utils.MyUseUtil
import com.lykj.library_lykj.common.BaseAct
import com.lykj.library_lykj.http.ApiCallback
import com.lykj.library_lykj.http.ApiHttp
import com.lykj.library_lykj.http.ApiRequest
import com.lykj.library_lykj.utils.Debug
import com.lykj.library_lykj.utils.TimeUtils
import com.orhanobut.logger.Logger

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject

import java.util.concurrent.TimeUnit

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * <pre>
 * author: Fan
 * time  : 2018/5/17 下午1:08
 * desc  : 广告首页
</pre> *
 */
class IndexAct : BaseAct() {

    private var tvWeather: TextView? = null
    private var tvTemp: TextView? = null
    private var tvLimitFirst: TextView? = null
    private var tvLimitLast: TextView? = null
    private var tvDate: TextView? = null
    private var tvWeek: TextView? = null
    private var tvTime: TextView? = null
    private var myWebView: WebView? = null
    private var index = 0
    private var subscribe: Disposable? = null

    private var mIsFitstIn = true
    private var mRefreshSubscribe: Disposable? = null

    override fun initLayoutId(): Int {
        return R.layout.act_index
    }

    override fun init() {
        hideHeader()
        tvWeather = getView(R.id.head_index_weather)
        tvTemp = getView(R.id.head_index_temp)
        tvLimitFirst = getView(R.id.head_index_limit_first)
        tvLimitLast = getView(R.id.head_index_limit_last)
        tvDate = getView(R.id.head_index_date)
        tvWeek = getView(R.id.head_index_week)
        tvTime = getView(R.id.head_index_time)
        getViewAndClick<View>(R.id.index_more)
        myWebView = getView(R.id.index_webview)
        getTempData()
        getTimeGap()
        EventBus.getDefault().register(this)
    }

    private fun getTempData() {
        val useUtil = MyUseUtil()
        tvLimitFirst!!.text = useUtil.getLimit(0)
        tvLimitLast!!.text = useUtil.getLimit(1)
        tvWeek!!.text = useUtil.getData(0)
        tvTime!!.text = useUtil.getData(1)
        tvDate!!.text = useUtil.getData(2)

        val apiHttp = ApiHttp()
        val url = "https://www.sojson.com/open/api/weather/json.shtml?city=成都"

        apiHttp.getToString(url, ApiRequest.RequestFlag.FLAG1, object : ApiCallback {
            override fun onSuccess(resultData: Any, flag: ApiRequest.RequestFlag) {
                val json = resultData.toString()
                var o: JSONObject? = null
                try {
                    o = JSONObject(json)
                    if (o.has("status") && o.optInt("status") == 200) {
                        val obj = o.optJSONObject("data")
                                .optJSONArray("forecast")
                                .optJSONObject(0)
                        val lowTemp = obj.optString("low").substring(3, 5)
                        val highTemp = obj.optString("high").substring(3, 5)
                        val type = obj.optString("type")

                        tvWeather!!.text = type
                        tvTemp!!.text = "$lowTemp~$highTemp℃"
                    } else {
                        if (index < 3) getTempData()//重新获取，因为出现获取出错的情况
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                index++
            }

            override fun onError(errors: String, flag: ApiRequest.RequestFlag) {
                if (index < 5) getTempData()//重新获取，因为出现获取出错的情况
                index++
            }
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        startAct(MainActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        subscribe = Flowable.interval(5, 5, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    //延迟5秒后，每隔5秒钟获取一次当前的时间
                    //                        Logger.e("----accept----");
                    tvTime!!.text = MyUseUtil().getData(1)
                }

        val web = AgentWeb.with(this)//
                .setAgentWebParent(LinearLayout(context), LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                //                .setReceivedTitleCallback(mCallback)
                //                .setWebChromeClient(mWebChromeClient)
                //                .setWebViewClient(mWebViewClient)
                .createAgentWeb()//
                .ready()
                .go("")

    }

    /**
     * 每天凌晨刷新数据
     */
    private fun getTimeGap() {
        val timeMills = TimeUtils.getCurTimeMills()//获取当前的时间
        val string = TimeUtils.milliseconds2String(timeMills, TimeUtils.DEFAULT_SDF2) + " 23:59:59"
        //获取到当前时间到晚上凌晨的时间段的秒数
        val time = TimeUtils.string2Milliseconds(string, TimeUtils.DEFAULT_SDF1) / 1000 - timeMills / 1000
        //当第一次进入程序的时候
        if (mIsFitstIn) {
            mRefreshSubscribe = Flowable.interval(time, TimeUnit.SECONDS)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mIsFitstIn = false
                        getTempData()
                    }
        } else {
            if (!mRefreshSubscribe!!.isDisposed) mRefreshSubscribe!!.dispose()
            //当再次刷新过后，重新设置任务
            mRefreshSubscribe = Flowable.interval((24 * 60 * 60).toLong(), TimeUnit.SECONDS)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { getTempData() }
        }
    }

    override fun onStop() {
        super.onStop()
        if (!subscribe!!.isDisposed)
            subscribe!!.dispose()
        if (!mRefreshSubscribe!!.isDisposed)
            mRefreshSubscribe!!.dispose()
    }
    //    接收消息
    @org.greenrobot.eventbus.Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN, sticky = true)
    @SuppressLint("JavascriptInterface")
    fun onEvent(event: StickyEvent) {
        if (myWebView != null) {
            myWebView!!.loadUrl(Constant.Url)
            myWebView!!.addJavascriptInterface(this, "android")//添加js监听 这样html就能调用客户端/添加js监听 这样html就能调用客户端
            myWebView!!.webChromeClient = WebChromeClient()
            myWebView!!.webViewClient = WebViewClient()
            val webSettings = myWebView!!.settings
            webSettings.javaScriptEnabled = true//允许使用js
            webSettings.setSupportZoom(true)
            webSettings.builtInZoomControls = true
            Debug.e("-----------------"+Constant.Url)

        }
    }
}