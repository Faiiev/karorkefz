package com.ms.karorkefz.thread;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Jason on 2017/9/10.
 */

public class Task {

    private static Handler sMainHandler = new Handler( Looper.getMainLooper() );

    public static void onMain(long msec, final Runnable runnable) {
        Runnable run = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                ;
            }
        };
        sMainHandler.postDelayed( run, msec );
    }

    public static void onMain(final Runnable runnable) {
        Runnable run = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
            }
        };
        sMainHandler.post( run );
    }
}
