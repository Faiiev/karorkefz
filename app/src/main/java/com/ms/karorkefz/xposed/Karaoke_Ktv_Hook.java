package com.ms.karorkefz.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ms.karorkefz.thread.MyThread;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.FileUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;
import com.ms.karorkefz.view.RoomPasswordDialogViewAdd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

class Karaoke_Ktv_Hook extends RoomPasswordDialogViewAdd {
    Config config;
    Adapter adapter;
    boolean enableKTV_cS, enableKTVY, enableKTVIN, enableKTV_Robot, enableRecording;
    boolean start = false;
    boolean b = false;
    boolean passwordhook = false;
    Object hObject, h$26Object;
    WeakReference one;
    Class h, h$26;
    com.ms.karorkefz.thread.MyThread MyThread = new MyThread();
    List<Integer> colationList = new ArrayList<Integer>();
    private ClassLoader classLoader;
    private EditText inputEditText;
    private int i = 0000;

    Karaoke_Ktv_Hook(ClassLoader mclassLoader, Config config) throws IOException, JSONException {
        classLoader = mclassLoader;
        this.config = config;
        this.adapter = new Adapter( "Ktv" );
        enableKTV_cS = config.isOn( "enableKTV_cS" );
        enableKTVY = config.isOn( "enableKTVY" );
        enableKTVIN = config.isOn( "enableKTVIN" );
        enableKTV_Robot = config.isOn( "enableKTV_Robot" );
        enableRecording = config.isOn( "enableRecording" );
        colationList.add( 447952980 );//凯
        colationList.add( 65788827 );//1640135964
        colationList.add( 71190071 );//943970306
        colationList.add( 262975440 );//1538259575

        String h_class = adapter.getString( "h_class" );
        String h$26_class = adapter.getString( "h$26_class" );
        h = XposedHelpers.findClass( h_class, classLoader );
        h$26 = XposedHelpers.findClass( h$26_class, classLoader );
    }

