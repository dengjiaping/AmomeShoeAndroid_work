package cn.com.amome.amomeshoes.view.main.health.promotion.MyVideoView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.DetailTrainingInfo;
import cn.jzvd.JZVideoPlayer;


/**
 * Created by ccf on 17-10-18.
 */

public class TrainingVideoFragment extends Fragment {
    private View view;
    private DetailTrainingInfo info;
    private int index;
    private Context mContext;

    private MyVideoViewTraining vv;
    private TextView tv_name, tv_detail;


    public TrainingVideoFragment setIndex(int index, DetailTrainingInfo info, Context context) {
        this.index = index;
        mContext = context;
        this.info = info;
        return this;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_detail_promotion_viewpage, container, false);
        vv = (MyVideoViewTraining) view.findViewById(R.id.vv);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_detail = (TextView) view.findViewById(R.id.tv_detail);

        vv.setUp(info.getIcon(), JZVideoPlayer.SCREEN_LAYOUT_NORMAL);
        Picasso.with(mContext).load(info.getImg())
                .fit()
                .placeholder(R.drawable.weijiazai_zubu)
                .error(R.drawable.weijiazai_zubu)
                .into(vv.thumbImageView);
        tv_name.setText(info.getName());
        tv_detail.setText(info.getDetail());
        if (index==0) {
            if (vv != null) {
                vv.startButton.performClick();
            }
        }

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (vv != null) {
            vv.startButton.performClick();
        }

    }
}
