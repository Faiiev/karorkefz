package com.ms.karorkefz;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_MainTab_Hook {
    private ClassLoader classLoader;

    Karaoke_MainTab_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        //停止开始页面
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.splash.ui.NewSplashAdView",
                classLoader,
                "a",// 被Hook的函数
                Context.class,
                new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //super.beforeHookedMethod(param);
                    }

                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedHelpers.callMethod( param.thisObject, "a", false );
                        //停止开始页面
                    }
                } );
    }
}
