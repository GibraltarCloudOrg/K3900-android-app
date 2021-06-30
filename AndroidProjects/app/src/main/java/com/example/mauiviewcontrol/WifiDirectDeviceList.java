package com.example.mauiviewcontrol;

import android.net.wifi.p2p.WifiP2pDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WifiDirectDeviceList {

    public static WifiDirectDeviceList getWifiDirectDeviceListSingletonInstance() {
        if (null == singletonInstance)
            singletonInstance = new WifiDirectDeviceList();
        return singletonInstance;
    }

    public void add(WifiP2pDevice wifiP2pDevice) {
        WifiDirectDevice wifiDirectDevice = new WifiDirectDevice(wifiP2pDevice);
        mWifiDirectDeviceList.add(wifiDirectDevice);
        mWifiDirectDeviceStringList.add(wifiDirectDevice.getString());
        mDeviceMap.put(wifiP2pDevice.deviceName, wifiDirectDevice);
        if (wifiDirectDevice.getName().startsWith(kMauiDeviceName))
            mMauiDeviceStringList.add(wifiDirectDevice.getName());
    }

    public WifiDirectDevice getSelectedDevice() {
        return mDeviceMap.get(mSelectedDeviceName);
    }

    public boolean connected() {
        WifiDirectDevice selectedDevice = getSelectedDevice();
        if (null == selectedDevice)
            return false;
        return WifiP2pDevice.CONNECTED == selectedDevice.getWifiP2pDevice().status;
    }

    public String getSelectedDeviceName() {
        return mSelectedDeviceName;
    }

    public void setSelected(String deviceName)
    {
        mSelectedDeviceName = deviceName;
        //mSelectedDeviceName = mDeviceMap.get(deviceName).getName();
    }

    public ArrayList<String> getStringList() {
        return mWifiDirectDeviceStringList;
    }

    public ArrayList<String> getMauiDeviceStringList() {
        return mMauiDeviceStringList;
    }

    public String getString(int index) {
        return mWifiDirectDeviceStringList.get(index);
    }

    public String getDeviceName(int index) {
        return mWifiDirectDeviceList.get(index).getWifiP2pDevice().deviceName;
    }

    public String getMauiDeviceName(int index) {
        if (mMauiDeviceStringList.size() > index)
            return mMauiDeviceStringList.get(index);
        return null;
    }

    public int getSize() {
        return mWifiDirectDeviceList.size();
    }

    public int getNumberOfMauiDevices() {
        return mMauiDeviceStringList.size();
    }

    public void clearAll() {
        //mSelected = -1;
        mWifiDirectDeviceStringList.clear();
        mMauiDeviceStringList.clear();
        mWifiDirectDeviceList.clear();
        mDeviceMap.clear();
    }

    private static WifiDirectDeviceList singletonInstance = null;
    private ArrayList<WifiDirectDevice> mWifiDirectDeviceList = new ArrayList<WifiDirectDevice>();
    private ArrayList<String> mWifiDirectDeviceStringList = new ArrayList<String>();
    private ArrayList<String> mMauiDeviceStringList = new ArrayList<String>();
    private Map<String, WifiDirectDevice> mDeviceMap = new HashMap<>();
    private String mSelectedDeviceName = "";
    private final String kMauiDeviceName = "MAUI-K3900-";
}
