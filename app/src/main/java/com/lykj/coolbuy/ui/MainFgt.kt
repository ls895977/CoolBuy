package com.lykj.coolbuy.ui

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.luck.picture.lib.rxbus2.RxBus
import com.luck.picture.lib.rxbus2.Subscribe
import com.luck.picture.lib.rxbus2.ThreadMode
import com.lykj.coolbuy.R
import com.lykj.coolbuy.constants.Constant
import com.lykj.coolbuy.utils.MyUseUtil
import com.lykj.library_lykj.common.BaseFgt
import com.lykj.library_lykj.http.ApiCallback
import com.lykj.library_lykj.http.ApiHttp
import com.lykj.library_lykj.http.ApiRequest
import com.orhanobut.logger.Logger
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 *<pre>
 *     author: Fan
 *     time  : 2018/5/7 下午3:28
 *     desc  : 主fragment
 *</pre>
 */
class MainFgt : BaseFgt() {

    private lateinit var mAddress: TextView

    override fun initLayoutId(): Int {
        return R.layout.fgt_main
    }

    override fun init() {
        hideHeader()

        getViewAndClick<ImageView>(R.id.iv_government)
        getViewAndClick<ImageView>(R.id.iv_market)
        getViewAndClick<ImageView>(R.id.iv_activity)
        getViewAndClick<ImageView>(R.id.iv_service)
        mAddress = getView(R.id.address)

        RxBus.getDefault().register(this)
        if (!TextUtils.isEmpty(Constant.ADDRESS)) mAddress.text = Constant.ADDRESS
    }

    @Subscribe(threadMode = ThreadMode.MAIN, code = Constant.RX_CODE_GET_ADDRESS)
    fun getLocationAddress(address: String){
        mAddress.text = address
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when {
            v.id == R.id.iv_government -> {//电子政务
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "0")
            }
            v.id == R.id.iv_market -> {//库购商城
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "4")
            }
            v.id == R.id.iv_activity -> {//活动促销
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "3")
            }
            v.id == R.id.iv_service -> {//便民服务
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "2")
            }
        }
    }

    override fun onDestroy() {
        if (RxBus.getDefault().isRegistered(this))
            RxBus.getDefault().unregister(this)
        super.onDestroy()
    }

}