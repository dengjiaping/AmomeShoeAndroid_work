package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.DetailNursingInfo;

/**
 * Created by ccf on 17-10-16.
 */

public class DetailNursingAdapter extends RecyclerView.Adapter<DetailNursingAdapter.ViewHolder> {
    private Context mContext;
    private List<DetailNursingInfo> infoList;

    public DetailNursingAdapter(Context context, List<DetailNursingInfo> infoList) {
        mContext = context;
        this.infoList = infoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_nursing;
        TextView tv_name, tv_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_nursing = (ImageView) itemView.findViewById(R.id.iv_nursing);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_detail = (TextView) itemView.findViewById(R.id.tv_detail);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_nursing_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailNursingInfo info = infoList.get(position);
        Picasso.with(mContext).load(info.getIcon())
                .fit()
                .placeholder(R.drawable.weijiazai_zubu)
                .error(R.drawable.weijiazai_zubu)
                .into(holder.iv_nursing);
        holder.tv_name.setText(info.getName());
        holder.tv_detail.setText(info.getDetail());

    }


    @Override
    public int getItemCount() {
        return infoList.size();
    }
}
