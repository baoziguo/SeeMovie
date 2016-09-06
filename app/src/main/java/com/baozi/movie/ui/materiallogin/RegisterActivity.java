package com.baozi.movie.ui.materiallogin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.event.FinishEvent;
import com.baozi.movie.model.BaseModel;
import com.baozi.movie.model.UserModel;
import com.baozi.movie.util.HideIMEUtil;
import com.baozi.movie.util.StatusBarUtil;
import com.baozi.seemovie.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.cv_add)
    CardView cvAdd;
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_repeatpassword)
    EditText etRepeatpassword;
    @Bind(R.id.bt_go)
    Button btGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);
        ButterKnife.bind(this);
        HideIMEUtil.wrap(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.mipmap.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Override
    public void setStatusState(int color) {
        StatusBarUtil.setTransparent(this);
    }

    @OnClick(R.id.bt_go)
    public void onClick() {
        String passWord = etPassword.getText().toString().trim();
        String repeatPassWord = etRepeatpassword.getText().toString().trim();
        UserModel.getInstance().register(etUsername.getText().toString(), passWord, repeatPassWord, new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new FinishEvent());
                    startActivity(com.baozi.movie.ui.MainActivity.class, null, true);
                } else {
                    if (e.getErrorCode() == BaseModel.CODE_NOT_EQUAL) {
                        etRepeatpassword.setText("");
                    }
//                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                    toast(e.getMessage());
                }
            }
        });
    }
}
