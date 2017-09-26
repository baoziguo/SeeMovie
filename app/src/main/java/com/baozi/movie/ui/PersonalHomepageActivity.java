package com.baozi.movie.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.baozi.movie.adapter.PictureAdapter;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import butterknife.Bind;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

/**
 * by:baozi
 * create on 2017/3/6 17:51
 */

public class PersonalHomepageActivity extends BaseActivity {

    @Bind(R.id.iv_bg)
    ImageView iv_bg;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.iv_avatar)
    AvatarImageView ivAvatar;

    private float movement = 1.1f;
    //图片的文字标题
    private String[] titles = new String[]
            {"开服监控", "金价查询", "外观查询", "配装查询", "加速宝典", "奇遇查询", "宠物监控", "日常提醒", "推送管理"};
    //图片ID数组
    private int[] images = new int[]{
            R.mipmap.aa, R.mipmap.bb, R.mipmap.cc,
            R.mipmap.dd, R.mipmap.ee, R.mipmap.ff,
            R.mipmap.gg, R.mipmap.hh, R.mipmap.ii
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_homepage);
        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void initView() {
        iv_bg.animate().scaleX(movement).scaleY(movement).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                movement = movement > 1.0f ? 1.0f : 1.1f;
                if (iv_bg != null)
                    iv_bg.animate().scaleX(movement).scaleY(movement).setDuration(1500).setListener(this).start();
            }
        }).start();
        PictureAdapter adapter = new PictureAdapter(titles, images, this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0://开服监控
                        startActivity(new Intent(PersonalHomepageActivity.this, OpenServiceMonitoringActivity.class));
                        break;
                    case 1://金价查询
                        WebViewActivity.startWebActivity(PersonalHomepageActivity.this, "http://www.yxdr.com/bijiaqi/jian3/", "");
                        break;
                    case 2://外观查询
                        startActivity(new Intent(PersonalHomepageActivity.this, AppearanceActivity.class));
                        break;
                    case 3://配装查询
                        WebViewActivity.startWebActivity(PersonalHomepageActivity.this, "https://www.j3pz.com/", "");
                        break;
                    case 4://加速宝典
                        WebViewActivity.startWebActivity(PersonalHomepageActivity.this, "https://www.j3pz.com/tools/haste/", "");
                        break;
                    case 5://奇遇查询
                        WebViewActivity.startWebActivity(PersonalHomepageActivity.this, "http://jx3.derzh.com/serendipity/", "");
                        break;
                    case 6://宠物监控 http://www.ab72.top/#/
                        WebViewActivity.startWebActivity(PersonalHomepageActivity.this, "http://www.yayaquanzhidao.top/SelectPet.aspx", "");
                        break;
                    case 7://日常提醒
                        WebViewActivity.startWebActivity(PersonalHomepageActivity.this, "https://haimanchajian.com/richangtixing?day=today", "");
                        break;
                    case 8://推送管理
                        startActivity(new Intent(PersonalHomepageActivity.this, OpenPushActivity.class));
                        break;
                }
            }
        });
    }

}
