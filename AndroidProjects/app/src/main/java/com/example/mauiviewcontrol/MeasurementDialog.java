package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
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

public class MeasurementDialog {
    public static final String TAG = "Measurement Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    /*final*/ //AutoCompleteTextView mSelectedProbeView = null;
    //int mSelectedListItem = -1;//getIntent().getIntExtra("PositionInList", -1);
    int mLastClickId = -1;
    DeleteMeasurement mDeleteMeasurement = null;
    EditMeasurement mEditMeasurement = null;

    public MeasurementDialog(MainWindowActivity parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.measurement_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    }

    /*public void process() {
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.measurement_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }*/

    private void setUpWidgets() {
        LinearLayout measurementTopLevelLinearLayout = mDialog.findViewById(R.id.measurementTopLevelLinearLayout);
        measurementTopLevelLinearLayout.setMinimumWidth(500);

        ListView measurementsListView = mDialog.findViewById(R.id.measurementsListView);
        measurementsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickId);
                mLastClickId = position;
                mDeleteMeasurement.setIndex(mLastClickId);
                mEditMeasurement.setIndex(mLastClickId);
                MauiToastMessage.displayToastMessage(mContext, mBackend.onHighlightMeasurement(mLastClickId), "Highlight Measurement at index: " + mLastClickId, Toast.LENGTH_LONG);
            }
        });
        final ArrayList<Float> list = mBackend.onGetMeasurements();
        //ArrayAdapter adapter = new ArrayAdapter<Float>(mContext, android.R.layout.simple_list_item_1, list);
        //measurementsListView.setAdapter(adapter);
        measurementsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //measurementsListView.setSelection(mSelectedListItem);
        //int groups = 1;
        measurementsListView.setAdapter(new ArrayAdapter<Float>(mContext, android.R.layout.simple_list_item_1, list)
        /*{
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                final View renderer = super.getView(position, convertView, parent);
                if (position == mLastClickId)
                {
                    //TODO: set the proper selection color here:
                    renderer.setBackgroundResource(android.R.color.darker_gray);
                }
                return renderer;
            }
        }*/);
        //Spinner measurementsSpinner = mDialog.findViewById(R.id.measurementsSpinner);
        //measurementsSpinner.setAdapter(adapter);
    }

    private void setUpListeners() {
        //setUpStartMeasurementButtonListener();
        //setUpStopMeasurementButtonListener();
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        WidgetUtility.setUpListener(mContext, mDialog, R.id.startMeasurementButton, new StartMeasurement(), backEndElementSendingMessageVisitor, false, "", true, "Start Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.stopMeasurementButton, new StopMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Stop Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.swapMeasurementButton, new SwapMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Swap Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.cancelMeasurementButton, new CancelMeasurement(), backEndElementSendingMessageVisitor, true, "Cancel Measurement?", true, "Cancel Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.deleteMeasurementButton, mDeleteMeasurement = new DeleteMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Delete Measurement at index: ", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.editMeasurementButton, mEditMeasurement = new EditMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Edit Measurement at index: ", false);
        //setUpDeleteMeasurementButtonListener();
        setUpExitDialogButtonListener();
    }

    /*private void setUpStartMeasurementButtonListener() {
        Button startMeasurementButton = mDialog.findViewById(R.id.startMeasurementButton);
        startMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainWindowActivity.displayToastMessage(mBackend.onStartMeasurement(), "Start Measurement", Toast.LENGTH_LONG);
            }
        });
    }*/

    /*private void setUpStopMeasurementButtonListener() {
        Button stopMeasurementButton = mDialog.findViewById(R.id.stopMeasurementButton);
        stopMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainWindowActivity.displayToastMessage(mBackend.onStopMeasurement(), "Stop Measurement", Toast.LENGTH_LONG);
            }
        });
    }*/
    /*private void setUpDeleteMeasurementButtonListener() {
        Button deleteMeasurementButton = mDialog.findViewById(R.id.deleteMeasurementButton);
        deleteMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                boolean result = mBackend.onDeleteMeasurement(mLastClickId);
                                WidgetUtility.displayToastMessage(mContext, result, "Delete Measurement at index: " + mLastClickId, Toast.LENGTH_LONG);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                            default:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }*/

    private void setUpExitDialogButtonListener() {
        Button exitMeasurementButton = mDialog.findViewById(R.id.exitMeasurementButton);
        exitMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Measurement Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}

