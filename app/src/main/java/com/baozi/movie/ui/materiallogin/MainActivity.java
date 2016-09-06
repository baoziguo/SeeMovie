package com.baozi.movie.ui.materiallogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.bean.User;
import com.baozi.movie.model.UserModel;
import com.baozi.movie.util.HideIMEUtil;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
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
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        HideIMEUtil.wrap(this);
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
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
                            Explode explode = new Explode();
                            explode.setDuration(500);

                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                            Intent i2 = new Intent(MainActivity.this, com.baozi.movie.ui.MainActivity.class);
                            startActivity(i2, oc2.toBundle());
//                            startActivity(com.baozi.movie.ui.MainActivity.class, null, true);
                        } else {
//                            toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                            toast("骚年，记性有点减退哦~");
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
