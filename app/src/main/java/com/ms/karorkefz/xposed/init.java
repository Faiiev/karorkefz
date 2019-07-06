package com.ms.karorkefz.xposed;

import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Log.LogUtil;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class init implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        LogUtil.v( "karorkefz", "进入init" );
        if (lpparam.packageName.equals( "com.ms.karorkefz" )) {
            findAndHookMethod( "com.ms.karorkefz.xposed.HookStatue", lpparam.classLoader, "isEnabled", XC_MethodReplacement.returnConstant( true ) );
            return;
        }
        Adapter adapter = new Adapter( "adapter" );
        String adapter_version = adapter.getString( "version" );
        if (adapter_version.equals( "false" )) {
            LogUtil.v( "karorkefz", "适配文件有误" );
            return;
        }
        if (lpparam.packageName.equals( "com.tencent.karaoke" )) {
            LogUtil.v( "karorkefz", "init-if包" );
            new KaraokeHook().init();
        }
    }
}
