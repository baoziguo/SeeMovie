package com.baozi.movie.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.baozi.movie.base.BaseActivity;
import com.baozi.seemovie.R;
import com.orhanobut.logger.Logger;

import butterknife.Bind;

/**
 * by:baozi
 * create on 2017/7/21 15:01
 */

public class VideoWebViewActivity extends BaseActivity {

    @Bind(R.id.webView)
    WebView webView;

    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web_view);
        initWebView();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                url = intent.getStringExtra(Intent.EXTRA_TEXT);
                Logger.d("share:" + url);
                if (url.contains("iqiyi")){
                    url = url.replace("//m", "//www");
                }else if (url.contains(".le.")){
                    url = url.replace("m.le.com/vplay_", "www.le.com/ptv/vplay/");
                }
                webView.loadUrl("http://65yw.2m.vc/chaojikan.php?url=" + url);
//                webView.loadUrl("http://www.wmxz.wang/video.php?url=" + url);
            }
        }

    }

    private void initWebView() {
        //是否可以缩放
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setBackgroundColor(Color.WHITE);
        webSettings.setSupportZoom(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webSettings.setUseWideViewPort(true);//设置加载进来的页面自适应手机屏幕（可缩放）
        webSettings.supportMultipleWindows();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);//允许DOM
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");

//        webView.clearFocus();
//        webView.removeAllViews();
//        webView.clearCache(true);
    }

}
