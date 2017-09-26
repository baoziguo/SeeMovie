package com.baozi.movie.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baozi.movie.bean.Appearance;
import com.baozi.movie.bean.ImageBean;
import com.baozi.movie.ui.multi.MultiView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.BaseViewHolder;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.RecyclerAdapter;
import com.baozi.movie.util.GsonUtil;
import com.baozi.movie.util.Utils;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import java.util.ArrayList;
import java.util.List;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * by:baozi
 * create on 2017/3/27 16:42
 */

public class AppearanceAdapter extends RecyclerAdapter<Appearance> {

    private Context mContext;

    public AppearanceAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<Appearance> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ChatTimeViewHolder(parent);
    }

    private class ChatTimeViewHolder extends BaseViewHolder<Appearance> {

        AvatarImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        MultiView iv_img;

        private ChatTimeViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.item_weibo_time);
        }

        @Override
        public void onInitializeView() {
            iv_head = (AvatarImageView) itemView.findViewById(R.id.iv_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            iv_img = (MultiView) itemView.findViewById(R.id.iv_img);
        }

        @Override
        public void setData(final Appearance appearance) {
            super.setData(appearance);
            tv_name.setText(appearance.getAuthor().getUsername());
            tv_time.setText(appearance.getCreatedAt());
            List<String> data = new ArrayList<>();
            if (TextUtils.isEmpty(appearance.getAuthor().getAvatar())) {
                iv_head.setTextAndColor(appearance.getAuthor().getUsername().substring(0, 1), Utils.generateBeautifulColor());
            } else {
                ImageLoader.getInstance().displayImage(appearance.getAuthor().getAvatar(), iv_head);
            }
            if(!appearance.getAuthor().getAvatar().equals(iv_head.getTag())){//增加tag标记，减少UIL的display次数
                iv_head.setTag(appearance.getAuthor().getAvatar());
                //不直接display imageview改为ImageAware，解决RecycleView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(iv_head, false);
                ImageLoader.getInstance().displayImage(appearance.getAuthor().getAvatar(), imageAware);
            }
            tv_content.setText(appearance.getContent());
            String imageUrl = appearance.getImageUrl();
            if (!TextUtils.isEmpty(imageUrl) && imageUrl.length() > 0){
                List<ImageBean> imageBeen = GsonUtil.jsonToList(imageUrl, ImageBean.class);
                for (int j = 0; j < imageBeen.size(); j++) {
                    data.add(imageBeen.get(j).getImageUrl());
                }
                if (data.size() > 0){
                    iv_img.clear();
                    iv_img.setImages(data);
                }
            }else {
                iv_img.clear();
            }
        }
    }

}
