package com.baozi.movie.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.util.DialogUtil;
import com.baozi.movie.util.HideIMEUtil;
import com.baozi.movie.util.NotifyUtil;
import com.baozi.movie.util.PreferencesUtil;
import com.baozi.movie.util.StringUtil;
import com.baozi.seemovie.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * by:baozi
 * create on 2017/3/7 16:13
 */

public class OpenServiceMonitoringActivity extends BaseActivity {

    @Bind(R.id.tv_open_monitor)
    TextView tvOpenMonitor;
    @Bind(R.id.tv_service)
    TextView tvService;
    @Bind(R.id.et_put_service_name)
    EditText et_put_service_name;
    @Bind(R.id.searchableSpinner)
    SearchableSpinner searchableSpinner;
    private Subscription sb;
    private boolean isKaiFU = false;
    private MediaPlayer mp;
    private List<String> serviceNameList = new ArrayList<>();
    private List<Subscription> subscriptionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_service_monitoring);
        HideIMEUtil.wrap(this);
    }

    @Override
    protected void initView() {
        setNormalTitle("开服监控");
        if (serviceNameList.size() > 0)
            tvService.setText("当前监控服务器为:\n" + serviceNameList.toString());
        String serviceName = PreferencesUtil.getPreferences("serviceName", "");
        if (!TextUtils.isEmpty(serviceName)) {
            et_put_service_name.setText(serviceName);
            tvOpenMonitor.setEnabled(true);
            et_put_service_name.setSelection(serviceName.length());
        } else {
            tvOpenMonitor.setEnabled(false);
        }

        et_put_service_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvOpenMonitor.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchableSpinner.setTitle("Select Item");
        searchableSpinner.setPositiveButton("OK");
    }

    @OnClick({R.id.iv_info, R.id.tv_open_monitor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_info:
                DialogUtil.showKnowDialog(OpenServiceMonitoringActivity.this, "服务器名称必须是正确的，如：" +
                        "电五唯满侠，不能输入唯满侠，必须输入唯我独尊或者满江红或者侠客行；" +
                        "开服手机会弹通知且播放音乐，点击通知栏会停止播放；" +
                        "支持多服务器开服监控，一个空格隔开, 一次最多3个吧，太长了影响输入框美观；" +
                        "如果你还想监控更多的，那么你继续重新输入，然后点击按钮即可，理论上可以无限次，不会影响之前的输入的服务器监控；" +
                        "记录最后一次输入的服务器名称，避免下次重复输入；" +
                        "支持后台，可以瞎几把去玩游戏。", false);
                break;
            case R.id.tv_open_monitor:
                if (!StringUtil.isFastClick())
                    openServiceMonitorPoller();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isPlay = intent.getBooleanExtra("isPlay", false);
        if (!isPlay) {
            if (mp != null) {
                mp.stop();
                mp = null;
            }
        }
    }

    /**
     * 开启轮询，查询是否开服，开服则通知且关闭轮询
     */
    private void openServiceMonitorPoller() {
//        String service_name = (String) searchableSpinner.getSelectedItem();
        String service_name = et_put_service_name.getText().toString().trim();
        PreferencesUtil.putPreferences("serviceName", service_name);
        final String[] serviceNames = service_name.split(" ");
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(serviceNames));
        log(strings.toString());
        if (strings.size() < 0)
            return;
        serviceNameList.addAll(strings);
        tvService.setText("当前监控服务器为:\n" + serviceNameList.toString());
        sb = Observable.interval(0, 10, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aLong) {
                        serviceRequest(serviceNameList);
                    }
                });
        subscriptionList.add(sb);
    }

    /**
     * 开服通知提示
     */
    private void openMonitorNotify(String str, int i) {
        if (mp == null) {
            mp = MediaPlayer.create(OpenServiceMonitoringActivity.this, R.raw.wuzhuizhicheng);
            try {
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }

        Intent intent = new Intent(OpenServiceMonitoringActivity.this, OpenServiceMonitoringActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("isPlay", false);
        PendingIntent pIntent = PendingIntent.getActivity(OpenServiceMonitoringActivity.this,
                (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.mipmap.snake;
        String ticker = "您有一条新通知";
        String title = str + "已开服！！！";
        String content = "死心吧，奇遇不是你的，连小宠都死光了！乖乖去点天工树吧。。。";
        //实例化工具类，并且调用接口
        NotifyUtil notify2 = new NotifyUtil(OpenServiceMonitoringActivity.this, i);
        notify2.notify_normail_moreline(pIntent, smallIcon, ticker, title, content, true, true, false);
    }

    public void serviceRequest(List<String> list) {
        String s = list.toString();
        log(s);
        if (list.toString().contains("[]")) {
            for (int i = 0; i < subscriptionList.size(); i++) {
                subscriptionList.get(i).unsubscribe();
                subscriptionList.remove(i);
            }
            return;
        }
        try {
            HttpURLConnection conn = null;
            InputStream is = null;
            URL url = new URL("http://jx3gc.autoupdate.kingsoft.com/jx3gc/zhcn/serverlist/serverlist.ini");
            conn = (HttpURLConnection) url
                    .openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is, "GBK"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
                in.close();
                String str_result = buffer.toString();
                String regex = "\\s+";
                String str1 = str_result.replaceAll(regex, " ");
                Log.d("baozi", str1);
                //把一个或多个空格替换成一个空格
                for (int i = 0; i < list.size(); i++) {
                    log("服务器：" + list.get(i));
                    Pattern p = Pattern.compile("区 " + list.get(i) + " (.*?) .*?3724.*?");
                    Matcher m = p.matcher(str1.toString());
                    while (m.find()) { //循环查找匹配字串
                        MatchResult mr = m.toMatchResult();
                        int kaifu = Integer.parseInt(mr.group(1));
                        log("kaifu:" + kaifu);
                        switch (kaifu) {
                            case 3:
                                isKaiFU = false;
                                break;
                            default:
                                isKaiFU = true;
                                break;
                        }
                    }
                    if (isKaiFU) {
                        isKaiFU = false;
                        log(list.get(i) + "开服啦~！");
                        openMonitorNotify(list.get(i), i);
                        serviceNameList.remove(i);
                        tvService.setText("当前监控服务器为:\n" + serviceNameList.toString());
                    }
                    if (list.size() == 0)
                        sb.unsubscribe();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
