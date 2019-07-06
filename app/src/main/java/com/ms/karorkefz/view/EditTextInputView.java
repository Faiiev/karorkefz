package com.ms.karorkefz.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ms.karorkefz.util.DpUtil;


public class EditTextInputView extends DialogFrameLayout {

    public static final String DEFAULT_HIDDEN_TEXT = "欢迎";

    private EditText mInputView;

    public EditTextInputView(@NonNull Context context) {
        super( context );
        initView( context );
    }

    public EditTextInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        initView( context );
    }

    public EditTextInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initView( context );
    }

    private void initView(Context context) {
        LinearLayout rootLLayout = new LinearLayout( context );
        rootLLayout.setOrientation( LinearLayout.VERTICAL );

        mInputView = new EditText( context );
        mInputView.setPadding( 0, 0, 0, 0 );
        mInputView.setBackgroundColor( Color.TRANSPARENT );
        mInputView.setFocusable( true );
        mInputView.setFocusableInTouchMode( true );
        mInputView.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        int defHMargin = DpUtil.dip2px( context, 15 );
        int defTMargin = DpUtil.dip2px( context, 20 );
        int defBMargin = DpUtil.dip2px( context, 4 );
        layoutParam.setMargins( defHMargin, defTMargin, defHMargin, defBMargin );

        View lineView = new View( context );
        lineView.setBackgroundColor( 0xFF45C01A );

        rootLLayout.addView( mInputView, layoutParam );
        rootLLayout.addView( lineView, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px( context, 1 ) ) );

        LayoutParams rootLLayoutParams = new LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
        rootLLayoutParams.setMargins( defHMargin, defTMargin, defHMargin, DpUtil.dip2px( context, 20 ) );
        this.addView( rootLLayout, rootLLayoutParams );

        withPositiveButtonText( "确定" );
    }

    @NonNull
    public String getInput() {
        Editable ediable = mInputView.getText();
        if (ediable == null) {
            return "";
        }
        return ediable.toString();
    }

    public void setDefaultText(String text) {
        mInputView.setText( text );
        int len = text.length();
        mInputView.setSelection( len, len );
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post( () -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.showSoftInput( mInputView, InputMethodManager.SHOW_IMPLICIT );
        } );
    }

    @Override
    public String getDialogTitle() {
        return "输入欢迎语";
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
