package com.lykj.library_lykj.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * 工具
 */
public class MyUtil {

    private Flowable rxTime(final Long time){
        return Flowable.interval(1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return time - aLong;
                    }
                }).take(time + 1);
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (str == null)
            return false;
        return Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$").matcher(str).matches(); // 验证手机号
    }

    /**
     * 密码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPassword(String str) {
        if (str == null)
            return false;
        return Pattern.compile("^[a-z0-9A-Z]+$").matcher(str).matches(); // 验证密码
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isNumber(String str) {
        if (str == null)
            return false;
        return Pattern.compile("[0-9]+").matcher(str).matches();
    }

    /**
     * 是否是中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (str == null)
            return false;
        return Pattern.compile("^[\u4e00-\u9fa5]+$").matcher(str).matches();
    }

    /**
     * 是否是IP地址
     *
     * @param str
     * @return
     */
    public static boolean isIpAddress(String str) {
        if (str == null)
            return false;
        return Pattern.compile("(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}").matcher(str).matches();
    }

    /**
     * 是否是身份证
     *
     * @param str
     * @return
     */
    public static boolean isIdentity(String str) {
        if (isEmpty(str))
            return false;
        if (str.length() == 15)
            return Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$").matcher(str).matches();
        if (str.length() == 18)
            return Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$").matcher(str).matches();
        return false;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        if (str == null)
            return false;
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        str = str.replaceAll("-", "");
        // p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        if (str.length() == 11) {
            p1 = Pattern.compile("^[0][1-9]{2,3}[0-9]{5,10}$"); // 验证带区号的
            m = p1.matcher(str);
            b = m.matches();
        } else if (str.length() <= 9) {
            p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
            m = p2.matcher(str);
            b = m.matches();
        }
        if (!b)
            return isMobile(str);
        return b;
    }

    /**
     * 邮箱验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isEmail(String str) {
        if (str == null)
            return false;
        return Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
                .matcher(str).matches();
    }

    public static boolean isNoEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        if (null == str)
            return true;
        if (str.length() == 0)
            return true;
        if (str.trim().length() == 0)
            return true;
        if (str.indexOf("null") == 0)
            return true;
        return false;
    }

    public static boolean isNoEmpty(List<?> datas) {
        return !isEmpty(datas);
    }

    public static boolean isEmpty(List<?> datas) {
        if (datas == null)
            return true;
        if (datas.size() == 0)
            return true;
        return false;
    }

    /**
     * 去掉多余的0
     *
     * @param str
     * @return
     */
    public static String removeNumberZero(String str) {
        if (isEmpty(str)) {
            return "0";
        }
        if (str.indexOf(".") > 0) {
            str = str.replaceAll("0+?$", "");// 去掉多余的0
            str = str.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return str;
    }

    /**
     * 把字体结果dimen转化成原sp值
     *
     * @return
     */
    public static float floatToSpDimension(float value, Context context) {
        return value / context.getResources().getDisplayMetrics().scaledDensity;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T getView(View v, int resId) {
        return (T) v.findViewById(resId);
    }

    /**
     * 获取当前时间Date
     *
     * @return 现在时间(Now)
     */
    public static String getNowTime() {
        Date d = new Date(System.currentTimeMillis());
         String type = "yyyy-MM-dd HH:mm:ss";
//        String type = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(type, Locale.CHINA);
        return formatter.format(d);
    }

    /**
     * 获取当前时间Date
     */
    public static String getDateTime(long ltime) {
        return getDateTime(ltime, null);
    }

    /**
     * 获取当前时间Date
     */
    public static String getDateTime(long ltime, String type) {
        if ((ltime + "").length() == 10) ltime = ltime * 1000L;
        if (type == null) type = "yyyy-MM-dd HH:mm:ss";
        Date d = new Date(ltime);
        SimpleDateFormat formatter = new SimpleDateFormat(type, Locale.CHINA);
        return formatter.format(d);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredHeight(View view) {
        // int height = view.getMeasuredHeight();
        // if(0 < height){
        // return height;
        // }
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     *
     * @param view
     * @return
     */
    public static int getViewMeasuredWidth(View view) {
        // int width = view.getMeasuredWidth();
        // if(0 < width){
        // return width;
        // }
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件的尺寸
     *
     * @param view
     */
    public static void calcViewMeasure(View view) {
        // int width = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // int height = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // view.measure(width,height);

        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }

    /**
     * 返回当前程序版本信息
     */
    public static PackageInfo getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            Debug.e("VersionInfo|Exception:" + e);
        }
        return null;
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
//    public static boolean checkPackage(Context context, String packageName) {
//
//        if (TextUtils.isEmpty(packageName))
//            return false;
//        try {
//            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (NameNotFoundException e) {
//            return false;
//        }
//    }

    private static long lastTime = 0;

    /**
     * 是否是快速点击
     * @return
     */
    public static boolean isFastClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime < 500)
            return true;
        lastTime = curTime;
        return false;
    }
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isMyFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    /**
     * 是否是车牌号
     *
     * @param str
     * @return
     */
    public static boolean isCarNumber(String str) {
        if (isNoEmpty(str))
            return Pattern.compile("^[\u4e00-\u9fa5|A-Z]{1}[A-Z]{1}[A-Z_0-9]{5}$").matcher(str).matches();
        return false;
    }

    /**
     * 获取周几
     *
     * @param week
     * @return
     */
    public static String getWeekName(int week) {
        switch (week) {
            case Calendar.SUNDAY:
                return "周日";
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
        }
        return null;
    }

    /**
     * 输入回车符无效
     * @param editText
     */
    public static void clearEnterEditText(final EditText editText){
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER){
                    return true;
                }
                return false;
            }
        });
    }

    public static void scroolEditText(final EditText editText){
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view == editText && canVerticalScroll(editText))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }



    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    private static int getMonthDays(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
                    return 29;
                }else{
                    return 28;
                }
            default:
                return  -1;
        }
    }

    public static String getStampTime(int year, int month){
        int days = getMonthDays(year, month);
        String dayFirst = year+"-"+month+"-01 00:00:00";
        String dayEnd = year+"-"+month+"-"+days+" 23:59:59";
        long start = TimeUtils.string2Milliseconds(dayFirst, TimeUtils.DEFAULT_SDF);
        long end = TimeUtils.string2Milliseconds(dayEnd, TimeUtils.DEFAULT_SDF);
        return start+","+end;
    }

    public static int[] splitDate(String date){
        int[] ret = new int[2];

        String[] split = date.split("年");
        ret[0] = Integer.parseInt(split[0]);
        ret[1] = Integer.parseInt(split[1].substring(0, 2));

        return ret;
    }

    /**
     *获取一个月前的日期
     * @param date 传入的日期
     * @return
     */
    public static String getMonthAgo(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        String monthAgo = simpleDateFormat.format(calendar.getTime());
        return monthAgo;
    }
}
