package com.lykj.library_lykj.common;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * @author HeYan
 */
public interface IBaseAct {
    /**
     * 初始化布局 可空,可以包含 Toolbar 否则 使用默认的 Toolbar
     */
    @LayoutRes
    int initLayoutId();

    /**
     * 初始化
     */
    void init();

//    void onViewClick(View v);

    /**
     * 头-左边图标点击
     */
    void onLeftClick();

    /**
     * 头-右边图标点击
     */
    void onRightClick();
}
