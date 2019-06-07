package com.ms.karorkefz;

import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;


class Karaoke_Live_Hook {
    XSharedPreferences xSharedPreferences = new XSharedPreferences( "com.ms.karorkefz" );
    boolean enableLIVE_cS = xSharedPreferences.getBoolean( "enableLIVE_cS", true );
    boolean enableLIVE_W = xSharedPreferences.getBoolean( "enableLIVE_W", true );
    boolean enableLIVE_Gift = xSharedPreferences.getBoolean( "enableLIVE_Gift", true );
    String enableLIVE_W_N = (String) xSharedPreferences.getString( "enableLIVE_W_N", "欢迎" );
    String two, three, six;
    Object uObject;
    com.ms.karorkefz.MyThread MyThread = new MyThread();
    XSingleThreadPool shot_XSingleThreadPool = new XSingleThreadPool();
    private Class LiveFragment;
    private ClassLoader classLoader;

    Karaoke_Live_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
    }

    public void init() {
        Log.e( "karorkefz", "进入Live" );
        final Class i = XposedHelpers.findClass( "com.tencent.karaoke.module.live.common.i", classLoader );
        final Class k = XposedHelpers.findClass( "com.tencent.karaoke.module.live.common.k", classLoader );
        LiveFragment = XposedHelpers.findClass( "com.tencent.karaoke.module.live.ui.LiveFragment", classLoader );

        Class GiftInfo = XposedHelpers.findClass( "com.tencent.karaoke.module.live.common.GiftInfo", classLoader );
        Class UserInfo = XposedHelpers.findClass( "PROTO_UGC_WEBAPP.UserInfo", classLoader );
        //禁止滑动
        if (enableLIVE_cS) {
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.live.ui.LivePageViewPager",
                    classLoader,
                    "canScroll",// 被Hook的函数
                    View.class,
                    boolean.class,
                    int.class,
                    int.class,
                    int.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            param.setResult( null );
                            //返回false
                            //KtvPageViewPager > canScroll
                        }
                    } );
        }
        if (enableLIVE_W) {
            //自动欢迎
            //准备参数
            Class UserInfoCacheData = XposedHelpers.findClass( "com.tencent.karaoke.common.database.entity.user.UserInfoCacheData", classLoader );
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.live.business.ag",
                    classLoader,
                    "a",
                    String.class,
                    UserInfoCacheData,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            two = String.valueOf( param.args[2] );
                            three = String.valueOf( param.args[5] );
                        }
                    } );
            //得数据信息
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.live.common.j",
                    classLoader,
                    "a",// 被Hook的函数
                    i,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            Log.v( "karorkefz", "common.j.a" );
                            if (enableLIVE_W) {
                                Object z = param.args[0];
                                Object c = XposedHelpers.callMethod( z, "c" );//名字
                                Object d = XposedHelpers.callMethod( z, "d" );//uid
                                String uid = String.valueOf( d );
                                String name = String.valueOf( c );
                                Log.v( "karorkefz", "common.j进入房间： " + uid + name );
                                if (two != null) {
                                    send( uid, name );
                                }
                            }
                        }
                    } );
        }
        //礼物监听
        if (enableLIVE_Gift) {
            XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.giftpanel.animation.widget.GiftUserBar",
                    classLoader,
                    "a",// 被Hook的函数
                    GiftInfo,
                    UserInfo,
                    UserInfo,
                    String.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            Log.i( "karorkefz", "第1个： " + param.args[0] );
                            Log.i( "karorkefz", "第2个： " + param.args[1] );
                            Log.i( "karorkefz", "第3个： " + param.args[2] );
                            Log.i( "karorkefz", "第4个： " + param.args[3] );
                            Object one = param.args[0];
                            Field GiftNumField = XposedHelpers.findField( one.getClass(), "GiftNum" );
                            int GiftNum = (int) GiftNumField.get( one );
                            Field GiftPriceField = XposedHelpers.findField( one.getClass(), "GiftPrice" );
                            int GiftPrice = (int) GiftPriceField.get( one );
                            Log.i( "karorkefz", "数量： " + GiftNum + "   价格：" + GiftPrice );
                            if ((GiftNum * GiftPrice) >= 1000) {
                                shot_XSingleThreadPool.add( new Runnable() {
                                    public void run() {
                                        Log.e( "karorkefz", "截图线程:" + TimeHook.SimpleDateFormat_Time() );
                                        Shot.getActivity();//截图
                                    }
                                } );
                            }
                            Class KaraokeContext = XposedHelpers.findClass( "com.tencent.karaoke.common.KaraokeContext", classLoader );
                            Object KaraokeContextObject = KaraokeContext.newInstance();
                            Object getLoginManager = XposedHelpers.callMethod( KaraokeContextObject, "getLoginManager" );
                            Object CurrentUserInfo = XposedHelpers.callMethod( getLoginManager, "getCurrentUserInfo" );
                            Field bField = XposedHelpers.findField( CurrentUserInfo.getClass(), "b" );
                            long blong = (long) bField.get( CurrentUserInfo );
                            Field cField = XposedHelpers.findField( CurrentUserInfo.getClass(), "c" );
                            Object clong = cField.get( CurrentUserInfo );
                            Log.i( "karorkefz", "CurrentUserInfo" );
                            Log.i( "karorkefz", "CurrentUserInfo 1:" + blong );
                            Log.i( "karorkefz", "CurrentUserInfo 2:" + clong );
                        }
                    } );
        }
    }

    private <LiveFragment> void send(String uid, String g) throws InstantiationException, IllegalAccessException {
        LiveFragment LiveFragmentObeject1 = (LiveFragment) LiveFragment.newInstance();
        Object LiveFragmentObeject = LiveFragment.newInstance();
        //对象
        Class uClass = XposedHelpers.findClass( "com.tencent.karaoke.module.ktv.a.u", classLoader );
        uObject = uClass.newInstance();
        //第一个参数
        WeakReference<Object> one = new WeakReference<>( XposedHelpers.callMethod( LiveFragmentObeject, "q", LiveFragmentObeject1 ) );
        //第五个参数 uid
        final ArrayList five = new ArrayList();
        five.add( Long.valueOf( uid ) );
        //第六个参数 文本内容
        six = "@" + g + enableLIVE_W_N;
        Log.i( "karorkefz", "send:" + g );
        MyThread.Live_init( uObject, one, two, three, 1, five, six );
    }
}