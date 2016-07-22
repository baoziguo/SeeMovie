package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.baozi.movie.base.BaseActivity;
import com.baozi.seemovie.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面
 */
public class TabActivity extends BaseActivity {


    @Bind(R.id.tab_view)
    SmartTabLayout tabView;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        viewpager.setOffscreenPageLimit(6);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
//                .add("全部", AllDetailedFragmentNew.class, new Bundler().putInt("type", 0).get())
//                .add("充值", RedDetailedFragmentNew.class, new Bundler().putInt("type", 1).get())
//                .add("投资", RedDetailedFragmentNew.class, new Bundler().putInt("type", 2).get())
//                .add("回款", RedDetailedFragmentNew.class, new Bundler().putInt("type", 3).get())
//                .add("收益", RedDetailedFragmentNew.class, new Bundler().putInt("type", 4).get())
//                .add("提现", RedDetailedFragmentNew.class, new Bundler().putInt("type", 5).get())
                .create());

        viewpager.setAdapter(adapter);
        tabView.setViewPager(viewpager);

    }

}
