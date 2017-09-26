package com.baozi.movie.baidutiebaauto.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.baozi.movie.baidutiebaauto.model.User;
import com.baozi.movie.baidutiebaauto.ui.fragment.ItemFragment;

import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<User> mUsers;

    public ViewPagerAdapter(FragmentManager fm, List<User> users) {
        super(fm);
        mUsers = users;
    }

    @Override
    public Fragment getItem(int position) {
        User user = mUsers.get(position);
        return ItemFragment.newInstance(user);
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mUsers.get(position).getName();
    }

}
