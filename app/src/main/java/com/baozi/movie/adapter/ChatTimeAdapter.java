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
import com.baozi.movie.ui.weight.CircleImageView;
import com.baozi.movie.ui.weight.MyImageView;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class ChatTimeAdapter extends RecyclerView.Adapter<ChatTimeAdapter.MyViewHolder> {

    Context context;
    List<weiXin> mListData;

    public ChatTimeAdapter(Context context, List<weiXin> mListData) {
        this.mListData = mListData;
        this.context = context;
    }

    public void setmListData(List<weiXin> mListData){
        this.mListData = mListData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_time,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final weiXin weiXin = mListData.get(i);
        runEnterAnimation(myViewHolder.itemView, i);
        ImageLoader.getInstance().displayImage(weiXin.getImgUrl(), myViewHolder.iv_head);

    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_head;
        TextView tv_name;
        ImageView iv_chat;
        MyImageView iv_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_head = (CircleImageView) itemView.findViewById(R.id.iv_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_chat = (ImageView) itemView.findViewById(R.id.iv_chat);
            iv_img = (MyImageView) itemView.findViewById(R.id.iv_img);
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

