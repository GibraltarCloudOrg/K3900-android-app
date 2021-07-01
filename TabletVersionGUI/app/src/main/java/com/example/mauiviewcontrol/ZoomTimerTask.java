package com.example.mauiviewcontrol;

import android.content.Context;
import android.os.Handler;

import java.util.TimerTask;

public class ZoomTimerTask extends TimerTask {
    private Context mContext;
    private Handler mHandler = new Handler();
    private BackEndElement mElement;
    BackEndElementSendingMessageVisitor mVisitor;
    
    public ZoomTimerTask(Context con, BackEndElement element, BackEndElementSendingMessageVisitor visitor) {
        mContext = con;
        mElement=element;
        mVisitor=visitor;
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mElement.accept(mVisitor);
                    }
                });
            }
        }).start();
    }
}
