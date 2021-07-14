package com.example.mauiviewcontrol;

import android.content.Context;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class EthernetConnectionModel {

    public static EthernetConnectionModel getEthernetConnectionModelSingletonInstance() {
        if (null == sSingletonInstance)
            sSingletonInstance = new EthernetConnectionModel();
        return sSingletonInstance;
    }

    private EthernetConnectionModel() {
        //mKeepUpdatingTimer = new Timer();
        //mKeepUpdatingTimerTask = new SpeedOfSoundTimerTask(null);
    }

    public void setIpAddress(String ipAddress) {
        mIpAddress = ipAddress;
    }

    public void connect() {
        mBackend.connect(mIpAddress, 50051);
    }

    public void reset() {
    }

    private static EthernetConnectionModel sSingletonInstance = null;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private String mIpAddress;
}
