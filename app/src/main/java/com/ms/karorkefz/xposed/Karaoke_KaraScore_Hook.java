package com.ms.karorkefz.xposed;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Log.LogUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_KaraScore_Hook {
    int i;
    private ClassLoader classLoader;

    Karaoke_KaraScore_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        LogUtil.d( "karorkefz", "KaraScore" );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.audiobasesdk.KaraScore",
                classLoader,
                "getAllScore",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.d( "karorkefz", "getAllScore: " + String.valueOf( param.getResult() ) );
                        Gson c3Gson = new Gson();
                        String c3_jsonString = c3Gson.toJson( param.getResult() );
                        LogUtil.d( "karorkefz", "getAllScore:原：  " + c3_jsonString );
                        int[] a = (int[]) param.getResult();
                        i = a.length;
                        for (int j = 0; j < i; j++) {
                            a[j] = 99;
                        }
                        i++;
                        Gson aGson = new Gson();
                        String a_jsonString = aGson.toJson( a );
                        LogUtil.d( "karorkefz", "getAllScore: 修： " + a_jsonString );
                        param.setResult( a );
                    }
                } );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.audiobasesdk.KaraScore",
                classLoader,
                "getLastScore",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (!String.valueOf( param.getResult() ).equals( "-1" )) {
                            LogUtil.d( "karorkefz", "getLastScore:" + String.valueOf( param.getResult() ) );
                            Gson c3Gson = new Gson();
                            String c3_jsonString = c3Gson.toJson( param.getResult() );
                            LogUtil.d( "karorkefz", "getLastScore: " + c3_jsonString );
                            param.setResult( 99 );
                        }
                    }
                } );
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.audiobasesdk.KaraScore",
                classLoader,
                "getTotalScore",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.d( "karorkefz", "getTotalScore: " + String.valueOf( param.getResult() ) );
                        if (i != 0) {
                            int j = 99 * i;
                            param.setResult( j );
                        }
                    }
                } );
    }
}
