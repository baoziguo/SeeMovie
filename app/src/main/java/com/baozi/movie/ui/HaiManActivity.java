//package com.baozi.movie.ui;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.baozi.movie.adapter.HaiManAdapter;
//import com.baozi.movie.base.BaseActivity;
//import com.baozi.movie.bean.HaiManChaoMi;
//import com.baozi.seemovie.R;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.reflect.TypeToken;
//import com.koushikdutta.async.future.FutureCallback;
//import com.koushikdutta.ion.Ion;
//
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import cn.bmob.v3.update.BmobUpdateAgent;
//
///**
// * Created by baozi on 2016/4/23 0023.
// */
//public class HaiManActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
//
//    @Bind(R.id.lv_ml_list_view)
//    RecyclerView lvMlListView;
//    @Bind(R.id.tv_left)
//    ImageView tvLeft;
//    @Bind(R.id.tv_title)
//    TextView tvTitle;
//    @Bind(R.id.swipeToLoadLayout)
//    SwipeRefreshLayout swipeToLoadLayout;
//    private HaiManAdapter mAdapter;
//    private LinearLayoutManager mLinearLayoutManager;
//    private int pastVisiblesItems, visibleItemCount, totalItemCount;
//    private boolean loading = false;
//    private int page = 0;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tumblr_layout);
//        ButterKnife.bind(this);
//        BmobUpdateAgent.update(this);
//        tvTitle.setText("超级秘密");
//        tvLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        mLinearLayoutManager = new LinearLayoutManager(this);
//        lvMlListView.setLayoutManager(mLinearLayoutManager);
//        lvMlListView.setHasFixedSize(true);
//        swipeToLoadLayout.setColorSchemeResources(R.color.colorPrimary);
//        swipeToLoadLayout.setOnRefreshListener(this);
//        lvMlListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) //向下滚动
//                {
//                    visibleItemCount = mLinearLayoutManager.getChildCount();
//                    totalItemCount = mLinearLayoutManager.getItemCount();
//                    pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();
//
//                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                        loading = true;
//                        onLoadMore();
//                    }
//                }
//            }
//        });
//        initData(page);
//    }
//
//    private void onLoadMore() {
//    }
//
//    private void initData(int page) {
//        Ion.with(this)
//                .load("https://jx3.hightman.cn/weixin/secret/api/posts/hyper?limit=50").addHeader("Authorization", "Bearer Gtm40FSa2XCIkNXLiMTqMiBXhB2GGBqPTqSUVH4gkRRczO7z")
//                .asJsonArray()
//                .setCallback(new FutureCallback<JsonArray>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonArray result) {
//                        if (swipeToLoadLayout.isRefreshing()) {
//                            swipeToLoadLayout.setRefreshing(false);
//                        }
//                        Gson gson = new Gson();
//                        List<HaiManChaoMi> haiManChaoMi = gson.fromJson(result, new TypeToken<List<HaiManChaoMi>>() {
//                        }.getType());
//                        if (mAdapter == null) {
//                            mAdapter = new HaiManAdapter(HaiManActivity.this, haiManChaoMi);
//                            lvMlListView.setAdapter(mAdapter);
//                        } else {
//                            mAdapter.setmListData(haiManChaoMi);
//                        }
//
//                    }
//                });
//
//    }
//
//    @Override
//    public void onRefresh() {
//        initData(page);
//    }
//}
