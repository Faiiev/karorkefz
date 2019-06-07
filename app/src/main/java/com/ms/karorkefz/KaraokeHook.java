package com.ms.karorkefz;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

class KaraokeHook {
    XSharedPreferences xSharedPreferences = new XSharedPreferences( "com.ms.karorkefz" );
    boolean enableStart = xSharedPreferences.getBoolean( "enableStart", true );
    boolean enableKTV = xSharedPreferences.getBoolean( "enableKTV", true );
    boolean enableLIVE = xSharedPreferences.getBoolean( "enableLIVE", true );

    void init() {
        Log.v( "karorkefz", "进入Karorke" );
        // 第一步：Hook方法ClassLoader#loadClass(String)
        XposedBridge.hookAllMethods( ClassLoader.class, "loadClass", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) return;
                if (param.args.length != 1) return;
                Class<?> cls = (Class<?>) param.getResult();
                String name = cls.getName();
                if (enableStart && name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" )) {
                    Log.v( "karorkefz", "Karorke-准备加载maintab" );
                    new Karaoke_MainTab_Hook( cls.getClassLoader() ).init();
                }
                if (enableKTV && name.equals( "com.tencent.karaoke.module.ktv.ui.KtvRoomActivity" )) {
                    Log.v( "karorkefz", "Karorke-准备加载Ktv" );
                    new Karaoke_Ktv_Hook( cls.getClassLoader() ).init();
                }
                if (enableLIVE && name.equals( "com.tencent.karaoke.module.live.ui.LiveActivity" )) {
                    Log.v( "karorkefz", "Karorke-准备加载Live" );
                    new Karaoke_Live_Hook( cls.getClassLoader() ).init();
                }
                if (enableStart && name.equals( "com.tencent.karaoke.module.detail.ui.DetailActivity" )) {
                    Log.v( "karorkefz", "Karorke-准备加载 歌曲" );
                    new Karaoke_DetailActivity_Hook( cls.getClassLoader() ).init();
                }
            }
        } );
    }
}
