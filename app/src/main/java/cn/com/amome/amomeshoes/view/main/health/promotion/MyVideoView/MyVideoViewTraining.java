package cn.com.amome.amomeshoes.view.main.health.promotion.MyVideoView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by ccf on 17-10-18.
 */

public class MyVideoViewTraining extends JZVideoPlayerStandard {
    public MyVideoViewTraining(Context context) {
        super(context);
    }

    public MyVideoViewTraining(Context context, AttributeSet attrs) {
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
        SAVE_PROGRESS = false;

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
}
