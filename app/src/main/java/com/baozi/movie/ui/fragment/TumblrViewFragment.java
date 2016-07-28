package com.baozi.movie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.baozi.movie.adapter.TumblrAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.bean.kePao;
import com.baozi.seemovie.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class TumblrViewFragment extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    private TumblrAdapter mAdapter;
    private int page = 0;
    private List<kePao> allList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = false;

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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //向下滚动
                {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        onLoadMore();
                    }
                }
            }
        });
        initData(page);
    }

    private void initData(int page) {
        BmobQuery<kePao> query = new BmobQuery<kePao>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        //跳过之前页数并去掉重复数据
        query.setSkip(page * 20);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(getActivity(), new FindListener<kePao>() {

            @Override
            public void onSuccess(List<kePao> loveEntity) {
                if (swipeToLoadLayout.isRefreshing()) {
                    swipeToLoadLayout.setRefreshing(false);
                }
                loading = false;
                allList.addAll(loveEntity);
                if (mAdapter == null) {
                    mAdapter = new TumblrAdapter(getActivity(), allList);
//                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
//                    alphaAdapter.setFirstOnly(true);
//                    alphaAdapter.setDuration(500);
//                    alphaAdapter.setInterpolator(new OvershootInterpolator(0.5f));
//                    mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    mRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
                } else {
                    if (loveEntity.isEmpty()){
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAdapter.setmListData(allList);
                    mAdapter.notifyDataSetChanged();
                }

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
    public void onRefresh() {
        allList.clear();
        initData(0);
    }

    private void onLoadMore() {
        page++;
        initData(page);
    }
}
