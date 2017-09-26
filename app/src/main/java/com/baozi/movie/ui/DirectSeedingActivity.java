package com.baozi.movie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import com.baozi.movie.adapter.BiliSectionAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.BiliSectionEntity;
import com.baozi.movie.ui.weight.refreshrecyclerview.RefreshRecyclerView;
import com.baozi.movie.ui.weight.refreshrecyclerview.adapter.Action;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * by:baozi
 * create on 2017/4/5 17:31
 */

public class DirectSeedingActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RefreshRecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    JellyToolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tv_title;
    private AppCompatEditText editText;
    private int page = 0;
    private BiliSectionAdapter mAdapter;
    private boolean isCache = true;
    private String INPUT_KEY = "input_key";
    private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {
            if (TextUtils.isEmpty(editText.getText())) {
                toolbar.collapse();
                tv_title.setVisibility(View.VISIBLE);
            } else {
                editText.getText().clear();
            }
        }

        @Override
        public void onToolbarExpandingStarted() {
            tv_title.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_seeding);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        super.initView();
        editText = (AppCompatEditText) LayoutInflater.from(this).inflate(R.layout.edit_text, null);
        mAdapter = new BiliSectionAdapter(this);
        mRecyclerView.setSwipeRefreshColors(0xFFFF4081, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 0;
                initData(page);
                isCache = false;
            }
        });

        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                initData(page);
            }
        });
        initData(page);

        toolbar.setJellyListener(jellyListener);

        editText = (AppCompatEditText) LayoutInflater.from(this).inflate(R.layout.edit_text, null);
        editText.setBackgroundResource(R.color.transparent);
        toolbar.setContentView(editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    Intent intent = new Intent(DirectSeedingActivity.this, PlayerLiveActivity.class);
                    intent.putExtra("pcUrl", editText.getText().toString().trim());
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    private void initData(int q_page) {
        BmobQuery<BiliSectionEntity> query = new BmobQuery<>();
        //按照时间降序
        query.order("-createdAt");
        //限制最多20条数据结果作为一页
        query.setLimit(20);
        //跳过之前页数并去掉重复数据
        query.setSkip(q_page * 20);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<BiliSectionEntity>() {

            @Override
            public void onSuccess(List<BiliSectionEntity> biliSectionEntity) {
                if (page == 1) {
                    mRecyclerView.showNoMore();
                    return;
                }
                mAdapter.clear();
                mAdapter.addAll(biliSectionEntity);
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
    public void setStatusState(int color) {
        StatusBarUtil.setTransparent(this);
    }

}
