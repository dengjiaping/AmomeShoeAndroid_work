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

public class MainShoeAdapter extends BaseAdapter {
	private Context context;
	private List<Integer> list;
	private List<Integer> list2;
	private List<Integer> list3;
	private int selectedPosition = -1;

	// private int[] shoeNum = { 9, 8, 7, 6, 5, 4, 3, 2, 1 };

	public MainShoeAdapter(Context context, List<Integer> list,
			List<Integer> list2, List<Integer> list3) {
		super();
		this.context = context;
		this.list = list;
		this.list2 = list2;
		this.list3 = list3;
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

	// 这句是把grideview的点击position,传递过来
	public void setSelection(int position) {
		selectedPosition = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.item_shoebox_categorylocal, null);
			holder.iv_shoe = (ImageView) convertView.findViewById(R.id.iv_shoe);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv_shoe.setImageBitmap(ImageUtil.readBitMap(
				context.getResources(), list.get(position)));
		holder.tv_num.setText(list2.get(position) + "");
		return convertView;
	}

	class ViewHolder {
		ImageView iv_shoe;
		private TextView tv_num;
	}
}
