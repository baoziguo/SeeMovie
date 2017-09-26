package com.baozi.movie.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.util.MediaUtils;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by user on 2016/8/19.
 */
public class PlayerLiveActivity extends BaseActivity {

    private PowerManager.WakeLock wakeLock;
    private PlayerView player;
    private String url;
//    private String title = "【九种声线】色情主播教你喘！";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);
        StatusBarUtil.setTransparent(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
        String type = intent.getStringExtra("type");
        String cid = intent.getStringExtra("cid");
        String pcUrl = intent.getStringExtra("pcUrl");
        final String imageUrl = intent.getStringExtra("imageUrl");
        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        player = new PlayerView(this, rootView)
                .setTitle(title)
                .setScaleType(PlayStateParams.fitparent)
//                .hideMenu(true)
//                .hideSteam(true)
//                .setForbidDoulbeUp(true)//设置禁止双击
//                .hideCenterPlayer(true)
//                .hideControlPanl(true)
                .hideHideTopBar(true)
                .forbidTouch(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Ion.with(ivThumbnail).load(imageUrl);
                    }
                });
        if ("bili".equals(type)) {
            Ion.with(this).load("http://live.bilibili.com/api/playurl?player=1&quality=0&cid=" + cid)
                    .asString().setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String s) {
                    if (TextUtils.isEmpty(s))
                        return;
                    try {
                        Document document = Jsoup.parse(s);
                        Elements durl = document.getElementsByTag("durl");
                        String url = durl.get(0).getElementsByTag("url").text();
                        String b1url = durl.get(0).getElementsByTag("b1url").text();
                        String b2url = durl.get(0).getElementsByTag("b2url").text();
                        String b3url = durl.get(0).getElementsByTag("b3url").text();
                        ArrayList<VideoijkBean> list = new ArrayList<>();
                        VideoijkBean m = new VideoijkBean();
                        m.setStream("主线");
                        m.setUrl(url);
                        VideoijkBean m1 = new VideoijkBean();
                        m1.setStream("备线1");
                        m1.setUrl(b1url);
                        VideoijkBean m2 = new VideoijkBean();
                        m2.setStream("备线2");
                        m2.setUrl(b2url);
                        VideoijkBean m3 = new VideoijkBean();
                        m3.setStream("备线3");
                        m3.setUrl(b3url);
                        list.add(m);
                        list.add(m1);
                        list.add(m2);
                        list.add(m3);
                        player.setPlaySource(list)
                                .startPlay();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } else if (TextUtils.isEmpty(pcUrl)) {
            player.setPlaySource(url)
                    .startPlay();
        } else if (!TextUtils.isEmpty(pcUrl)) {
            initWebview(pcUrl);
        }

    }

    private void initWebview(String pcUrl) {
        log("pcUrl：" + pcUrl);
        WebView webView = new WebView(this);
        webView.loadUrl(pcUrl);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                log("资源url：" + url);
                if (url.contains("m3u8") || url.contains("flv") || url.contains("mp4") || url.contains("rtmp")) {
                    log("直播资源：" + url);
                    PlayerLiveActivity.this.url = url;
                    player.setPlaySource(url)
                            .startPlay();
                }
                super.onLoadResource(view, url);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        MediaUtils.muteAudioFocus(this, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        MediaUtils.muteAudioFocus(this, false);
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

}
