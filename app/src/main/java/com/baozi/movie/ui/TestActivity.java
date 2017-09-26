package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baozi.movie.base.BaseActivity;
import com.baozi.seemovie.R;
import com.koushikdutta.ion.Ion;

import java.util.concurrent.ExecutionException;

/**
 * Created by user on 2016/8/19.
 */
public class TestActivity extends BaseActivity {

//    private String title = "【九种声线】色情主播教你喘！";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        try {
            String content = Ion.with(this).load("http://weibo.com/j3wujia?from=myfollow_all&is_all=1&is_search=1&key_word=%E9%B8%A1%E9%87%91").setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0").asString().get();
            log("外观搜索数据：" + content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
