package com.baozi.movie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import com.baozi.movie.base.BaseActivity;
import com.baozi.seemovie.R;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * by:baozi
 * create on 2017/3/13 17:48
 */

public class AppearanceActivity extends BaseActivity {

    @Bind(R.id.et_put_appearance_name)
    EditText etPutAppearanceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);
    }

    @Override
    protected void initView() {
        super.initView();
        setNormalTitle("外观查询");
    }

    @OnClick({R.id.tv_appearance_query, R.id.tv_appearance_editor})
    public void onClick(View view) {
        String appearanceName = etPutAppearanceName.getText().toString().trim();
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_appearance_query:
                intent = new Intent(AppearanceActivity.this, AppearanceQueryActivity.class);
                intent.putExtra("appearanceName", appearanceName);
                break;
            case R.id.tv_appearance_editor:
                intent = new Intent(AppearanceActivity.this, AppearanceEditorActivity.class);
                intent.putExtra("appearanceName", appearanceName);
                break;
        }
        if (intent != null)startActivity(intent);
    }
}
