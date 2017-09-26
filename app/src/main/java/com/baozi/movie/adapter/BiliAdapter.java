package com.baozi.movie.adapter;

import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baozi.movie.BmobIMApplication;
import com.baozi.movie.bean.BiliEntity;
import com.baozi.movie.bean.BiliUrlEntity;
import com.baozi.movie.bean.User;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.BaseViewHolder;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.RecyclerAdapter;
import com.baozi.movie.util.GsonUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.danikula.videocache.HttpProxyCacheServer;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.carbs.android.avatarimageview.library.AvatarImageView;
import fm.jiecao.jcvideoplayer.JCVideoPlayer;

import static com.baozi.seemovie.R.id.iv_head;

public class BiliAdapter extends RecyclerAdapter<BiliEntity> {

    private Context mContext;

    public BiliAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<BiliEntity> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new BiliViewHolder(parent);
    }

    private class BiliViewHolder extends BaseViewHolder<BiliEntity> {

        JCVideoPlayer videocontroller;
        AvatarImageView ivHead;
        TextView tvNick;
        Button btn_down;


        private BiliViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.bili_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            videocontroller = findViewById(R.id.videocontroller);
            ivHead = findViewById(iv_head);
            tvNick = findViewById(R.id.tv_nick);
            btn_down = findViewById(R.id.btn_download);
        }

        @Override
        public void setData(final BiliEntity data) {
            super.setData(data);
            String avatar = BmobUser.getCurrentUser(mContext, User.class).getAvatar();
            if(!avatar.equals(ivHead.getTag())){//增加tag标记，减少UIL的display次数
                ivHead.setTag(avatar);
                //不直接display imageview改为ImageAware，解决RecycleView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(ivHead, false);
                ImageLoader.getInstance().displayImage(avatar, imageAware);
            }
            Ion.with(videocontroller.ivThumb).load(data.getImageUrl());
            final String old_url = data.getUrl();
            final String title = data.getTitle();
            videocontroller.setOnStartClickListener(new JCVideoPlayer.onStartClickListener() {
                @Override
                public void onStartClick(final JCVideoPlayer.Player player) {
                    Ion.with(mContext).load("http://api.bilibili.com/playurl?aid=" + data.getAid() + "&page=1")
                            .setHeader("Cookie", "UM_distinctid=15b2cc8cd143e-0b02ee7b027bb2-7d147170-1fa400-15b2cc8cd1740; pgv_pvi=8618593280; pgv_si=s5608606720; fts=1491105009; buvid3=88537A43-01B7-4061-A5AE-097F667F391539881infoc; sid=bg8fn4e1")
                            .asString().setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String string) {
                            BiliUrlEntity biliUrlEntity = GsonUtil.GsonToBean(string, BiliUrlEntity.class);
                            String url = biliUrlEntity.getDurl().get(0).getUrl();
                            HttpProxyCacheServer proxy = BmobIMApplication.getProxy(mContext);
                            final String proxyUrl = proxy.getProxyUrl(url);
                            player.onPlayer(proxyUrl);
                            if (!url.equals(old_url)) {
                                data.setObjectId(data.getObjectId());
                                data.setUrl(url);
                                data.update(mContext, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        log("更新bili最新视频url");
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {

                                    }
                                });
                            }
                        }
                    });
                }
            });
            videocontroller.setUp(old_url, data.getTitle());
            btn_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videocontroller.setUp("http://txy.live-play.acgvideo.com/live-txy/103407/live_2255740_332_c521e483.flv?wsSecret=4bf2eb50b3a8ab4eee90d1723ebcaf50&wsTime=1491363968", data.getTitle());
                    BmobFile bmobFile = new BmobFile(title + ".mp4", "", old_url);
                    File saveFile = new File(Environment.getExternalStorageDirectory(), "baozi/" + bmobFile.getFilename());
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    ToastUtils.showCustomToast("开始下载...");
                    bmobFile.download(mContext, saveFile, new DownloadFileListener() {
                        public Vibrator vibrator;

                        @Override
                        public void onSuccess(String s) {
                            ToastUtils.showCustomToast("下载完毕...");
                            //想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
                            vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                            long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                            vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtils.showCustomToast(s);
                        }

                        @Override
                        public void onProgress(Integer progress, long total) {

                        }
                    });
                }
            });
        }
    }
}

