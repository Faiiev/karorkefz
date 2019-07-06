package com.ms.karorkefz.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.view.SettingsView;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_Setting_Hook {
    private ClassLoader classLoader;

    Karaoke_Setting_Hook(ClassLoader mclassLoader) {

        classLoader = mclassLoader;
    }

    public void init() {
        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.config.ui.j",
                classLoader,
                "t",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.i( "karorkefz", "Karorke-进入设置" );
                        Field BField = XposedHelpers.findField( param.thisObject.getClass(), "r" );
                        View B = (View) BField.get( param.thisObject );
                        LinearLayout Parent = (LinearLayout) B.getParent();
                        Context context = AndroidAppHelper.currentApplication();

                        LinearLayout settingsItemRootLLayout = new LinearLayout( context );
                        settingsItemRootLLayout.setOrientation( LinearLayout.VERTICAL );
                        settingsItemRootLLayout.setLayoutParams( new AbsListView.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );
                        settingsItemRootLLayout.setPadding( 0, DpUtil.dip2px( context, 1 ), 0, 0 );


                        LinearLayout settingsItemLinearLayout = new LinearLayout( context );
                        settingsItemLinearLayout.setOrientation( LinearLayout.VERTICAL );
                        settingsItemLinearLayout.setLayoutParams( new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );


                        LinearLayout itemHlinearLayout = new LinearLayout( context );
                        itemHlinearLayout.setOrientation( LinearLayout.HORIZONTAL );
                        itemHlinearLayout.setWeightSum( 1 );
                        itemHlinearLayout.setBackgroundColor( Color.argb( 0, 0, 0, 0 ) );
                        itemHlinearLayout.setGravity( Gravity.CENTER_VERTICAL );
                        itemHlinearLayout.setClickable( true );
                        itemHlinearLayout.setOnClickListener( view -> new SettingsView( context ).showInDialog() );

                        TextView name = new TextView( context );
                        name.setText( "全民K歌辅助" );
                        name.setTextColor( Color.BLACK );
                        name.setGravity( Gravity.CENTER_VERTICAL );
                        name.setPadding( DpUtil.dip2px( context, 15 ), 0, 0, 0 );
                        name.setTextSize( 16 );

                        int defHPadding = DpUtil.dip2px( context, 18 );
                        TextView versionName = new TextView( context );
                        versionName.setText( BuildConfig.VERSION_NAME );
                        versionName.setGravity( Gravity.CENTER_VERTICAL );
                        versionName.setPadding( 0, 0, defHPadding, 0 );
                        versionName.setTextColor( Color.BLACK );
                        versionName.setTextSize( 16 );

                        itemHlinearLayout.addView( name, new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 1 ) );
                        itemHlinearLayout.addView( versionName, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT ) );

                        View lineView = new View( context );
                        lineView.setBackgroundColor( 0xFFD5D5D5 );
                        settingsItemLinearLayout.addView( lineView, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 1 ) );
                        settingsItemLinearLayout.addView( itemHlinearLayout, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px( context, 50 ) ) );

                        settingsItemRootLLayout.addView( settingsItemLinearLayout );
                        settingsItemRootLLayout.setTag( BuildConfig.APPLICATION_ID );

                        Parent.addView( settingsItemRootLLayout, 2 );
                    }
                } );
    }
}