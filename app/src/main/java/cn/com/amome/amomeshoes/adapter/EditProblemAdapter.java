package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ShoesProblem;
import cn.com.amome.amomeshoes.view.main.health.service.shoebox.EditShoesBoxActivity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class EditProblemAdapter extends BaseAdapter{

	private Context context;
	private List<Integer> clickTempArr;
	private List<ShoesProblem> list;
	public EditProblemAdapter(Context context, List<ShoesProblem> list, List<Integer> clickTempArr) {
		super();
		this.context = context;
		this.list = list;
		this.clickTempArr = clickTempArr;
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
			convertView = View
					.inflate(context, R.layout.item_shoeproblem, null);
			holder.cb_pro = (CheckBox) convertView.findViewById(R.id.cb_pro);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(clickTempArr!=null && clickTempArr.size()>0){
			for(int i =0;i<clickTempArr.size();i++){
				int temp =(Integer) clickTempArr.get(i);
				if (temp == position) {
					holder.cb_pro.setChecked(true);
				}
			}
		}
		holder.cb_pro.setText(list.get(position).prodes);
		holder.cb_pro.setTag(position);
		holder.cb_pro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(clickTempArr!=null && clickTempArr.size()>0)
					clickTempArr.clear();
				CheckBox cb = (CheckBox) v;
				int position = (Integer) v.getTag();
				notifyDataSetChanged();
				((EditShoesBoxActivity) context).setSelect(position,
						cb.isChecked());
			}
		});
		return convertView;
	}

	class ViewHolder {
		CheckBox cb_pro;
	}
}
