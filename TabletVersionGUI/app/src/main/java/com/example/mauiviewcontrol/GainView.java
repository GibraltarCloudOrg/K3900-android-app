package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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


public class GainView {

    public GainView(Context parent, TextView beamformerParameterValueLowerTextView, TextView beamformerParameterValueTextView) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.gain_view);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        mBeamformerParameterValueLowerTextView = beamformerParameterValueLowerTextView;
        mBeamformerParameterValueTextView = beamformerParameterValueTextView;
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mDialog.setCancelable(false);
        mReadyForCheckRealtimeStates = true;
    }

    private void setUpWidgets() {
        //setUpSosMauiSlider();
        mVgaGainSeekBar = mDialog.findViewById(R.id.vgaGainSlider);
        mAcousticPowerSeekBar = mDialog.findViewById(R.id.acousticPowerSlider);
    }

    private void setUpSosMauiSlider() {
        /*try {
            LinearLayout speedOfSoundLinearLayout = mDialog.findViewById(R.id.speedOfSoundLinearLayout);
            mSpeedOfSoundSlider = new SeekBar(mContext);
            //mSpeedOfSoundSlider = new MauiSlider(mContext);
            mSpeedOfSoundSlider.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
            mSpeedOfSoundSlider.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(580, 200);
            //mSpeedOfSoundSlider.setLayoutParams(params);
            speedOfSoundLinearLayout.addView(mSpeedOfSoundSlider, 2);
        } catch (Exception e) {
            Log.d(TAG, ", Set up Speed of Sound Slider failed: " + e.getMessage());
        }*/
    }

    private void setUpListeners() {
        MauiSlider.setUpSliderListener(mContext, mBeamformerParameterValueTextView, new MasterGain(), new BackEndSliderElementSendingMessageVisitor(), true, "VGA Gain", ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep, mVgaGainSeekBar);
        MauiSlider.setUpSliderListener(mContext, mBeamformerParameterValueTextView, new AcousticPower(), new BackEndSliderElementSendingMessageVisitor(), true, "Acoustic Power", ParameterLimits.MinAcousticPower, ParameterLimits.MaxAcousticPower, ParameterLimits.FloatValueStep, mAcousticPowerSeekBar);
        Button exitSpeedOfSoundDialogButton = mDialog.findViewById(R.id.exitGainViewDialogButton);
        exitSpeedOfSoundDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public void checkRealtimeStates() {
        if (!mReadyForCheckRealtimeStates)
            return;
        try {
            //float speedOfSoundValueInMeterPerSecond = mBackend.getSpeedOfSoundValue();
            //((TextView)mDialog.findViewById(R.id.speedOfSoundCurrentValueTextView)).setText(Float.toString(speedOfSoundValueInMeterPerSecond) + " m/s");
            MauiSlider.setCurrentSliderPosition(mVgaGainSeekBar, mBackend.getVgaGainValue(), ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep);
            MauiSlider.setCurrentSliderPosition(mAcousticPowerSeekBar, mBackend.getAcousticPowerValue(), ParameterLimits.MinAcousticPower, ParameterLimits.MaxAcousticPower, ParameterLimits.FloatValueStep);
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Gain View";
    private final Context mContext;
    private Dialog mDialog = null;
    TextView mBeamformerParameterValueLowerTextView;
    TextView mBeamformerParameterValueTextView;
    private SeekBar mVgaGainSeekBar = null;
    private SeekBar mAcousticPowerSeekBar = null;
    private boolean mReadyForCheckRealtimeStates = false;
}
