package com.baozi.movie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baozi.movie.adapter.TumblrAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.bean.kePao;
import com.baozi.movie.ui.HomeNewActivity;
import com.baozi.seemovie.R;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class TumblrViewFragment extends HeaderViewPagerFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    @Bind(R.id.status_view)
    View status_view;
    private TumblrAdapter mAdapter;
    private int page = 0;
    private LinearLayoutManager mLinearLayoutManager;

    public static TumblrViewFragment newInstance() {
        return new TumblrViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeToLoadLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeToLoadLayout.setOnRefreshListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        initData(page);
        ((HomeNewActivity)getActivity()).getHeight(status_view);
    }

    private void initData(int q_page) {
        BmobQuery<kePao> query = new BmobQuery<kePao>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        //跳过之前页数并去掉重复数据
        query.setSkip(q_page * 20);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(getActivity(), new FindListener<kePao>() {

            @Override
            public void onSuccess(List<kePao> loveEntity) {
//                allList.addAll(loveEntity);

                if (mAdapter == null) {
                    mAdapter = new TumblrAdapter(loveEntity);
                    mAdapter.setOnLoadMoreListener(TumblrViewFragment.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    if (swipeToLoadLayout.isRefreshing()) {
                        mAdapter.setNewData(loveEntity);
                        swipeToLoadLayout.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }else if (mAdapter.isLoading()){
                        mAdapter.addData(loveEntity);
                        mAdapter.loadMoreComplete();
                        swipeToLoadLayout.setEnabled(true);
                    }else if (loveEntity.isEmpty()){
                        mAdapter.loadMoreEnd();
                    }
                }
            }

            @Override
            public void onError(int code, String arg0) {
                if (mAdapter != null)
                    mAdapter.loadMoreFail();
            }
        });

    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        initData(0);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        swipeToLoadLayout.setEnabled(false);
        initData(page);
    }
}
