package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.adapter.LookFootpainAdapter.ViewHolder;
import cn.com.amome.amomeshoes.model.LookModel;
import cn.com.amome.amomeshoes.util.ImageUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 看一看-脚肤色适配器
 * 
 * @author css
 * 
 */
public class LookFootcolorAdapter extends BaseAdapter {
	private Context context;
	private List<LookModel> list;
	private int selectedPosition = -1;

	public LookFootcolorAdapter(Context context, List<LookModel> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_look_footdiseas,
					null);
			holder.iv_diseas = (ImageView) convertView
					.findViewById(R.id.iv_diseas);
			holder.main = (RelativeLayout) convertView.findViewById(R.id.main);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (((LookModel) getItem(position)).isSelect()) {
			holder.iv_diseas.setImageResource(((LookModel) getItem(position))
					.getBgLightResource());
		} else
			holder.iv_diseas.setImageResource(((LookModel) getItem(position))
					.getBgDarkResource());

		holder.main.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((LookModel) getItem(position)).isSelect())
					((LookModel) getItem(position)).setSelect(false);
				else {
					for (LookModel model : list) {
						model.setSelect(false);
					}
					((LookModel) getItem(position)).setSelect(true);
				}
				notifyDataSetChanged();
			}
		});

		// holder.iv_diseas.setImageBitmap(ImageUtil.readBitMap(
		// context.getResources(), list.get(position)));
		// // 判断点击了哪个item,然后判断，让他变色
		// if (selectedPosition == position) {
		// holder.iv_diseas.setImageBitmap(ImageUtil.readBitMap(
		// context.getResources(), imgList_se.get(position)));
		// } else {
		// holder.iv_diseas.setImageBitmap(ImageUtil.readBitMap(
		// context.getResources(), list.get(position)));
		// }
		return convertView;
	}

	class ViewHolder {
		RelativeLayout main;
		ImageView iv_diseas;
	}

	public String getSelectedTag() {
		String tagStr = "";
		for (LookModel model : list) {
			if (model.isSelect())
				tagStr = tagStr + model.getTag() + ",";
		}
		if (tagStr.length() > 0)
			tagStr = tagStr.substring(0, tagStr.length() - 1);
		return tagStr;
	}

}
