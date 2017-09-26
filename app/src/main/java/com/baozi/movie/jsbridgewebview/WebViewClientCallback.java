package com.baozi.movie.jsbridgewebview;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

/**
 * @author: Jason
 * @date: 15/10/13.
 * @time: 18:29.
 * 提供webviewclient回调使用的接口
 */
public interface WebViewClientCallback {


    void pageFinishedCallBack(WebView view, String url);

    void pageStartedCallBack(WebView view, String url, Bitmap favicon);

    void receivedSslErrorCallBack(WebView view, SslErrorHandler handler, SslError error);

    void receivedErrorCallBack(WebView view, int errorCode, String description, String failingUrl);

    void shouldOverrideUrlLoadingCallBack(WebView view, String url);

}
