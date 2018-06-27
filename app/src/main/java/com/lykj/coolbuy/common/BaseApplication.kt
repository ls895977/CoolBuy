package com.lykj.coolbuy.common

import com.lykj.library_lykj.common.BaseApp

/**
 * <pre>
 * author: Fan
 * time  : 2018/5/2 上午10:59
 * desc  :
 *</pre>
 */
class BaseApplication : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        IS_DEBUG = true
    }
}
