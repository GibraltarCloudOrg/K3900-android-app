package com.example.mauiviewcontrol;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MeasureImagingDialog {
    public static final String TAG = "Measure Imaging Dialog";
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private final Context mContext;
    /*final*/ TouchDialog mDialog = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean readyForCheckRealtimeStates = false;
    private ImageView mPreviousImageView = null;
    int mLastClickId = -1;
    DeleteMeasurement mDeleteMeasurement = null;
    EditMeasurement mEditMeasurement = null;
    private boolean mEnableDisplayWidgets = true;

    public MeasureImagingDialog(Context parent, boolean enableDisplayWidgets) {
        mContext = parent;
        mEnableDisplayWidgets = enableDisplayWidgets;
        mDialog = new TouchDialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen, new ScaleGestureDetector(mContext, new ScaleListener(mContext)));
        mDialog.setContentView(R.layout.main_imaging_view);
        //mDialog.setContentView(R.layout.measure_imaging_view);
        includeMeasurementView();
        setUpWidgets();
        setUpListeners();
        mPreviousImageView = ImageStreamer.getImageStreamerSingletonInstance().getImageView();
        ImageStreamer.getImageStreamerSingletonInstance().setImageView(mDialog.findViewById(R.id.imagingImageView));
        //ImageStreamer.getImageStreamerSingletonInstance().setImageView(mDialog.findViewById(R.id.measureImagingImageView));
        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.CENTER);
        readyForCheckRealtimeStates = true;
    }

    private void includeMeasurementView() {
        //View  i = mDialog.findViewById(R.id.allControllersPage);
        //i.setBackgroundResource(R.layout.measurement_view.xml);
        //LinearLayout myLayout = (LinearLayout)findViewById(R.id.linearLayout1);
        //View itemInfo1 = getLayoutInflater().inflate(R.layout.item, myLayout, true);
        //View itemInfo2 = getLayoutInflater().inflate(R.layout.item, myLayout, true);
        //View itemInfo3 = getLayoutInflater().inflate(R.layout.item, myLayout, true);

        LinearLayout mainImagingCustomPageLinearLayout = (LinearLayout)mDialog.findViewById(R.id.mainImagingCustomPageLinearLayout);
        ViewGroup viewGroup = mDialog.findViewById(android.R.id.content);
        //View dialogView = LayoutInflater.from(mContext).inflate(R.layout.measurement_view, viewGroup, false);
        mainImagingCustomPageLinearLayout.removeAllViewsInLayout();
        /*View itemInfo1 =*/ ((MainActivity)mContext).getLayoutInflater().inflate(R.layout.measurement_view, mainImagingCustomPageLinearLayout, true);
    }

    private void setUpWidgets() {
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
        measurementsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        measurementsListView.setAdapter(new ArrayAdapter<Float>(mContext, android.R.layout.simple_list_item_1, list));
    }

    private void setUpListeners() {
        WidgetUtility.setUpPowerImageView(mDialog.findViewById(R.id.powerImageView), mContext);
        WidgetUtility.setUpCleanScreenButton(mDialog.findViewById(R.id.cleanScreenButton), mContext);
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        WidgetUtility.setUpListener(mContext, mDialog, R.id.startMeasurementButton, new StartMeasurement(), backEndElementSendingMessageVisitor, false, "", true, "Start Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.stopMeasurementButton, new StopMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Stop Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.swapMeasurementButton, new SwapMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Swap Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.cancelMeasurementButton, new CancelMeasurement(), backEndElementSendingMessageVisitor, true, "Cancel Measurement?", true, "Cancel Measurement", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.deleteMeasurementButton, mDeleteMeasurement = new DeleteMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Delete Measurement at index: ", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.editMeasurementButton, mEditMeasurement = new EditMeasurement(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Edit Measurement at index: ", false);
        //setUpDeleteMeasurementButtonListener();
        setUpExitButtonListener();
    }

    private void setUpExitButtonListener() {
        ((Button)mDialog.findViewById(R.id.exitMeasurementButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ImageStreamer.getImageStreamerSingletonInstance().setImageView(mPreviousImageView);
                Toast.makeText(mContext, "Measure Imaging Dialog Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isVisible() {
        return mDialog.isShowing();
    }

    void checkRealtimeStates() {
        if (!readyForCheckRealtimeStates)
            return;
        checkButtonsStates();
        updateDisplayWidgets();
        WidgetUtility.updateDateTime(mDialog.findViewById(R.id.currentTimeTextView), mDialog.findViewById(R.id.currentDateTextView));
    }

    private void checkButtonsStates() {
        String buttonText = !mBackend.loggedIn() ? "Log In" : "Log Out";
        ((Button)mDialog.findViewById(R.id.logInButton)).setText(buttonText);

        mDialog.findViewById(R.id.patientButton).setEnabled(mBackend.loggedIn() & mBackend.connected());
        mDialog.findViewById(R.id.saveLoadButton).setEnabled(mBackend.loggedIn() & mBackend.connected());
    }

    private void updateDisplayWidgets() {
        ConvertibleRuler.setUpHorizontalRuler(mDialog.findViewById(R.id.horizontalRulerImageView), mDialog.findViewById(R.id.horizontalMeasurementNumberLabelsTextView), mEnableDisplayWidgets);
        ConvertibleRuler.setUpVerticalRuler(mDialog.findViewById(R.id.verticalRulerImageView), mDialog.findViewById(R.id.verticalMeasurementNumberLabelsTextView), mEnableDisplayWidgets);
        if (mEnableDisplayWidgets) {
            CineLoop.update(mDialog.findViewById(R.id.cineLoopSeekBar));
            ((TextView) mDialog.findViewById(R.id.unitNameOfRulersTextView)).setText(mBackend.getUnitName());
        }
        else {
            if (View.VISIBLE == mDialog.findViewById(R.id.cineLoopSeekBar).getVisibility())
                mDialog.findViewById(R.id.cineLoopSeekBar).setVisibility(View.INVISIBLE);
            if (View.VISIBLE == mDialog.findViewById(R.id.unitNameOfRulersTextView).getVisibility())
                mDialog.findViewById(R.id.unitNameOfRulersTextView).setVisibility(View.INVISIBLE);
        }
        //CineLoop.update(mDialog.findViewById(R.id.cineLoopInMeasureImagingSeekBar));
        //ConvertibleRuler.setUpHorizontalRuler(mDialog.findViewById(R.id.horizontalRulerInMeasureImagingImageView), mDialog.findViewById(R.id.horizontalUnitMeasurementNumberLabelsInMeasureImagingTextView), mEnableDisplayWidgets);
        //ConvertibleRuler.setUpVerticalRuler(mDialog.findViewById(R.id.verticalRulerInMeasureImagingImageView), mDialog.findViewById(R.id.verticalUnitMeasurementNumberLabelsInMeasureImagingTextView), mEnableDisplayWidgets);
    }
}
