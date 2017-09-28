package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ShoesProblem;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.AddShoesBoxActivity;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.ShoesDelReasonActivity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class ShoeDelReasonConstantAdapter extends BaseAdapter {

	private Context context;
	private int clickTemp = -1;
	private List<String> list;

	public ShoeDelReasonConstantAdapter(Context context, List<String> list) {
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
			convertView = View.inflate(context, R.layout.item_shoe_del_reason,
					null);
			holder.cb_pro = (CheckBox) convertView.findViewById(R.id.cb_pro);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (clickTemp == position) {
		} else {
		}
		holder.cb_pro.setText(list.get(position));
		holder.cb_pro.setTag(position);
		holder.cb_pro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				int position = (Integer) v.getTag();
				notifyDataSetChanged();
				((ShoesDelReasonActivity) context).setConstantSelect(position,
						cb.isChecked());
			}
		});
		return convertView;
	}

	class ViewHolder {
		CheckBox cb_pro;
	}
}
