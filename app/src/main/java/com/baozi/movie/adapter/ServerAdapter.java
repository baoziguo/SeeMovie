package com.baozi.movie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baozi.movie.bean.ServerEntity;
import com.baozi.seemovie.R;
import java.util.List;

/**
 * 左侧菜单ListView的适配器
 *
 * @author Administrator
 */
public class ServerAdapter extends BaseAdapter {

    private Context context;
    private int selectItem = 0;
    private List<ServerEntity.DataBean> data;

    public ServerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ServerEntity.DataBean> data){
        this.data = data;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder = null;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(context, R.layout.item_server, null);
            holder.tv_user_select_server = (TextView) arg1.findViewById(R.id.tv_user_select_server);
            holder.tv_status = (TextView) arg1.findViewById(R.id.tv_status);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        ServerEntity.DataBean dataBean = data.get(arg0);
        holder.tv_user_select_server.setText(dataBean.getZone() + "-" + dataBean.getServer());
        if (dataBean.getStatus().equals("1"))
            holder.tv_status.setText("畅通");
        return arg1;
    }

    static class ViewHolder {
        private TextView tv_user_select_server;
        private TextView tv_status;
    }
}
