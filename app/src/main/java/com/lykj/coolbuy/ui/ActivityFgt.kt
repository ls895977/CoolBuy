package com.lykj.coolbuy.ui

import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.luck.picture.lib.rxbus2.RxBus
import com.lykj.coolbuy.R
import com.lykj.coolbuy.constants.Constant
import com.lykj.coolbuy.utils.MyUseUtil
import com.lykj.library_lykj.common.BaseFgt

/**
 * <pre>
 * author: Fan
 * time  : 2018/5/8 下午2:36
 * desc  : 活动促销
</pre>
 */
class ActivityFgt : BaseFgt() {
    override fun initLayoutId(): Int {
        return R.layout.fgt_activity
    }

    override fun init() {
        hideHeader()

        getViewAndClick<LinearLayout>(R.id.ll_gov_back)
        val address = getView<TextView>(R.id.address)
        if (!TextUtils.isEmpty(Constant.ADDRESS)) address.text = Constant.ADDRESS
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.ll_gov_back -> RxBus.getDefault().send(Constant.RX_CODE_JUMP, "1")
        }
    }
}
