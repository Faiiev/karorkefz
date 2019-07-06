package com.ms.karorkefz.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.adapter.PreferenceAdapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.Log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.PixelFormat.TRANSPARENT;
import static com.ms.karorkefz.view.EditTextInputView.DEFAULT_HIDDEN_TEXT;

public class SettingsView extends DialogFrameLayout implements AdapterView.OnItemClickListener {
    private List<PreferenceAdapter.Data> mSettingsDataList = new ArrayList<>();
    private PreferenceAdapter mListAdapter;
    private ListView mListView;

    public SettingsView(Context context) {
        super( context );

        LogUtil.e( "karorkefz", "进入设置页" );
        init( context );
    }

    private void init(Context context) {
        LinearLayout rootVerticalLayout = new LinearLayout( context );
        rootVerticalLayout.setOrientation( LinearLayout.VERTICAL );

        View lineView = new View( context );
        lineView.setBackgroundColor( TRANSPARENT );

        TextView settingsTitle = new TextView( context );
        settingsTitle.setTextSize( TypedValue.COMPLEX_UNIT_DIP, 18 );
        settingsTitle.setText( "全民K歌辅助  " + BuildConfig.VERSION_NAME );
        settingsTitle.setTextColor( Color.WHITE );
        settingsTitle.setTypeface( null, Typeface.BOLD );
        settingsTitle.setBackgroundColor( 0xFF1AAEE5 );
        int defHPadding = DpUtil.dip2px( context, 15 );
        int defVPadding = DpUtil.dip2px( context, 12 );
        settingsTitle.setPadding( defHPadding, defVPadding, defHPadding, defVPadding );

        mListView = new ListView( context );
        mListView.setDividerHeight( 0 );
        mListView.setOnItemClickListener( this );
        mListView.setPadding( defHPadding, 0, 2, 0 );
        mListView.setDivider( new ColorDrawable( Color.TRANSPARENT ) );
        Config config = new Config( context );
        mSettingsDataList.add( new PreferenceAdapter.Data( "启动页广告关闭", "启动页广告关闭", "enableStart", true, config.isOn( "enableStart" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "移除粉丝", "移除粉丝", "enableFansDelete", true, config.isOn( "enableFansDelete" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "歌曲评分", "歌曲评分", "enableRecording", true, config.isOn( "enableRecording" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "歌房功能", "歌房功能", "enableKTV", true, config.isOn( "enableKTV" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "歌房语音席点击触发", "歌房语音席点击触发", "enableKTVY", true, config.isOn( "enableKTVY" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "歌房密码自动输入", "歌房密码自动输入", "enableKTVIN", true, config.isOn( "enableKTVIN" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "歌房滑动禁用", "歌房滑动禁用", "enableKTV_cS", true, config.isOn( "enableKTV_cS" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "歌房机器人", "歌房机器人", "enableKTV_Robot", true, config.isOn( "enableKTV_Robot" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "直播间功能", "直播间功能", "enableLIVE", true, config.isOn( "enableLIVE" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "直播间滑动禁用", "直播间滑动禁用", "enableLIVE_cS", true, config.isOn( "enableLIVE_cS" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "直播间机器人", "直播间机器人", "enableLIVE_Robot", true, config.isOn( "enableLIVE_Robot" ) ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "直播间自动欢迎语", "直播间自动欢迎语", "enableLIVE_W_N" ) );
        mSettingsDataList.add( new PreferenceAdapter.Data( "直播间礼物截屏", "测试中", "enableLIVE_Gift", true, config.isOn( "enableLIVE_Gift" ) ) );
        mListAdapter = new PreferenceAdapter( mSettingsDataList );
        rootVerticalLayout.addView( settingsTitle );
        rootVerticalLayout.addView( lineView, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px( context, 2 ) ) );
        rootVerticalLayout.addView( mListView );
        this.addView( rootVerticalLayout );

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mListView.setAdapter( mListAdapter );
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Context context = getContext();
        final Config config = new Config( context );
        PreferenceAdapter.Data data = mListAdapter.getItem( position );
        if ("enableLIVE_W_N".equals( data.key )) {
            LogUtil.e( "karorkefz", "编辑欢迎语" );
            EditTextInputView passwordInputView = new EditTextInputView( context );
            if (!TextUtils.isEmpty( config.getEditText( data.key ) )) {
                passwordInputView.setDefaultText( DEFAULT_HIDDEN_TEXT );
            }
            passwordInputView.withOnDismissListener( v -> {
                EditTextInputView inputView = (EditTextInputView) v;
                String inputText = inputView.getInput();
                if (TextUtils.isEmpty( inputText )) {
                    config.setEditText( data.key, "" );
                    return;
                }
                if (DEFAULT_HIDDEN_TEXT.equals( inputText )) {
                    return;
                }
                config.setEditText( data.key, inputText );
            } ).showInDialog();
        } else {
            data.selectionState = !data.selectionState;
            config.setOn( data.key, data.selectionState );
            mListAdapter.notifyDataSetChanged();
        }
    }
}
