package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.LookModel;
import cn.com.amome.amomeshoes.util.ImageUtil;
import cn.com.amome.amomeshoes.view.main.health.service.look.LookFootpainActivity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class LookFootpainAdapter extends BaseAdapter {
	private Context context;
	private List<LookModel> list;
	
	public LookFootpainAdapter(Context context, List<LookModel> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public LookModel getItem(int position) {
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
			convertView = View.inflate(context, R.layout.item_look_footdiseas, null);
			holder.iv_diseas = (ImageView) convertView.findViewById(R.id.iv_diseas);
			holder.main = (RelativeLayout) convertView.findViewById(R.id.main);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(getItem(position).isSelect())
		    holder.iv_diseas.setImageResource(getItem(position).getBgLightResource());
	    else
	        holder.iv_diseas.setImageResource(getItem(position).getBgDarkResource());
		
		holder.main.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(position).isSelect())
                    getItem(position).setSelect(false);
                else
                    getItem(position).setSelect(true);
                notifyDataSetChanged();
            }
        });
		
		return convertView;
	}


	class ViewHolder {
	    RelativeLayout main;
		ImageView iv_diseas;
	}
	
	public String getSelectedTag() {
	    String tagStr = "";
	    for (LookModel model : list) {
            if(model.isSelect())
                tagStr = tagStr + model.getTag() + ",";
        }
	    if(tagStr.length() > 0) 
	        tagStr = tagStr.substring(0,tagStr.length()-1);
	    return tagStr;
	}
	
}
