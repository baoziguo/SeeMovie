package com.baozi.movie.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baozi.movie.baidutiebaauto.database.TiebaSQLiteDao;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.TiebaPost;
import com.baozi.movie.jsbridgewebview.BridgeWebView;
import com.baozi.movie.ui.weight.JumpingBeans;
import com.baozi.movie.ui.weight.NumberProgressBar;
import com.baozi.movie.util.PreferencesUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * Created by baozi on 2016/7/22.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    BridgeWebView webView;
    //    @Bind(R.id.nest)
//    NestedScrollView nest;
    @Bind(R.id.progressbar)
    NumberProgressBar progressbar;

    private String cookieStr;
    private String shareUrl;
    private String tiebaFid;//贴吧id
    private String tiebaTid;//帖子id
    private String tiebaFname;//贴吧名称
    private String jump_url;//最后显示页面url
    private String title;//帖子标题
    private String titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initWebView();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView();
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String type = intent.getStringExtra("type");
        if (url.startsWith("http://tieba.baidu.com")) {
            // 修改ua使得web端正确判断
//            webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
        }
        titleName = intent.getStringExtra("titleName");
        cookieStr = PreferencesUtil.getPreferences("cookieStr", this.cookieStr);
        if (!TextUtils.isEmpty(cookieStr)) {
            CookieSyncManager.createInstance(WebViewActivity.this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookieStr);
            CookieSyncManager.getInstance().sync();
        }
//        url = "http://www.huya.com/tingxiaojie";
        if (url != null) {
            webView.loadUrl(url);
        }
        showLeftButton(R.mipmap.ic_topbar_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("tieba".equals(type)) {
            showRightTextButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jump_url.startsWith("https://tieba.baidu.com/p/")) {
                        TiebaPost tiebaPost = new TiebaPost();
                        tiebaPost.setJump_url(jump_url);
                        tiebaPost.setTiebaFid(tiebaFid);
                        tiebaPost.setTiebaFname(tiebaFname);
                        tiebaPost.setTiebaTid(tiebaTid);
                        tiebaPost.setTitle(title);
                        TiebaSQLiteDao dao = TiebaSQLiteDao.getInstance(WebViewActivity.this);
                        dao.addPost(tiebaPost); // 存入数据库
                        finish();
                    } else {
                        ToastUtils.showCustomToast("骚年，这不是贴吧帖子呢~");
                    }
                }
            });
        } else {
            showRightButton(R.mipmap.ic_find_share, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(shareUrl)) {
//                    new ShareAction(WebViewActivity.this).setPlatform(SHARE_MEDIA.QQ).withMedia(new UMImage(WebViewActivity.this, shareUrl))
//                            .setCallback(umShareListener)
//                            .share();
                        new ShareAction(WebViewActivity.this).withMedia(new UMImage(WebViewActivity.this, shareUrl))
                                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)//,SHARE_MEDIA.WEIXIN
                                .setCallback(umShareListener).open();
                    } else {
                        ToastUtils.showCustomToast("这里是分享，暂未开放~");
                    }
                }
            });
        }

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                log("url:" + url);
                if (!url.contains("http") || !url.contains("https")) {
                    return false;
                }
                jump_url = url;
                log("跳转url：" + url);
                //处理自动跳转到浏览器的问题
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                title = view.getTitle();
                if (!TextUtils.isEmpty(title)) {
                    setTitle(TextUtils.isEmpty(titleName) ? title : titleName);
                }
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                WebViewActivity.this.cookieStr = cookieManager.getCookie(url);
                PreferencesUtil.putPreferences("cookieStr", WebViewActivity.this.cookieStr);
                log("Cookies = " + WebViewActivity.this.cookieStr);
                if (!TextUtils.isEmpty(WebViewActivity.this.cookieStr))
                    getCookieByStr(WebViewActivity.this.cookieStr);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                log("资源url：" + url);
                if (url.contains("m3u8") || url.contains("flv") || url.contains("mp4") || url.contains("rtmp"))
                    log("直播资源：" + url);
                else if (url.startsWith("https://j3pz") && url.endsWith("jpg")) {
                    shareUrl = url;
                } else if (url.contains("fname") && url.contains("fid") && url.contains("tid")) {
                    url = getURLDecoderString(url);//url解码
                    tiebaFid = getTiebaFid(url);
                    tiebaTid = getTiebaTid(url);
                    tiebaFname = getTiebaFname(url);
                    log("tiebaFid:" + tiebaFid);
                    log("tiebaTid:" + tiebaTid);
                    log("tiebaFname:" + tiebaFname);
                }
                super.onLoadResource(view, url);
            }
        });
    }

    private String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从Cookie里获取BDUSS
     */
    private void getCookieByStr(String str) {
        String[] cookiestrs = str.split(";");
        for (int i = 0; i < cookiestrs.length; i++) {
            String[] onecookie = cookiestrs[i].split("=");
            if ("BDUSS".equals(onecookie[0].trim())) {
                log("BDUSS:" + onecookie[1]);
                PreferencesUtil.putPreferences("BDUSS", onecookie[1]);
            }
        }
    }

    /**
     * 用正则获取贴吧fid
     *
     * @param htmlStr
     * @return
     */
    private String getTiebaFid(String htmlStr) {
        String fid = "";
        Pattern pattern = Pattern.compile("fid=(\\d+)");
        Matcher matcher = pattern.matcher(htmlStr);
        if (matcher.find()) {
            fid = matcher.group(1);
        }
        return fid;
    }

    /**
     * 用正则获取贴吧tid
     *
     * @param htmlStr
     * @return
     */
    private String getTiebaTid(String htmlStr) {
        String fid = "";
        Pattern pattern = Pattern.compile("tid=(\\d+)");
        Matcher matcher = pattern.matcher(htmlStr);
        if (matcher.find()) {
            fid = matcher.group(1);
        }
        return fid;
    }

    /**
     * 用正则获取贴吧fname
     *
     * @return
     */
    private String getTiebaFname(String url) {
        String fname = "";
        Pattern pattern = Pattern.compile("fname=(\\w+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            fname = matcher.group(1);
        }
        return fname;
    }

    /**
     * 用正则获取贴吧tbs
     *
     * @param htmlStr
     * @return
     */
    private String getTiebaTbs(String htmlStr) {
        String tbs = "";
        Pattern pattern = Pattern.compile("\"tbs\" *: *\"(\\w+)\"");
        Matcher matcher = pattern.matcher(htmlStr);
        if (matcher.find()) {
            tbs = matcher.group(1);
        }
        return tbs;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

//            Toast.makeText(WebViewActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(WebViewActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(WebViewActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public static void startWebActivity(Context context, String url, String titleName) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("titleName", titleName);
        context.startActivity(intent);
    }

//    @OnClick({R.id.fabButton})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.fabButton:
//                nest.smoothScrollTo(0, 0);
//                break;
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    JumpingBeans jumpingBeans = null;

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressbar == null)
                return;
            progressbar.setProgress(newProgress);
            if (newProgress < 100) {
                if (jumpingBeans == null) {
                    jumpingBeans = JumpingBeans.with(setTitle("加载中...")).appendJumpingDots().build();
                }
                progressbar.setVisibility(View.VISIBLE);
            } else {
                jumpingBeans.stopJumping();
                progressbar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) { // WebView 销毁
            try {
                progressbar = null;
                webView.removeAllViews();
                webView.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
