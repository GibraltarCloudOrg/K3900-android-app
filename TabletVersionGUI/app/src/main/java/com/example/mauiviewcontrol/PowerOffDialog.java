package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PowerOffDialog {
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Power Off Dialog";
    private final Context mContext;
    private Dialog mDialog = null;
    private ArrayList<String> mProbes = null;
    private ListView mProbeListView = null;

    public PowerOffDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.power_off_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    private void setUpWidgets() {
        /*try {
            mProbeListView = mDialog.findViewById(R.id.probeListView);
            mProbes = mBackend.onGetProbeList();
            if (null != mProbes) {
                mProbeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mProbeListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, mProbes));
                mProbeListView.setSelection(mProbes.indexOf(mBackend.getProbeName()));
            }
        } catch (Exception e) {
            Log.d(TAG, ", Set up Widgets failed: " + e.getMessage());
        }*/
    }

    private void setUpListeners() {
        setUpPowerOffButtonListener();
        Button cancelPowerOffDialogButton = mDialog.findViewById(R.id.cancelPowerOffDialogButton);
        cancelPowerOffDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void setUpPowerOffButtonListener() {
        Button powerOffButton = mDialog.findViewById(R.id.powerOffButton);
        powerOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().setSelected("");
                ImageStreamer.getImageStreamerSingletonInstance().clear();
                MauiToastMessage.displayToastMessage(mContext, mBackend.onRequestSystemShutdown(), "Shutdown Requested.", Toast.LENGTH_LONG);
                Toast.makeText(mContext, "Shutdown Processing...", Toast.LENGTH_LONG).show();
                //WidgetUtility.waitFor(5);
                ((MainActivity)mContext).finish();
                Intent intent = new Intent(mContext, com.example.mauiviewcontrol.MainActivity.class);
                //WidgetUtility.waitFor(1);
                ((MainActivity)mContext).startActivity(intent);
                Toast.makeText(mContext, "GUI Application restarted.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
