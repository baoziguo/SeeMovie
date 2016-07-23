package com.baozi.movie.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.ui.weight.ProgressWebView;
import com.baozi.seemovie.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by baozi on 2016/7/22.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webview)
    ProgressWebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseweb);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String titleName = intent.getStringExtra("titleName");
        if (url != null) {
            webview.loadUrl(url);
        }
        titleName = "【捏脸】（超多图）剑网3玩家捏脸数据分享 心水的带走";
        webview.loadUrl("http://mp.weixin.qq.com/s?src=3&timestamp=1469176961&ver=1&signature=X6EIyr653dboag2iu5*2aVYOWslLVYcLXoLaqTdVefEY3l-8YBVQdk2zuqJpR2wmoV3cqs0GYeQLks9sGb5ycGvsiJh34JbARbOauRa9Ck6AdHS1AmyS1xN02Vu-K-D9lAAQxYQIlnqWNV3PUNTzyX8GWcHR5weFPO0*TUpDB2Y=");
        setNormalTitle(titleName);
    }

    public static void startWebActivity(Context context, String url, String titleName) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("titleName", titleName);
        context.startActivity(intent);
    }

}
