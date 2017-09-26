package com.baozi.movie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.util.CacheUtils;
import com.baozi.seemovie.R;
import java.io.File;
import butterknife.Bind;
import sun.bob.pooredit.PoorEdit;

/**
 * 外观编辑
 * by:baozi
 * create on 2017/3/13 18:08
 */

public class AppearanceEditorActivity extends BaseActivity {

    @Bind(R.id.poor_edit)
    PoorEdit poorEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance_editor);
    }

    @Override
    protected void initView() {
        super.initView();
        setNormalTitle("外观编辑");
        final String appearanceName = getIntent().getStringExtra("appearanceName");
        showRightTextButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CacheUtils.hasSdcard()) {
                    String appearacePath = new File(Environment.getExternalStorageDirectory() + "/appearanceDir").getAbsolutePath();
                    poorEdit.exportJSON(appearacePath, appearanceName);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        poorEdit.onActivityResult(requestCode, resultCode, data);
    }
}
