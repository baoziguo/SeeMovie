package com.baozi.movie.ui.weight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.baozi.seemovie.R;

/**
 * @Description:带进度条的WebView
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));

        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //处理自动跳转到浏览器的问题
                view.loadUrl(url);
                return true;
            }
        });
        //是否可以缩放
        setBackgroundColor(Color.WHITE);
        getSettings().setSupportZoom(true);
        setVerticalScrollBarEnabled(false);
        getSettings().setUseWideViewPort(true);
        getSettings().supportMultipleWindows();
        getSettings().setDomStorageEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setDefaultTextEncodingName("UTF-8");
        getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        clearFocus();
//        removeAllViews();
        clearCache(true);

    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
