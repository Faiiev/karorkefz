package com.ms.karorkefz.view;

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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class RoomPasswordDialogViewAdd {
    public void password(LinearLayout Parent, Context context) {
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
        TextView start = new TextView( context );
        start.setText( "开始" );
        start.setTextColor( Color.rgb( 128, 128, 128 ) );
        start.setBackgroundColor( Color.WHITE );
        start.setGravity( Gravity.CENTER_VERTICAL );
        start.setPadding( DpUtil.dip2px( context, 60 ), 0, 0, 0 );
        start.setTextSize( 18 );
        start.setOnClickListener( view -> {
            try {
                startOnClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } );


        TextView proceed = new TextView( context );
        proceed.setText( "继续" );
        proceed.setTextColor( Color.rgb( 128, 128, 128 ) );
        proceed.setBackgroundColor( Color.WHITE );
        proceed.setGravity( Gravity.CENTER_VERTICAL );
        proceed.setPadding( DpUtil.dip2px( context, 64 ), 0, DpUtil.dip2px( context, 60 ), 0 );
        proceed.setTextSize( 18 );
        proceed.setOnClickListener( view -> {
            try {
                proceedOnClick();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } );

        View itemlineView = new View( context );
        itemHlinearLayout.addView( start, new LinearLayout.LayoutParams( WRAP_CONTENT, MATCH_PARENT, 1 ) );
        itemHlinearLayout.addView( itemlineView, new LinearLayout.LayoutParams( 1, MATCH_PARENT ) );
        itemHlinearLayout.addView( proceed, new LinearLayout.LayoutParams( WRAP_CONTENT, MATCH_PARENT, 1 ) );

        View lineView = new View( context );
        settingsItemLinearLayout.addView( lineView, new LinearLayout.LayoutParams( MATCH_PARENT, 1 ) );
        settingsItemLinearLayout.addView( itemHlinearLayout, new LinearLayout.LayoutParams( MATCH_PARENT, DpUtil.dip2px( context, 50 ) ) );

        settingsItemRootLLayout.addView( settingsItemLinearLayout );
        settingsItemRootLLayout.setTag( BuildConfig.APPLICATION_ID );

        Parent.addView( settingsItemRootLLayout, 1 );
    }

    public void startOnClick() throws Exception {
    }

    public void proceedOnClick() throws Exception {
    }
}
