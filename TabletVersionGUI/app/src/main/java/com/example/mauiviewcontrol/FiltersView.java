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


public class FiltersView {

    public FiltersView(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.filters_view);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mDialog.setCancelable(false);
        mReadyForCheckRealtimeStates = true;
    }

    private void setUpWidgets() {
        //setUpSosMauiSlider();
        mGaussianSlider = mDialog.findViewById(R.id.gaussianSlider);
        mEdgeSlider = mDialog.findViewById(R.id.edgeSlider);
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
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Gaussian(), new BackEndSliderElementSendingMessageVisitor(), true, "Gaussian", ParameterLimits.MinGaussianFilter, ParameterLimits.MaxGaussianFilter, ParameterLimits.FloatValueStep, mGaussianSlider);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Edge(), new BackEndSliderElementSendingMessageVisitor(), true, "Edge", ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep, mEdgeSlider);
        Button exitSpeedOfSoundDialogButton = mDialog.findViewById(R.id.exitFiltersDialogButton);
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
            MauiSlider.setCurrentSliderPosition(mGaussianSlider, mBackend.getGaussianValue(), ParameterLimits.MinGaussianFilter, ParameterLimits.MaxGaussianFilter, ParameterLimits.FloatValueStep);
            MauiSlider.setCurrentSliderPosition(mEdgeSlider, mBackend.getEdgeValue(), ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep);
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Filters View";
    private final Context mContext;
    private Dialog mDialog = null;
    //private SeekBar mSpeedOfSoundSlider = null;
    //private MauiSlider mSpeedOfSoundSlider = null;
    private SeekBar mGaussianSlider = null;
    private SeekBar mEdgeSlider = null;
    private boolean mReadyForCheckRealtimeStates = false;
}
