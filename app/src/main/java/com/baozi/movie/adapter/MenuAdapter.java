package com.baozi.movie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baozi.movie.bean.ZoneEntity;
import com.baozi.seemovie.R;
import java.util.List;

/**
 * 左侧菜单ListView的适配器
 * @author Administrator
 */
public class MenuAdapter extends BaseAdapter{
	
	private Context context;
	private int selectItem = 0;
	private List<ZoneEntity.DataBean> data;

	public MenuAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<ZoneEntity.DataBean> data){
		this.data = data;
	}

	
	public int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
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
		ViewHolder holder = null;
		if(arg1 == null) {
			holder = new ViewHolder();
			arg1 = View.inflate(context, R.layout.item_menu, null);
			holder.tv_name = (TextView)arg1.findViewById(R.id.item_name);
			holder.select_status = arg1.findViewById(R.id.select_status);
			arg1.setTag(holder);
		}else {
			holder = (ViewHolder)arg1.getTag();
		}
		if(arg0 == selectItem){
			holder.tv_name.setBackgroundResource(R.color.base_color_text_white);
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
			holder.select_status.setBackgroundResource(R.color.colorPrimary);
		}else {
			holder.tv_name.setBackgroundResource(R.color.transparent);
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.grey_9b9b9b));
			holder.select_status.setBackgroundResource(R.color.transparent);
		}
		holder.tv_name.setText(data.get(arg0).getZone());
		return arg1;
	}
	static class ViewHolder{
		private TextView tv_name;
		private View select_status;
	}
}
