package com.ms.karorkefz;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

class KaraokeHook  {
    private ClassLoader classLoader;
    KaraokeHook(ClassLoader mClassLoader) {
        classLoader = mClassLoader;
    }

    void init() {
        // 第一步：Hook方法ClassLoader#loadClass(String)
        findAndHookMethod( ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                List<String> classes = new ArrayList<>();
                //TODO: Add the package name of application your want to hook!
                classes.add( "com.tencent.karaoke.module.ktv.ui.KtvRoomActivity" );
                classes.add( "com.tencent.karaoke.module.main.ui.MainTabActivity" );
                classes.add( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" );
//              classes.add( "com.tencent.karaoke.module.ktv.ui.KtvAudienceListActivity" );
                if (param.hasThrowable()) return;
                Class<?> cls = (Class<?>) param.getResult();
                String name = cls.getName();
                //XposedBridge.log(  name );
                if (classes.contains( name )) {
                    // 所有的类都是通过loadClass方法加载的
                    // 所以这里通过判断全限定类名，查找到目标类
                    // 第二步：Hook目标方法
                    if (classLoader == null) {
                        Log.e( "Xposed", "Can't get ClassLoader!" );
                        return;
                    }
                    if (name.equals( "com.tencent.karaoke.module.ktv.ui.KtvRoomActivity" )) {
                        new Karaoke_Ktv_Hook( classLoader ).init();
                    }
                    if (name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" )) {
                        new Karaoke_MainTab_Hook( classLoader ).init();
                    }
                }
            }
        } );
    }

}
