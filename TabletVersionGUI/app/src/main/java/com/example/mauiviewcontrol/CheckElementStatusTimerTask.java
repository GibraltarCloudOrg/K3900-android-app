package com.example.mauiviewcontrol;

import android.content.Context;
import android.os.Handler;
import android.widget.Switch;

import java.util.TimerTask;

public class CheckElementStatusTimerTask extends TimerTask {
    private Context mContext;
    private Handler mHandler=new Handler();
    private TxElementMaskingSetup mTxSetup;
    private RxElementMaskingSetup mRxSetup;

    public CheckElementStatusTimerTask(Context context, TxElementMaskingSetup txSetup, RxElementMaskingSetup rxSetup){
        mContext=context;
        mTxSetup=txSetup;
        mRxSetup=rxSetup;

    }

    @Override
    public void run(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                mHandler.post(new Runnable(){
                    @Override
                    public void run(){
                        mTxSetup.setData();
                        mTxSetup.checkSwitches();
                        mRxSetup.setData();
                        mRxSetup.checkSwitches();
                    }
                });
            }
        }).start();
    }
}
