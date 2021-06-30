package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Constraints;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import k3900.K3900;

import static android.content.Intent.getIntent;

public class SystemStatusDialog {
    public static final String TAG = "System Status Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    /*final*/ AutoCompleteTextView mSelectedProbeView = null;
    int mLastClickId = -1;

    public SystemStatusDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.system_state_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    private void setUpWidgets() {
        LinearLayout detailedStateLinearLayout = mDialog.findViewById(R.id.detailedStateLinearLayout);
        detailedStateLinearLayout.setVisibility(View.INVISIBLE);
        //LinearLayout measurementTopLevelLinearLayout = mDialog.findViewById(R.id.measurementTopLevelLinearLayout);
        //measurementTopLevelLinearLayout.setMinimumWidth(500);

        ListView systemStateListView = mDialog.findViewById(R.id.systemStateListView);
        systemStateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickId);
                mLastClickId = position;
                //WidgetUtility.displayToastMessage(mContext, mBackend.onHighlightMeasurement(mLastClickId), "Highlight Measurement at index: " + mLastClickId, Toast.LENGTH_LONG);
            }
        });
        final ArrayList<String> list = mBackend.onGetSystemStateList();
        systemStateListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        systemStateListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list));
    }

    private void setUpListeners() {
        setUpShowSystemStateDetailsButtonListener();
        setUpExitSystemStateDetailsButtonListener();
    }

    private void setUpShowSystemStateDetailsButtonListener() {
        Button showSystemStateDetailsButton= mDialog.findViewById(R.id.showSystemStateDetailsButton);
        showSystemStateDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout detailedStateLinearLayout = mDialog.findViewById(R.id.detailedStateLinearLayout);
                detailedStateLinearLayout.setVisibility(View.VISIBLE);
                TextView detailedStateTextView = mDialog.findViewById(R.id.detailedStateTextView);
                detailedStateTextView.setText(mBackend.onGetSystemState(mLastClickId));
            }
        });
    }

    private void setUpExitSystemStateDetailsButtonListener() {
        Button exitDetailedStateButton= mDialog.findViewById(R.id.exitDetailedStateButton);
        exitDetailedStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout detailedStateLinearLayout = mDialog.findViewById(R.id.detailedStateLinearLayout);
                detailedStateLinearLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setUpExitDialogButtonListener() {
        /*Button exitMeasurementButton = mDialog.findViewById(R.id.exitMeasurementButton);
        exitMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Measurement Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
