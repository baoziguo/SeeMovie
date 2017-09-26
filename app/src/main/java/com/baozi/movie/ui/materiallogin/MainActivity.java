package com.baozi.movie.ui.materiallogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.User;
import com.baozi.movie.model.UserModel;
import com.baozi.movie.util.HideIMEUtil;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class MainActivity extends BaseActivity {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.tv_weibo)
    TextView tv_weibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        HideIMEUtil.wrap(this);
        tv_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMShareAPI.get(MainActivity.this).doOauthVerify(MainActivity.this, SHARE_MEDIA.SINA, mUMAuthListener);
            }
        });
    }

    /**
     * 新浪授权回调
     */
    private UMAuthListener mUMAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (map != null){
                String accesstoken = map.get("accesstoken");
                String name = map.get("name");
                String iconurl = map.get("iconurl");
                log("accesstoken:" + accesstoken);
                log("name:" + name);
                log("iconurl:" + iconurl);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                try {
//                    getWindow().setExitTransition(null);
//                    getWindow().setEnterTransition(null);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                        startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                    } else {
                        startActivity(new Intent(this, RegisterActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                UserModel.getInstance().login(etUsername.getText().toString(), etPassword.getText().toString(), new LogInListener() {

                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            User user = (User) o;
                            BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
//                            Explode explode = new Explode();
//                            explode.setDuration(500);
//
//                            getWindow().setExitTransition(explode);
//                            getWindow().setEnterTransition(explode);
//                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
//                            Intent i2 = new Intent(MainActivity.this, com.baozi.movie.ui.MainActivity.class);
                            Intent i2 = new Intent(MainActivity.this, com.baozi.movie.ui.HomeNewActivity.class);
                            startActivity(i2);
                            finish();
//                            startActivity(com.baozi.movie.ui.MainActivity.class, null, true);
                        } else {
//                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                            ToastUtils.showCustomToast("骚年，记性有点减退哦~");
                        }
                    }
                });

                break;
        }
    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparent(this);
    }
}
