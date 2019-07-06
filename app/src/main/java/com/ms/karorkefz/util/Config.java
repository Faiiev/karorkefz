package com.ms.karorkefz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.util.Log.LogUtil;


public class Config {
    SharedPreferences sharedPreferences;

    public Config(Context context) {

        LogUtil.i( "karorkefz", "Config" );
        sharedPreferences = context.getSharedPreferences( BuildConfig.APPLICATION_ID + ".settings", Context.MODE_PRIVATE );
    }

    public boolean isOn(String key) {
        boolean b = sharedPreferences.getBoolean( key, true );
        return b;
    }

    public void setOn(String key, boolean on) {
        sharedPreferences.edit().putBoolean( key, on ).apply();
    }

    @Nullable
    public String getEditText(String key) {
        String enc = sharedPreferences.getString( key, "欢迎" );
        LogUtil.i( "karorkefz", "getEditText" + enc );
        if (TextUtils.isEmpty( enc )) {
            return null;
        }
        return enc;
    }

    public void setEditText(String key, String getEditText) {
        LogUtil.i( "karorkefz", "setEditText" + getEditText );
        sharedPreferences.edit().putString( key, getEditText ).apply();
    }

    public void setPassword(String key, int Password) {
        LogUtil.i( "karorkefz", "setPassword" + Password );
        sharedPreferences.edit().putInt( key, Password ).apply();
    }

    public int getPassword(String key) {
        int enc = sharedPreferences.getInt( key, 0 );
        LogUtil.i( "karorkefz", "getPassword" + enc );
        return enc;
    }
}
