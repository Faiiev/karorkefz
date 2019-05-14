package com.ms.karorkefz;

import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.robv.android.xposed.XposedHelpers;

public class MyThread {
    String g;
    EditText inputEditText;
    Object ClickObject;
    View view;
    ExecutorService live_fixedThreadPool = Executors.newFixedThreadPool( 50 );
    ExecutorService send_fixedThreadPool = Executors.newSingleThreadExecutor();

    MyThread(String g, EditText inputEditText, Object ClickObject, View view) {
        this.g = g;
        this.inputEditText = inputEditText;
        this.ClickObject = ClickObject;
        this.view = view;
    }

    public void Live_init() {
        live_fixedThreadPool.execute( new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep( 2000 );
                    String a1 = "@" + g + "  欢迎来到直播间";
                    send( a1 );
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } );
    }

    private void send(final String message) {
        send_fixedThreadPool.execute( new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep( 1000 );
                    inputEditText.setText( message );
                    XposedHelpers.callMethod( ClickObject, "onClick", view );
//                    Log.e( "karorkefz", "发送消息：" + a1 );
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } );
    }
}
