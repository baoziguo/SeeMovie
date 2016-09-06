package com.baozi.movie.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;
import com.baozi.movie.bean.User;
import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.model.UserModel;

/**启动界面
 * @author :smile
 * @project:SplashActivity
 * @date :2016-01-15-18:23
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(com.baozi.movie.ui.materiallogin.MainActivity.class,null,true);
                }else{
                    startActivity(HomeActivity.class,null,true);
                }
            }
        },2000);

    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparent(this);
    }
}
