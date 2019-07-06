package com.ms.karorkefz.xposed;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Log.LogUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class KaraokeHook {
    boolean enableStart, enableFansDelete, enableKTV, enableLIVE, enableRecording;
    Config config;

    public void init() {

        LogUtil.v( "karorkefz", "进入Karorke" );
        XposedHelpers.findAndHookMethod( Instrumentation.class, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
            private boolean mCalled = false;

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (mCalled == false) {
                    mCalled = true;
                    Context context = (Context) param.args[0];
                    config = new Config( context );
                    enableStart = config.isOn( "enableStart" );
                    enableFansDelete = config.isOn( "enableFansDelete" );
                    enableKTV = config.isOn( "enableKTV" );
                    enableLIVE = config.isOn( "enableLIVE" );
                    enableRecording = config.isOn( "enableRecording" );
                }
            }
        } );
        XposedBridge.hookAllMethods( ClassLoader.class, "loadClass", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) return;
                if (param.args.length != 1) return;
                Class<?> cls = (Class<?>) param.getResult();
                String name = cls.getName();
                if (name.equals( "com.tencent.karaoke.module.config.ui.j" )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载 设置" );
                    new Karaoke_Setting_Hook( cls.getClassLoader() ).init();
                }
                if (enableStart && name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载maintab" );
                    new Karaoke_MainTab_Hook( cls.getClassLoader() ).init();
                }
                if (enableFansDelete && name.equals( "com.tencent.karaoke.module.user.ui.FollowFansActivity" )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载 user" );
                    new Karaoke_User_Hook( cls.getClassLoader() ).init();
                }
                if (enableKTV && name.equals( "com.tencent.karaoke.module.ktv.ui.KtvRoomActivity" )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载单麦Ktv" );
                    new Karaoke_Ktv_Hook( cls.getClassLoader(), config ).init();
                }
                if (enableLIVE && name.equals( "com.tencent.karaoke.module.live.ui.LiveActivity" )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载Live" );
                    new Karaoke_Live_Hook( cls.getClassLoader(), config ).init();
                }
                if (enableRecording && name.equals( "com.tencent.karaoke.module.recording.ui.main.RecordingActivity" )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载 歌曲录制" );
                    new Karaoke_RecordingActivity_Hook( cls.getClassLoader() ).init();
                }
            }
        } );
    }
}
