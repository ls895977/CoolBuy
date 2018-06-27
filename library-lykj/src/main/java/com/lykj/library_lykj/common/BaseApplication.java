package com.lykj.library_lykj.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public abstract class BaseApplication extends Application {
    /**
     * Activity栈管理
     */
    public Stack<Activity> acts;
    /**
     * 全局数据
     */
    private HashMap<String, Object> hashMap;
    /**
     * 是否打印日志
     */
    protected boolean IS_DEBUG = false;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Logger.e("-------Application start!");
        acts = new Stack<>();
        hashMap = new HashMap<>();

        initNoHttp();
        initLogger();
    }

    /**
     * 初始化日志
     */
    private void initLogger() {
        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("------")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(strategy){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return IS_DEBUG;
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 初始化网络请求
     */
    public void initNoHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        builder.readTimeout(6000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(6000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(6000, TimeUnit.MILLISECONDS);

        //https配置 方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params)                       //全局公共参数;
        ;
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        //防止重复添加
        remove(activity);
        acts.add(activity);
    }

    /**
     * 清理activity栈 并退出
     */
    public void exit() {
        clear();
        System.exit(0);
    }

    /**
     * 清理activity栈
     */
    public void remove(Activity activity) {
        if (acts != null && !acts.isEmpty()) {
            acts.remove(activity);
        }
    }

    /**
     * 清理activity栈
     */
    public void removeFinish(Class cls) {
        if (acts != null && !acts.isEmpty()) {
            Activity act = null;
            for (Activity item : acts) {
                if (TextUtils.equals(item.getClass().getName(), cls.getName())) {
                    act = item;
                    break;
                }
            }
            if (act != null) {
                act.finish();
                acts.remove(act);
            }
        }
    }

    /**
     * 清理activity栈
     */
    public void clear() {
        if (acts != null && !acts.isEmpty()) {
            for (Activity activity : acts) {
                activity.finish();
            }
            acts.clear();
        }
    }

    /**
     * 存数据
     *
     * @param key
     * @param value
     */
    public void putValue(String key, Object value) {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        hashMap.put(key, value);
    }

    /**
     * 取数据
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        if (hashMap != null) {
            return hashMap.get(key);
        }
        return null;
    }

}
