package com.baozi.movie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.util.Utils;
import com.baozi.seemovie.R;

public class ScrollViewFragment extends HeaderViewPagerFragment {

    private View scrollView;

    public static ScrollViewFragment newInstance() {
        return new ScrollViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scrollView = inflater.inflate(R.layout.fragment_scrollview, container, false);
        LinearLayout views = (LinearLayout) scrollView.findViewById(R.id.container);
        for (int i = 0; i < 10; i++) {
            View view = new View(getActivity());
            view.setBackgroundColor(Utils.generateBeautifulColor());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            view.setLayoutParams(params);
            views.addView(view);
        }
        return scrollView;
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
