package com.ms.karorkefz;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.robv.android.xposed.XposedHelpers;

public class MyThread {
    XSingleThreadPool send_XSingleThreadPool = new XSingleThreadPool();

    public void Live_init(final Object uObject, final WeakReference one, final String two, final String three, final int four, final ArrayList five, final String six) {
        new Thread() {
            public void run() {
                try {
                    sleep( 2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                send( uObject, one, two, three, four, five, six );
            }
        }.start();
    }

    private void send(final Object uObject, final WeakReference one, final String two, final String three, final int four, final ArrayList five, final String six) {
        send_XSingleThreadPool.add( new Runnable() {
            public void run() {
                Log.e( "karorkefz", "线程2:" + six + TimeHook.SimpleDateFormat_Time() );
                XposedHelpers.callMethod( uObject, "a", one, two, three, four, five, six );
            }
        } );
    }
}
