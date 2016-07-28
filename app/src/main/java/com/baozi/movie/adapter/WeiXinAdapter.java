package com.baozi.movie.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.baozi.movie.bean.weiXin;
import com.baozi.movie.ui.WebViewActivity;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class WeiXinAdapter extends RecyclerView.Adapter<WeiXinAdapter.MyViewHolder> {

    Context context;
    List<weiXin> mListData;

    public WeiXinAdapter(Context context, List<weiXin> mListData) {
        this.mListData = mListData;
        this.context = context;
    }

    public void setmListData(List<weiXin> mListData){
        this.mListData = mListData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weixin_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final weiXin weiXin = mListData.get(i);
        runEnterAnimation(myViewHolder.itemView, i);
        ImageLoader.getInstance().displayImage(weiXin.getImgUrl(), myViewHolder.iv_weixin);
        myViewHolder.tv_title.setText(weiXin.getTitle());
        myViewHolder.tv_description.setText("来源:" + weiXin.getDescribe());
        myViewHolder.tv_time.setText(weiXin.getCreatedAt());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.startWebActivity(context, weiXin.getUrl(), weiXin.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_weixin;
        TextView tv_title;
        TextView tv_description;
        TextView tv_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_weixin = (ImageView) itemView.findViewById(R.id.iv_weixin);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    private void runEnterAnimation(View view, int position) {
        view.setTranslationY(((Activity)context).getWindowManager().getDefaultDisplay().getHeight());
        view.animate()
                .translationY(0)
                .setStartDelay(100 * (position % 5))
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

}

