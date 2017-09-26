package com.baozi.movie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baozi.movie.adapter.BiliAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.bean.BiliEntity;
import com.baozi.movie.ui.HomeNewActivity;
import com.baozi.movie.ui.weight.refreshrecyclerview.RefreshRecyclerView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.Action;
import com.baozi.seemovie.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import fm.jiecao.jcvideoplayer.JCVideoPlayer;

public class BilibiliFragment extends HeaderViewPagerFragment {

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;
    @Bind(R.id.status_view)
    View status_view;
    private BiliAdapter mAdapter;
    private int page = 0;

    public static BilibiliFragment newInstance() {
        return new BilibiliFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bilibili_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new BiliAdapter(getActivity());
        mRecyclerView.setSwipeRefreshColors(0xFFFF4081,0xFFE44F98,0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        ((HomeNewActivity)getActivity()).getHeight(status_view);
    }

    private void initData(int q_page) {
        BmobQuery<BiliEntity> query = new BmobQuery<BiliEntity>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        //跳过之前页数并去掉重复数据
        query.setSkip(q_page * 20);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(getActivity(), new FindListener<BiliEntity>() {

            @Override
            public void onSuccess(List<BiliEntity> biliEntity) {
                if(page == 1){
                    mRecyclerView.showNoMore();
                    return;
                }
                mAdapter.clear();
                mAdapter.addAll(biliEntity);
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
                page++;
            }

            @Override
            public void onError(int code, String arg0) {

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
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
