package com.baozi.movie.adapter;

import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.baozi.movie.bean.kePao;
import com.baozi.seemovie.R;
import java.io.File;
import java.util.List;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by @vitovalov on 30/9/15.
 */
public class TumblrAdapter extends RecyclerView.Adapter<TumblrAdapter.MyViewHolder> {

    Context context;
    List<kePao> mListData;

    public TumblrAdapter(Context context, List<kePao> mListData) {
        this.mListData = mListData;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final kePao kePao = mListData.get(i);
        myViewHolder.jCVideoPlayer.setUp(kePao.getUrl(), kePao.getName());
        myViewHolder.btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobFile fileUrl = kePao.getFileUrl();
                File saveFile = new File(Environment.getExternalStorageDirectory(), "baozi/" + fileUrl.getFilename());
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }
                Toast.makeText(context, "开始下载", Toast.LENGTH_LONG).show();
                fileUrl.download(context, saveFile, new DownloadFileListener() {
                    public Vibrator vibrator;

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(context, "下载成功", Toast.LENGTH_LONG).show();
                        //想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
                            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                            vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(Integer progress, long total) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button btn_down;
        CardView cardlist_item;
        JCVideoPlayer jCVideoPlayer;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);
            jCVideoPlayer = (JCVideoPlayer) itemView.findViewById(R.id.videocontroller);
            btn_down = (Button) itemView.findViewById(R.id.btn_down);
        }
    }


}

