package cn.com.amome.amomeshoes.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.DetailTrainingInfo;

/**
 * Created by ccf on 17-10-13.
 */

public class DetailTrainingAdapter extends PagerAdapter {
    private List<DetailTrainingInfo> list;
    private Context mContext;
    private View mView;
    private VideoView vv_Training;
    private TextView tv_name, tv_detail;


    public DetailTrainingAdapter(List<DetailTrainingInfo> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_detail_promotion_viewpage, container, false);
        vv_Training = (VideoView) mView.findViewById(R.id.vv_viewpager_promotion);
        tv_name = (TextView) mView.findViewById(R.id.tv_name);
        tv_detail = (TextView) mView.findViewById(R.id.tv_detail);
        DetailTrainingInfo info = list.get(position);
        Log.e("TAG", "instantiateItem: " + info.getIcon());
        vv_Training.setVideoURI(Uri.parse(info.getIcon()));
        vv_Training.setMediaController(new MediaController(mContext));
//        start_video();
        tv_name.setText(info.getName());
        tv_detail.setText(info.getDetail());
        vv_Training.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(mView);
    }


    public void stop_Video() {
        vv_Training.pause();
    }
    public void start_video(){
        vv_Training.start();
    }

}
