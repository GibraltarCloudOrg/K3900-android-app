package com.example.mauiviewcontrol;

import android.content.Context;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class SpeedOfSoundModel {

    public static SpeedOfSoundModel getSpeedOfSoundModelSingletonInstance() {
        if (null == sSingletonInstance)
            sSingletonInstance = new SpeedOfSoundModel();
        return sSingletonInstance;
    }

    private SpeedOfSoundModel() {
        //mKeepUpdatingTimer = new Timer();
        //mKeepUpdatingTimerTask = new SpeedOfSoundTimerTask(null);
    }

    public void increase() {
        if (!mKeepAccelerating)
            mBackend.onFocusChange(50);
    }

    public void decrease() {
        if (!mKeepAccelerating)
            mBackend.onFocusChange(-50);
    }

    public void setKeepIncreasing() {
        mKeepIncreasing = true;
        mKeepDecreasing = false;
        mKeepUpdatingTimer = new Timer();
        mKeepUpdatingTimerTask = new SpeedOfSoundTimerTask(null);
        mKeepUpdatingTimer.scheduleAtFixedRate(mKeepUpdatingTimerTask, 3000, 500);
    }

    public void setKeepDecreasing() {
        mKeepDecreasing = true;
        mKeepIncreasing = false;
        mKeepUpdatingTimer = new Timer();
        mKeepUpdatingTimerTask = new SpeedOfSoundTimerTask(null);
        mKeepUpdatingTimer.scheduleAtFixedRate(mKeepUpdatingTimerTask, 3000, 500);
    }

    public void accelerate() {
        //mAcceleration = mAcceleration + 0.1f;
        mAcceleration = mAcceleration * 1.2f;
        if (mKeepDecreasing)
            mBackend.onFocusChange(-mDelta * mAcceleration);
        else if (mKeepIncreasing)
            mBackend.onFocusChange(mDelta * mAcceleration);
    }

    public void reset() {
        mKeepIncreasing = false;
        mKeepDecreasing = false;
        mAcceleration = 1.0f;
        mDelta = 250.0f;
        mKeepAccelerating = false;
        mKeepUpdatingTimer.cancel();
        mKeepUpdatingTimerTask.cancel();
    }

    private void update() {
        mKeepAccelerating = true;
        if (mKeepDecreasing || mKeepIncreasing)
            accelerate();
    }

    private static SpeedOfSoundModel sSingletonInstance = null;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private boolean mKeepIncreasing = false;
    private boolean mKeepDecreasing = false;
    private float mAcceleration = 1.0f;
    private float mDelta = 250.0f;
    private boolean mKeepAccelerating = false;
    Timer mKeepUpdatingTimer = null;
    TimerTask mKeepUpdatingTimerTask = null;

    private class SpeedOfSoundTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public SpeedOfSoundTimerTask(Context con) {
            this.mContext = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!mBackend.isAvailable())
                                return;
                            update();
                        }
                    });
                }
            }).start();
        }
    }
}
