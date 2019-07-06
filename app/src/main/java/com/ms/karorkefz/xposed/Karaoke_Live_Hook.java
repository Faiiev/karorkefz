package com.ms.karorkefz.xposed;

import android.view.View;

import com.google.gson.Gson;
import com.ms.karorkefz.thread.MyThread;
import com.ms.karorkefz.thread.XSingleThreadPool;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.Shot;
import com.ms.karorkefz.util.TimeHook;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;


class Karaoke_Live_Hook {
    Adapter adapter;
    boolean enableLIVE_cS, enableLIVE_Robot, enableLIVE_Gift;
    String enableLIVE_W_N, two, three, six;
    Object uObject;
    com.ms.karorkefz.thread.MyThread MyThread = new MyThread();
    XSingleThreadPool shot_XSingleThreadPool = new XSingleThreadPool();
    List<Integer> colationList = new ArrayList<Integer>();
    private Class LiveFragment;
    private ClassLoader classLoader;

    Karaoke_Live_Hook(ClassLoader mclassLoader, Config config) throws IOException, JSONException {

        classLoader = mclassLoader;
        this.adapter = new Adapter( "Live" );
        enableLIVE_cS = config.isOn( "enableLIVE_cS" );
        enableLIVE_Robot = config.isOn( "enableLIVE_Robot" );
        enableLIVE_Gift = config.isOn( "enableLIVE_Gift" );
        enableLIVE_W_N = config.getEditText( "enableLIVE_W_N" );
        colationList.add( 447952980 );//凯
        colationList.add( 65788827 );//1640135964
        colationList.add( 71190071 );//943970306

    }

