package com.ms.karorkefz.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.thread.XSingleThreadPool;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Karaoke_User_Hook {
    Adapter adapter;
    boolean kViewhook = false;
    ArrayList vctUserList;
    WeakReference four;
    XSingleThreadPool xSingleThreadPool = new XSingleThreadPool();
    private ClassLoader classLoader;

    Karaoke_User_Hook(ClassLoader mclassLoader) throws JSONException {
        classLoader = mclassLoader;
        this.adapter = new Adapter( "User" );
    }

    public <WebappGetFollowerListRsp> void init() throws JSONException {
        LogUtil.d( "karorkefz", "user" );
        String h_Class = adapter.getString( "h_Class" );
        Class h = XposedHelpers.findClass( h_Class, classLoader );
        String i_Class = adapter.getString( "i_Class" );
        Class i = XposedHelpers.findClass( i_Class, classLoader );
        String WebappGetFollowerListRsp_Class = adapter.getString( "WebappGetFollowerListRsp_Class" );
        Class WebappGetFollowerListRsp = XposedHelpers.findClass( WebappGetFollowerListRsp_Class, classLoader );
        String View_Class = adapter.getString( "View_Class" );
        String View_Method = adapter.getString( "View_Method" );
        XposedHelpers.findAndHookMethod( View_Class,
                classLoader,
                View_Method,
                LayoutInflater.class,
                ViewGroup.class,
                Bundle.class,
                new XC_MethodHook() {
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                        String kFieldName = adapter.getString( "kFieldName" );
                        Field kField = XposedHelpers.findField( param.thisObject.getClass(), kFieldName );
                        View kView = (View) kField.get( param.thisObject );
                        if (!kViewhook) {
                            kViewhook = true;
                            RelativeLayout kViewParent = (RelativeLayout) kView.getParent();
                            LinearLayout Parent = (LinearLayout) kViewParent.getParent();
                            Context context = AndroidAppHelper.currentApplication();
                            FansView( Parent, context );
                        }
                    }
                }
        );
        String Fan_List_Class = adapter.getString( "Fan_List_Class" );
        String Fan_List_Method = adapter.getString( "Fan_List_Method" );
        XposedHelpers.findAndHookMethod( Fan_List_Class,
                classLoader,
                Fan_List_Method,
                h,
                i,
                new XC_MethodHook() {
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                        LogUtil.d( "karorkefz", "列表:" + param.args[0] );
                        LogUtil.d( "karorkefz", "列表:" + param.args[1] );
                        LogUtil.d( "karorkefz", "列表:" + param.getResult() );
                        Object hObject = param.args[0];
                        Object iObject = param.args[1];
                        String requestType_Method = adapter.getString( "requestType_Method" );
                        int requestType = (int) XposedHelpers.callMethod( hObject, requestType_Method );
                        LogUtil.d( "karorkefz", "列表requestType:" + requestType );
                        if (requestType == 5) {
                            String c_Method = adapter.getString( "c_Method" );
                            WebappGetFollowerListRsp c = (WebappGetFollowerListRsp) XposedHelpers.callMethod( iObject, c_Method );
                            LogUtil.d( "karorkefz", "列表c:" + c );
                            String vctUserListFieldName = adapter.getString( "vctUserListFieldName" );
                            Field vctUserListField = XposedHelpers.findField( WebappGetFollowerListRsp, vctUserListFieldName );
                            vctUserList = (ArrayList) vctUserListField.get( c );
                            LogUtil.d( "karorkefz", "列表vctUserList:" + vctUserList );
                        }
                    }
                } );
        String JceStruct_Class = adapter.getString( "JceStruct_Class" );
        Class JceStruct = XposedHelpers.findClass( JceStruct_Class, classLoader );
        String a_Class = adapter.getString( "a_Class" );
        Class a = XposedHelpers.findClass( a_Class, classLoader );
        XposedHelpers.findAndHookConstructor( a,
                String.class,
                String.class,
                JceStruct,
                WeakReference.class,
                Object[].class,
                new XC_MethodHook() {
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                        LogUtil.d( "karorkefz", "网络发送参数:" + param.getResult() );
                        LogUtil.d( "karorkefz", "网络发送参数，business.a:" + param.args[0] );
                        LogUtil.d( "karorkefz", "网络发送参数，business.a:" + param.args[1] );
                        LogUtil.d( "karorkefz", "网络发送参数，business.a:" + param.args[2] );
                        LogUtil.d( "karorkefz", "网络发送参数，business.a:" + param.args[3] );
                        LogUtil.d( "karorkefz", "网络发送参数，business.a:" + param.args[4] );
                        String one = "relation.rmfan";
                        if (param.args[0].equals( one )) {
                            Field lUidField = XposedHelpers.findField( param.args[2].getClass(), "lUid" );
                            long lUid = (long) lUidField.get( param.args[2] );
                            Field lFanUidField = XposedHelpers.findField( param.args[2].getClass(), "lFanUid" );
                            long lFanUid = (long) lFanUidField.get( param.args[2] );
                            LogUtil.d( "karorkefz", "移除粉丝网络发送参数，business.a:" + lUid );
                            LogUtil.d( "karorkefz", "移除粉丝网络发送参数，business.a:" + lFanUid );
                            four = (WeakReference) param.args[3];
                        }
                    }
                }
        );

    }

    private void FansView(LinearLayout Parent, Context context) {
        LinearLayout settingsItemRootLLayout = new LinearLayout( context );
        settingsItemRootLLayout.setOrientation( LinearLayout.VERTICAL );
        settingsItemRootLLayout.setLayoutParams( new AbsListView.LayoutParams( MATCH_PARENT, WRAP_CONTENT ) );
        LinearLayout settingsItemLinearLayout = new LinearLayout( context );
        settingsItemLinearLayout.setOrientation( LinearLayout.VERTICAL );
        settingsItemLinearLayout.setLayoutParams( new ViewGroup.LayoutParams( MATCH_PARENT, WRAP_CONTENT ) );
        LinearLayout itemHlinearLayout = new LinearLayout( context );
        itemHlinearLayout.setOrientation( LinearLayout.HORIZONTAL );
        itemHlinearLayout.setWeightSum( 1 );
        itemHlinearLayout.setGravity( Gravity.CENTER_VERTICAL );
        itemHlinearLayout.setClickable( true );
        Button delete = new Button( context );
        delete.setText( "一键移除全部粉丝" );
        delete.setTextColor( Color.rgb( 128, 128, 128 ) );
        delete.setBackgroundColor( Color.WHITE );
        delete.setGravity( Gravity.CENTER );
        delete.setPadding( 0, 0, 0, 0 );
        delete.setTextSize( 14 );
        delete.setOnClickListener( view -> {
            try {
                deleteOnClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } );
        itemHlinearLayout.addView( delete, new LinearLayout.LayoutParams( MATCH_PARENT, MATCH_PARENT, 1 ) );
        View lineView = new View( context );
        lineView.setBackgroundColor( 0xFFD5D5D5 );
        settingsItemLinearLayout.addView( itemHlinearLayout, new LinearLayout.LayoutParams( MATCH_PARENT, DpUtil.dip2px( context, 50 ) ) );
        settingsItemLinearLayout.addView( lineView, new LinearLayout.LayoutParams( MATCH_PARENT, DpUtil.dip2px( context, 6 ) ) );
        settingsItemRootLLayout.addView( settingsItemLinearLayout );
        settingsItemRootLLayout.setTag( BuildConfig.APPLICATION_ID );
        Parent.addView( settingsItemRootLLayout, 1 );
    }

    private <RelationUserInfo> void deleteOnClick() throws IllegalAccessException, InstantiationException, JSONException {
        Iterator<RelationUserInfo> it = vctUserList.iterator();
        while (it.hasNext()) {
            RelationUserInfo relationuserinfo = it.next();
            Field lUidField = XposedHelpers.findField( relationuserinfo.getClass(), "lUid" );
            long lUid = (long) lUidField.get( relationuserinfo );
            Field strNicknameField = XposedHelpers.findField( relationuserinfo.getClass(), "strNickname" );
            String strNickname = (String) strNicknameField.get( relationuserinfo );
            send( lUid );
            LogUtil.d( "karorkefz", "列表it: lUid: " + lUid + " strNickname: " + strNickname );
        }
    }

    private <WebappRmFanReq> void send(long lUid) throws InstantiationException, IllegalAccessException {
        String WebappRmFanReq_Class = adapter.getString( "WebappRmFanReq_Class" );
        Class WebappRmFanReq = XposedHelpers.findClass( WebappRmFanReq_Class, classLoader );
        String a_Class = adapter.getString( "a_Class" );
        Class aClass = XposedHelpers.findClass( a_Class, classLoader );
        String one = "relation.rmfan";
        String two = "71190071";
        WebappRmFanReq webapprmfanreq = (WebappRmFanReq) WebappRmFanReq.newInstance();
        XposedHelpers.setLongField( webapprmfanreq, "lUid", 71190071 );
        XposedHelpers.setLongField( webapprmfanreq, "lFanUid", lUid );
        WebappRmFanReq three = webapprmfanreq;
        Object[] five = new Object[0];
        Object a = XposedHelpers.newInstance( aClass, one, two, three, four, five );
        xSingleThreadPool.add( new Runnable() {
            public void run() {
                LogUtil.d( "karorkefz", "线程:" + TimeHook.SimpleDateFormat_Time() );
                XposedHelpers.callMethod( a, "b" );
            }
        }, 1000 );

    }
}
