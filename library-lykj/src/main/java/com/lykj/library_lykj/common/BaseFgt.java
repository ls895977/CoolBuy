package com.lykj.library_lykj.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.dialog.PictureDialog;
import com.lykj.library_lykj.R;
import com.lykj.library_lykj.utils.MyUtil;


/**
 * 父类Fragment
 */
public abstract class BaseFgt extends Fragment implements View.OnClickListener, IBaseAct {
    // private boolean custom;
    private BaseApp app;
    protected Context context;
    private Toolbar toolbar;
    private TextView txtTitle;
    private FrameLayout flyMain;
    private boolean init;

    public void setInit(boolean init) {
        this.init = init;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!init) {
            init = true;
            init();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        try {
            app = (BaseApp) getActivity().getApplication();
        } catch (Exception e) {
            Log.e(getClass().toString(), "base fragment get a null application");
        }

        boolean custumHeader = true;

        int layout = initLayoutId();
        View v = null;
        if (layout > 0) {
            v = inflater.inflate(layout, container, false);
            toolbar = getView(v, R.id.head_toolBar);
        }
        if (toolbar == null) {
            custumHeader = false;
            v = inflater.inflate(R.layout.act_base, container, false);
            toolbar = getView(v, R.id.head_toolBar);
        }
        toolbar = getView(v, R.id.head_toolBar);
        toolbar.setTitle("");
        txtTitle = getView(v, R.id.head_vTitle);
        flyMain = getView(v, R.id.base_flyMain);
        if (!custumHeader && flyMain != null && layout > 0) {
            View.inflate(context, layout, flyMain);
        }
        init = false;

        return v;
    }

    private PictureDialog dialog;
    protected void showLoading() {
        showCView();
        if (null == dialog) dialog = new PictureDialog(context);
        dialog.show();
    }

    protected void showCView() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    public void setToolbarBackBg(int clor) {
        toolbar.setBackgroundColor(getResources().getColor(clor));
    }

    public void setTitleColor(int clor) {
        txtTitle.setTextColor(getResources().getColor(clor));
    }

    public BaseApp getApp() {
        return app;
    }

    /**
     * 设置标题
     */
    public void setTitle(@StringRes int titleId) {
        setTitle(getString(titleId));
    }

    /**
     * 设置标题
     */
    public void setTitle(CharSequence title) {
        if (txtTitle != null) txtTitle.setText(title);
    }

    /**
     * 隐藏标题
     */
    protected void hideHeader() {
        if (toolbar != null) toolbar.setVisibility(View.GONE);
    }


    /**
     * 设置左边图片
     *
     * @param left
     */
    protected void setHeaderLeft(@DrawableRes int left) {
        if (toolbar.findViewById(R.id.head_vLeft) == null) {
            View v = View.inflate(context, R.layout.in_head_left, toolbar);
            ImageView img = getView(v, R.id.head_vLeft);
            img.setOnClickListener(this);
            img.setImageResource(left);
        } else {
            ImageView img = getView(toolbar, R.id.head_vLeft);
            img.setImageResource(left);
        }
    }

    /**
     * 设置左边文字
     *
     * @param left
     */
    protected TextView setHeaderLeftTxt(@StringRes int left) {
        TextView txt;
        if (toolbar.findViewById(R.id.head_vLeft) == null) {
            View v = View.inflate(context, R.layout.in_head_tleft, toolbar);
            txt = getView(v, R.id.head_vLeft);
            txt.setOnClickListener(this);
            txt.setText(left);
            if (txtTitle != null)
                txt.setTextColor(txtTitle.getTextColors());
        } else {
            txt = getView(toolbar, R.id.head_vLeft);
            txt.setText(left);
        }
        return txt;
    }

    /**
     * 设置左边文字及大小
     * @param left
     * @param txtSize
     */
    protected void setHeaderLeftTxt(@StringRes int left, int txtSize) {
        TextView txt = setHeaderLeftTxt(left);
        if (txt != null && txtSize > 0)
            txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
    }