    public void init() throws JSONException {
        LogUtil.d( "karorkefz", "进入Live" );
        String LiveFragment_Class = adapter.getString( "LiveFragment_Class" );
        LiveFragment = XposedHelpers.findClass( LiveFragment_Class, classLoader );
        String GiftInfo_Class = adapter.getString( "GiftInfo_Class" );
        Class GiftInfo = XposedHelpers.findClass( GiftInfo_Class, classLoader );
        String UserInfo_Class = adapter.getString( "UserInfo_Class" );
        Class UserInfo = XposedHelpers.findClass( UserInfo_Class, classLoader );
        if (enableLIVE_cS) {
            String LIVE_cS_Class = adapter.getString( "LIVE_cS_Class" );
            String LIVE_cS_Method = adapter.getString( "LIVE_cS_Method" );
            XposedHelpers.findAndHookMethod( LIVE_cS_Class,
                    classLoader,
                    LIVE_cS_Method,
                    View.class,
                    boolean.class,
                    int.class,
                    int.class,
                    int.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            param.setResult( null );
                        }
                    } );
        }
        if (enableLIVE_Robot) {
            String UserInfoCacheData_Class = adapter.getString( "UserInfoCacheData_Class" );
            Class UserInfoCacheData = XposedHelpers.findClass( UserInfoCacheData_Class, classLoader );
            String Robot_ready_Class = adapter.getString( "Robot_ready_Class" );
            String Robot_ready_Method = adapter.getString( "Robot_ready_Method" );
            XposedHelpers.findAndHookMethod( Robot_ready_Class,
                    classLoader,
                    Robot_ready_Method,
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
            String Robot_Class = adapter.getString( "Robot_Class" );
            String Robot_Method = adapter.getString( "Robot_Method" );
            XposedHelpers.findAndHookMethod( Robot_Class,
                    classLoader,
                    Robot_Method,
                    List.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            List list = (List) param.args[0];
                            lists( list );
                        }
                    } );
        }
        if (enableLIVE_Gift) {
            String Gift_Class = adapter.getString( "Gift_Class" );
            String Gift_Method = adapter.getString( "Gift_Method" );
            XposedHelpers.findAndHookMethod( Gift_Class,
                    classLoader,
                    Gift_Method,
                    GiftInfo,
                    UserInfo,
                    UserInfo,
                    String.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "第1个： " + param.args[0] );
                            LogUtil.d( "karorkefz", "第2个： " + param.args[1] );
                            LogUtil.d( "karorkefz", "第3个： " + param.args[2] );
                            LogUtil.d( "karorkefz", "第4个： " + param.args[3] );
                            Object one = param.args[0];
                            Field GiftNumField = XposedHelpers.findField( one.getClass(), "GiftNum" );
                            int GiftNum = (int) GiftNumField.get( one );
                            Field GiftPriceField = XposedHelpers.findField( one.getClass(), "GiftPrice" );
                            int GiftPrice = (int) GiftPriceField.get( one );
                            LogUtil.d( "karorkefz", "数量： " + GiftNum + "   价格：" + GiftPrice );
                            if ((GiftNum * GiftPrice) >= 300) {
                                shot_XSingleThreadPool.add( new Runnable() {
                                    public void run() {
                                        LogUtil.d( "karorkefz", "截图线程:" + TimeHook.SimpleDateFormat_Time() );
                                        Shot.activityShot( Shot.getActivity() );
                                    }
                                }, 1000 );
                            }
                            Class KaraokeContext = XposedHelpers.findClass( "com.tencent.karaoke.common.KaraokeContext", classLoader );
                            Object KaraokeContextObject = KaraokeContext.newInstance();
                            Object getLoginManager = XposedHelpers.callMethod( KaraokeContextObject, "getLoginManager" );
                            Object CurrentUserInfo = XposedHelpers.callMethod( getLoginManager, "getCurrentUserInfo" );
                            Field bField = XposedHelpers.findField( CurrentUserInfo.getClass(), "b" );
                            long blong = (long) bField.get( CurrentUserInfo );
                            Field cField = XposedHelpers.findField( CurrentUserInfo.getClass(), "c" );
                            Object clong = cField.get( CurrentUserInfo );
                            LogUtil.d( "karorkefz", "CurrentUserInfo" );
                            LogUtil.d( "karorkefz", "CurrentUserInfo 1:" + blong );
                            LogUtil.d( "karorkefz", "CurrentUserInfo 2:" + clong );
                        }
                    } );
        }
    }

    private <k> void lists(List list) throws JSONException, IllegalAccessException, InstantiationException {
        List<k> list2 = list;
        if (list != null) {
            if (!list.isEmpty()) {
                int size = list.size() - 1;
                for (; size >= 0; size--) {
                    k kVar2 = (k) list2.get( size );
                    Gson kGson = new Gson();
                    String kVar2_jsonString = kGson.toJson( kVar2 );
                    LogUtil.d( "karorkefz", kVar2_jsonString );
                    JSONObject kVar2_JSONObject = new JSONObject( kVar2_jsonString );
                    String e_jsonString = kVar2_JSONObject.getString( "e" );
                    if (e_jsonString != null) {
                        JSONObject e_jsonObject = new JSONObject( e_jsonString );
                        int uid = Integer.parseInt( e_jsonObject.getString( "uid" ) );
                        if (uid != 0) {
                            String nick = e_jsonObject.getString( "nick" );
                            int i2 = kVar2_JSONObject.getInt( "a" );
                            if (i2 == 1) {
                                String h = kVar2_JSONObject.getString( "h" );
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                            } else if (i2 == 2) {
                                String i_jsonString = kVar2_JSONObject.getString( "i" );
                                JSONObject i_jsonObject = new JSONObject( i_jsonString );
                                String GiftName = i_jsonObject.getString( "GiftName" );
                                int GiftNum = i_jsonObject.getInt( "GiftNum" );
                                int GiftPrice = i_jsonObject.getInt( "GiftPrice" );
                                int RealUid = i_jsonObject.getInt( "RealUid" );
                                if (GiftPrice * GiftNum < 300) {
                                    return;
                                }
                                if (RealUid == 0) {
                                    RealUid = uid;
                                }
                                String text = "@" + nick + " " + "谢谢送来的 “" + GiftName + "” *  " + GiftNum;
                                send( RealUid, text );
                                LogUtil.d( "karorkefz", "RealUid:" + RealUid + "  送出" + GiftName + " * " + GiftNum );
                            } else if (i2 == 3) {
                                String h = kVar2_JSONObject.getString( "h" );
                                String text = "@" + nick + " " + enableLIVE_W_N;
                                send( uid, text );
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                            } else if (i2 == 37) {
                                String w_jsonString = kVar2_JSONObject.getString( "w" );
                                JSONObject w_jsonJSONObject = new JSONObject( w_jsonString );
                                int b = w_jsonJSONObject.getInt( "b" );
                                if (b == 1) {
                                    String text = "@" + nick + " " + "感谢关注";
                                    send( uid, text );
                                } else if (b == 2) {
                                    String text = "@" + nick + " " + "感谢分享";
                                    send( uid, text );
                                } else if (b == 3) {
                                    String text = "@" + nick + " " + "感谢转发";
                                    send( uid, text );
                                }
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick );
                            }
                        }
                    }
                }
            }
        }
    }

    private <LiveFragment> void send(int uid, String text) throws InstantiationException, IllegalAccessException, JSONException {
        if (colationList.contains( uid )) {
            LogUtil.d( "karorkefz", "过滤名单，存在:" + uid + "  " + text );
            return;
        }
        LiveFragment LiveFragmentObeject1 = (LiveFragment) LiveFragment.newInstance();
        Object LiveFragmentObeject = LiveFragment.newInstance();
        String send_object_Class = adapter.getString( "send_object_Class" );
        Class uClass = XposedHelpers.findClass( send_object_Class, classLoader );
        uObject = uClass.newInstance();
        WeakReference<Object> one = new WeakReference<>( XposedHelpers.callMethod( LiveFragmentObeject, "q", LiveFragmentObeject1 ) );
        final ArrayList five = new ArrayList();
        five.add( Long.valueOf( uid ) );
        six = text;
        LogUtil.d( "karorkefz", "send:" + text );
        MyThread.Live_init( uObject, one, two, three, 1, five, six );
    }

}