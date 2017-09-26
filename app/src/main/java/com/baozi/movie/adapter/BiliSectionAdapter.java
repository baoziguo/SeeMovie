package com.baozi.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baozi.movie.bean.BiliSectionEntity;
import com.baozi.movie.ui.PlayerLiveActivity;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.BaseViewHolder;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.RecyclerAdapter;
import com.baozi.seemovie.R;
import com.koushikdutta.ion.Ion;

public class BiliSectionAdapter extends RecyclerAdapter<BiliSectionEntity> {

    private Context mContext;

    public BiliSectionAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<BiliSectionEntity> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new BiliViewHolder(parent);
    }

    private class BiliViewHolder extends BaseViewHolder<BiliSectionEntity> {

        TextView title;
        ImageView iv_bg;
        ImageView iv_start;


        private BiliViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.bili_direct_seeding_item);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            title = findViewById(R.id.title);
            iv_bg = findViewById(R.id.iv_bg);
            iv_start = findViewById(R.id.iv_start);
        }

        @Override
        public void setData(final BiliSectionEntity data) {
            super.setData(data);
            Ion.with(iv_bg).load(data.getImageUrl());
            title.setText(data.getTitle());
            iv_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlayerLiveActivity.class);
                    intent.putExtra("title", data.getTitle());
                    intent.putExtra("url", data.getUrl());
                    intent.putExtra("imageUrl", data.getImageUrl());
                    intent.putExtra("type", data.getType());
                    intent.putExtra("cid", data.getCid());
                    intent.putExtra("pcUrl", data.getPcUrl());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}

