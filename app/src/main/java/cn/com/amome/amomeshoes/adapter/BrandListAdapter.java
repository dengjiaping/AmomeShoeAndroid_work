package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.Brand;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.ShoeBrandListActivity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**鞋品牌适配器 */
public class BrandListAdapter extends BaseAdapter {
	private Context context;
	private List<Brand> list;
	private int index = -1 ;
	private String brand;

	public BrandListAdapter(Context context, List<Brand> list, String brand) {
		super();
		this.context = context;
		this.list = list;
		this.brand = brand;
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
			convertView = View.inflate(context, R.layout.item_joblist, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_jobname);
			holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
			holder.rl_job = (RelativeLayout) convertView.findViewById(R.id.rl_job);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).brand);
		if (list.get(position).brand.equals(brand))
			holder.iv_selected.setVisibility(View.VISIBLE);
		else
			holder.iv_selected.setVisibility(View.GONE);

		holder.rl_job.setTag(position);
		holder.rl_job.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				index = position;
				brand = list.get(index).brand;
				notifyDataSetChanged();
				((ShoeBrandListActivity) context).setSelect(index);
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
