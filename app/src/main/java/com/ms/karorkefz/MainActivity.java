package com.ms.karorkefz;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class MainActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //        setContentView( R.layout.activity_main );
        getPreferenceManager().setSharedPreferencesMode( MODE_WORLD_READABLE );
        addPreferencesFromResource( R.xml.settings );
        findPreference( "version" ).setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent();
                intent.setAction( "android.intent.action.VIEW" );
                intent.setData( Uri.parse( "http://60.205.227.119/karorkefz" ) );
                MainActivity.this.startActivity( intent );
                return true;
            }
        } );
        findPreference( "Contact" ).setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                /****************
                 *
                 * 发起添加群流程。群号：全名辅助(1004952748) 的 key 为： AF4gXBHuhdkO2CsIrTFJ-rHYhL5MDtQp
                 * 调用 joinQQGroup(AF4gXBHuhdkO2CsIrTFJ-rHYhL5MDtQp) 即可发起手Q客户端申请加群 全名辅助(1004952748)
                 *
                 * @param key 由官网生成的key
                 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
                 ******************/
                String key = "AF4gXBHuhdkO2CsIrTFJ-rHYhL5MDtQp";
                Intent intent = new Intent();
                intent.setData( Uri.parse( "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key ) );
                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity( intent );
                    return true;
                } catch (Exception e) {
                    // 未安装手Q或安装的版本不支持
                    Toast.makeText( MainActivity.this, "未安装QQ或者QQ版本不支持", Toast.LENGTH_SHORT ).show();
                    return false;
                }
            }
        } );
        findPreference( "github" ).setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent();
                intent.setAction( "android.intent.action.VIEW" );
                intent.setData( Uri.parse( "https://github.com/jiumoshou/karorkefz" ) );
                MainActivity.this.startActivity( intent );
                return true;
            }
        } );
        Preference preference = findPreference( "statue" );
        if (HookStatue.isEnabled()) preference.setSummary( "模块已激活" );
        else if (HookStatue.isExpModuleActive( this )) preference.setSummary( "太极已激活" );
        else {
            preference.setSummary( "模块未激活" );
            preference.setOnPreferenceClickListener( new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference1) {
                    Intent t = new Intent( "me.weishu.exp.ACTION_MODULE_MANAGE" );
                    t.setData( Uri.parse( "package:" + "com.ms.karorkefz" ) );
                    t.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    try {
                        MainActivity.this.startActivity( t );
                    } catch (ActivityNotFoundException e) {
                        // TaiChi not installed.
                        return false;
                    }
                    return true;
                }
            } );
        }
    }
}
