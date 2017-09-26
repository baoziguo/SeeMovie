package com.baozi.movie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.baozi.movie.base.BaseActivity;
import com.baozi.movie.ui.fragment.ConversationFragment;
import com.baozi.seemovie.R;

import butterknife.Bind;

/**
 * by:baozi
 * create on 2017/4/5 15:59
 */

public class ConversationActivity extends BaseActivity {

    @Bind(R.id.root)
    FrameLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ConversationFragment conversationFragment = new ConversationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.root, conversationFragment).commitAllowingStateLoss();
    }
}
