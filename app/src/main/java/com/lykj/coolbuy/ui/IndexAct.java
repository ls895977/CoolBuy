package com.lykj.coolbuy.ui;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.just.agentweb.AgentWeb;
import com.lykj.coolbuy.MainActivity;
import com.lykj.coolbuy.R;
import com.lykj.coolbuy.utils.MyUseUtil;
import com.lykj.library_lykj.common.BaseAct;
import com.lykj.library_lykj.http.ApiCallback;
import com.lykj.library_lykj.http.ApiHttp;
import com.lykj.library_lykj.http.ApiRequest;
import com.lykj.library_lykj.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     author: Fan
 *     time  : 2018/5/17 下午1:08
 *     desc  : 广告首页
 * </pre>
 */
public class IndexAct extends BaseAct {

    private TextView tvWeather, tvTemp, tvLimitFirst, tvLimitLast, tvDate, tvWeek, tvTime;

    private int index = 0;
    private Disposable subscribe;

    private boolean mIsFitstIn = true;
    private Disposable mRefreshSubscribe;

    @Override
    public int initLayoutId() {
        return R.layout.act_index;
    }

    @Override
    public void init() {
        hideHeader();
        tvWeather = getView(R.id.head_index_weather);
        tvTemp = getView(R.id.head_index_temp);
        tvLimitFirst = getView(R.id.head_index_limit_first);
        tvLimitLast = getView(R.id.head_index_limit_last);
        tvDate = getView(R.id.head_index_date);
        tvWeek = getView(R.id.head_index_week);
        tvTime = getView(R.id.head_index_time);
        getViewAndClick(R.id.index_more);

        getTempData();
        getTimeGap();
    }

    private void getTempData() {
        final MyUseUtil useUtil = new MyUseUtil();
        tvLimitFirst.setText(useUtil.getLimit(0));
        tvLimitLast.setText(useUtil.getLimit(1));
        tvWeek.setText(useUtil.getData(0));
        tvTime.setText(useUtil.getData(1));
        tvDate.setText(useUtil.getData(2));

        ApiHttp apiHttp = new ApiHttp();
        String url = "https://www.sojson.com/open/api/weather/json.shtml?city=成都";

        apiHttp.getToString(url, ApiRequest.RequestFlag.FLAG1, new ApiCallback() {
            @Override
            public void onSuccess(Object resultData, ApiRequest.RequestFlag flag) {
                String json = resultData.toString();
                JSONObject o = null;
                try {
                    o = new JSONObject(json);
                    if (o.has("status") && o.optInt("status") == 200){
                        JSONObject obj = o.optJSONObject("data")
                                .optJSONArray("forecast")
                                .optJSONObject(0);
                        String lowTemp = obj.optString("low").substring(3, 5);
                        String highTemp = obj.optString("high").substring(3, 5);
                        String type = obj.optString("type");

                        tvWeather.setText(type);
                        tvTemp.setText(lowTemp+"~"+highTemp+"℃");
                    }else {
                        if (index < 3) getTempData();//重新获取，因为出现获取出错的情况
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                index++;
            }

            @Override
            public void onError(String errors, ApiRequest.RequestFlag flag) {
                if (index < 5) getTempData();//重新获取，因为出现获取出错的情况
                index++;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        startAct(MainActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribe = Flowable.interval(5, 5, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        //延迟5秒后，每隔5秒钟获取一次当前的时间
//                        Logger.e("----accept----");
                        tvTime.setText(new MyUseUtil().getData(1));
                    }
                });

        AgentWeb web = AgentWeb.with(this)//
                .setAgentWebParent(new LinearLayout(context), new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                //                .setReceivedTitleCallback(mCallback)
                //                .setWebChromeClient(mWebChromeClient)
                //                .setWebViewClient(mWebViewClient)
                .createAgentWeb()//
                .ready()
                .go("");

    }

    /**
     * 每天凌晨刷新数据
     */
    private void getTimeGap(){
        long timeMills = TimeUtils.getCurTimeMills();//获取当前的时间
        String string = TimeUtils.milliseconds2String(timeMills, TimeUtils.DEFAULT_SDF2) + " 23:59:59";
        //获取到当前时间到晚上凌晨的时间段的秒数
        long time = TimeUtils.string2Milliseconds(string, TimeUtils.DEFAULT_SDF1)/1000 - timeMills/1000;
        //当第一次进入程序的时候
        if (mIsFitstIn){
            mRefreshSubscribe = Flowable.interval(time, TimeUnit.SECONDS)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) {
                            mIsFitstIn = false;
                            getTempData();
                        }
                    });
        }else {
            if (!mRefreshSubscribe.isDisposed()) mRefreshSubscribe.dispose();
            //当再次刷新过后，重新设置任务
            mRefreshSubscribe = Flowable.interval(24*60*60, TimeUnit.SECONDS)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) {
                            getTempData();
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!subscribe.isDisposed())
            subscribe.dispose();
        if (!mRefreshSubscribe.isDisposed())
            mRefreshSubscribe.dispose();
    }
}
