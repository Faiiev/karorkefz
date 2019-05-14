package com.ms.karorkefz;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

class Karaoke_Ktv_Hook {
    XSharedPreferences xSharedPreferences = new XSharedPreferences( "com.ms.karorkefz" );
    boolean enableKTVY = xSharedPreferences.getBoolean( "enableKTVY", true );
    boolean enableKTVIN = xSharedPreferences.getBoolean( "enableKTVIN", true );
    boolean enableKTV_cS = xSharedPreferences.getBoolean( "enableKTV_cS", true );
    private ClassLoader classLoader;
    private EditText inputEditText;
    private View view;
    private Object ChickObject;
    private int i = 0000;

    Karaoke_Ktv_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        Log.e( "karorkefz", "进入ktv" );
        if (false) {
            //飘屏
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.ui.h$2",
                    classLoader,
                    "a",// 被Hook的函数
                    boolean.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                            Log.e( "karorkefz", "Ktv-监听a" );
                            Log.e( "karorkefz", String.valueOf( param.args[0] ) );
//                        param.setResult( false );
//                        XposedHelpers.callMethod( param.thisObject,"k" ,false);
//                        Field EnField = XposedHelpers.findField( param.thisObject.getClass(), "E" );
//                        boolean Eboolean = (boolean) EnField.get( param.thisObject );
//                        Eboolean = true;
                        }
                    } );
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.widget.comment.b",
                    classLoader,
                    "k",// 被Hook的函数
                    boolean.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                            Log.e( "karorkefz", "Ktv-监听k" );
                            i = 1 / 0;
//                        XposedHelpers.callMethod( param.thisObject,"k" ,false);
//                        Field EnField = XposedHelpers.findField( param.thisObject.getClass(), "E" );
//                        boolean Eboolean = (boolean) EnField.get( param.thisObject );
//                        Eboolean = true;
                        }
                    } );
        }
        //长按语音席改开关
        if (enableKTVY) {
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.ui.h",
                    classLoader,
                    "P",// 被Hook的函数
                    new XC_MethodHook() {
                        boolean i = true;

                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            Log.e( "karorkefz", "Ktv-find" );
                            Field quRenField = XposedHelpers.findField( param.thisObject.getClass(), "P" );
                            final ImageView quRenButton = (ImageView) quRenField.get( param.thisObject );
                            quRenButton.setOnClickListener( new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( "karorkefz", "Ktv-onclick" );
                                    if (i) {
                                        Log.e( "karorkefz", "Ktv-onclick-if" );
                                        XposedHelpers.callMethod( param.thisObject, "v" );
                                        i = !i;
                                    } else {
                                        Log.e( "karorkefz", "Ktv-onclick-if：else" );
                                        i = !i;
                                    }
                                }
                            } );
                        }
                    } );
        }
        //禁止滑动
        if (enableKTV_cS) {
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.ui.l$d",
                    classLoader,
                    "a",// 被Hook的函数
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {

                        }

                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            param.setResult( false );
                            //返回false
                            //KtvPageViewPager > canScroll
                        }
                    } );
        }
        //歌房密码
        if (enableKTVIN) {
            Class<?> network_h = XposedHelpers.findClass( "com.tencent.karaoke.common.network.h", classLoader );
            Class<?> network_i = XposedHelpers.findClass( "com.tencent.karaoke.common.network.i", classLoader );
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.a.u",
                    classLoader,
                    "onReply",// 被Hook的函数
                    network_h,
                    network_i,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.i( "karorkefz", "network.i" );
                            try {
                                String value = (String) XposedHelpers.callMethod( param.args[1], "b" );
                                String text = String.format( "%04d", i );
                                if (value.equals( "需要密码" )) {
                                    i = 0000;
                                }
                                if (value.equals( "密码不正确" )) {
                                    inputEditText.setText( text );
                                    XposedHelpers.callMethod( ChickObject, "onClick", view );
                                    i++;
                                }
                                inputEditText.setText( text );
                            } catch (Exception e) {

                            }
                        }
                    } );
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.widget.RoomPasswordDialog",
                    classLoader,
                    "a",// 被Hook的函数
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.e( "karorkefz", "进入a" );
                            Field inputField = XposedHelpers.findField( param.thisObject.getClass(), "d" );
                            inputEditText = (EditText) inputField.get( param.thisObject );
                            ChickObject = param.thisObject;
                        }
                    } );
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.widget.RoomPasswordDialog",
                    classLoader,
                    "onClick",// 被Hook的函数
                    View.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod( param );
                            view = (View) param.args[0];
                            Log.e( "karorkefz", "检测onclick" );
                        }
                    } );
        }


//        XposedBridge.hookAllMethods( mycls,
//                "P",
//                new XC_MethodHook() {
//                    boolean i = true;
//                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
//                        Log.e("karorkefz","Ktv-find");
//                        Field quRenField = XposedHelpers.findField( param.thisObject.getClass(), "P" );
//                        final ImageView quRenButton = (ImageView) quRenField.get( param.thisObject );
//                        quRenButton.setOnClickListener( new Button.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Log.e("karorkefz","Ktv-onclick");
//                                if (i) {
//                                    Log.e("karorkefz","Ktv-onclick-if");
//                                    XposedHelpers.callMethod( param.thisObject, "v" );
//                                    i = !i;
//                                } else {
//                                    Log.e("karorkefz","Ktv-onclick-if：else");
//                                    i = !i;
//                                }
//                            }
//                        } );
//                    }
//                } );
    }
}
