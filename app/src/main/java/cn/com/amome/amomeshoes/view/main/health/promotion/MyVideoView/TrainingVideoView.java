package cn.com.amome.amomeshoes.view.main.health.promotion.MyVideoView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.com.amome.amomeshoes.R;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by ccf on 17-10-20.
 */

public class TrainingVideoView extends JZVideoPlayerStandard {
    private int num;
    private FinishListener lisener;
    public long currentDuration;

    public TrainingVideoView(Context context) {
        super(context);
    }

    public TrainingVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void init(Context context) {
        super.init(context);
        batteryTimeLayout.setVisibility(GONE);
        tinyBackImageView.setVisibility(GONE);
        backButton.setVisibility(GONE);
        titleTextView.setVisibility(GONE);
        //clarityPopWindow.dismiss();
        bottomContainer.setVisibility(GONE);
        findViewById(R.id.start_layout).setVisibility(GONE);
        findViewById(R.id.loading).setVisibility(GONE);
        findViewById(R.id.retry_text).setVisibility(GONE);
        findViewById(R.id.bottom_progress).setVisibility(GONE);
        findViewById(R.id.surface_container).setEnabled(false);


    }

    @Override
    public void onClickUiToggle() {
        if (bottomContainer.getVisibility() != View.VISIBLE) {
            setSystemTimeAndBattery();
            clarity.setVisibility(GONE);
            fullscreenButton.setVisibility(INVISIBLE);
        }
        if (currentState == CURRENT_STATE_PREPARING) {
            changeUiToPreparing();
            if (bottomContainer.getVisibility() == View.VISIBLE) {
            } else {
                setSystemTimeAndBattery();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }


    @Override
    public void onCLickUiToggleToClear() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparing();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToComplete();
            } else {
            }
        }
    }

    @Override
    public void setProgressAndText(int progress, int position, int duration) {
        super.setProgressAndText(progress, position, duration);
    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        if (lisener != null) {
            lisener.setOnFinish();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        currentDuration = getDuration();
    }

    //播放完成的监听接口
    public interface FinishListener {
        void setOnFinish();
    }

    public void setOnFinishListener(FinishListener lisener) {
        this.lisener = lisener;
    }


}

