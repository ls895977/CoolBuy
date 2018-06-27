package com.lykj.coolbuy.ui

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.luck.picture.lib.rxbus2.RxBus
import com.lykj.coolbuy.R
import com.lykj.coolbuy.constants.Constant
import com.lykj.library_lykj.common.BaseFgt

/**
 *<pre>
 *     author: Fan
 *     time  : 2018/5/21 上午9:32
 *     desc  :
 *</pre>
 */
class ServiceFgt : BaseFgt() {

    override fun initLayoutId(): Int {
        return R.layout.fgt_service
    }

    override fun init() {
        hideHeader()

        getViewAndClick<ImageView>(R.id.fee_electric)
        getViewAndClick<ImageView>(R.id.fee_gas)
        getViewAndClick<ImageView>(R.id.fee_water)
        getViewAndClick<LinearLayout>(R.id.ll_gov_back)
        val address = getView<TextView>(R.id.address)
        if (!TextUtils.isEmpty(Constant.ADDRESS)) address.text = Constant.ADDRESS
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.fee_electric -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "electric")
            }
            R.id.fee_gas -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "gas")
            }
            R.id.fee_water -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "water")
            }
            R.id.ll_gov_back -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "1")
            }
        }
    }
}