    /**
     * 设置图片
     *
     * @param right
     */
    protected void setHeaderRight(@DrawableRes int right) {
        if (toolbar.findViewById(R.id.head_vRight) == null) {
            View v = View.inflate(context, R.layout.in_head_right, toolbar);
            ImageView img = getView(v, R.id.head_vRight);
            img.setOnClickListener(this);
            img.setImageResource(right);
        } else {
            ImageView img = getView(toolbar, R.id.head_vRight);
            img.setImageResource(right);
        }
    }

    /**
     * 设置右边文字
     * @param right
     * @return
     */
    protected TextView setHeaderRightTxt(@StringRes int right) {
        TextView txt;
        if (toolbar.findViewById(R.id.head_vRight) == null) {
            View v = View.inflate(context, R.layout.in_head_tright, toolbar);
            txt = getView(v, R.id.head_vRight);
            txt.setOnClickListener(this);
            txt.setText(right);
            if (txtTitle != null)
                txt.setTextColor(txtTitle.getTextColors());
        } else {
            txt = getView(toolbar, R.id.head_vRight);
            txt.setText(right);
        }
        return txt;
    }

    /**
     * 设置右边文字及大小
     * @param right
     * @param txtSize
     */
    protected void setHeaderRightTxt(@StringRes int right, int txtSize) {
        TextView txt = setHeaderRightTxt(right);
        if (txt != null && txtSize > 0)
            txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
    }

    /**
     * 设置右边文字
     * @param right
     * @return
     */
    protected TextView setHeaderRightTxt(String right) {
        TextView txt = getView(toolbar, R.id.head_vRight);
        txt.setText(right);
        return txt;
    }

    /**
     * 设置右边文字及大小
     * @param right
     * @param txtSize
     */
    protected void setHeaderRightTxt(String right, int txtSize) {
        TextView txt = setHeaderRightTxt(right);
        if (txt != null && txtSize > 0)
            txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
    }


    /**
     * 头部
     */
    protected View getHeader() {
        return toolbar;
    }

    protected View getHeaderRight() {
        return toolbar.findViewById(R.id.head_vRight);
    }

    protected View getMainView() {
        return flyMain;
    }

    /**
     * @see #startAct(Intent, Class)
     */
    protected void startAct(Class<?> cls) {
        startAct(null, cls);
    }

    /**
     * 启动Activity
     */
    protected void startAct(Intent intent, Class<?> cls) {
        if (intent == null)
            intent = new Intent();
        intent.putExtra(Constant.LAST_ACT, this.getClass().getSimpleName());
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(Constant.LAST_ACT, this.getClass().getSimpleName());
        startActivityForResult(intent, requestCode);
    }

    protected String getStrings(Integer... ids) {
        if (ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int id : ids) {
                sb.append(getString(id));
            }
            return sb.toString();
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        if (MyUtil.isFastClick())
            return;
        if (v.getId() == R.id.head_vLeft)
            onLeftClick();
        else if (v.getId() == R.id.head_vRight)
            onRightClick();
    }

    /**
     * 获取 控件
     *
     * @param v  布局
     * @param id 行布局中某个组件的id
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(View v, @IdRes int id) {
        return (T) v.findViewById(id);
    }

    /**
     * 获取 控件
     *
     * @param id 行布局中某个组件的id
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        return getView(getView(), id);
    }

    /**
     * 获取并绑定点击
     *
     * @param id id
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewAndClick(@IdRes int id) {
        T v = getView(id);
        v.setOnClickListener(this);
        return v;
    }

    /**
     * 获取并绑定点击
     *
     * @param id id
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewAndClick(View view, @IdRes int id) {
        T v = getView(view, id);
        v.setOnClickListener(this);
        return v;
    }

    protected void setOnClickListener(@IdRes int id) {
        if (getView() != null) getView().findViewById(id).setOnClickListener(this);
    }

    protected void setOnClickListener(View v, @IdRes int id) {
        v.findViewById(id).setOnClickListener(this);
    }

    /**
     * 头-左边图标点击
     */
    public void onLeftClick() {

    }

    /**
     * 头-右边图标点击
     */
    public void onRightClick() {
    }

}
