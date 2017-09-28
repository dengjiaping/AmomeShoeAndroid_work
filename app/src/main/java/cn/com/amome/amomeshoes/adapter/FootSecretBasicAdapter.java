package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.util.ImageUtil;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 脚的秘密-基本情况适配器
 * 
 * @author css
 *
 */
public class FootSecretBasicAdapter extends BaseAdapter {

	private Context context;
	private List<Integer> list;
	private List<String> list2;

	public FootSecretBasicAdapter(Context context, List<Integer> list,
			List<String> list2) {
		super();
		this.context = context;
		this.list = list;
		this.list2 = list2;
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
			convertView = View.inflate(context, R.layout.item_footsecretbasic,
					null);
			holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_info.setText(list2.get(position));
		holder.iv.setImageBitmap(ImageUtil.readBitMap(context.getResources(),
				list.get(position)));

		return convertView;
	}

	class ViewHolder {
		TextView tv_info;
		ImageView iv;
	}

}
