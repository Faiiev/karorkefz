package com.ms.karorkefz;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookToast implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals( "com.ms.karorkefz" )) {
            XposedBridge.log( "Loaded app: " + loadPackageParam.packageName );
            Class clazz = loadPackageParam.classLoader.loadClass( "com.ms.karorkefz.MainActivity" );

            XposedHelpers.findAndHookMethod( clazz, "toastMessage", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod( param );
                    XposedBridge.log( "您已被Hook" );
                }

                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult( "你已被劫持" );
                }
            } );
        }
    }
}