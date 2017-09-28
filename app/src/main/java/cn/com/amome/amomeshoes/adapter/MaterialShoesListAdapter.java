package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.MaterialShoes;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.ShoeCategoryListActivity;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.ShoeMaterialListActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MaterialShoesListAdapter extends BaseAdapter {
	private Context context;
	private List<MaterialShoes> list;
	private int index = -1;
	private String material;

	public MaterialShoesListAdapter(Context context, List<MaterialShoes> list,
			String material) {
		super();
		this.context = context;
		this.list = list;
		this.material = material;
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

	public void setString(String str) {
		material = str;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, R.layout.item_joblist, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_jobname);
			holder.iv_selected = (ImageView) convertView
					.findViewById(R.id.iv_selected);
			holder.rl_job = (RelativeLayout) convertView
					.findViewById(R.id.rl_job);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).material);
		if (list.get(position).material.equals(material))
			holder.iv_selected.setVisibility(View.VISIBLE);
		else
			holder.iv_selected.setVisibility(View.GONE);
		
		holder.rl_job.setTag(position);
		holder.rl_job.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				index = position;
				material = list.get(index).material;
				notifyDataSetChanged();
				((ShoeMaterialListActivity) context).setSelect(index);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv_selected;
		RelativeLayout rl_job;
	}
}
