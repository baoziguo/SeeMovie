package com.baozi.movie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baozi.movie.baidutiebaauto.database.TiebaSQLiteDao;
import com.baozi.movie.bean.TiebaPost;
import com.baozi.movie.ui.WebViewActivity;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.BaseViewHolder;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.RecyclerAdapter;
import com.baozi.movie.util.DialogUtil;
import com.baozi.seemovie.R;


/**
 * by:baozi
 * create on 2017/3/27 16:42
 */

public class TieBaListAdapter extends RecyclerAdapter<TiebaPost> {

    private Context mContext;

    public TieBaListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<TiebaPost> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new ChatTimeViewHolder(parent);
    }

    private class ChatTimeViewHolder extends BaseViewHolder<TiebaPost> {

        TextView tv_title;

        private ChatTimeViewHolder(ViewGroup itemView) {
            super(itemView, R.layout.item_tieba);
        }

        @Override
        public void onInitializeView() {
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }

        @Override
        public void setData(final TiebaPost post) {
            super.setData(post);
            final int i = getLayoutPosition() + 1;
            tv_title.setText(i + "." + post.getTitle() + "       " + post.getTiebaFname() + "吧");
            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.startWebActivity(mContext, post.getJump_url(), "");
                }
            });
            tv_title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogUtil.showDialog(mContext, "你确认要删除第" + i + "个帖子吗?", false, new DialogUtil.DialogClickListener() {
                        @Override
                        public void confirm() {
                            TiebaSQLiteDao mDao = TiebaSQLiteDao.getInstance(mContext);
                            mDao.removePost(post);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                    return true;
                }
            });
        }
    }

}
