package com.baozi.movie.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.util.CacheUtils;
import com.baozi.seemovie.R;

import java.io.File;

import butterknife.Bind;
import sun.bob.pooredit.ShowView;

/**
 * by:baozi
 * create on 2017/3/13 12:01
 */

public class AppearanceQueryActivity extends BaseActivity {

    @Bind(R.id.poor_edit)
    ShowView poorEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance_query);
    }

    @Override
    protected void initView() {
        super.initView();
        setNormalTitle("外观查询");
        String appearanceName = getIntent().getStringExtra("appearanceName");
        if (CacheUtils.hasSdcard()) {
            String appearacePath = new File(Environment.getExternalStorageDirectory() + "/appearanceDir").getAbsolutePath();
            poorEdit.loadJson(appearacePath, appearanceName);
        }
    }

}
