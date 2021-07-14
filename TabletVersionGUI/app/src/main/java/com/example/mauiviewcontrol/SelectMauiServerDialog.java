package com.example.mauiviewcontrol;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.WIFI_SERVICE;
import static android.net.wifi.p2p.WifiP2pDevice.AVAILABLE;
import static android.os.SystemClock.sleep;

public class SelectMauiServerDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "Select Maui Server Dialog";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    String m_selectedDeviceName;
    int mLastClickedWifiDevice = -1;
    ArrayAdapter<String> mWiFiDirectDeviceArrayAdapter = null;
    private WifiDirectDeviceList mWifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();

    //public SelectWiFiDirectDeviceDialog(MainWindowActivity parent) {
    public SelectMauiServerDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.select_maui_server_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        startTimer();
    }

    private void startTimer() {
        Timer statusTimer = new Timer();
        TimerTask checkDeviceListTimerTask = new CheckDeviceListTimerTask(mContext);
        statusTimer.scheduleAtFixedRate(checkDeviceListTimerTask, 5000, 10 * 1000);
    }

    private void setUpWidgets() {
    }

    private void setUpListeners() {
        setUpServerListTitleTextViewListener();
        setUpWifiDirectDevicesListViewListener();
    }

    private void setUpServerListTitleTextViewListener() {
        TextView serverListTitleTextView = mDialog.findViewById(R.id.serverListTitleTextView);
        serverListTitleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SelectWiFiDirectDeviceDialog(mContext);
                return true;
            }
        });
    }

    private void setUpWifiDirectDevicesListViewListener() {
        ListView mauiDeviceListView = mDialog.findViewById(R.id.mauiDeviceListView);
        mauiDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedWifiDevice);
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
                SelectLogInDialog selectLogInDialog = new SelectLogInDialog(mContext, mWifiDirectDeviceList.getMauiDeviceName(position));
                //wifiManager.setWifiEnabled(false);
                //sleep(1000);
                //wifiManager.setWifiEnabled(true);
                //mWifiDirectDeviceList.setSelected(mWifiDirectDeviceList.getMauiDeviceName(position));
                mWifiDirectDeviceList.setSelected("");
                /*((MainActivity)mContext).finish();
                Intent intent = new Intent(mContext, com.example.mauiviewcontrol.MainActivity.class);
                intent.putExtra("ShowServerListDialog", false);
                intent.putExtra("ServerName", mWifiDirectDeviceList.getSelectedDeviceName());
                ((MainActivity)mContext).startActivity(intent);*/
                //wifiManager.setWifiEnabled(true);
                try {
                    ((MainActivity)mContext).processPeer();
                    mDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //if (position != mLastClickedWifiDevice) {
                    /*try {
                        disconnect();
                    } catch (NullPointerException le) {
                        Log.d(TAG, "Failed to disconnect");
                    }*/
                    //mConnected = false;
                    //connect();
                    try {
                        mLastClickedWifiDevice = position;
                        ImageStreamer.getImageStreamerSingletonInstance().clear();
                        //mWifiDirectDeviceList.setSelected(mWifiDirectDeviceList.getMauiDeviceName(position));

                        //WifiDirectDeviceList wifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
                        //String selected = mWifiDirectDeviceList.getString(mLastClickedWifiDevice);
                        String selected = mWifiDirectDeviceList.getMauiDeviceName(mLastClickedWifiDevice);
                        Toast.makeText(mContext, "Ready: " + selected, Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "Connecting..., Please wait....", Toast.LENGTH_LONG).show();
                        //Toast.makeText(mContext, "WiFi Direct Device Hash Code: " + wifiDirectDeviceList.getSelectedDevice().getDeviceHashCode() + selected, Toast.LENGTH_LONG).show();
                        //mConnected = false;
                        //mAvailableOccurred = false;
                        //connect();
                        //((MainActivity)mContext).startGrpcExecutor();
                        //((MainActivity)mContext).finish();
                        //Intent intent = new Intent(mContext, com.example.mauiviewcontrol.MainActivity.class);
                        //WidgetUtility.waitFor(1);
                        //((MainActivity)mContext).startActivity(intent);
                        //((MainActivity)mContext).finish();
                        //sleep(2000);
                        //((MainActivity)mContext).processPeer(null);
                        //wifiManager.setWifiEnabled(true);
                    } catch (NullPointerException le) {
                        Toast.makeText(mContext, "NullPointerException: " + le.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "NullPointerException: " + le.getMessage());
                    }
                //}
            }
        });
        mauiDeviceListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //ArrayList<String> list = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getMauiDeviceStringList();
        //mWiFiDirectDeviceArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, list);
        //mauiDeviceListView.setAdapter(mWiFiDirectDeviceArrayAdapter);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
        updateDeviceList();
    }

    private void updateDeviceList() {
        ListView mauiDeviceListView = mDialog.findViewById(R.id.mauiDeviceListView);
        ArrayList<String> list = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getMauiDeviceStringList();
        mWiFiDirectDeviceArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, list);
        mauiDeviceListView.setAdapter(mWiFiDirectDeviceArrayAdapter);
        if (list.size() > mLastClickedWifiDevice)
            mauiDeviceListView.setSelection(mLastClickedWifiDevice);
        if (0 == list.size())
            Toast.makeText(mContext, "No Maui K3900 reachable....", Toast.LENGTH_SHORT).show();
    }

    private void updateStatusLogs() {
        ((TextView)mDialog.findViewById(R.id.connectionStatusLogsTextView)).setText(ConnectionStatusLoggingModel.getConnectionStatusLoggingModelSingletonInstance().getLogHistory());
        //((ProgressBar)mDialog.findViewById(R.id.wifiDirectConnectionProgressBar)).setIndeterminate(false);
        ((ProgressBar)mDialog.findViewById(R.id.wifiDirectConnectionProgressBar)).setMax(100);
        ((ProgressBar)mDialog.findViewById(R.id.wifiDirectConnectionProgressBar)).setProgress(50, true);
    }

    private void checkConnection() {
        ListView mauiDeviceListView = mDialog.findViewById(R.id.mauiDeviceListView);
        //if (mWifiDirectDeviceList.connected())
            //mDialog.dismiss();
        /*if (1 == mWifiDirectDeviceList.getNumberOfMauiDevices() && 0 < mWifiDirectDeviceList.getSelectedDeviceName().length()) {
            mWifiDirectDeviceList.setSelected(mWifiDirectDeviceList.getMauiDeviceName(0));
            ((ListView)mDialog.findViewById(R.id.mauiDeviceListView)).setBackgroundResource(android.R.color.holo_green_dark);
        }*/
    }

    public class CheckDeviceListTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public CheckDeviceListTimerTask(Context con) {
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
                            updateDeviceList();
                            updateStatusLogs();
                            checkConnection();
                        }
                    });
                }
            }).start();
        }
    }
}
