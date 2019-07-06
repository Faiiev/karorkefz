package com.ms.karorkefz.util;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.util.Date;

public class TimeHook {
    @SuppressLint("NewApi")
    public static String SimpleDateFormat_Time() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss" );
        Date date = new Date( System.currentTimeMillis() );
        String time = simpleDateFormat.format( date );
        return time;
    }
}
