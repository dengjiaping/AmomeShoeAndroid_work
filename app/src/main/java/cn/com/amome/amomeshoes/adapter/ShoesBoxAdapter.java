package cn.com.amome.amomeshoes.adapter;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ShoeBoxList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShoesBoxAdapter extends BaseAdapter {
	private Context context;
	private List<ShoeBoxList> list;
	private ImageLoader loader;
	private DisplayImageOptions options;

	public ShoesBoxAdapter(Context context, List<ShoeBoxList> list) {
		super();
		this.context = context;
		this.list = list;
		loader = ImageLoader.getInstance();
		setOptions();
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
			convertView = View.inflate(context, R.layout.item_shoebox, null);
			holder.tv_brand = (TextView) convertView
					.findViewById(R.id.tv_brand);
			holder.tv_price = (TextView) convertView
					.findViewById(R.id.tv_price);
			holder.tv_category = (TextView) convertView
					.findViewById(R.id.tv_category);
			holder.tv_heel = (TextView) convertView.findViewById(R.id.tv_heel);
			holder.iv_shoe = (ImageView) convertView.findViewById(R.id.iv_shoe);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_brand.setText(list.get(position).brand);
		holder.tv_price.setText(list.get(position).price);
		holder.tv_heel.setText(list.get(position).heel);
		holder.tv_category.setText(list.get(position).category);
		loader.clearMemoryCache();
		loader.clearDiskCache();
		loader.displayImage(list.get(position).picture, holder.iv_shoe, options);
		return convertView;
	}

	class ViewHolder {
		ImageView iv_shoe;
		TextView tv_brand, tv_price, tv_category, tv_heel;
	}

	private void setOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				// .resetViewBeforeLoading(true)
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
}
