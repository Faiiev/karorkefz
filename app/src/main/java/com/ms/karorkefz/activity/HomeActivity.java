package com.ms.karorkefz.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.R;
import com.ms.karorkefz.adapter.PreferenceAdapter;
import com.ms.karorkefz.xposed.HookStatue;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private PreferenceAdapter mListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home );


        ListView listView = (ListView) findViewById( R.id.list );
        List<PreferenceAdapter.Data> list = new ArrayList<>();
        list.add( new PreferenceAdapter.Data( "启动页广告关闭", "查看使用教程", "enableStart" ) );
        list.add( new PreferenceAdapter.Data( "移除粉丝", "查看使用教程", "enableFansDelete" ) );
        list.add( new PreferenceAdapter.Data( "歌房功能", "查看使用教程", "enableKTV" ) );
        list.add( new PreferenceAdapter.Data( "歌房滑动禁用", "查看使用教程", "enableKTV_cS" ) );
        list.add( new PreferenceAdapter.Data( "歌房语音席点击触发", "查看使用教程", "enableKTVY" ) );
        list.add( new PreferenceAdapter.Data( "歌房密码自动输入", "查看使用教程", "enableKTVIN" ) );
        list.add( new PreferenceAdapter.Data( "直播间功能", "查看使用教程", "enableLIVE" ) );
        list.add( new PreferenceAdapter.Data( "直播间滑动禁用", "查看使用教程", "enableLIVE_cS" ) );
        list.add( new PreferenceAdapter.Data( "直播间自动欢迎", "查看使用教程", "enableLIVE_W" ) );
        list.add( new PreferenceAdapter.Data( "直播间自动欢迎语", "查看使用教程", "enableLIVE_W_N" ) );
        list.add( new PreferenceAdapter.Data( "直播间礼物截屏", "查看使用教程", "enableLIVE_Gift" ) );

        String statue;
        if (HookStatue.isEnabled()) statue = "模块已激活";
        else if (HookStatue.isExpModuleActive( this )) statue = "太极已激活";
        else {
            statue = "模块未激活";
        }
        list.add( new PreferenceAdapter.Data( "状态", statue, "statue" ) );
        list.add( new PreferenceAdapter.Data( "项目地址", "https://github.com/jiumoshou/karorkefz", "github" ) );
        list.add( new PreferenceAdapter.Data( "联系Q群", "1004952748", "Contact" ) );
        list.add( new PreferenceAdapter.Data( "版本", BuildConfig.VERSION_NAME, "version" ) );
        mListAdapter = new PreferenceAdapter( list );
        listView.setAdapter( mListAdapter );
        listView.setOnItemClickListener( this );
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PreferenceAdapter.Data data = mListAdapter.getItem( i );
        String url = "http://60.205.227.119/karorkefz/";
        if (data == null || TextUtils.isEmpty( data.title )) {
            return;
        }
        if ("启动页广告关闭".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction" );
        } else if ("移除粉丝".equals( data.title )) {
//            WebActivity.openUrl( this, url + "Introduction/ktv.html" );
        } else if ("歌房功能".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/ktv.html" );
        } else if ("歌房语音席点击触发".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/ktv.html" );
        } else if ("歌房密码自动输入".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/ktv.html" );
        } else if ("歌房滑动禁用".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/ktv.html" );
        } else if ("直播间功能".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/live.html" );
        } else if ("直播间滑动禁用".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/live.html" );
        } else if ("直播间自动欢迎".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/live.html" );
        } else if ("直播间自动欢迎语".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/live.html" );
        } else if ("直播间礼物截屏".equals( data.title )) {
            WebActivity.openUrl( this, url + "Introduction/live.html" );
        } else if ("状态".equals( data.title )) {
            if ("模块未激活".equals( data.subTitle )) {
                statue();
            }
        } else if ("项目地址".equals( data.title )) {
            WebActivity.openUrl( this, data.subTitle );
        } else if ("联系Q群".equals( data.title )) {
            joinQQGroup();
        } else if ("版本".equals( data.title )) {
            Intent intent = new Intent();
            intent.setAction( "android.intent.action.VIEW" );
            intent.setData( Uri.parse( "http://60.205.227.119/karorkefz" ) );
            HomeActivity.this.startActivity( intent );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                SettingsActivity.open( this );
                return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private boolean statue() {
        Intent t = new Intent( "me.weishu.exp.ACTION_MODULE_MANAGE" );
        t.setData( Uri.parse( "package:" + "com.ms.karorkefz" ) );
        t.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        try {
            HomeActivity.this.startActivity( t );
        } catch (ActivityNotFoundException e) {
            return false;
        }
        return true;
    }

    private boolean joinQQGroup() {
        String key = "AF4gXBHuhdkO2CsIrTFJ-rHYhL5MDtQp";
        Intent intent = new Intent();
        intent.setData( Uri.parse( "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key ) );
        try {
            startActivity( intent );
            return true;
        } catch (Exception e) {
            Toast.makeText( HomeActivity.this, "未安装QQ或者QQ版本不支持", Toast.LENGTH_SHORT ).show();
            return false;
        }
    }

}

