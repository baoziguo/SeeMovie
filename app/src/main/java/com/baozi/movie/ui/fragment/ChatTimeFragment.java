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

import com.baozi.movie.adapter.ChatTimeAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.bean.Post;
import com.baozi.movie.bean.User;
import com.baozi.movie.bean.weiXin;
import com.baozi.seemovie.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ChatTimeFragment extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeToLoadLayout)
    SwipeRefreshLayout swipeToLoadLayout;
    private ChatTimeAdapter mAdapter;
    private int page = 0;
    private List<weiXin> allList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = false;

    public static ChatTimeFragment newInstance() {
        return new ChatTimeFragment();
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
        BmobQuery<weiXin> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        //跳过之前页数并去掉重复数据
        query.setSkip(page * 20);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(getActivity(), new FindListener<weiXin>() {

            @Override
            public void onSuccess(List<weiXin> weiXinList) {
                if (swipeToLoadLayout.isRefreshing()) {
                    swipeToLoadLayout.setRefreshing(false);
                }
                loading = false;
                allList.addAll(weiXinList);
                if (mAdapter == null) {
                    mAdapter = new ChatTimeAdapter(getActivity(), allList);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    if (weiXinList.isEmpty()) {
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

    /**
     * 发表帖子
     *
     * @param content 帖子内容
     */
    private void sendPost(String content) {
        User user = BmobUser.getCurrentUser(getActivity(), User.class);
        // 创建帖子信息
        Post post = new Post();
        post.setContent(content);
        //添加一对一关联
        post.setAuthor(user);
        post.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "发表成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.fabButton)
    public void onClick() {
        sendPost("包子大帅哥");
    }
}
