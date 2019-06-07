package com.ms.karorkefz;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;

public class TimeHook {
    protected static String Calendar_Time() {
        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get( Calendar.YEAR );
        //月
        int month = calendar.get( Calendar.MONTH ) + 1;
        //日
        int day = calendar.get( Calendar.DAY_OF_MONTH );
        //获取系统时间
        //小时
        int hour = calendar.get( Calendar.HOUR_OF_DAY );
        //分钟
        int minute = calendar.get( Calendar.MINUTE );
        //秒
        int second = calendar.get( Calendar.SECOND );
//        Log.v( "karorkefz", String.valueOf( "Calendar获取当前日期" + year + "年" + month + "月" + day + "日" + hour + ":" + minute + ":" + second ) );
        String time = year + "" + month + "" + day + "" + hour + "" + minute + "" + second + "";
        return time;
    }

    @SuppressLint("NewApi")
    protected static String SimpleDateFormat_Time() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );// HH:mm:ss
//获取当前时间
        Date date = new Date( System.currentTimeMillis() );
//        Log.v( "karorkefz", "SimpleDateFormat获取当前日期时间"+simpleDateFormat.format(date) );
        String time = simpleDateFormat.format( date );
        return time;
    }

    protected static String Time_Time() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
//        Log.v( "karorkefz", "Time获取当前日期"+year+"年"+month+"月"+day+"日"+hour+":"+minute+":"+second);
        String time = year + month + day + hour + minute + second + "";
        return time;
    }

    protected static String CurrentTimeMillis_Time() {
        long time = System.currentTimeMillis();
        return String.valueOf( time );
    }
}
