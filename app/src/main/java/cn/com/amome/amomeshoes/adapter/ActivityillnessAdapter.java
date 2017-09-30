package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.IllnessInfo;

/**
 * Created by ccf on 17-9-30.
 * Activity页面中活动模块下（足部/姿态/平衡/步态）放置部分病的Adapter
 */

public class ActivityillnessAdapter extends BaseAdapter {
    private Context context;
    private List<IllnessInfo> list;

    public ActivityillnessAdapter(Context context, List<IllnessInfo> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_activity_foot,
                    null);
            holder.illness_name = (TextView) convertView.findViewById(R.id.activity_illness_name);
            holder.illness_photo = (ImageView) convertView.findViewById(R.id.iv_activity_gr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IllnessInfo info= (IllnessInfo) getItem(position);
        holder.illness_name.setText(info.getType());
        //holder.illness_photo.setImageBitmap();
        /**
         * 根据ImageView大小，显示图片
         * .fit()                                  说明：控件不能设置成wrap_content,也就是必须有大小才行,fit()才让图片的宽高等于控件的宽高，设置fit()，不能再调用resize()
         * .placeholder()      说明：当图片没有加载上的时候，显示的图片
         * .error()            说明：当图片加载错误的时候，显示图片
         * .into(holder.illness_photo)                          说明：将图片加载到哪个控件中
         */
        //TODO 添加错误显示的图片
        Picasso.with(context).load(info.getIcon())
                .fit()
                //.placeholder()
                //.error()
                .into(holder.illness_photo);
        return convertView;
    }

    class ViewHolder {
        TextView illness_name;
        ImageView illness_photo;

    }
}
