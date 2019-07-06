package com.ms.karorkefz.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.ms.karorkefz.thread.Task;
import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.UrlUtil;


public class WebActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;

    public static void openUrl(Context context, String url) {
        try {
            Intent intent = new Intent( context, WebActivity.class );
            intent.putExtra( "url", url );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity( intent );
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Intent intent = getIntent();
        String url = intent.getStringExtra( "url" );

        try {
            FrameLayout rootFLayout = new FrameLayout( this );

            mProgressBar = initProgressBar();
            WebView webView = initWebView();
            if (!TextUtils.isEmpty( url )) {
                webView.loadUrl( url );
            }
            rootFLayout.addView( webView, new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
            rootFLayout.addView( mProgressBar, new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dip2px( this, 4 ) ) );
            setContentView( rootFLayout );
        } catch (RuntimeException e) {
            UrlUtil.openUrl( getApplicationContext(), url );
            Task.onMain( 100, this::onBackPressed );
        } catch (Exception | Error e) {
            UrlUtil.openUrl( getApplicationContext(), url );
            Task.onMain( 100, this::onBackPressed );
        }
    }

    private ProgressBar initProgressBar() {
        ProgressBar progressBar = new ProgressBar( this, null, android.R.attr.progressBarStyleHorizontal );
        progressBar.getIndeterminateDrawable().setColorFilter( Color.BLUE, android.graphics.PorterDuff.Mode.MULTIPLY );
        progressBar.setBackgroundColor( Color.TRANSPARENT );
        return progressBar;
    }

    private WebView initWebView() throws Exception {
        WebView webView = new WebView( this );

        webView.getSettings().setJavaScriptEnabled( true );
        webView.setWebViewClient( new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (TextUtils.isEmpty( url )) {
                    return super.shouldOverrideUrlLoading( view, url );
                }
                String lurl = url.toLowerCase();
                if (lurl.startsWith( "http://" ) || lurl.startsWith( "https://" )) {
                    if (lurl.endsWith( ".apk" ) || lurl.endsWith( ".zip" ) || lurl.endsWith( ".tar.gz" ) || lurl.contains( "pan.baidu.com/s/" )) {
                        UrlUtil.openUrl( WebActivity.this, url );
                        return true;
                    }
                    view.loadUrl( url );
                    return true;
                }
                UrlUtil.openUrl( WebActivity.this, url );
                return true;
            }
        } );
        webView.setWebChromeClient( new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged( view, newProgress );
                handleProgressChanged( newProgress );
            }
        } );
        return webView;
    }

    private void handleProgressChanged(int progress) {
        ProgressBar progressBar = mProgressBar;
        if (progress >= 100) {
            Task.onMain( 1000, () -> {
                if (progressBar.getVisibility() != View.GONE) {
                    progressBar.setVisibility( View.GONE );
                }
                progressBar.setProgress( 0 );
            } );
        } else {
            if (progressBar.getVisibility() != View.VISIBLE) {
                progressBar.setVisibility( View.VISIBLE );
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator animation = ObjectAnimator.ofInt( progressBar, "progress", progress );
            animation.setDuration( 600 );
            animation.setInterpolator( new DecelerateInterpolator() );
            animation.start();
        } else {
            progressBar.setProgress( progress );
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
}
