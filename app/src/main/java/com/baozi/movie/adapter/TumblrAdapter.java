package com.baozi.movie.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baozi.movie.bean.kePao;
import com.baozi.seemovie.R;
import java.util.List;
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
        kePao kePao = mListData.get(i);
        myViewHolder.jCVideoPlayer.setUp(kePao.getUrl(), kePao.getName());
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardlist_item;
        TextView title;
        JCVideoPlayer jCVideoPlayer;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);
            jCVideoPlayer = (JCVideoPlayer) itemView.findViewById(R.id.videocontroller);
            title = (TextView) itemView.findViewById(R.id.listitem_name);
        }
    }


}