    public void init() {
        LogUtil.d( "karorkefz", "进入ktv" );
        if (enableRecording) {
            new Karaoke_KaraScore_Hook( classLoader ).init();
        }
        if (enableKTVY) {
            String KTVY_Class = adapter.getString( "KTVY_Class" );
            String KTVY_Method = adapter.getString( "KTVY_Method" );
            XposedHelpers.findAndHookMethod( KTVY_Class,
                    classLoader,
                    KTVY_Method,// 被Hook的函数
                    new XC_MethodHook() {
                        boolean i = true;

                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "Ktv-find" );
                            Field quRenField = XposedHelpers.findField( param.thisObject.getClass(), "Q" );
                            final ImageView quRenButton = (ImageView) quRenField.get( param.thisObject );
                            quRenButton.setOnClickListener( new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LogUtil.d( "karorkefz", "Ktv-onclick" );
                                    if (i) {
                                        LogUtil.d( "karorkefz", "Ktv-onclick-if" );
                                        XposedHelpers.callMethod( param.thisObject, "w" );
                                        i = !i;
                                    } else {
                                        LogUtil.d( "karorkefz", "Ktv-onclick-if：else" );
                                        i = !i;
                                    }
                                }
                            } );
                        }
                    } );
        }
        if (enableKTV_cS) {
            String KTV_cS_Class = adapter.getString( "KTV_cS_Class" );
            String KTV_cS_Method = adapter.getString( "KTV_cS_Method" );
            XposedHelpers.findAndHookMethod( KTV_cS_Class,
                    classLoader,
                    KTV_cS_Method,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            param.setResult( false );
                        }
                    } );
        }
        if (enableKTVIN) {
            String GetKtvInfoRsp_Class = adapter.getString( "GetKtvInfoRsp_Class" );
            Class<?> GetKtvInfoRsp = XposedHelpers.findClass( GetKtvInfoRsp_Class, classLoader );
            XposedHelpers.findAndHookConstructor( h,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "调用函数准备" );
                            hObject = param.thisObject;
                        }
                    } );
            String RoomPasswordDialog_Class = adapter.getString( "RoomPasswordDialog_Class" );
            String RoomPasswordDialog_View_Method = adapter.getString( "RoomPasswordDialog_View_Method" );
            XposedHelpers.findAndHookMethod( RoomPasswordDialog_Class,
                    classLoader,
                    RoomPasswordDialog_View_Method,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "输入框准备" );
                            String inputFieldName = adapter.getString( "inputFieldName" );
                            Field inputField = XposedHelpers.findField( param.thisObject.getClass(), inputFieldName );
                            inputEditText = (EditText) inputField.get( param.thisObject );
                            LinearLayout inputEditTextParent = (LinearLayout) inputEditText.getParent();
                            LinearLayout Parent = (LinearLayout) inputEditTextParent.getParent();
                            Context context = AndroidAppHelper.currentApplication();
                            if (!passwordhook) {
                                passwordhook = true;
                                password( Parent, context );
                            }
                        }
                    } );
            XposedHelpers.findAndHookMethod( RoomPasswordDialog_Class,
                    classLoader,
                    "onClick",
                    View.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) {
                            start = false;
                            b = false;
                        }
                    } );

            String failure_Class = adapter.getString( "failure_Class" );
            String failure_Method = adapter.getString( "failure_Method" );
            XposedHelpers.findAndHookMethod( failure_Class,
                    classLoader,
                    failure_Method,
                    GetKtvInfoRsp,
                    int.class,
                    String.class,
                    int.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            String resultMsg = String.valueOf( param.args[2] );
                            if (enableKTVIN && start) {
                                try {
                                    String text = String.format( "%04d", i );
                                    LogUtil.d( "karorkefz", "密码:" + text );
                                    if (resultMsg.equals( "需要密码" )) {
                                        b = true;
                                    }
                                    if (resultMsg.equals( "密码不正确" )) {
                                        LogUtil.d( "karorkefz", "密码不正确" );
                                        config.setPassword( "password", i );
                                        try {
                                            inputEditText.setText( text );
                                        } catch (Exception e) {
                                            LogUtil.d( "karorkefz", e.toString() );
                                        }

                                        XposedHelpers.callMethod( h$26Object, "a", text );
                                        i++;
                                    }
                                    if (resultMsg.equals( "null" ) && b) {
                                        b = false;
                                        i--;
                                        if (i == -001) return;
                                        text = String.format( "%04d", i );
                                        config.setPassword( "password", i );
                                        String filetext = TimeHook.SimpleDateFormat_Time() + " : " + text;
                                        FileUtil.writeFileSdcardFile( "/txt/password.txt", filetext );
                                        try {
                                            inputEditText.setText( text );
                                        } catch (Exception e) {
                                            LogUtil.d( "karorkefz", e.toString() );
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }
                    } );
        }
        if (enableKTV_Robot) {
            String one_Class = adapter.getString( "one_Class" );
            String one_Method = adapter.getString( "one_Method" );
            XposedHelpers.findAndHookMethod( one_Class,
                    classLoader,
                    one_Method,
                    WeakReference.class,
                    String.class,
                    String.class,
                    int.class,
                    ArrayList.class,
                    String.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", String.valueOf( param.args[0] ) );
                            LogUtil.d( "karorkefz", String.valueOf( param.args[1] ) );
                            LogUtil.d( "karorkefz", String.valueOf( param.args[2] ) );
                            LogUtil.d( "karorkefz", String.valueOf( param.args[3] ) );
                            LogUtil.d( "karorkefz", String.valueOf( param.args[4] ) );
                            LogUtil.d( "karorkefz", String.valueOf( param.args[5] ) );
                            one = (WeakReference) param.args[0];
                        }
                    } );
            String Robot_Class = adapter.getString( "Robot_Class" );
            String Robot_Method = adapter.getString( "Robot_Method" );
            XposedHelpers.findAndHookMethod( Robot_Class,
                    classLoader,
                    Robot_Method,// 被Hook的函数
                    List.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            List list = (List) param.args[0];
                            lists( list );
                        }
                    } );

        }
    }


    public void startOnClick() throws Exception {
        start = true;
        b = true;
        i = 0000;
        String text = String.format( "%04d", i );
        h$26Object = XposedHelpers.newInstance( h$26, hObject );
        XposedHelpers.callMethod( h$26Object, "a", text );
    }

    public void proceedOnClick() throws Exception {
        start = true;
        b = true;
        i = config.getPassword( "password" );
        String text = String.format( "%04d", i );
        h$26Object = XposedHelpers.newInstance( h$26, hObject );
        XposedHelpers.callMethod( h$26Object, "a", text );
    }

    private <c> void lists(List list) throws JSONException, IllegalAccessException, InstantiationException {
        List<c> list2 = list;
        if (list != null) {
            if (!list.isEmpty()) {
                int size = list.size() - 1;
                for (; size >= 0; size--) {
                    c cVar2 = (c) list2.get( size );
                    Gson cGson = new Gson();
                    String cVar2_jsonString = cGson.toJson( cVar2 );
                    LogUtil.d( "karorkefz", cVar2_jsonString );
                    JSONObject cVar2_JSONObject = new JSONObject( cVar2_jsonString );
                    String e_jsonString = cVar2_JSONObject.getString( "e" );
                    if (e_jsonString != null) {
                        JSONObject e_jsonObject = new JSONObject( e_jsonString );
                        int uid = Integer.parseInt( e_jsonObject.getString( "uid" ) );
                        if (uid != 0) {
                            String nick = e_jsonObject.getString( "nick" );
                            int i2 = cVar2_JSONObject.getInt( "a" );
                            if (i2 == 1) {
                                String h = cVar2_JSONObject.getString( "h" );
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                            } else if (i2 == 3) {
                                String h = cVar2_JSONObject.getString( "h" );
                                String text = "@" + nick + "  " + "欢迎。";
                                send( uid, text );
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                            } else if (i2 == 29) {
                                String i_jsonString = cVar2_JSONObject.getString( "i" );
                                String g_jsonString = cVar2_JSONObject.getString( "g" );
                                JSONObject i_jsonObject = new JSONObject( i_jsonString );
                                JSONObject g_jsonObject = new JSONObject( g_jsonString );
                                String g_nick = g_jsonObject.getString( "nick" );
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
                                String text = "@" + nick + "   " + "谢谢 " + nick + " 送给" + g_nick + "的 “" + GiftName + "” *  " + GiftNum;
                                send( RealUid, text );
                                LogUtil.d( "karorkefz", "RealUid:" + RealUid + "  送出" + GiftName + " * " + GiftNum );
                            } else if (i2 == 31) {
                                String h = cVar2_JSONObject.getString( "h" );
                                if (h.indexOf( "删除" ) != -1) {
                                    System.out.println( "存在包含关系，因为返回的值不等于-1" );
                                    return;
                                }
                                if (h.indexOf( "置顶" ) != -1) {
                                    System.out.println( "存在包含关系，因为返回的值不等于-1" );
                                    return;
                                }
                                String text = "@" + nick + "  " + "感谢" + h;
                                send( uid, text );
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                            } else if (i2 == 37) {
                                String w_jsonString = cVar2_JSONObject.getString( "w" );
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

    private <h> void send(int uid, String text) throws InstantiationException, IllegalAccessException, JSONException {
        if (colationList.contains( uid )) {
            LogUtil.d( "karorkefz", "过滤名单，存在:" + uid + "  " + text );
            return;
        }
        String send_object_Class = adapter.getString( "send_object_Class" );
        Class uClass = XposedHelpers.findClass( send_object_Class, classLoader );
        Object uObject = uClass.newInstance();
        Class KC = XposedHelpers.findClass( "com.tencent.karaoke.common.KaraokeContext", classLoader );
        Object KCObject = KC.newInstance();
        Object getRoomController = XposedHelpers.callMethod( KCObject, "getRoomController" );
        Object b = XposedHelpers.callMethod( getRoomController, "b" );
        Gson bGson = new Gson();
        String b_jsonString = bGson.toJson( b );
        JSONObject b_JSONObject = new JSONObject( b_jsonString );
        String strRoomId = b_JSONObject.getString( "strRoomId" );
        String strShowId = b_JSONObject.getString( "strShowId" );
        final ArrayList five = new ArrayList();
        five.add( Long.valueOf( uid ) );
        String six = text;
        LogUtil.d( "karorkefz", "send:" + one );
        if (one != null) {
            MyThread.Ktv_init( uObject, one, strRoomId, strShowId, 1, five, six );
        }
    }
}