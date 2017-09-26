package com.baozi.movie.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baozi.movie.adapter.HomeAdapter;
import com.baozi.movie.adapter.MenuAdapter;
import com.baozi.movie.adapter.ServerAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.ServerEntity;
import com.baozi.movie.bean.ServerSign;
import com.baozi.movie.bean.ZoneEntity;
import com.baozi.movie.util.CacheUtils;
import com.baozi.movie.util.GsonUtil;
import com.baozi.movie.util.NotifyUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.baozi.seemovie.R.id.lv_home;
import static com.baozi.seemovie.R.id.lv_menu;

/**
 * by:baozi
 * create on 2017/3/7 16:13
 */

public class GetUserServerActivity extends BaseActivity {

    @Bind(lv_menu)
    ListView lvMenu;
    @Bind(lv_home)
    ListView lvHome;
    @Bind(R.id.lv_server)
    ListView lvServer;
    private MenuAdapter menuAdapter;
    private List<ZoneEntity.DataBean> data;
    private HomeAdapter homeAdapter;
    private String sign;
    private List<ServerEntity.DataBean> server_data;
    private List<ServerEntity.DataBean> server_name_list = new ArrayList<>();
    private ServerAdapter serverAdapter;
    private Subscription sb;
    private List<Subscription> subscriptionList = new ArrayList<>();
    private boolean isKaiFU = false;
    ArrayList<String> server_temp_list = new ArrayList<>();
    private boolean isFirst =true;
    private MediaPlayer mp;
    private CacheUtils cacheUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_server);
        ButterKnife.bind(this);
        getNet();
    }

    private void getNet() {
//        Ion.with(this).load("http://sq.seasungame.com/index.php?app=api&mod=GameServer&act=get_zone_list")
//                .setBodyParameter("game", "jx3")
//                .setBodyParameter("_sign", "EMcj/nxLInh+gkDEBFjNTg==")
//                .setBodyParameter("_token", "a6d76885ac132f8b")
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject jsonObject) {
//                        try {
//                            ZoneEntity zoneEntity = GsonUtil.GsonToBean(jsonObject.toString(), ZoneEntity.class);
//                            data = zoneEntity.getData();
//                            menuAdapter.setData(data);
//                            menuAdapter.notifyDataSetInvalidated();
//                        } catch (Exception e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                });

        BmobQuery<ServerSign> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<ServerSign>() {

            @Override
            public void onSuccess(List<ServerSign> serverSign) {
                sign = serverSign.get(0).getSign();
                ZoneEntity zoneEntity = GsonUtil.GsonToBean(sign, ZoneEntity.class);
                data = zoneEntity.getData();
                menuAdapter.setData(data);
                menuAdapter.notifyDataSetInvalidated();
                Ion.with(GetUserServerActivity.this).load("http://sq.seasungame.com/index.php?app=api&mod=GameServer&act=get_server_list")
                        .setHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.0; MI 5 MIUI/V8.2.2.0.NAACNEB)")
                        .setBodyParameter("_token", data.get(0).get_token())
                        .setBodyParameter("zone_id", data.get(0).getZone_id())
                        .setBodyParameter("game", "jx3")
                        .setBodyParameter("_sign", data.get(0).getSign())
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject jsonObject) {
                                try {
                                    ServerEntity serverEntity = GsonUtil.GsonToBean(jsonObject.toString(), ServerEntity.class);
                                    server_data = serverEntity.getData();
                                    homeAdapter.setData(server_data);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
            }

            @Override
            public void onError(int code, String arg0) {

            }
        });
    }

    @Override
    protected void initView() {
        cacheUtils = CacheUtils.get(this);
        setNormalTitle("开服提醒设置");
        showRightTextButton("开启", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst = true;
                ToastUtils.showCustomToast("已开启监控，不影响你手机去做其它事哦~");
                openServiceMonitorPoller();
                cacheUtils.put("server", (Serializable) server_name_list);
            }
        });
        serverAdapter = new ServerAdapter(this);
        lvServer.setAdapter(serverAdapter);
        List<ServerEntity.DataBean> server = (List<ServerEntity.DataBean>) cacheUtils.getAsObject("server");
        if (server != null && server.size() > 0){
            try {
                server_name_list.addAll(server);
                serverAdapter.setData(server_name_list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        menuAdapter = new MenuAdapter(this);
        lvMenu.setAdapter(menuAdapter);
        homeAdapter = new HomeAdapter(this);
        lvHome.setAdapter(homeAdapter);
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                menuAdapter.setSelectItem(arg2);
                menuAdapter.notifyDataSetInvalidated();
                if (!TextUtils.isEmpty(sign)) {
                    Ion.with(GetUserServerActivity.this).load("http://sq.seasungame.com/index.php?app=api&mod=GameServer&act=get_server_list")
                            .setHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 7.0; MI 5 MIUI/V8.2.2.0.NAACNEB)")
                            .setBodyParameter("_token", data.get(arg2).get_token())
                            .setBodyParameter("zone_id", data.get(arg2).getZone_id())
                            .setBodyParameter("game", "jx3")
                            .setBodyParameter("_sign", data.get(arg2).getSign())
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject jsonObject) {
                                    try {
                                        ServerEntity serverEntity = GsonUtil.GsonToBean(jsonObject.toString(), ServerEntity.class);
                                        server_data = serverEntity.getData();
                                        homeAdapter.setData(server_data);
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
        lvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServerEntity.DataBean dataBean = server_data.get(position);
                for (int i = 0; i < server_name_list.size(); i++) {
                    if (server_name_list.get(i).getServer().equals(dataBean.getServer())){
                        server_name_list.remove(i);
                        serverAdapter.setData(server_name_list);
                        return;
                    }
                }
                if (server_name_list.size() < 3){
                    server_name_list.add(dataBean);
                }else {
                    server_name_list.remove(0);
                    server_name_list.add(dataBean);
                }
                serverAdapter.setData(server_name_list);
            }
        });
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
        sb = Observable.interval(0, 10, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aLong) {
                        serviceRequest(server_name_list);
                    }
                });
        subscriptionList.add(sb);
    }

    /**
     * 开服通知提示
     */
    private void openMonitorNotify(String str, int i) {
        if (mp == null) {
            mp = MediaPlayer.create(this, R.raw.wuzhuizhicheng);
            try {
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }

        Intent intent = new Intent(this, OpenServiceMonitoringActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("isPlay", false);
        PendingIntent pIntent = PendingIntent.getActivity(this,
                (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.mipmap.snake;
        String ticker = "您有一条新通知";
        String title = str + "已开服！！！";
        String content = "死心吧，奇遇不是你的，连小宠都死光了！乖乖去点天工树吧。。。";
        //实例化工具类，并且调用接口
        NotifyUtil notify2 = new NotifyUtil(this, i);
        notify2.notify_normail_moreline(pIntent, smallIcon, ticker, title, content, true, true, false);
    }

    public void serviceRequest(List<ServerEntity.DataBean> list) {
        if (isFirst){
            for (int i = 0; i < list.size(); i++) {
                server_temp_list.add(list.get(i).getServer());
            }
            isFirst = false;
        }
        if (server_temp_list == null || server_temp_list.size() == 0) {
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
                for (int i = 0; i < server_temp_list.size(); i++) {
                    log("服务器：" + server_temp_list.get(i));
                    Pattern p = Pattern.compile("区 " + server_temp_list.get(i) + " (.*?) .*?3724.*?");
                    Matcher m = p.matcher(str1);
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
                        log(server_temp_list.get(i) + "开服啦~！");
                        openMonitorNotify(server_temp_list.get(i), i);
                        server_temp_list.remove(i);
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
