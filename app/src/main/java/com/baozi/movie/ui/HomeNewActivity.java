package com.baozi.movie.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.ui.fragment.BilibiliFragment;
import com.baozi.movie.ui.fragment.ChatTimeFragment;
import com.baozi.movie.ui.fragment.PersonalHomepageFragment;
import com.baozi.movie.ui.fragment.SectionFragment;
import com.baozi.movie.ui.weight.NoScrollViewPager;
import com.baozi.movie.util.DeviceAdaptationUtils;
import com.baozi.movie.util.NavbarUtil;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import com.jpeng.jptabbar.JPTabBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * 主界面
 * @author :baozi
 */
public class HomeNewActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    NoScrollViewPager viewPager;
    @Bind(R.id.tabbar)
    JPTabBar tabbar;
    @Bind(R.id.ll_bottom_bar)
    LinearLayout ll_bottom_bar;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        StatusBarUtil.setTransparent(this);
//        try {
//            Explode explode = new Explode();
//            explode.setDuration(500);
//            getWindow().setExitTransition(explode);
//            getWindow().setEnterTransition(explode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        BmobUpdateAgent.update(this);

        int navigationBarHeight = NavbarUtil.getNavigationBarHeight(this);
        log("navigationBarHeight----->" + navigationBarHeight);
        log("设备------->" + Build.MANUFACTURER.replace(" ", ""));
        if (DeviceAdaptationUtils.isSetTabHeight(navigationBarHeight, this)) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_bottom_bar.getLayoutParams();
            layoutParams.bottomMargin = navigationBarHeight;
            ll_bottom_bar.setLayoutParams(layoutParams);
        }
        viewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
        tabbar = (JPTabBar) findViewById(R.id.tabbar);
        tabbar.setTitles(R.string.tab1,R.string.tab2,R.string.tab3,R.string.tab4)
                .setNormalIcons(R.mipmap.tab1_normal, R.mipmap.tab2_normal, R.mipmap.tab3_normal, R.mipmap.tab4_normal)
                .setSelectedIcons(R.mipmap.tab1_selected, R.mipmap.tab2_selected, R.mipmap.tab3_selected, R.mipmap.tab4_selected)
                .generate();
        viewPager.setNoScroll(false);
        viewPager.setOffscreenPageLimit(4);
        fragments.add(BilibiliFragment.newInstance());
        fragments.add(SectionFragment.newInstance());
//        fragments.add(WeixinViewFragment.newInstance());
        fragments.add(ChatTimeFragment.newInstance());
        fragments.add(PersonalHomepageFragment.newInstance());

        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));

        tabbar.setContainer(viewPager);
    }

    /**
     * 内容页的适配器
     */
    private class ContentAdapter extends FragmentPagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void getHeight(View view) {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        log("statusBarHeight:" + statusBarHeight);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = statusBarHeight;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(layoutParams);
    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparentColor(this, R.color.colorPrimary, 0);
    }
}
