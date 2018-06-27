package com.lykj.coolbuy.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *<pre>
 *     author: Fan
 *     time  : 2018/5/7 下午7:25
 *     desc  :
 *</pre>
 */
class MyUseUtil{

    /**
     * type = 0 获取当前时间的星期
     * type = 1 获取当前时间
     * type = 2 获取当前时间的日期
     */
    fun getData(type : Int): String {
        var format : SimpleDateFormat?= null
        when (type){
            0 -> format = SimpleDateFormat("EEEE", Locale.getDefault())
            1 -> format = SimpleDateFormat("HH:mm", Locale.getDefault())
            2 -> format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        }
        return format!!.format(Date(System.currentTimeMillis()))
    }

    /**
     * 获取限行尾号
     * type = 0 获取第一位限行的号码
     * type = (其他) 获取第二位限行的号码
     */
    fun getLimit(type : Int): String {
        if (type == 0){
            when(getData(0)){
                "星期一" -> return "1"
                "星期二" -> return "2"
                "星期三" -> return "3"
                "星期四" -> return "4"
                "星期五" -> return "5"
            }
        }else{
            when(getData(0)){
                "星期一" -> return "6"
                "星期二" -> return "7"
                "星期三" -> return "8"
                "星期四" -> return "9"
                "星期五" -> return "0"
            }
        }
        return "0"
    }
}