package com.example.mauiviewcontrol;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectWiFiDirectDeviceDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "Select WiFi Direct Device Dialog";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    String m_selectedDeviceName;
    int mLastClickedWifiDevice = -1;
    ArrayAdapter<String> mWiFiDirectDeviceArrayAdapter = null;

    //public SelectWiFiDirectDeviceDialog(MainWindowActivity parent) {
    public SelectWiFiDirectDeviceDialog(MainActivity parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.select_wifi_direct_device_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    private void setUpWidgets() {
    }

    private void setUpListeners() {
        setUpWifiDirectDevicesListViewListener();
        setUpConnectButtonListener();
        setUpExitButtonListener();
    }

    private void setUpWifiDirectDevicesListViewListener() {
        ListView wifiDirectsListView = mDialog.findViewById(R.id.wifiDirectsListView);
        wifiDirectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedWifiDevice);
                if (position != mLastClickedWifiDevice) {
                    /*try {
                        disconnect();
                    } catch (NullPointerException le) {
                        Log.d(TAG, "Failed to disconnect");
                    }*/
                    //mConnected = false;
                    //connect();
                    mLastClickedWifiDevice = position;
                    WifiDirectDeviceList wifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
                    wifiDirectDeviceList.setSelected(wifiDirectDeviceList.getDeviceName(mLastClickedWifiDevice));
                    String selected = wifiDirectDeviceList.getString(mLastClickedWifiDevice);
                    Toast.makeText(mContext, "Selected: " + selected, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "WiFi Direct Device Hash Code: " + wifiDirectDeviceList.getSelectedDevice().getDeviceHashCode() + selected, Toast.LENGTH_LONG).show();
                    //mConnected = false;
                    //mAvailableOccurred = false;
                    //connect();
                }
            }
        });
        wifiDirectsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayList<String> list = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getStringList();
        mWiFiDirectDeviceArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, list);
        wifiDirectsListView.setAdapter(mWiFiDirectDeviceArrayAdapter);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    public void refreshWiFiDirectDeviceList(View view) {
        mWiFiDirectDeviceArrayAdapter.notifyDataSetChanged();
    }

    public void disconnectWiFiDirect(View view) {
        //disconnect();
        mLastClickedWifiDevice = -1;
    }

    private void setUpConnectButtonListener() {
        ((Button)mDialog.findViewById(R.id.connectWiFiDirectDeviceButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().setSelected(m_selectedDeviceName);
                Toast.makeText(mContext, "Trying to connect to " + m_selectedDeviceName, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpExitButtonListener() {
        ((Button)mDialog.findViewById(R.id.exitSelectWiFiDirectDeviceButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Select WiFi Direct Device Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
