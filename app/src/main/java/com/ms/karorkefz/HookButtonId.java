package com.ms.karorkefz;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookButtonId implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.v( "karorkefz", "进入HookButtonId" );
        if (lpparam.packageName.equals( "com.tencent.karaoke" )) {
            findAndHookMethod( View.class, "setOnClickListener", View.OnClickListener.class, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    View view = (View) param.thisObject;
                    //ImageView
                    String Str = null;
                    if (view instanceof TextView) {//也有可能是ImageView，所以得判断一下
                        Str = ((TextView) view).getText().toString();
                    }
                    int btnId = view.getId();
                    Log.e( "karorkefz", Str + " " + btnId );
                    Log.e( "karorkefz", Str + ";" + "Id:" + btnId );
                }
            } );
            new KaraokeHook().init();
        }

    }
}
