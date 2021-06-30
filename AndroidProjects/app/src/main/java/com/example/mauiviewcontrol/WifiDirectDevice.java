package com.example.mauiviewcontrol;

import android.net.wifi.p2p.WifiP2pDevice;

public class WifiDirectDevice {

    public WifiDirectDevice(WifiP2pDevice wifiP2pDevice) {
        mWifiP2pDevice = wifiP2pDevice;
        mName = wifiP2pDevice.deviceName;
        mDeviceHashCode = System.identityHashCode(mWifiP2pDevice);
        mItemString = new String(mWifiP2pDevice.deviceName + "  " + mWifiP2pDevice.deviceAddress + "  " +  mWifiP2pDevice.status + "  " +  mWifiP2pDevice.primaryDeviceType + "  " +  mWifiP2pDevice.secondaryDeviceType);
    }

    public WifiP2pDevice getWifiP2pDevice() {
        return mWifiP2pDevice;
    }

    public String getName() {
        return mName;
    }
    public String getString() {
        return mItemString;
    }
    public int getDeviceHashCode() { return mDeviceHashCode; }

    private String mName;
    private String mItemString;
    private WifiP2pDevice mWifiP2pDevice;
    private int mDeviceHashCode = -1;
}
