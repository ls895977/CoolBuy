package com.lykj.coolbuy.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luck.picture.lib.rxbus2.RxBus;
import com.lykj.coolbuy.R;
import com.lykj.coolbuy.constants.Constant;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * <pre>
 *     author: Fan
 *     time  : 2018/6/13 下午3:51
 *     desc  :
 * </pre>
 */
public class CountDownPop extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private View mPickerContainerV;
    private long count;
    private Disposable disposable;

    public CountDownPop(Context mContext, long count) {
        this.mContext = mContext;
        this.count = count;
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.pop_count_down, null);
        mPickerContainerV = mRootView.findViewById(R.id.container_picker);
        TextView tvCount = mRootView.findViewById(R.id.count);
        disposable = timer(tvCount);

        mRootView.setOnClickListener(this);
        mPickerContainerV.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        setContentView(mRootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private Disposable timer(final TextView mCodeGet) {
        return Flowable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .onBackpressureBuffer()//加上背压策略
                .take(count) //设置循环次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return count - aLong - 1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (aLong == 0) {
                            dismissPopWin();
                        }else {
                            mCodeGet.setText(aLong+"");
                        }
                    }
                });
    }

    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            mPickerContainerV.startAnimation(trans);
        }
    }

    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (disposable.isDisposed()) disposable.dispose();
                dismiss();
            }
        });

        mPickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {
        RxBus.getDefault().send(Constant.RX_CODE_RESET_TIME, "");
        dismissPopWin();
    }
}
