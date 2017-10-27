package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.PromotionDataInfo;
import cn.com.amome.amomeshoes.view.main.health.promotion.PromotionFootAddActivity;
import cn.com.amome.amomeshoes.view.main.health.promotion.detail.IllnessDetailTrueActivity;

/**
 * Created by ccf on 17-10-25.
 */

public class HealthPromotionAdapter extends RecyclerView.Adapter<HealthPromotionAdapter.ViewHolder> {
    private Context mContext;
    private List<PromotionDataInfo> infoList;
    private String title;
    private String type;

    public HealthPromotionAdapter(Context context, List<PromotionDataInfo> infoList, String title, String type) {
        mContext = context;
        this.infoList = infoList;
        this.title = title;
        this.type = type;
    }




    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_promotion_illness, iv_promotion_fitting, iv_promotion_curing, iv_promotion_action;
        TextView tv_promotion_illness_name;
        LinearLayout ll_promotion;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_promotion_illness = itemView.findViewById(R.id.iv_promotion_illness);
            iv_promotion_fitting = itemView.findViewById(R.id.iv_promotion_fitting);
            iv_promotion_curing = itemView.findViewById(R.id.iv_promotion_curing);
            iv_promotion_action = itemView.findViewById(R.id.iv_promotion_action);
            tv_promotion_illness_name = itemView.findViewById(R.id.tv_promotion_illness_name);
            ll_promotion = itemView.findViewById(R.id.ll_promotion);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_promotion_gv, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PromotionDataInfo info = infoList.get(position);
        Log.e("TAG", "onBindViewHolder: "+infoList.size());
        if (position == (infoList.size() - 1)) {
            Picasso.with(mContext).load(R.drawable.add_promotion)
                    .fit()
                    .into(holder.iv_promotion_illness);
            holder.ll_promotion.setVisibility(View.INVISIBLE);

        } else {
            holder.ll_promotion.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(info.getIcon())
                    .fit()
                    .placeholder(R.drawable.weijiazai_zubu)
                    .error(R.drawable.weijiazai_zubu)
                    .into(holder.iv_promotion_illness);
            holder.tv_promotion_illness_name.setText(info.getType());

            //这里加一个判断，判断需要转换的值是否为空
            if (info.getTraining() != null && info.getNursing() != null && info.getAccessory() != null) {
                switch (Integer.parseInt(info.getAccessory())) {
                    case 0:
                        Picasso.with(mContext).load(R.drawable.cuo)
                                .fit()
                                .into(holder.iv_promotion_fitting);
                        break;
                    case 5:
                        break;
                    default:
                        Picasso.with(mContext).load(R.drawable.dui)
                                .fit()
                                .into(holder.iv_promotion_fitting);
                        break;
                }

                switch (Integer.parseInt(info.getNursing())) {
                    case 0:
                        Picasso.with(mContext).load(R.drawable.cuo)
                                .fit()
                                .into(holder.iv_promotion_curing);
                        break;
                    case 5:
                        break;
                    default:
                        Picasso.with(mContext).load(R.drawable.dui)
                                .fit()
                                .into(holder.iv_promotion_curing);
                        break;
                }

                switch (Integer.parseInt(info.getTraining())) {
                    case 0:
                        Picasso.with(mContext).load(R.drawable.cuo)
                                .fit()
                                .into(holder.iv_promotion_action);
                        break;
                    case 5:
                        break;
                    default:
                        Picasso.with(mContext).load(R.drawable.dui)
                                .fit()
                                .into(holder.iv_promotion_action);
                        break;
                }
            }
        }

        holder.iv_promotion_illness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == (infoList.size() - 1)) {
                    Intent intent = new Intent(mContext, PromotionFootAddActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("type", type);
                    mContext.startActivity(intent);

                } else {
                    Intent intent = new Intent(mContext, IllnessDetailTrueActivity.class);
                    intent.putExtra("disease", info.getType());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }


}
