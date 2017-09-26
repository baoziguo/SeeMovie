package com.baozi.movie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;
import com.baozi.movie.BmobIMApplication;
import com.baozi.movie.bean.kePao;
import com.baozi.seemovie.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.danikula.videocache.HttpProxyCacheServer;
import com.koushikdutta.ion.Ion;
import com.orhanobut.logger.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import fm.jiecao.jcvideoplayer.JCVideoPlayer;


public class TumblrAdapter extends BaseQuickAdapter<kePao, BaseViewHolder> {

    public TumblrAdapter(List<kePao> mListData) {
        super(R.layout.list_item, mListData);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final kePao kePao) {
        final String url = "http://cn-zjwz5-dx.acgvideo.com/vg5/6/ed/14915621-1.flv?expires=1489066500&platform=pc&ssig=EGCaLHGaaSVrVxc6AyQw6w&oi=3054637986&nfa=B2jsoD9cEoAmG7KPYo7s2g==&dynamic=1";

        HttpProxyCacheServer proxy = BmobIMApplication.getProxy(mContext);
        final String proxyUrl = proxy.getProxyUrl(url);
//        runEnterAnimation(baseViewHolder.getConvertView(), baseViewHolder.getLayoutPosition());

        JCVideoPlayer jcVideoPlayer = baseViewHolder.getView(R.id.videocontroller);
        jcVideoPlayer.setOnStartClickListener(new JCVideoPlayer.onStartClickListener() {
            @Override
            public void onStartClick(JCVideoPlayer.Player player) {
                player.onPlayer(proxyUrl);
            }
        });
        jcVideoPlayer.setUp(proxyUrl, kePao.getName());
        Button btn_down = baseViewHolder.getView(R.id.btn_down);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BmobFile fileUrl = kePao.getFileUrl();
                BmobFile bmobFile = new BmobFile("baozi.mp4", "", url);
                File saveFile = new File(Environment.getExternalStorageDirectory(), "baozi/" + bmobFile.getFilename());
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }
                Toast.makeText(mContext, "开始下载", Toast.LENGTH_LONG).show();
                bmobFile.download(mContext, saveFile, new DownloadFileListener() {
                    public Vibrator vibrator;

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(mContext, "下载成功", Toast.LENGTH_LONG).show();
                        //想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
                        vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                        vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(Integer progress, long total) {

                    }
                });
            }
        });
        Button btn_share = baseViewHolder.getView(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                share(kePao);
                /**
                 * 1=畅通 2=爆满 3=维护
                 */
//                Ion.with(mContext).load("http://www.jx3pve.com/api/server/list.php").asJsonArray().setCallback(new FutureCallback<JsonArray>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonArray result) {
//                        if (e == null) {
//                            Logger.json(result.toString());
//                        } else {
//                            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            String test_url = "http://tieba.baidu.com/p/4911885706?lp=5027&mo_device=1&is_jingpost=1&pn=2&&red_tag=2391548032";
                            String test_url = "https://tieba.baidu.com/p/5024196795?pn=";
                            for (int page = 23; page < 28; page++) {
                                String hh_url = test_url + page;
                                String stringResponseFuture = null;
                                stringResponseFuture = Ion.with(mContext).load(hh_url).setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0").asString().get();
//                                Connection conn = Jsoup.connect(hh_url);
                                Document doc = Jsoup.parse(stringResponseFuture);
                                // 修改http包中的header,伪装成浏览器进行抓取
//                            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
//                                Document doc = conn.get();
                                Elements divs = doc.getElementsByTag("div");
                                for (Element div : divs) {
                                    String linkText = div.text();
                                    if (linkText.contains("楼") && linkText.contains("回复") && linkText.contains("鸡金") && linkText.contains("0")) {
                                        Logger.i("外观查询：" + linkText);
                                        if (hasDigit(linkText.substring(8, 15))){
                                            Logger.i("过滤：" + linkText);
                                        }
                                    }
                                }
//                                Thread.sleep(2000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    /**
     * 分享
     */
    private void share(kePao kePao) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, kePao.getName() + " " + kePao.getUrl() + "\r\n(分享自花海，下载地址：http://fir.im/huahai！)");
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        mContext.startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    private void runEnterAnimation(View view, int position) {
        view.setTranslationY(((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight());
        view.animate()
                .translationY(0)
                .setStartDelay(100 * (position % 5))
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    // 判断一个字符串是否含有数字

    public boolean hasDigit(String content) {

        boolean flag = false;

        Pattern p = Pattern.compile(".*\\d+.*");

        Matcher m = p.matcher(content);

        if (m.matches())

            flag = true;

        return flag;

    }

}

