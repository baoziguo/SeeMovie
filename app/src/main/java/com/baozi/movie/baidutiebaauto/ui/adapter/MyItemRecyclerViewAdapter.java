package com.baozi.movie.baidutiebaauto.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baozi.movie.baidutiebaauto.model.Tieba;
import com.baozi.movie.baidutiebaauto.ui.fragment.ItemFragment;
import com.baozi.seemovie.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.List;

/**
 * RecyclerView适配器
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MyItemRecyclerViewAdapter";

    private List<Tieba> mTiebas;
    private int position; // 当前选择的位置
    private ItemFragment.OnRecyclerItemListener mListener;

    public MyItemRecyclerViewAdapter(List<Tieba> tiebas, ItemFragment.OnRecyclerItemListener listener) {
        mTiebas = tiebas;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tieba tieba = mTiebas.get(position);

        holder.mTieba = tieba;
        holder.mTiebaName.setText(tieba.getName());
        holder.mLevelView.setText(tieba.getLevel());
        holder.mExpView.setText(tieba.getExp() + "/" + tieba.getLevelup_score());
        if(!tieba.getAvatar().equals(holder.iv_avatar.getTag())){//增加tag标记，减少UIL的display次数
            holder.iv_avatar.setTag(tieba.getAvatar());
            //不直接display imageview改为ImageAware，解决RecycleView滚动时重复加载图片
            ImageAware imageAware = new ImageViewAware(holder.iv_avatar, false);
            ImageLoader.getInstance().displayImage(tieba.getAvatar(), imageAware);
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                String name = ((TextView) v.findViewById(R.id.tieba_name)).getText().toString();
                menu.setHeaderTitle("请选择要对\"" + name + "\"吧进行的操作:");
                menu.add(0, R.id.context_menu_sign, 0, "签到");
                menu.add(0, R.id.context_menu_delete, 1, "删除");
                setPosition((int) v.getTag());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mTiebas.size();
    }

    /**
     * 得到当前选择的Item的位置
     * 参考: http://stackoverflow.com/questions/26466877/how-to-create-context-menu-for-recyclerview
     */
    public int getPosition() {
        return position;
    }

    /**
     * 设置当前的Item的位置
     */
    public void setPosition(int position) {
        this.position = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTiebaName;
        public final TextView mLevelView;
        public final TextView mExpView;
        public final ImageView iv_avatar;
        public Tieba mTieba;

        public ViewHolder(View view) {
            super(view);
            mTiebaName = (TextView) view.findViewById(R.id.tieba_name);
            mLevelView = (TextView) view.findViewById(R.id.level);
            mExpView = (TextView) view.findViewById(R.id.exp);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        }

    }
}
