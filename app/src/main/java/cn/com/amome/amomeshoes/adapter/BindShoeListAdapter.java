package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.BleDeviceInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BindShoeListAdapter extends BaseAdapter {
	private Context context;
	private List<BleDeviceInfo> list;
	private Handler mHandler;
	@SuppressWarnings("unused")
	private int index = -1;
	private static final int MSG_CHANGE = 2;
	private static final int MSG_LONG_CLICK = 3;

	public BindShoeListAdapter(Context context, List<BleDeviceInfo> list,
			Handler mHandler) {
		super();
		this.context = context;
		this.list = list;
		this.mHandler = mHandler;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, R.layout.item_my_bindshoe, null);
			holder.tv_shoe_name = (TextView) convertView
					.findViewById(R.id.tv_shoe_name); // 设备名称
			holder.tv_shoe_num = (TextView) convertView
					.findViewById(R.id.tv_shoe_num); // 设备序号
			holder.tv_bind_time = (TextView) convertView
					.findViewById(R.id.tv_bind_time); // 设备绑定的时间
			holder.rl_bind_shoes = (RelativeLayout) convertView
					.findViewById(R.id.rl_bind_shoes); // item整体布局
			holder.tv_bind_left_address = (TextView) convertView
					.findViewById(R.id.tv_bind_left_address); // 左脚蓝牙地址
			holder.tv_bind_right_address = (TextView) convertView
					.findViewById(R.id.tv_bind_right_address); // 右脚蓝牙地址
			holder.tv_shoe_select = (TextView) convertView
					.findViewById(R.id.tv_shoe_select);
			holder.iv_shoe_select = (ImageView) convertView
					.findViewById(R.id.iv_shoe_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_shoe_name.setText(list.get(position).name);
		holder.tv_shoe_num.setText("NO." + (position + 1));
		holder.tv_bind_time.setText("绑定日期:" + list.get(position).create_time);
		if (list.get(position).getState().equals("1")) {
			holder.iv_shoe_select.setImageResource(R.drawable.bind_shoe_select);
			holder.tv_shoe_select.setText("使用中");
		}
		holder.tv_bind_left_address.setText("左脚地址：" + list.get(position).lble);
		holder.tv_bind_right_address.setText("右脚地址：" + list.get(position).rble);
		holder.rl_bind_shoes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (list.get(position).getState().equals("1")) {
					list.get(position).setState("0");
					Message msg = Message.obtain();
					msg.what = MSG_CHANGE;
					msg.arg1 = position;
					msg.arg2 = 0;
					mHandler.sendMessage(msg);
				} else {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setState("0");
					}
					list.get(position).setState("1");
					Message msg = Message.obtain();
					msg.what = MSG_CHANGE;
					msg.arg1 = position;
					msg.arg2 = 1;
					mHandler.sendMessage(msg);
				}
				notifyDataSetChanged();
			}
		});
		for (int i = 0; i < list.size(); i++) {
			if (list.get(position).getState().equals("1")) {
				holder.iv_shoe_select
						.setImageResource(R.drawable.bind_shoe_select);
				holder.tv_shoe_select.setText("使用中");
			} else {
				holder.iv_shoe_select
						.setImageResource(R.drawable.bind_shoe_unselect);
				holder.tv_shoe_select.setText("未使用");
			}
		}
		holder.rl_bind_shoes.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				// Log.i("bindshoe", "长按");
				Message msg = Message.obtain();
				msg.arg1 = position;
				msg.what = MSG_LONG_CLICK;
				mHandler.sendMessage(msg);
				return true;
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_shoe_name, tv_shoe_num, tv_bind_time, tv_bind_left_address,
				tv_bind_right_address, tv_shoe_select;
		ImageView iv_shoe_select;
		RelativeLayout rl_bind_shoes;
	}
}
