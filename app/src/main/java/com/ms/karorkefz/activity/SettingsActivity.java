package com.ms.karorkefz.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.R;
import com.ms.karorkefz.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.ms.karorkefz.util.Shot.getActivity;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    final String url = "http://60.205.227.119/karorkefz/";
    private ListPreference preference;

    public static void open(Context context) {
        try {
            Intent intent = new Intent( context, SettingsActivity.class );
            context.startActivity( intent );
        } catch (Exception e) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getPreferenceManager().setSharedPreferencesMode( MODE_WORLD_READABLE );
        addPreferencesFromResource( R.xml.settings );
        PreferenceManager.getDefaultSharedPreferences( this ).registerOnSharedPreferenceChangeListener( this );
        preference = (ListPreference) findPreference( "Log_Level" );
        preference.setOnPreferenceChangeListener( this );
        if (preference.getEntry() != null) {
            preference.setSummary( preference.getEntry() );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().setSharedPreferencesMode( MODE_WORLD_READABLE );
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences( this ).unregisterOnSharedPreferenceChangeListener( this );
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence[] entries = listPreference.getEntries();
            int index = listPreference.findIndexOfValue( (String) newValue );
            listPreference.setSummary( entries[index] );
            Log.d( "karorkefz", "onPreferenceChange run: s" + newValue );
            Toast.makeText( this, entries[index].toString(), Toast.LENGTH_LONG ).show();
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "show_icon":
                boolean showIcon = sharedPreferences.getBoolean( "show_icon", true );
                Log.e( "karorkefz", String.valueOf( showIcon ) );
                onShowIconChange( showIcon );
                break;
            case "Log":
                boolean log = sharedPreferences.getBoolean( "Log", true );
                Log.e( "karorkefz", "Log文件输出：" + String.valueOf( log ) );
                if (!log) {
                    DeleteLog();
                }
                break;
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        switch (preference.getKey()) {
            case "DeleteLog":
                DeleteLog();
                return true;
            case "DeleteApdate":
                DeleteApdate();
                return true;
            case "adapter":
                try {
                    getAdapter();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            case "update":
                getUpdate();
                return true;
        }
        return false;
    }

    private void DeleteLog() {
        String filePath$Name = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou/Log/Hook.txt";
        File file = new File( filePath$Name );
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e( "karorkefz", "删除日志文件:" + filePath$Name + "成功！" );
                Toast.makeText( this, "日志文件已删除", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( this, "删除日志文件:" + filePath$Name + "失败！", Toast.LENGTH_SHORT ).show();
            }
        } else {
            Toast.makeText( this, "删除日志文件：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT ).show();
        }
    }

    private void DeleteApdate() {
        String filePath$Name = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou/adapter.json";
        File file = new File( filePath$Name );
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e( "karorkefz", "删除适配文件:" + filePath$Name + "成功！" );
                Toast.makeText( this, "适配文件已删除，请重新获取", Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( this, "删除适配文件:" + filePath$Name + "失败！", Toast.LENGTH_SHORT ).show();
            }
        } else {
            Toast.makeText( this, "适配文件：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT ).show();
        }
    }

    private void onShowIconChange(boolean showIcon) {
        int state = showIcon ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        Log.e( "karorkefz", String.valueOf( state ) );
        ComponentName aliasName = new ComponentName( this, "com.ms.karorkefz.Main" );
        getPackageManager().setComponentEnabledSetting( aliasName, state, PackageManager.DONT_KILL_APP );
    }

    private void getAdapter() throws IOException, JSONException {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo( "com.tencent.karaoke", 0 );
            String KgVersionName = packageInfo.versionName;
            Log.v( "karorkefz", "全民K歌版本号：" + KgVersionName );
            if (KgVersionName.equals( "" )) {
                return;
            }
            String adapter = FileUtil.readFileSdcardFile( "/adapter.json" );
            if (adapter != null) {
                JSONObject jsonObject = new JSONObject( adapter );
                String adapterVersionName = jsonObject.optString( "version" );
                if (KgVersionName.equals( adapterVersionName )) {
                    Log.v( "karorkefz", "全民K歌版本号与配置版本号一致" );
                    getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "此版本已适配", Toast.LENGTH_SHORT ).show() );
                    return;
                }
            }
            new Thread( () -> {
                try {
                    getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "正在联网检查最新适配", Toast.LENGTH_SHORT ).show() );
                    HttpURLConnection adapterConn = (HttpURLConnection) new URL( url + "app/check/adapter.json" ).openConnection();
                    adapterConn.setConnectTimeout( 3000 );
                    adapterConn.setRequestMethod( "GET" );
                    if (adapterConn.getResponseCode() == 200) {
                        InputStream adapterInputStream = adapterConn.getInputStream();
                        byte[] adapterJsonBytes = convertIsToByteArray( adapterInputStream );
                        String adaperJson = new String( adapterJsonBytes );
                        if (adaperJson.length() > 0) {
                            getActivity().runOnUiThread( () -> {
                                try {
                                    JSONArray adapterJsonArray = new JSONArray( adaperJson );
                                    for (int j = 0; j < adapterJsonArray.length(); j++) {
                                        JSONObject adapterJsonObject = adapterJsonArray.getJSONObject( j );
                                        String versionJsonString = adapterJsonObject.getString( "version" );
                                        if (versionJsonString.equals( KgVersionName )) {
                                            new AlertDialog.Builder( getActivity() )
                                                    .setTitle( "发现配置文件" )
                                                    .setMessage( adapterJsonObject.optString( "version" ) )
                                                    .setNegativeButton( "取消", null )
                                                    .setPositiveButton( "适配", (dialogInterface, i) -> {
                                                        new Thread( () -> {
                                                            try {
                                                                getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "正在联网下载适配", Toast.LENGTH_SHORT ).show() );
                                                                HttpURLConnection adapterDownConn = (HttpURLConnection) new URL( url + "app/check/adapter/" + adapterJsonObject.optString( "name" ) + ".json" ).openConnection();
                                                                adapterDownConn.setConnectTimeout( 3000 );
                                                                adapterDownConn.setRequestMethod( "GET" );
                                                                if (adapterDownConn.getResponseCode() == 200) {
                                                                    InputStream inputStream = adapterDownConn.getInputStream();
                                                                    byte[] jsonBytes = convertIsToByteArray( inputStream );
                                                                    String json = new String( jsonBytes );
                                                                    if (json.length() > 0) {
                                                                        try {
                                                                            FileUtil.writeFileSdcardFile( "/adapter.json", json );
                                                                            getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "适配文件已保存", Toast.LENGTH_SHORT ).show() );
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        return;
                                                                    }
                                                                }
                                                                getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "下载适配文件出错", Toast.LENGTH_SHORT ).show() );
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "下载适配文件出错：" + e.getMessage(), Toast.LENGTH_LONG ).show() );
                                                            }
                                                        } ).start();
                                                        ;
                                                    } )
                                                    .create()
                                                    .show();

                                            return;
                                        }
                                    }
                                    getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "检查适配成功，当前已是最新适配", Toast.LENGTH_SHORT ).show() );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } );
                            return;
                        }
                    }
                    getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "检查适配时出错", Toast.LENGTH_SHORT ).show() );
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "检查适配时出错：" + e.getMessage(), Toast.LENGTH_LONG ).show() );
                }
            } ).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getUpdate() {
        new Thread( () -> {
            try {
                getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "正在联网检查最新版本", Toast.LENGTH_SHORT ).show() );
                HttpURLConnection conn = (HttpURLConnection) new URL( url + "app/check/version.json" ).openConnection();
                conn.setConnectTimeout( 3000 );
                conn.setRequestMethod( "GET" );
                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    byte[] jsonBytes = convertIsToByteArray( inputStream );
                    String json = new String( jsonBytes );
                    if (json.length() > 0) {
                        getActivity().runOnUiThread( () -> {
                            try {
                                JSONObject jsonObject = new JSONObject( json );
                                int versionCode = jsonObject.optInt( "code" );
                                int nowCode = BuildConfig.VERSION_CODE;
                                if (versionCode > nowCode) {
                                    new AlertDialog.Builder( getActivity() )
                                            .setTitle( "发现新版本" )
                                            .setMessage( jsonObject.optString( "name" ) )
                                            .setNegativeButton( "取消", null )
                                            .setPositiveButton( "下载", (dialogInterface, i) -> {
                                                Intent localIntent = new Intent( "android.intent.action.VIEW" );
                                                localIntent.setData( Uri.parse( url ) );
                                                startActivity( localIntent );
                                            } )
                                            .create()
                                            .show();
                                } else
                                    getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "检查更新成功，当前已是最新版本", Toast.LENGTH_SHORT ).show() );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } );
                        return;
                    }
                }
                getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "检查更新时出错", Toast.LENGTH_SHORT ).show() );
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().runOnUiThread( () -> Toast.makeText( getActivity(), "检查更新时出错：" + e.getMessage(), Toast.LENGTH_LONG ).show() );
            }
        } ).start();

    }

    private byte[] convertIsToByteArray(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read( buffer )) != -1) {
                baos.write( buffer, 0, length );
            }
            inputStream.close();
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
