package com.lykj.library_lykj.common;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <pre>
 *     author: Fan
 *     time  : 2018/3/21 上午11:33
 *     desc  :
 * </pre>
 */

public class AppManager {
    private static Stack<AppCompatActivity> activityStack;
    private static AppManager      instance;

    /**
     * 单例模式 创建单一实例
     *
     * @return
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 初始化Stack<Activity>
     */
    private void initActivityStack() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(AppCompatActivity activity) {
        initActivityStack();
        activityStack.add(activity);
//        Logger.e(activityStack.size()+"");
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return
     */
    public AppCompatActivity currentActivity() {
        AppCompatActivity activity = activityStack.lastElement();
        return activity;
    }

    public void getTotalActivity(){
        Logger.e("--getTotalActivity--", activityStack.get(activityStack.size() - 1).getLocalClassName());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            Logger.e(activity.getClass().getSimpleName(), "--> activity finish");
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finish() {
        //获取到当前Activity
        AppCompatActivity activity = activityStack.lastElement();
        //结束指定Activity
        finishActivity(activity);
    }

    /**
     * 结束指定类名之外的Activity
     */
    public void finishExceptActivity(Class<?> cls) {
        List<AppCompatActivity> activities = new ArrayList<>();
        for (AppCompatActivity activity : activityStack) {
            // 如果不是指定的类，就加入待处理集合
            if (!activity.getClass()
                    .equals(cls)) {
                activities.add(activity);
            }
        }

        // 结束所有除开指定类 类名相同activity
        for (AppCompatActivity activity : activities) {
            finishActivity(activity);
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        List<AppCompatActivity> activities = new ArrayList<>();
        for (AppCompatActivity activity : activityStack) {
            if (activity.getClass()
                    .equals(cls)) {
                // finishActivity(activity);
                activities.add(activity);
            }
        }
        // 结束所有类名相同activity
        activityStack.removeAll(activities);
        for (AppCompatActivity activity : activities) {
            finishActivity(activity);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                AppCompatActivity activity = activityStack.get(i);
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     *
     * 这里关闭的是所有的Activity，没有关闭Activity之外的其他组件;
     * android.os.Process.killProcess(android.os.Process.myPid())
     * 杀死进程关闭了整个应用的所有资源，有时候是不合理的，通常是用堆栈管理Activity;
     * System.exit(0)杀死了整个进程，这时候活动所占的
     * 资源也会被释放,它会执行所有通过Runtime.addShutdownHook注册的shutdown hooks.
     * 它能有效的释放JVM之外的资源,执行清除任务，运行相关的finalizer方法终结对象，
     * 而finish只是退出了Activity。
     */
    public void exit() {
        try {
            finishAllActivity();
            //DalvikVM的本地方法
            // 杀死该应用进程
            //android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(0);
            //这些方法如果是放到主Activity就可以退出应用，如果不是主Activity
            //就是退出当前的Activity
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
