package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.IllnessInfo;
import cn.com.amome.amomeshoes.view.main.health.promotion.detail.IllnessDetailActivity;
import cn.com.amome.amomeshoes.view.main.health.promotion.detail.IllnessDetailTrueActivity;

/**
 * 健康促进主页面适配器
 *
 * @author css
 */
public class PromotionAddAdapter extends RecyclerView.Adapter<PromotionAddAdapter.ViewHolder> {
    private Context context;
    private List<IllnessInfo> list = null;



    public PromotionAddAdapter(Context context, List<IllnessInfo> list) {
        this.context = context;
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView illness_name, isAdd;
        ImageView illness_photo;

        public ViewHolder(View itemView) {
            super(itemView);
            illness_name = (TextView) itemView.findViewById(R.id.manager_illness_name);
            illness_photo = (ImageView) itemView.findViewById(R.id.iv_manager_illness);
            isAdd = (TextView) itemView.findViewById(R.id.manager_illness_ischecked);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_footreport_manager, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final IllnessInfo info = list.get(position);
        holder.illness_name.setText(info.getType());
        Picasso.with(context).load(info.getIcon())
                .fit()
                .placeholder(R.drawable.weijiazai_zubu)
                .error(R.drawable.weijiazai_zubu)
                .into(holder.illness_photo);
        if (info.getIs_user_related().equals("1")) {
            holder.isAdd.setText("已添加");
            holder.isAdd.setBackgroundColor(Color.TRANSPARENT);
            holder.isAdd.setTextColor(Color.WHITE);
        } else {
            holder.isAdd.setText("未添加");
            holder.isAdd.setBackgroundColor(Color.WHITE);
            holder.isAdd.setTextColor(Color.rgb(55,210,202));
        }


        //设置点击监听
        holder.illness_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: "+info.getIs_user_related());
                if (info.getIs_user_related().equals("0")) {
                    Intent intent = new Intent(context, IllnessDetailActivity.class);
                    intent.putExtra("name", info.getType());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, IllnessDetailTrueActivity.class);
                    intent.putExtra("disease", info.getType());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

/*
    public void changeSelectStatus(Boolean isSelect ,int position) {

        if (isSelect) {
            list.get(position).setIs_user_related("1");
        } else {
            list.get(position).setIs_user_related("0");
        }

    }*/

	/*private Context context;
    private List<IllnessInfo> list=null;

	public PromotionAddAdapter(Context context, List<IllnessInfo> list) {
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
		ViewHolder holder=null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_footreport_manager, null);
			holder.illness_name = (TextView) convertView.findViewById(R.id.manager_illness_name);
			holder.illness_photo = (ImageView) convertView.findViewById(R.id.iv_manager_illness);
			holder.isAdd = (TextView) convertView.findViewById(R.id.manager_illness_ischecked);
			convertView.setTag(holder);
		} else {
			holder= (ViewHolder) convertView.getTag();
		}
		IllnessInfo info= (IllnessInfo) getItem(position);
		holder.illness_name.setText(info.getType());
		Picasso.with(context).load(info.getIcon())
				.fit()
				.placeholder(R.drawable.weijiazai_zubu)
				.error(R.drawable.weijiazai_zubu)
				.into(holder.illness_photo);
		if (info.getIs_user_related().equals("1")) {
			holder.isAdd.setText("已添加");
			holder.isAdd.setBackgroundColor(Color.TRANSPARENT);
		} else {
			holder.isAdd.setText("未添加");
			holder.isAdd.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView illness_name,isAdd;
		ImageView illness_photo;
	}*/

}
