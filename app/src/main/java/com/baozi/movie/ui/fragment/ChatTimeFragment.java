package com.baozi.movie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baozi.movie.adapter.ChatTimeNewAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.bean.Post;
import com.baozi.movie.ui.HomeNewActivity;
import com.baozi.movie.ui.SendMessageActivity;
import com.baozi.movie.ui.springactionmenu.ActionMenu;
import com.baozi.movie.ui.springactionmenu.OnActionItemClickListener;
import com.baozi.movie.ui.weight.refreshrecyclerview.RefreshRecyclerView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.Action;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ChatTimeFragment extends HeaderViewPagerFragment {

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;
    @Bind(R.id.status_view)
    View status_view;
    @Bind(R.id.actionMenu)
    ActionMenu actionMenu;

    private ChatTimeNewAdapter mAdapter;
    private int page = 0;

    public static ChatTimeFragment newInstance() {
        return new ChatTimeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chattime_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChatTimeNewAdapter(getActivity());
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
        actionMenu.addView(R.drawable.picture, getItemColor(R.color.violet_e06627A2), getItemColor(R.color.violet_e06627A2));
        actionMenu.addView(R.drawable.movie, getItemColor(R.color.color_bottom_text_press), getItemColor(R.color.color_bottom_text_press));
        actionMenu.setItemClickListener(new OnActionItemClickListener() {
            @Override
            public void onItemClick(int index) {
                switch (index){
                    case 1://图文
                        startActivity(new Intent(getActivity(), SendMessageActivity.class));
                        break;
                    case 2://视频
                        ToastUtils.showCustomToast("视频");
                        break;
                }
            }

            @Override
            public void onAnimationEnd(boolean isOpen) {

            }
        });
    }

    private void initData(int mPage) {
        BmobQuery<Post> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        query.include("author");
        //跳过之前页数并去掉重复数据
        query.setSkip(mPage * 20);
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(getActivity(), new FindListener<Post>() {

            @Override
            public void onSuccess(List<Post> weiXinList) {
                if(page == 1){
                    mRecyclerView.showNoMore();
                    return;
                }
                mAdapter.clear();
                mAdapter.addAll(weiXinList);
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
                page++;

            }

            @Override
            public void onError(int code, String arg0) {

            }
        });

    }

    private int getItemColor(int colorID) {
        return getResources().getColor(colorID);
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

}
