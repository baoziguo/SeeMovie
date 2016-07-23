package com.baozi.movie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import com.baozi.movie.adapter.TumblrAdapter;
import com.baozi.movie.base.HeaderViewPagerFragment;
import com.baozi.movie.bean.kePao;
import com.baozi.seemovie.R;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class TumblrViewFragment extends HeaderViewPagerFragment {

    private RecyclerView mRecyclerView;

    public static TumblrViewFragment newInstance() {
        return new TumblrViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        BmobQuery<kePao> query = new BmobQuery<kePao>();
        //按照时间降序
        query.order("-createdAt");
        //执行查询，第一个参数为上下文，第二个参数为查找的回调
        query.findObjects(getActivity(), new FindListener<kePao>() {

            public TumblrAdapter mAdapter;

            @Override
            public void onSuccess(List<kePao> loveEntity) {
                mAdapter = new TumblrAdapter(getActivity(), loveEntity);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setHasFixedSize(true);
                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
                alphaAdapter.setFirstOnly(true);
                alphaAdapter.setDuration(500);
                alphaAdapter.setInterpolator(new OvershootInterpolator(0.5f));
                mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
            }

            @Override
            public void onError(int code, String arg0) {

            }
        });

    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

//    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.SimpleViewHolder> {
//
//        private List<String> strings;
//
//        public RecyclerAdapter() {
//            strings = new ArrayList<>();
//            for (int i = 0; i < 40; i++) {
//                strings.add("条目" + i);
//            }
//        }
//
//        @Override
//        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new SimpleViewHolder(View.inflate(getActivity(), android.R.layout.simple_list_item_1, null));
//        }
//
//        @Override
//        public void onBindViewHolder(SimpleViewHolder holder, int position) {
//            holder.bindData(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return strings.size();
//        }
//
//        public class SimpleViewHolder extends RecyclerView.ViewHolder {
//            TextView itemView;
//            int position;
//
//            public SimpleViewHolder(View itemView) {
//                super(itemView);
//                this.itemView = (TextView) itemView;
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(), "点击RecycleView item" + position, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            public void bindData(int position) {
//                itemView.setGravity(Gravity.CENTER);
//                itemView.setTextColor(Color.WHITE);
//                ViewGroup.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
//                itemView.setLayoutParams(params);
//                itemView.setText(strings.get(position));
//                itemView.setBackgroundColor(Utils.generateBeautifulColor());
//            }
//        }
//    }
}
