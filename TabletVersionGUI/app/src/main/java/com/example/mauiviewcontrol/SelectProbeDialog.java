package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectProbeDialog {
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Select Probe Dialog";
    private final Context mContext;
    private /*final*/ Dialog mDialog = null;
    private int mLastClickId = -1;
    private boolean readyForCheckRealtimeStates = false;
    private ArrayList<String> mProbes = null;
    private ListView mProbeListView = null;

    public SelectProbeDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.select_probe_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        readyForCheckRealtimeStates = true;
    }

    private void setUpWidgets() {
        try {
            mProbeListView = mDialog.findViewById(R.id.probeListView);
            //Spinner selectProbeSpinner = mDialog.findViewById(R.id.selectProbeSpinner);
            mProbes = mBackend.onGetProbeList();
            if (null != mProbes) {
                mProbeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                mProbeListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, mProbes));
                mProbeListView.setSelection(mProbes.indexOf(mBackend.getProbeName()));
            }
        } catch (Exception e) {
            Log.d(TAG, ", Set up Widgets failed: " + e.getMessage());
        }
    }

    private void setUpListeners() {
        setUpProbeListViewListener();
        /*Spinner selectProbeSpinner = mDialog.findViewById(R.id.selectProbeSpinner);
        selectProbeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                MauiToastMessage.displayToastMessage(mContext, mBackend.onSelectProbe(selectedItem), selectedItem + " selected: ", Toast.LENGTH_LONG);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });*/
        Button exitSelectProbeDialogButton = mDialog.findViewById(R.id.exitSelectProbeDialogButton);
        exitSelectProbeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Select Probe Dialog Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpProbeListViewListener() {
        mProbeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickId);
                mLastClickId = position;
                String newlySelectedProbeName = mProbeListView.getAdapter().getItem(mLastClickId).toString();
                //if (null != mStartExam)
                    //mStartExam.setLine(line);
                //Toast.makeText(mContext, "Selected:" + newlySelectedProbeName, Toast.LENGTH_LONG).show();
                MauiToastMessage.displayToastMessage(mContext, mBackend.onSelectProbe(newlySelectedProbeName), newlySelectedProbeName + " Selected: ", Toast.LENGTH_LONG);
            }
        });
        //probeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //probeListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, mPatients));
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, /*list*/mPatients);
    }

    public void checkRealtimeStates() {
        if (readyForCheckRealtimeStates && null != mProbes) {
            try {
                MauiListView.clearAllItemsBackgroundColor(mProbeListView);
                String selectedProbeName = mBackend.getProbeName();
                ((TextView)mDialog.findViewById(R.id.selectedProbeTextView)).setText(selectedProbeName);
                //mProbeListView.setSelection(mProbes.indexOf(selectedProbeName));
                int index = mProbes.indexOf(selectedProbeName);
                MauiListView.changeListViewSelectedItemColor(mProbeListView, mProbeListView.getChildAt(index), index, mLastClickId);
            } catch (Exception e) {
                Log.d(TAG, ", Check Realtime States failed: " + e.getMessage());
            }
        }
    }
}
