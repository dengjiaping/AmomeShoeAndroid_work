package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.HobbyInfo;
import cn.com.amome.amomeshoes.view.main.my.user.HobbyListActivity;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HobbyListAdapter extends BaseAdapter {
	private Context context;
	private List<HobbyInfo> list;
	private String hobby;
	@SuppressWarnings("unused")
	private int index = -1 ;

	public HobbyListAdapter(Context context, List<HobbyInfo> list, String hobby) {
		super();
		this.context = context;
		this.list = list;
		this.hobby = hobby;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_hobbylist, null);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_hobbyname);        		//兴趣名称textview
			holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected); 	//选择勾imageview
			holder.rl_hobby = (RelativeLayout) convertView.findViewById(R.id.rl_hobby);		//item整体布局
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).hobbysdes);
		
		if(hobby.contains(list.get(position).hobbysdes)){
			holder.iv_selected.setVisibility(View.VISIBLE);
		}else
		{
			holder.iv_selected.setVisibility(View.GONE);
		}
//		if (list.get(position).hobbysdes.equals(hobby))
//			holder.iv_selected.setVisibility(View.VISIBLE);
//		else
//			holder.iv_selected.setVisibility(View.GONE);

		holder.rl_hobby.setTag(position);
		holder.rl_hobby.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				index = position;
				hobby = list.get(position).hobbysdes;
				notifyDataSetChanged();
				((HobbyListActivity) context).setSelect(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv_selected;
		RelativeLayout rl_hobby;
	}
}
