package com.ms.karorkefz;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_DetailActivity_Hook {
    private ClassLoader classLoader;
    private boolean b = true;

    Karaoke_DetailActivity_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        Log.e( "karorkefz", "进入歌曲" );
        //停止开始页面
        Class a = XposedHelpers.findClass( "com.tencent.karaoke.module.vip.ui.d.a", classLoader );
        Class c = XposedHelpers.findClass( "com.tencent.karaoke.module.vip.ui.d.c", classLoader );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.vip.ui.a",
                classLoader,
                "a",
                c,
                int.class,
                String.class,
                a,
                new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        int i = 1 / 0;
                        //super.beforeHookedMethod(param);
                    }

                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.e( "karorkefz", "歌曲-find" );

                    }
                } );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.detailnew.controller.f",
                classLoader,
                "a",
                new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //super.beforeHookedMethod(param);
                    }

                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.e( "karorkefz", "歌曲-find" );
                        int i = 1 / 0;
                    }
                } );
    }
}
