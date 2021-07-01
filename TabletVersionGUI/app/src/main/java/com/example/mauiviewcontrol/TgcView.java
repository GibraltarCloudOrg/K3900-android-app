package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class TgcView {

    public TgcView(Context parent, TextView beamformerParameterValueLowerTextView, TextView beamformerParameterValueTextView) {
        mContext = parent;
        ScaleListener scaleListener = new ScaleListener(mContext);
        mDialog = new TouchDialog(mContext, android.R.style.ThemeOverlay_Material_Dialog, new ScaleGestureDetector(mContext, scaleListener));
        mDialog.setOffset(2050);
        mDialog.setScale(0.5f);
        mDialog.setContentView(R.layout.tgc_view);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.FILL_VERTICAL | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        scaleListener.setTextView(beamformerParameterValueLowerTextView);
        mBeamformerParameterValueTextView = beamformerParameterValueTextView;
        TouchEvent.setTextView(mBeamformerParameterValueTextView);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mDialog.setCancelable(true);
        mReadyForCheckRealtimeStates = true;
    }

    private void setUpWidgets() {
        setUpTgcMauiSliders();
        mDlcSlider = mDialog.findViewById(R.id.dlcSlider);
    }

    private void setUpTgcMauiSliders() {
        /*
            <SeekBar
                android:id="@+id/tgc6SeekBar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginVertical="10dp"
                android:progressBackgroundTint="@color/white" />
         */
        LinearLayout tgcDlcLinearLayout = mDialog.findViewById(R.id.tgcSlidersLinerLayout);
        for (int index = 0; index < 9; ++index) {
            MauiSlider tgcSlider = new MauiSlider(mContext);
            tgcSlider.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            tgcSlider.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);            //tgcSlider.setTint
            //Drawable thumb = tgcSlider.getThumb();
            //thumb.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            mTgcSliders.add(tgcSlider);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(900, 100);
            //params.setMargins(0, 0, 0, 77);
            //tgcSlider.setBackgroundColor(Color.WHITE);
            tgcSlider.setLayoutParams(params);
            tgcDlcLinearLayout.addView(tgcSlider, index + 2);
        }
        mDialog.setTgcSliderArray(mTgcSliders);
    }

    private void setUpListeners() {
        setUpSlidersListeners();
        Button exitSpeedOfSoundDialogButton = mDialog.findViewById(R.id.exitTgcDialogButton);
        exitSpeedOfSoundDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void setUpSlidersListeners() {
        mBackEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
        /*MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(0), mBackEndSliderElementSendingMessageVisitor, true, "Tgc1", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(0));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(1), mBackEndSliderElementSendingMessageVisitor, true, "Tgc2", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(1));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(2), mBackEndSliderElementSendingMessageVisitor, true, "Tgc3", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(2));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(3), mBackEndSliderElementSendingMessageVisitor, true, "Tgc4", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(3));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(4), mBackEndSliderElementSendingMessageVisitor, true, "Tgc5", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(4));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(5), mBackEndSliderElementSendingMessageVisitor, true, "Tgc6", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(5));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(6), mBackEndSliderElementSendingMessageVisitor, true, "Tgc7", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(6));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(7), mBackEndSliderElementSendingMessageVisitor, true, "Tgc8", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(7));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(8), mBackEndSliderElementSendingMessageVisitor, true, "Tgc9", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(8));*/

        for (int index = 0; index < mTgcSliders.size(); ++index)
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(index), mBackEndSliderElementSendingMessageVisitor, true, "Tgc" + String.valueOf(index + 1), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(index));
        MauiSlider.setUpSliderListener(mContext, mBeamformerParameterValueTextView, new Dlc(), mBackEndSliderElementSendingMessageVisitor, true, "Dlc", ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep, mDlcSlider);
    }

    public void checkRealtimeStates() {
        if (!mReadyForCheckRealtimeStates)
            return;
        try {
            updateSlidersPositions();
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void updateSlidersPositions() {
        /*MauiSlider.setCurrentSliderPosition(mTgcSliders.get(0), mBackend.getTgcValue(0), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(1), mBackend.getTgcValue(1), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(2), mBackend.getTgcValue(2), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(3), mBackend.getTgcValue(3), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(4), mBackend.getTgcValue(4), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(5), mBackend.getTgcValue(5), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(6), mBackend.getTgcValue(6), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(7), mBackend.getTgcValue(7), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(8), mBackend.getTgcValue(8), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);*/

        for (int index = 0; index < mTgcSliders.size(); ++index)
            MauiSlider.setCurrentSliderPosition(mTgcSliders.get(index), mBackend.getTgcValue(index), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDlcSlider, mBackend.getDlcValue(), ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep);
    }

    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Tgc View";
    private final Context mContext;
    private TouchDialog mDialog = null;
    private TextView mBeamformerParameterValueTextView = null;
    private SeekBar mDlcSlider = null;
    private ArrayList<MauiSlider> mTgcSliders = new ArrayList();
    BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean mReadyForCheckRealtimeStates = false;
}
