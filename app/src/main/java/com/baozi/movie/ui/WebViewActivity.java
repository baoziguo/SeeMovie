package com.baozi.movie.ui;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.ui.weight.ProgressWebView;
import com.baozi.movie.util.DensityUtil;
import com.baozi.movie.util.PreferencesUtil;
import com.baozi.seemovie.R;
import com.liaoinstan.springview.widget.SpringView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by baozi on 2016/7/22.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    ProgressWebView webview;
    @Bind(R.id.nest)
    NestedScrollView nest;
    @Bind(R.id.spring_view)
    SpringView springView;
    @Bind(R.id.fabButton)
    FloatingActionButton fabButton;
    private int top;
    private String cookieStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseweb);
        ButterKnife.bind(this);
//        caoNiMa();
    }

    private void caoNiMa() {
        top = DensityUtil.dip2px(this, 48);
        springView.animate().translationY(top);
        nest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                log("scrollX:" + scrollX + ",scrollY:" + scrollY + ",oldScrollX:" + oldScrollX + ",oldScrollY:" + oldScrollY);
                if (scrollY == 0)
                    springView.animate().translationY(top);
                else if (scrollY - oldScrollY > 0) {
                    springView.animate().translationY(scrollY - oldScrollY);
                } else {
                    springView.animate().translationY(scrollY - oldScrollY);
                }
            }
        });
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String titleName = intent.getStringExtra("titleName");
        cookieStr = PreferencesUtil.getPreferences("cookieStr", this.cookieStr);
        CookieSyncManager.createInstance(WebViewActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, WebViewActivity.this.cookieStr);
        CookieSyncManager.getInstance().sync();
        if (url != null) {
            webview.loadUrl(url);
        }
        setTitle(titleName);
        showLeftButton(R.mipmap.ic_topbar_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showRightButton(R.mipmap.ic_find_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("这里是分享哦~待实现");
            }
        });
        webview.setWebViewClient(new WebViewClient(){
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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                setTitle(title);
                CookieManager cookieManager = CookieManager.getInstance();
                WebViewActivity.this.cookieStr = cookieManager.getCookie(url);
                PreferencesUtil.putPreferences("cookieStr", WebViewActivity.this.cookieStr);
                log("Cookies = " + WebViewActivity.this.cookieStr);
            }
        });
    }

    public static void startWebActivity(Context context, String url, String titleName) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("titleName", titleName);
        context.startActivity(intent);
    }

    @OnClick({R.id.spring_view, R.id.fabButton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.spring_view:
                break;
            case R.id.fabButton:
                nest.smoothScrollTo(0, 0);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
