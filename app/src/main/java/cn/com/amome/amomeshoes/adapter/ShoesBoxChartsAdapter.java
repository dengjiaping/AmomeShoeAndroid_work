package cn.com.amome.amomeshoes.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.util.ColorTemplate;
import cn.com.amome.amomeshoes.widget.CircleImageView;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoesBoxChartsAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	List<Integer> numList;
	List<String> scaleList;
	List<Integer> colorList = new ArrayList<Integer>();

	public ShoesBoxChartsAdapter(Context context, List<String> list,
			List<Integer> numList, List<String> scaleList) {
		super();
		this.context = context;
		this.list = list;
		this.numList = numList;
		this.scaleList = scaleList;
		colorList = getColorList16((ArrayList<Integer>) colorList);
		// for (int i = 0; i < colorList.size(); i++) {
		// Log.i("ShoesBoxChartsAdapter", "颜色" + colorList.get(i));
		// }
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
			convertView = View.inflate(context, R.layout.item_shoebox_charts,
					null);
			holder.tv_class = (TextView) convertView
					.findViewById(R.id.tv_class);
			holder.tv_scale = (TextView) convertView
					.findViewById(R.id.tv_scale);
			holder.tv_count = (TextView) convertView
					.findViewById(R.id.tv_count);
			holder.iv_color = convertView.findViewById(R.id.iv_color);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_class.setText(list.get(position));
		holder.tv_scale.setText(scaleList.get(position) + "");
		int count = numList.get(position);
		holder.tv_count.setText(numList.get(position) + "");
		// holder.iv_color.setImageResource(colorList.get(position));
		holder.iv_color.setBackgroundResource(colorList.get(position));
		return convertView;
	}

	class ViewHolder {
		View iv_color;
		TextView tv_class, tv_scale, tv_count;
	}

	private final int[] COLORFUL_COLORS16 = { R.color.color1, R.color.color2,
			R.color.color3, R.color.color4, R.color.color5, R.color.color6,
			R.color.color7, R.color.color8, R.color.color9, R.color.color10,
			R.color.color11, R.color.color12, R.color.color13, R.color.color14,
			R.color.color15, R.color.color16 };

	private ArrayList<Integer> getColorList16(ArrayList<Integer> colorList) {
		for (int i = 0; i < 16; i++) {
			colorList.add(COLORFUL_COLORS16[i]);
		}
		return colorList;
	}
}
