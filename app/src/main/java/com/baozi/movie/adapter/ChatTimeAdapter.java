package com.baozi.movie.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.baozi.movie.bean.ImageBean;
import com.baozi.movie.bean.Post;
import com.baozi.movie.ui.multi.MultiView;
import com.baozi.movie.util.GsonUtil;
import com.baozi.movie.util.Utils;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class ChatTimeAdapter extends RecyclerView.Adapter<ChatTimeAdapter.MyViewHolder> {

    Context context;
    List<Post> mListData;
    private List<String> data = new ArrayList<>();

    public ChatTimeAdapter(Context context, List<Post> mListData) {
        this.mListData = mListData;
        this.context = context;
    }

    public void setmListData(List<Post> mListData){
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
        final Post post = mListData.get(i);
        Logger.d("post:" + post.getAuthor().getUsername());
        runEnterAnimation(myViewHolder.itemView, i);
        if (TextUtils.isEmpty(post.getAuthor().getAvatar())){
            myViewHolder.iv_head.setTextAndColor(post.getAuthor().getUsername().substring(0, 1), Utils.generateBeautifulColor());
        }else {
            ImageLoader.getInstance().displayImage(post.getAuthor().getAvatar(), myViewHolder.iv_head);
        }
        myViewHolder.tv_content.setText(post.getContent());
        String imageUrl = post.getImageUrl();
        List<ImageBean> imageBeen = GsonUtil.jsonToList(imageUrl, ImageBean.class);
        for (int j = 0; j < imageBeen.size(); j++) {
            data.add(imageBeen.get(j).getImageUrl());
        }
        if (data.size() > 0){
            myViewHolder.iv_img.clear();
            myViewHolder.iv_img.setImages(data);
        }


//        Ion.with(myViewHolder.iv_img)
//                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489585575324&di=d70a13be31e38a309cf76414bb2e0b6a&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F359b033b5bb5c9eac939425fd239b6003bf3b340.jpg");
//        data.add("http://img2.imgtn.bdimg.com/it/u=1129872862,2532778270&fm=23&gp=0.jpg");
//        data.add("http://img4.imgtn.bdimg.com/it/u=3460036485,2448243983&fm=23&gp=0.jpg");
//        data.add("http://img1.imgtn.bdimg.com/it/u=3533821055,2578418634&fm=23&gp=0.jpg");
//        data.add("http://img2.imgtn.bdimg.com/it/u=1745775257,508697643&fm=23&gp=0.jpg");
//        data.add("http://img4.imgtn.bdimg.com/it/u=948729161,3257619677&fm=23&gp=0.jpg");
//        data.add("http://img3.imgtn.bdimg.com/it/u=266777875,2420215328&fm=23&gp=0.jpg");
//        data.add("http://img0.imgtn.bdimg.com/it/u=915285971,1710943711&fm=23&gp=0.jpg");
//        data.add("http://images.17173.com/2013/jx3//2013/07/08/20130708180316774.gif");
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AvatarImageView iv_head;
        TextView tv_name;
        TextView tv_content;
        ImageView iv_chat;
        MultiView iv_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_head = (AvatarImageView) itemView.findViewById(R.id.iv_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            iv_chat = (ImageView) itemView.findViewById(R.id.iv_chat);
            iv_img = (MultiView) itemView.findViewById(R.id.iv_img);
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

