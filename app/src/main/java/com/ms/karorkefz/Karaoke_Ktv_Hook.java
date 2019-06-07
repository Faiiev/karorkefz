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
    boolean enableKTV_cS = xSharedPreferences.getBoolean( "enableKTV_cS", true );
    boolean enableKTVY = xSharedPreferences.getBoolean( "enableKTVY", true );
    boolean enableKTVIN = xSharedPreferences.getBoolean( "enableKTVIN", true );
    Object h$26Object;
    private ClassLoader classLoader;
    private EditText inputEditText;
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
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            param.setResult( false );
                            //返回false
                            //KtvPageViewPager > canScroll
                        }
                    } );
        }
        //歌房密码
        if (enableKTVIN) {
            //参数准备
            final Class h$26 = XposedHelpers.findClass( "com.tencent.karaoke.module.ktv.ui.h$26", classLoader );
            Class<?> GetKtvInfoRsp = XposedHelpers.findClass( "proto_room.GetKtvInfoRsp", classLoader );
            XposedHelpers.findAndHookMethod( h$26,
                    "a",// 被Hook的函数
                    String.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.v( "karorkefz", "调用函数准备" );
                            h$26Object = param.thisObject;
                        }
                    } );
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.widget.RoomPasswordDialog",
                    classLoader,
                    "a",// 被Hook的函数
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Log.v( "karorkefz", "输入框准备" );
                            Field inputField = XposedHelpers.findField( param.thisObject.getClass(), "d" );
                            inputEditText = (EditText) inputField.get( param.thisObject );
                        }
                    } );
            //监听密码错误进行下次调用
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.ktv.ui.h$17",
                    classLoader,
                    "a",// 被Hook的函数
                    GetKtvInfoRsp,
                    int.class,
                    String.class,
                    int.class,
                    new XC_MethodHook() {
                        boolean b = false;
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            //mGetKtvRoomInfoListener -> onGetKtvRoomInfo -> resultCode
                            String resultMsg = String.valueOf( param.args[2] );
                            int resultCode = (int) param.args[1];
                            if (enableKTVIN) {
                                try {
                                    String text = String.format( "%04d", i );
                                    if (resultMsg.equals( "需要密码" )) {
                                        i = 0000;
                                        b = true;
                                    }
                                    if (resultMsg.equals( "密码不正确" )) {
                                        Log.d( "karorkefz", "密码不正确" );
                                        XposedHelpers.callMethod( h$26Object, "a", text );
                                        i++;
                                    }
                                    if (resultMsg.equals( "null" ) && b) {
                                        b = false;
                                        i--;
                                        text = String.format( "%04d", i );
                                        String filetext = TimeHook.SimpleDateFormat_Time() + " : " + text;
                                        SaveFile.writeFileSdcardFile( "password", filetext );
                                        inputEditText.setText( text );
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }
                    } );
        }
    }
}
