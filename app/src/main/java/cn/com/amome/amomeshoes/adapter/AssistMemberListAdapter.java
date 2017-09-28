package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.MaterialShoes;
import cn.com.amome.amomeshoes.view.main.health.service.assistant.AssistMainActivity;
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

public class AssistMemberListAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;

	public AssistMemberListAdapter(Context context, List<String> list) {
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
					R.layout.item_assist_memberlist, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_jobname);
			holder.rl_job = (RelativeLayout) convertView
					.findViewById(R.id.rl_job);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position));
		holder.rl_job.setTag(position);
		// holder.rl_job.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// notifyDataSetChanged();
		// }
		// });
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		RelativeLayout rl_job;
	}
}
