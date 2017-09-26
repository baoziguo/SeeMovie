package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import com.baozi.movie.adapter.AppearanceAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.Appearance;
import com.baozi.movie.ui.weight.refreshrecyclerview.RefreshRecyclerView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.Action;
import com.baozi.seemovie.R;
import java.util.List;
import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * by:baozi
 * create on 2017/4/25 12:40
 */

public class AppearanceSearchActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;

    private AppearanceAdapter mAdapter;
    private int page = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance_search);
    }

    @Override
    protected void initView() {
        setNormalTitle("近期外观");
        mAdapter = new AppearanceAdapter(this);
        mRecyclerView.setSwipeRefreshColors(0xFFFF4081,0xFFE44F98,0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 0;
                initData(page);
            }
        });

        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                initData(page);
            }
        });
        initData(page);
    }

    private void initData(int mPage) {
        BmobQuery<Appearance> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        query.include("author");
        //跳过之前页数并去掉重复数据
        query.setSkip(mPage * 20);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<Appearance>() {

            @Override
            public void onSuccess(List<Appearance> appearanceList) {
                if(page == 1){
                    mRecyclerView.showNoMore();
                    return;
                }
                mAdapter.clear();
                mAdapter.addAll(appearanceList);
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
                page++;

            }

            @Override
            public void onError(int code, String arg0) {

            }
        });
    }

}
