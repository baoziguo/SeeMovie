package com.baozi.movie.ui.multi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.ui.multi.Info;
import com.baozi.movie.ui.multi.util.Util;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.List;

import butterknife.Bind;

/**
 * Created by linlongxin on 2016/1/22.
 */
public class ViewImageActivity extends BaseActivity {

    public static final String IMAGES_DATA_LIST = "DATA_LIST";
    public static final String IMAGE_NUM = "IMAGE_NUM";
    @Bind(R.id.ll_head_title)
    LinearLayout llHeadTitle;
    @Bind(R.id.status_view)
    View status_view;
    //    private NoPreloadViewPager viewPager;
    private ViewPager viewPager;
    private TextView number;
    private List<String> data;
    private int position;
    private int dataLength = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Util.init(this);

        StatusBarUtil.setTransparent(this);

        llHeadTitle.setBackgroundResource(R.color.transparent);

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) status_view.getLayoutParams();
        layoutParams.height = statusBarHeight;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        status_view.setLayoutParams(layoutParams);

        showLeftButton(R.drawable.save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.downloadImage(data.get(viewPager.getCurrentItem()));
            }
        });

        showRightButton(R.mipmap.ic_find_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(data.get(viewPager.getCurrentItem()))) {
                    new ShareAction(ViewImageActivity.this).withMedia(new UMImage(ViewImageActivity.this, data.get(viewPager.getCurrentItem())))
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ)//,SHARE_MEDIA.WEIXIN
                            .setCallback(umShareListener).open();
                } else {
                    ToastUtils.showCustomToast("图片尚未加载出来或图片链接已失效");
                }
            }
        });

//        viewPager = (NoPreloadViewPager) findViewById(R.id.view_pager);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        number = (TextView) findViewById(R.id.number);

        data = (List<String>) getIntent().getSerializableExtra(IMAGES_DATA_LIST);
        position = getIntent().getIntExtra(IMAGE_NUM, -1);
        dataLength = data.size();

        Info info = getIntent().getParcelableExtra("info");

        viewPager.setAdapter(new ViewImageAdapter(info, data, this));
        viewPager.setCurrentItem(position);
        number.setText(position + 1 + "/" + dataLength);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                number.setText(viewPager.getCurrentItem() + 1 + "/" + dataLength);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        viewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                number.setText(viewPager.getCurrentItem() + 1 + "/" + dataLength);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

//            Toast.makeText(WebViewActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(WebViewActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(WebViewActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
