package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.PromotionInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 健康促进主页面适配器
 * 
 * @author css
 *
 */
public class PromotionAddAdapter extends BaseAdapter {

	private Context context;
	private List<PromotionInfo> list;

	public PromotionAddAdapter(Context context, List<PromotionInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.item_health_promotion_add, null);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(position).type);
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
	}

}
