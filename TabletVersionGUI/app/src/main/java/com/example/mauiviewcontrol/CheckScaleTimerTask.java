package com.example.mauiviewcontrol;

import android.content.Context;
import android.os.Handler;

import java.util.TimerTask;

public class CheckScaleTimerTask extends TimerTask {
    private Context mContext;
    private Handler mHandler=new Handler();

    public CheckScaleTimerTask(Context context){
        mContext=context;
    }

    @Override
    public void run(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                mHandler.post(new Runnable(){
                    @Override
                    public void run(){
                        ImagePositionDialog.getSingletonInstance(mContext).updateSliderPosition();
                        ImagePositionDialog.getSingletonInstance(mContext).updateDisplayWidgets();
                    }
                });
            }
        }).start();
    }
}
