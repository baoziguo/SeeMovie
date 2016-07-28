package com.baozi.movie.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.KeyEvent;
import android.view.View;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.ui.weight.ProgressWebView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseweb);
        ButterKnife.bind(this);
//        caoNiMa();
    }

//    private void caoNiMa() {
//        top = DensityUtil.dip2px(this, 48);
//        springView.animate().translationY(top);
//        nest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                log("scrollX:" + scrollX + ",scrollY:" + scrollY + ",oldScrollX:" + oldScrollX + ",oldScrollY:" + oldScrollY);
//                if (scrollY == 0)
//                    springView.animate().translationY(top);
//                else if (scrollY - oldScrollY > 0) {
//                    springView.animate().translationY(scrollY - oldScrollY);
//                } else {
//                    springView.animate().translationY(scrollY - oldScrollY);
//                }
//
//            }
//        });
//    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String titleName = intent.getStringExtra("titleName");
        if (url != null) {
            webview.loadUrl(url);
        }
        setNormalTitle(titleName);
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
