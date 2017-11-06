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
import cn.com.amome.amomeshoes.model.NursingTrueInfo;

/**
 * Created by ccf on 17-10-19.
 */

public class DetailNursingTrueAdapter extends RecyclerView.Adapter<DetailNursingTrueAdapter.ViewHolder> {

    private Context mContext;
    private List<NursingTrueInfo> list;
    private MyItemClickListener mMyItemClickListener;
    private int num;
    private String type;

    public DetailNursingTrueAdapter(Context context, List<NursingTrueInfo> list, int listsize, String type) {
        mContext = context;
        this.list = list;
        this.type = type;
        num = listsize;
        if (num < 6) {
            for (int i = num; i < 6; i++) {
                list.add(new NursingTrueInfo());

            }

        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_item_recycler;
        TextView tv_item_recycler;
        MyItemClickListener myListener;

        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            myListener = listener;
            iv_item_recycler = itemView.findViewById(R.id.iv_item_recycler);
            tv_item_recycler = itemView.findViewById(R.id.tv_item_recycler);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (myListener != null) {
                myListener.onItemClick(view, getPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_nursing_true_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view, mMyItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NursingTrueInfo info = list.get(position);
        if (position < num) {
            if (info.getIs_done().equals("0")) {
                holder.tv_item_recycler.setText(info.getName());
                Picasso.with(mContext).load(info.getIcon())
                        .fit()
                        .into(holder.iv_item_recycler);

            } else if (info.getIs_done().equals("1")) {
                if (Integer.valueOf(info.getUser_breakdays()) < Integer.valueOf(info.getBreakdays())) {
                    if (type.equals("nursing")) {
                        holder.tv_item_recycler.setText("已养护(" + info.getWeardays() + ")天");
                    } else if (type.equals("accessory")) {
                        holder.tv_item_recycler.setText("已穿戴(" + info.getWeardays() + ")天");
                    }
                    Picasso.with(mContext).load(info.getIcon_done())
                            .fit()
                            .into(holder.iv_item_recycler);
                } else {
                    holder.tv_item_recycler.setText("已间断(" + info.getUser_breakdays() + ")天");
                    Picasso.with(mContext).load(info.getIcon_done())
                            .fit()
                            .into(holder.iv_item_recycler);
                }
            }

        } else {
           /* Picasso.with(mContext).load(R.drawable.daiding)
                    .fit()
                    .into(holder.iv_item_recycler);*/
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

   /* public void reLoading(List<NursingTrueInfo> infolist) {
        list.clear();
        list=infolist;
        if (num < 6) {
            for (int i = num; i < 6; i++) {
                list.add(new NursingTrueInfo());

            }

        }
    }*/

    //对外提供的点击监听接口
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mMyItemClickListener = listener;
    }


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }
}
