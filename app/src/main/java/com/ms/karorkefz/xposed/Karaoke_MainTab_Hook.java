package com.ms.karorkefz.xposed;

import com.ms.karorkefz.util.Log.LogUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_MainTab_Hook {
    private ClassLoader classLoader;
    private boolean b = true;

    Karaoke_MainTab_Hook(ClassLoader mclassLoader) {

        classLoader = mclassLoader;
    }

    public void init() {
        LogUtil.d( "karorkefz", "进入maintab" );
        //停止开始页面
        Class NewSplashAdView = XposedHelpers.findClass( "com.tencent.karaoke.module.splash.ui.NewSplashAdView", classLoader );
        XposedBridge.hookAllMethods( NewSplashAdView,
                "a",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.d( "karorkefz", "maintab-find" );
                        try {
                            if (b) {
                                b = false;
                                XposedHelpers.callMethod( param.thisObject, "a", false );
                            }
                        } catch (Exception e) {
                        }
                    }
                } );
    }
}
