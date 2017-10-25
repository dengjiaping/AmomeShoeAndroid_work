package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.DetailTrainingInfo;
import cn.com.amome.amomeshoes.view.main.health.promotion.detail.DetailTrainingActivity;

/**
 * Created by ccf on 17-10-25.
 */

public class TrainingListAdapter extends RecyclerView.Adapter<TrainingListAdapter.ViewHolder> {
    private List<DetailTrainingInfo> infoList;
    private Context mContext;
    private String disease, type;

    public TrainingListAdapter(List<DetailTrainingInfo> infoList, Context context, String disease, String type) {
        this.infoList = infoList;
        mContext = context;
        this.disease = disease;
        this.type = type;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_first, iv_more;
        TextView tv_name, tv_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_first = itemView.findViewById(R.id.iv_first);
            iv_more = itemView.findViewById(R.id.iv_more);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_detail = itemView.findViewById(R.id.tv_detail);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_training_list_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DetailTrainingInfo info = infoList.get(position);
        Picasso.with(mContext).load(info.getImg())
                .fit()
                .placeholder(R.drawable.weijiazai_zubu)
                .error(R.drawable.weijiazai_zubu)
                .into(holder.iv_first);
        holder.tv_name.setText(info.getName());
        holder.tv_detail.setText(info.getDetail());
        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailTrainingActivity.class);
                intent.putExtra("disease", disease);
                intent.putExtra("type", type);
                intent.putExtra("toNum", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


}
