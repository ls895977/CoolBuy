package com.lykj.library_lykj.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.dialog.PictureDialog;
import com.lykj.library_lykj.R;
import com.lykj.library_lykj.utils.Debug;
import com.lykj.library_lykj.utils.MyUtil;

public abstract class BaseAct extends AppCompatActivity implements View.OnClickListener, IBaseAct {

    private BaseApp app;//
    protected Context context = this;
    private Class<?> lastAct;// 上一级 Activity
    private String lastSkipAct;// 跳转过来的Activity
    private Toolbar toolbar;
    private TextView txtTitle;
    private FrameLayout flyMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initBase();
        init();
    }

    private void initBase() {
        // 获取上一级Activity的Name
        lastSkipAct = getIntent().getStringExtra(Constant.LAST_ACT);

        try {
            app = (BaseApp) getApplication();
        } catch (Exception e) {
            return;
        }

        //添加activity
        app.mManager.addActivity(this);

        int layout = initLayoutId();
        if (layout < 1) return;
        //添加布局
        setContentView(layout);

        toolbar = getView(R.id.head_toolBar);
        boolean custumHeader = true;
        if (toolbar == null) {
            custumHeader = false;
            setContentView(R.layout.act_base);
            toolbar = getView(R.id.head_toolBar);
        }
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        txtTitle = getView(R.id.head_vTitle);
        flyMain = getView(R.id.base_flyMain);
        if (!custumHeader && flyMain != null) {
            LayoutInflater.from(context).inflate(layout, flyMain);
        }
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

    public TextView getTitleText() {
        if (txtTitle != null)
            return txtTitle;
        return null;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public BaseApp getApp() {
        return app;
    }

    /**
     * 获取上一级的Activity名
     */
    public String getLastSkipAct() {
        return lastSkipAct;
    }

    /**
     * 设置标题
     */
    @Override
    public void setTitle(@StringRes int titleId) {
        setTitle(getString(titleId));
    }

    /**
     * 设置标题
     */
    @Override
    public void setTitle(CharSequence title) {
        if (txtTitle != null)
            txtTitle.setText(title);
        else
            super.setTitle(title);
    }

    /**
     * 隐藏title
     */
    protected void hideHeader() {
        if (toolbar != null) toolbar.setVisibility(View.GONE);
    }

    /**
     * 设置左边图片
     *
     * @param left
     */
    protected void setHeaderLeftImg(@DrawableRes int left) {
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

    protected View getHeaderLeft() {
        return toolbar.findViewById(R.id.head_vLeft);
    }

    protected View getMainView() {
        return flyMain;
    }

    protected void setLastAct(Class<?> cls) {
        this.lastAct = cls;
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
        intent.setClass(this, cls);
        startActivity(intent);
    }

    /**
     * @see #startActClear(Intent, Class)
     */
    public void startActClear(Class<?> cls) {
        startActClear(null, cls);
    }

    /**
     * 启动Activity，清空栈 并添加到栈顶，慎用
     */
    protected void startActClear(Intent intent, Class<?> cls) {
        app.mManager.finishAllActivity();
        startAct(intent, cls);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
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
        return (T) findViewById(id);
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

    /**
     * 头-左边图标点击
     */
    public void onLeftClick() {
        finish();
    }

    /**
     * 头-右边图标点击
     */
    public void onRightClick(){

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        app.mManager.finish();
    }

    public void exitApp(){
        app.mManager.exit();
    }

}
