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
 * 右侧主界面ListView的适配器
 *
 * @author Administrator
 *
 */
public class HomeAdapter extends BaseAdapter {
	private Context context;
	private List<ServerEntity.DataBean> data;

	public HomeAdapter(Context context) {
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
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHold holder = null;
		if(arg1 == null){
			arg1 = View.inflate(context, R.layout.item_home, null);
			holder = new ViewHold();
			holder.tv_server = (TextView)arg1.findViewById(R.id.tv_server);
			holder.tv_status = (TextView)arg1.findViewById(R.id.tv_status);
			arg1.setTag(holder);
		}else {
			holder = (ViewHold)arg1.getTag();
		}
        holder.tv_server.setText(data.get(arg0).getServer());
		if ("1".equals(data.get(arg0).getStatus())){
			holder.tv_status.setText("畅通");
		}
		return arg1;
	}
	private static class ViewHold{
		private TextView tv_server;
		private TextView tv_status;
	}
}
