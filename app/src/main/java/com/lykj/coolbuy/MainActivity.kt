package com.lykj.coolbuy

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.amap.api.location.AMapLocation
import com.luck.picture.lib.rxbus2.RxBus
import com.luck.picture.lib.rxbus2.Subscribe
import com.luck.picture.lib.rxbus2.ThreadMode
import com.lykj.coolbuy.constants.Constant
import com.lykj.coolbuy.ui.*
import com.lykj.library_lykj.common.BaseAct
import com.lykj.library_lykj.common.BaseFgt
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.lykj.coolbuy.view.CountDownPop
import com.orhanobut.logger.Logger


class MainActivity : BaseAct(), AMapLocationListener {

    private lateinit var fgtList :ArrayList<BaseFgt>
    private var mIsBack: Long = 0    //时间计数
    private var mInterval: Long = 10 //返回主界面的阙值
    private var mShowPop: Long = 5   //显示的倒计时

    override fun initLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        hideHeader()

        startLocation()

        supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, MainFgt())
                .show(MainFgt())
                .commit()

        RxBus.getDefault().register(this)
        fgtList = ArrayList()
        fgtList.add(MainFgt())

        Flowable.interval( 1, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (mIsBack == mInterval) this@MainActivity.finish()
                    if (mInterval - mIsBack == mShowPop) {
                        val popWin = CountDownPop(context, mShowPop)
                        popWin.showPopWin(this@MainActivity)
                    }
                }

        Flowable.interval( 1, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mIsBack++
                }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, code = Constant.RX_CODE_JUMP)
    fun jump2Fragment(type: String){
        mIsBack = 0
        val transaction = supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        var fgt :BaseFgt = MainFgt()
//        Logger.e("-----jump2Fragment-----${fgtList!!.size}")
        when(type){
            "0" -> {//跳转到电子政务
                fgt = GovernmentFgt()
                fgtList.add(fgt)
            }
            "0back" -> {//返回到电子政务
                fgt = GovernmentFgt()
                fgtList.removeAt(fgtList.size - 1)
            }
            "1" -> {//返回到到首页
                fgt = MainFgt()
                fgtList.removeAt(fgtList.size - 1)
            }
            "2" -> {//跳转到便民服务
                fgt = ServiceFgt()
                fgtList.add(fgt)
            }
            "2back" -> {//返回到便民服务
                fgt = ServiceFgt()
                fgtList.removeAt(fgtList.size - 1)
            }
            "3" -> {//跳转到活动促销
                fgt = ActivityFgt()
                fgtList.add(fgt)
            }
            "4" -> {//跳转到商城
                fgt = MarketFgt()
                fgtList.add(fgt)
            }
            "notice" -> {//跳转到政府公告
                fgt = WebViewFgt()
                val bundle = Bundle()
                bundle.putInt("type", 0)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }"guide" -> {//跳转到办事指南
                fgt = WebViewFgt()
                val bundle = Bundle()
                bundle.putInt("type", 1)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }
            "do" -> {//跳转到办事进度
                fgt = WebViewFgt()
                val bundle = Bundle()
                bundle.putInt("type", 2)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }
            "search" -> {//跳转到公积金查询
                fgt = WebViewFgt()
                val bundle = Bundle()
                bundle.putInt("type", 3)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }
            "electric" -> {//跳转到缴电费
                fgt = ServiceDetailFgt()
                val bundle = Bundle()
                bundle.putInt("flag", 0)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }
            "gas" -> {//跳转到缴气费
                fgt = ServiceDetailFgt()
                val bundle = Bundle()
                bundle.putInt("flag", 1)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }
            "water" -> {//跳转到缴水费
                fgt = ServiceDetailFgt()
                val bundle = Bundle()
                bundle.putInt("flag", 2)
                fgt.arguments = bundle
                fgtList.add(fgt)
            }
        }

        transaction.replace(R.id.frame_layout, fgt)
                .commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, code = Constant.RX_CODE_RESET_TIME)
    fun resetTime(type: String){
        mIsBack = 0
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsBack = 0
//                MyToast.show(context, "000")
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startLocation(){
        val locationClient = AMapLocationClient(this)
        val clientOption = AMapLocationClientOption()
        //获取一次定位结果：
        //该方法默认为false。
        clientOption.isOnceLocation = true
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        clientOption.isOnceLocationLatest = true
        //设置是否返回地址信息（默认返回地址信息）
        clientOption.isNeedAddress = true
        //设置高精度模式
        clientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationClient.setLocationOption(clientOption)
        locationClient.setLocationListener(this)
        locationClient.startLocation()
    }

    override fun onLocationChanged(location: AMapLocation?) {
        if (location != null) {
            if (location.errorCode == 0) {
                //可在其中解析amapLocation获取相应内容。
                val district = location.district//城区信息
                val street = location.street//街道信息
                val streetNum = location.streetNum//街道门牌号信息
                Logger.e("--------$district, $street, $streetNum")
                Constant.ADDRESS = "$district$street$streetNum"
                RxBus.getDefault().send(Constant.RX_CODE_GET_ADDRESS, Constant.ADDRESS)
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + location.errorCode + ", errInfo:"
                        + location.errorInfo)
//                MyToast.show(context, "定位失败")
            }
        }else{
//            MyToast.show(context, "定位失败")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (RxBus.getDefault().isRegistered(this))
            RxBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        val size = fgtList.size
//        Logger.e("-----onBackPressed--$size")
        if (size == 1) finish()
        else {
            fgtList.removeAt(size - 1)
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.frame_layout, fgtList[size - 2])
                    .commit()
        }
    }
}
