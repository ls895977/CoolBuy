package com.lykj.coolbuy.ui

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.luck.picture.lib.rxbus2.RxBus
import com.lykj.coolbuy.R
import com.lykj.coolbuy.constants.Constant
import com.lykj.coolbuy.utils.MyUseUtil
import com.lykj.library_lykj.common.BaseFgt
import com.orhanobut.logger.Logger

/**
 *<pre>
 *     author: Fan
 *     time  : 2018/5/7 下午7:22
 *     desc  :
 *</pre>
 */
class GovernmentFgt : BaseFgt() {

    override fun initLayoutId(): Int {
        return R.layout.fgt_government
    }

    override fun init() {
        hideHeader()

        getViewAndClick<LinearLayout>(R.id.ll_gov_back)
        getViewAndClick<ImageView>(R.id.iv_gov_notice)
        getViewAndClick<ImageView>(R.id.iv_gov_guide)
        getViewAndClick<ImageView>(R.id.iv_gov_dothing)
        getViewAndClick<ImageView>(R.id.iv_gov_search)
        val address = getView<TextView>(R.id.address)
        if (!TextUtils.isEmpty(Constant.ADDRESS)) address.text = Constant.ADDRESS
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.ll_gov_back -> RxBus.getDefault().send(Constant.RX_CODE_JUMP, "1")
            R.id.iv_gov_notice -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "notice")
            }
            R.id.iv_gov_guide -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "guide")
            }
            R.id.iv_gov_dothing -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "do")
            }
            R.id.iv_gov_search -> {
                RxBus.getDefault().send(Constant.RX_CODE_JUMP, "search")
            }
        }
    }
}