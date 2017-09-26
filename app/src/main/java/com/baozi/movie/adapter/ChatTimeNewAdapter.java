package com.baozi.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baozi.movie.bean.ImageBean;
import com.baozi.movie.bean.Post;
import com.baozi.movie.bean.User;
import com.baozi.movie.ui.ChatActivity;
import com.baozi.movie.ui.PersonalDataNewActivity;
import com.baozi.movie.ui.multi.MultiView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.BaseViewHolder;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.RecyclerAdapter;
import com.baozi.movie.util.GsonUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.movie.util.Utils;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * by:baozi
 * create on 2017/3/27 16:42
 */

public class ChatTimeNewAdapter extends RecyclerAdapter<Post> {

    private Context mContext;

    public ChatTimeNewAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<Post> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ChatTimeViewHolder(parent);
    }

    private class ChatTimeViewHolder extends BaseViewHolder<Post> {

        AvatarImageView iv_head;
        TextView tv_name;
        TextView tv_time;
        TextView tv_content;
        ImageView iv_chat;
        MultiView iv_img;
        LinearLayout ll_zan;
        LinearLayout ll_avatar;
        TextView tv_like_tip;
        ImageView iv_more;

        private ChatTimeViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.item_chat_time);
        }

        @Override
        public void onInitializeView() {
            iv_head = (AvatarImageView) itemView.findViewById(R.id.iv_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            iv_chat = (ImageView) itemView.findViewById(R.id.iv_chat);
            iv_img = (MultiView) itemView.findViewById(R.id.iv_img);
            ll_zan = (LinearLayout) itemView.findViewById(R.id.ll_zan);
            ll_avatar = (LinearLayout) itemView.findViewById(R.id.ll_avatar);
            tv_like_tip = (TextView) itemView.findViewById(R.id.tv_like_tip);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
        }

        @Override
        public void setData(final Post post) {
            super.setData(post);
            tv_name.setText(post.getAuthor().getUsername());
            tv_time.setText(post.getCreatedAt());
            List<String> data = new ArrayList<>();
            final String avatar = post.getAuthor().getAvatar();

            if (!TextUtils.isEmpty(avatar) && !avatar.equals(iv_head.getTag())) {//增加tag标记，减少UIL的display次数
                iv_head.setTag(avatar);
                //不直接display imageview改为ImageAware，解决RecycleView滚动时重复加载图片
                ImageAware imageAware = new ImageViewAware(iv_head, false);
                ImageLoader.getInstance().displayImage(avatar, imageAware);
            } else if (TextUtils.isEmpty(avatar)) {
                iv_head.setTextAndColor(post.getAuthor().getUsername().substring(0, 1), Utils.generateBeautifulColor());
            }

            if (post.getAuthor().getUsername().equals(BmobUser.getCurrentUser(mContext).getUsername())) {
                iv_chat.setBackgroundResource(R.mipmap.delete_new);
                iv_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post.setObjectId(post.getObjectId());
                        post.delete(mContext, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                remove(post);
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });
                    }
                });
            } else {
                iv_chat.setBackgroundResource(R.mipmap.item_mine_chat);
                iv_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BmobIMUserInfo info = new BmobIMUserInfo(post.getAuthor().getObjectId(), post.getAuthor().getUsername(), avatar);
                        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, false, null);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("c", c);
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra(mContext.getPackageName(), bundle);
                        mContext.startActivity(intent);
                    }
                });
            }
            tv_content.setText(post.getContent());
            String imageUrl = post.getImageUrl();
            if (!TextUtils.isEmpty(imageUrl) && imageUrl.length() > 0) {
                List<ImageBean> imageBeen = GsonUtil.jsonToList(imageUrl, ImageBean.class);
                for (int j = 0; j < imageBeen.size(); j++) {
                    data.add(imageBeen.get(j).getImageUrl());
                }
                if (data.size() > 0) {
                    iv_img.clear();
                    iv_img.setImages(data);
                }
            } else {
                iv_img.clear();
            }

            //点赞
            ll_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post mPost = new Post();
                    mPost.setObjectId(post.getObjectId());
                    BmobRelation relation = new BmobRelation();
                    relation.add(BmobUser.getCurrentUser(mContext));
                    mPost.setLikes(relation);
                    mPost.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            notifyItemChanged(getLayoutPosition());
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
            });

            BmobQuery<User> query = new BmobQuery<>();
            Post mPost = new Post();
            mPost.setObjectId(post.getObjectId());
            query.addWhereRelatedTo("likes", new BmobPointer(mPost));
            query.findObjects(mContext, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    if (list != null && list.size() > 0) {
                        ll_avatar.removeAllViews();
                        ll_avatar.setVisibility(View.VISIBLE);
                        tv_like_tip.setVisibility(View.VISIBLE);
                        tv_like_tip.setText(list.size() + "人喜欢:");
                        int size = list.size() > 5 ? 5 : list.size();
                        for (int i = 0; i < size; i++) {
                            View view = View.inflate(mContext, R.layout.item_avatar, null);
                            AvatarImageView iv_head = (AvatarImageView) view.findViewById(R.id.iv_head);
                            ImageLoader.getInstance().displayImage(list.get(i).getAvatar(), iv_head);
                            iv_head.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mContext.startActivity(new Intent(mContext, PersonalDataNewActivity.class));
                                }
                            });
                            ll_avatar.addView(view);
                        }
                        iv_more.setVisibility(size == 5 ? View.VISIBLE : View.GONE);
                        iv_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showCustomToast("TODO更多点赞的人");
                            }
                        });
                    } else {
                        ll_avatar.setVisibility(View.GONE);
                        tv_like_tip.setVisibility(View.GONE);
                        iv_more.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }

}
