package com.baozi.movie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.baozi.movie.adapter.PictureNewAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.ui.AppearanceSearchActivity;
import com.baozi.movie.ui.DirectSeedingActivity;
import com.baozi.movie.ui.HomeNewActivity;
import com.baozi.movie.ui.PVEActivity;
import com.baozi.movie.ui.PVPActivity;
import com.baozi.movie.ui.TiebaPostSearchActivity;
import com.baozi.movie.ui.WeiBoActivity;
import com.baozi.movie.util.ToastUtils;
import com.baozi.seemovie.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by:baozi
 * create on 2017/4/5 16:17
 */

public class SectionFragment extends HeaderViewPagerFragment {

    @Bind(R.id.status_view)
    View statusView;
    @Bind(R.id.gridView)
    GridView gridView;

    //图片的文字标题
    private String[] titles = new String[]
            {"直播", "近期外观", "微博一览", "PVE专版", "PVP专版", "CP交友", "帖子搜索" };
    //图片ID数组
    private int[] images = new int[]{
            R.drawable.ban1, R.drawable.ban2, R.drawable.ban3, R.drawable.ban4, R.drawable.ban5, R.drawable.ban6,
            R.drawable.ban7
    };

    public static SectionFragment newInstance() {
        return new SectionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeNewActivity) getActivity()).getHeight(statusView);
        initView();
    }

    private void initView() {
        PictureNewAdapter adapter = new PictureNewAdapter(titles, images, getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0://直播
                        startActivity(new Intent(getActivity(), DirectSeedingActivity.class));
                        break;
                    case 1://近期外观
                        startActivity(new Intent(getActivity(), AppearanceSearchActivity.class));
                        break;
                    case 2://微博一览
                        startActivity(new Intent(getActivity(), WeiBoActivity.class));
                        break;
                    case 3://PVE专版
//                        if (statusView.getVisibility() == View.GONE){
//                            statusView.setVisibility(View.VISIBLE);
//                            ToastUtils.showCustomToast("我是状态栏，我显示啦");
//                        }else {
//                            statusView.setVisibility(View.GONE);
//                            ToastUtils.showCustomToast("我是状态栏，我隐藏啦");
//                        }

                        startActivity(new Intent(getActivity(), PVEActivity.class));
                        break;
                    case 4://PVP专版
                        startActivity(new Intent(getActivity(), PVPActivity.class));
                        break;
                    case 5://CP交友
                        ToastUtils.showCustomToast("考虑弄不弄~~");
                        break;
                    case 6://帖子搜索
                       startActivity(new Intent(getActivity(), TiebaPostSearchActivity.class));
                       break;
                }
            }
        });
    }

    @Override
    public View getScrollableView() {
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
