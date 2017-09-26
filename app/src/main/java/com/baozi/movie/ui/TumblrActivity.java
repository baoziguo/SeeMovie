package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.baozi.movie.adapter.TumblrAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.User;
import com.baozi.movie.bean.kePao;
import com.baozi.movie.model.UserModel;
import com.baozi.seemovie.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by baozi on 2016/4/23 0023.
 */
public class TumblrActivity extends BaseActivity {

    @Bind(R.id.lv_ml_list_view)
    RecyclerView lvMlListView;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.tv_left)
    ImageView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_right)
    TextView tv_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tumblr_layout);
        ButterKnife.bind(this);
//        BmobUpdateAgent.initAppVersion(this);
        BmobUpdateAgent.update(this);
        tvTitle.setText("视频污污哒");
        tv_right.setText("聊天");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class,null,false);
                }else {
                    startActivity(MainActivity.class,null,false);
                }
            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        BmobQuery<kePao> query = new BmobQuery<kePao>();
        //按照时间降序
        query.order("-createdAt");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(this, new FindListener<kePao>() {

            public TumblrAdapter mAdapter;

            @Override
            public void onSuccess(List<kePao> loveEntity) {
                mAdapter = new TumblrAdapter(loveEntity);
                lvMlListView.setLayoutManager(new LinearLayoutManager(TumblrActivity.this));
                lvMlListView.setHasFixedSize(true);
                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
                alphaAdapter.setFirstOnly(true);
                alphaAdapter.setDuration(500);
                alphaAdapter.setInterpolator(new OvershootInterpolator(0.5f));
                lvMlListView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
            }

            @Override
            public void onError(int code, String arg0) {

            }
        });

    }


}
