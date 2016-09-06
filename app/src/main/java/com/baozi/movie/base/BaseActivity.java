package com.baozi.movie.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baozi.movie.Config;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**基类
 * @author :smile
 * @project:BaseActivity
 * @date :2016-01-15-18:23
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setStatusState(getResources().getColor(R.color.colorPrimary));
        initView();
    }

    public void setStatusState(int color) {
        if (Build.VERSION.SDK_INT == 19)
            StatusBarUtil.setColor(this, color, 0);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void onEvent(Boolean empty){

    }

    protected void initView() {}

    protected void runOnMain(Runnable runnable) {
        runOnUiThread(runnable);
    }

    protected final static String NULL = "";
    private Toast toast;
    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(BaseActivity.this, NULL,Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle,boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**隐藏软键盘-一般是EditText.getWindowToken()
     * @param token
     */
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**Log日志
     * @param msg
     */
    public void log(String msg){
        if(Config.DEBUG){
            Logger.i(msg);
        }
    }

    /**
     * 设置title
     * @param title
     */
    protected void setTitle(String title){//设置title
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
    }

    /**
     * 左侧返回按钮 by：baozi
     * @param listener
     */
    protected void showLeftButton(View.OnClickListener listener){//
        ImageView tv_left = (ImageView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(listener);
    }

    /**
     * 右侧按钮 by：baozi
     * @param listener
     */
    protected void showRightButton(int resourceId, View.OnClickListener listener){//
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(listener);
        tv_right.setBackgroundResource(resourceId);
    }

    /**
     * 左侧图标替换按钮 by：baozi
     * @param resourceId
     * @param listener
     */
    protected void showLeftButton(int resourceId, View.OnClickListener listener){
        ImageView tv_left = (ImageView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(listener);
        tv_left.setBackgroundResource(resourceId);
    }

    /**
     * 设置正常的标题和返回键
     */
    protected void setNormalTitle(String titleName){
        setTitle(titleName);
        showLeftButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}
