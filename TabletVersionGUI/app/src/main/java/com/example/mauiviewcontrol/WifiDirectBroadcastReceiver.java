package com.example.mauiviewcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WifiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel channel, MainActivity mActivity){
        this.mManager = mManager;
        this.mChannel = channel;
        this.mActivity = mActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);

            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Log.d(MainActivity.TAG, "P2P Enabled");
            }else {
                Log.d(MainActivity.TAG, "P2P Disabled");
            }
        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            Log.d(MainActivity.TAG, "P2P PEERS Changed!");
            if(mManager != null){
                mManager.requestPeers(mChannel, mActivity.peerListListener);
            }
        }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            Log.d(MainActivity.TAG, "P2P Connection Changed");
            //do something
            if(mManager == null){
                return;
            }
            mManager.requestConnectionInfo(mChannel, mActivity.connectionInfoListener);
        }
        else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {
            Log.d(MainActivity.TAG, "Discovery changed");
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 100);

            if (state == WIFI_P2P_DISCOVERY_STOPPED) {
                Log.d(MainActivity.TAG, "Discovery stopped");
            }
        }
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            if (device != null) {
                String msg = "Device Status : " + device.status + device.deviceName + " = ";

                switch (device.status) {
                    case WifiP2pDevice.CONNECTED:
                        msg += "Connected";
                        break;
                    case WifiP2pDevice.INVITED:
                        msg += "Invited";
                        break;
                    case WifiP2pDevice.FAILED:
                        msg += "Failed";
                        break;
                    case WifiP2pDevice.AVAILABLE:
                        msg += "Available";
                        break;
                    case WifiP2pDevice.UNAVAILABLE:
                        msg += "Unavailable";
                    default:
                        msg += "Unknown";
                        break;
                }
                Log.d(MainActivity.TAG, msg);
            }

        }
    }
}