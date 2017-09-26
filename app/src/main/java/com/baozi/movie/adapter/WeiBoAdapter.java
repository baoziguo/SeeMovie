package com.baozi.movie.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baozi.movie.bean.WeiBoEntity;
import com.baozi.movie.ui.multi.MultiView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.BaseViewHolder;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.RecyclerAdapter;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * by:baozi
 * create on 2017/3/27 16:42
 */

public class WeiBoAdapter extends RecyclerAdapter<WeiBoEntity.StatusesBean> {

    private Context mContext;

    public WeiBoAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<WeiBoEntity.StatusesBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ChatTimeViewHolder(parent);
    }

    private class ChatTimeViewHolder extends BaseViewHolder<WeiBoEntity.StatusesBean> {

        AvatarImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_chat;
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
        public void setData(final WeiBoEntity.StatusesBean statusesBean) {
            super.setData(statusesBean);
            tv_name.setText(statusesBean.getUser().getName());
            String created_at = statusesBean.getCreated_at();
            try {
                Date date = new Date(created_at);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(date);
                tv_time.setText(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(!statusesBean.getUser().getAvatar_hd().equals(iv_head.getTag())){//增加tag标记，减少UIL的display次数
                iv_head.setTag(statusesBean.getUser().getAvatar_hd());
                //不直接display imageview改为ImageAware，解决RecycleView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(iv_head, false);
                ImageLoader.getInstance().displayImage(statusesBean.getUser().getAvatar_hd(), imageAware);
            }
            tv_content.setText(statusesBean.getText());
            List<WeiBoEntity.StatusesBean.PicUrlsBean> pic_urls = statusesBean.getPic_urls();
            List<String> data = new ArrayList<>();
            if (pic_urls != null && pic_urls.size() > 0) {
                for (int j = 0; j < pic_urls.size(); j++) {
                    String thumbnail_pic = pic_urls.get(j).getThumbnail_pic();
                    //缩略图转化成大图
                    String large_pic = thumbnail_pic.replace("thumbnail", "large");
                    data.add(large_pic);
                }
                if (data.size() > 0) {
                    iv_img.clear();
                    iv_img.setImages(data);
                }
            } else {
                iv_img.clear();
            }
        }
    }

}
