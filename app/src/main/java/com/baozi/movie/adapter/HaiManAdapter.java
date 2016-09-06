package com.baozi.movie.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.baozi.movie.bean.HaiManChaoMi;
import com.baozi.seemovie.R;

import java.util.List;

public class HaiManAdapter extends RecyclerView.Adapter<HaiManAdapter.MyViewHolder> {

    Context context;
    List<HaiManChaoMi> mListData;

    public HaiManAdapter(Context context, List<HaiManChaoMi> mListData) {
        this.mListData = mListData;
        this.context = context;
    }

    public void setmListData(List<HaiManChaoMi> mListData){
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hanman_chaojimimi_item,
                viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final HaiManChaoMi haiManChaoMi = mListData.get(i);
        runEnterAnimation(myViewHolder.itemView, i);//瞎鸡巴弄个动画
        HaiManChaoMi.UserBean user = haiManChaoMi.getUser();
        myViewHolder.tv_content.setText(haiManChaoMi.getContent());
        myViewHolder.tv_name.setText(user.getName().split("<")[0]);
    }
    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_content;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private void runEnterAnimation(View view, int position) {
        view.setTranslationY(((Activity)context).getWindowManager().getDefaultDisplay().getHeight());
        view.animate()
                .translationY(0)
                .setStartDelay(100 * (position % 5))
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

}

