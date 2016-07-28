package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.ui.fragment.TumblrViewFragment;
import com.baozi.movie.ui.fragment.WeixinViewFragment;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.movie.util.Utils;
import com.baozi.seemovie.R;
import com.lzy.widget.HeaderViewPager;
import com.lzy.widget.tab.CircleIndicator;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    public List<HeaderViewPagerFragment> fragments;
    @Bind(R.id.pagerHeader)
    ViewPager pagerHeader;
    @Bind(R.id.ci)
    CircleIndicator ci;
    @Bind(R.id.tab_view)
    SmartTabLayout tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @Bind(R.id.titleBar)
    View titleBar;
    @Bind(R.id.back)
    ImageView back;
    private View titleBar_Bg;
    private TextView titleBar_title;
    private View status_bar_fix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        //内容的fragment
        fragments = new ArrayList<>();
        fragments.add(TumblrViewFragment.newInstance());
        fragments.add(WeixinViewFragment.newInstance());
        fragments.add(TumblrViewFragment.newInstance());
//        fragments.add(RecyclerViewFragment.newInstance());
//        fragments.add(WebViewFragment.newInstance());

        scrollableLayout = (HeaderViewPager) findViewById(R.id.scrollableLayout);
        titleBar = findViewById(R.id.titleBar);
        titleBar_Bg = titleBar.findViewById(R.id.bg);
        //当状态栏透明后，内容布局会上移，这里使用一个和状态栏高度相同的view来修正内容区域
        status_bar_fix = titleBar.findViewById(R.id.status_bar_fix);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getStatusHeight(this)));
        titleBar_title = (TextView) titleBar.findViewById(R.id.title);
        titleBar_Bg.setAlpha(0);
        status_bar_fix.setAlpha(0);
        titleBar_title.setAlpha(0);
        pagerHeader = (ViewPager) findViewById(R.id.pagerHeader);
        pagerHeader.setAdapter(new HeaderAdapter());
        ci.setViewPager(pagerHeader);

        //tab标签和内容viewpager
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                pagerHeader.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;
                titleBar_Bg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                status_bar_fix.setAlpha(alpha);
                titleBar_title.setAlpha(alpha);
                back.setVisibility(alpha == 1 ? View.VISIBLE : View.GONE);
            }
        });
        viewPager.setCurrentItem(0);
    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparent(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当前窗口获取焦点时，才能正真拿到titlebar的高度，此时将需要固定的偏移量设置给scrollableLayout即可
        scrollableLayout.setTopOffset(titleBar.getHeight());
    }

    /**
     * 内容页的适配器
     */
    private class ContentAdapter extends FragmentPagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        public String[] titles = new String[]{"B站视频", "微信文章", "小电影?"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
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

    /**
     * 头布局的适配器
     */
    private class HeaderAdapter extends PagerAdapter {

        public int[] images = new int[]{//
                R.mipmap.c, R.mipmap.b, R.mipmap.e,//
                R.mipmap.g, R.mipmap.v, R.mipmap.x, R.mipmap.z};

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(images[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
