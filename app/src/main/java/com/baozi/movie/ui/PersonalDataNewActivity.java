package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.baozi.movie.adapter.ChatTimeNewAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.Post;
import com.baozi.movie.bean.User;
import com.baozi.movie.ui.weight.refreshrecyclerview.RefreshRecyclerView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.Action;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * by:baozi
 * create on 2017/5/6 15:38
 */

public class PersonalDataNewActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;
    @Bind(R.id.ll_head_title)
    LinearLayout ll_head_title;
    private ChatTimeNewAdapter mAdapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_new);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);

        mAdapter = new ChatTimeNewAdapter(this);
        mRecyclerView.setSwipeRefreshColors(0xFFFF4081, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                initData(page);
            }
        });

        initData(page);

    }

    @Override
    protected void initView() {
        ll_head_title.setBackgroundResource(R.color.transparent);
    }

    private void initData(int mPage) {
        BmobQuery<User> innerQuery = new BmobQuery<>();
        String[] friendIds = {"0deeae7270"};
        innerQuery.addWhereContainedIn("objectId", Arrays.asList(friendIds));
        BmobQuery<Post> query = new BmobQuery<>();
        query.include("author");
        query.addWhereMatchesQuery("author", "_User", innerQuery);
        query.findObjects(this, new FindListener<Post>() {
            @Override
            public void onSuccess(List<Post> list) {
                if(page == 1){
                    mRecyclerView.showNoMore();
                    return;
                }
                mAdapter.clear();
                mAdapter.addAll(list);
                mRecyclerView.getRecyclerView().scrollToPosition(0);
                page++;
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparentColor(this, getResources().getColor(R.color.transparent), 0);
    }
}
