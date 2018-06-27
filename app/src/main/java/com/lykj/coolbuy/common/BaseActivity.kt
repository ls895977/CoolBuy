package com.lykj.coolbuy.common

import android.widget.TextView

import com.lykj.coolbuy.R
import com.lykj.library_lykj.common.BaseAct

/**
 * <pre>
 * author: Fan
 * time  : 2018/5/2 上午9:57
 * desc  :
</pre>
 */
abstract class BaseActivity : BaseAct() {

    override fun setTitle(titleId: Int) {
        setHeaderLeftImg(R.mipmap.icon_back)
        super.setTitle(titleId)
    }

    override fun setTitle(title: CharSequence) {
        setHeaderLeftImg(R.mipmap.icon_back)
        super.setTitle(title)
    }

    override fun setHeaderRightTxt(right: Int): TextView {
        setHeaderLeftImg(R.mipmap.icon_back)
        return super.setHeaderRightTxt(right)
    }

    override fun setHeaderRightTxt(right: String): TextView {
        setHeaderLeftImg(R.mipmap.icon_back)
        return super.setHeaderRightTxt(right)
    }

    override fun setHeaderRightTxt(right: Int, txtSize: Int) {
        setHeaderLeftImg(R.mipmap.icon_back)
        super.setHeaderRightTxt(right, txtSize)
    }

}
