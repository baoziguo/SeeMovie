package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.util.HideIMEUtil;
import com.baozi.seemovie.R;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobPushManager;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * by:baozi
 * create on 2017/3/10 14:16
 */

public class OpenPushActivity extends BaseActivity {


    @Bind(R.id.et_put_push_content)
    EditText etPutPushContent;
    @Bind(R.id.tv_open_push)
    TextView tvOpenPush;
    private BmobPushManager bmobPushManager;
    private Subscription sb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_push);
        HideIMEUtil.wrap(this);
    }

    @Override
    protected void initView() {
        setNormalTitle("推送管理");
        tvOpenPush.setEnabled(false);
        etPutPushContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvOpenPush.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.tv_open_push)
    public void onClick() {
        final String pushContent = etPutPushContent.getText().toString().trim();
        if (!pushContent.endsWith("520"))
            return;
        sb = Observable.interval(0, 1, TimeUnit.HOURS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aLong) {
                        Calendar now = Calendar.getInstance();
                        int hour = now.get(Calendar.HOUR_OF_DAY);
                        log("hour:" + hour);
                        if (hour == 7){
                            bmobPushManager = new BmobPushManager(OpenPushActivity.this);
                            bmobPushManager.pushMessageAll(pushContent.substring(0, pushContent.length()-3));
                            sb.unsubscribe();
                        }

                    }
                });
    }
}
