package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.amome.amomeshoes.R;

/**
 * 健康促进主页面适配器
 * 
 * @author css
 *
 */
public class HealthPromotionMainAdapter extends BaseAdapter {

	private Context context;
	private List<Integer> list;

	public HealthPromotionMainAdapter(Context context, List<Integer> list) {
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
			convertView = View.inflate(context, R.layout.item_health_promotion,
					null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		TextView illness_name;
		ImageView illness_photo,fitting_icon,curing_icon,action_icon;

	}

}
