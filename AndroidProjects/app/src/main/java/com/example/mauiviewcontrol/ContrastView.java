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


public class ContrastView {

    public ContrastView(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.contrast_view);
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
        mGrayscaleAdjust1stSlider = mDialog.findViewById(R.id.grayscaleAdjust1stSlider);
        mGrayscaleAdjust2ndSlider = mDialog.findViewById(R.id.grayscaleAdjust2ndSlider);
    }

    private void setUpGrayscaleAdjustButtonsListeners() {
        Button grayscaleAutoButton = mDialog.findViewById(R.id.grayscaleAutoButton);
        grayscaleAutoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackend.onAutoContrastChanged(false);
            }
        });

        Button grayscaleManualButton = mDialog.findViewById(R.id.grayscaleManualButton);
        grayscaleManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackend.onAutoContrastChanged(true);
            }
        });
    }

    private void setUpListeners() {
        mBackEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Contrast(), mBackEndSliderElementSendingMessageVisitor, true, "Contrast", ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep, mGrayscaleAdjust1stSlider);
        //WidgetUtility.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Brightness value updated", ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep, mGrayscaleAdjust2ndSlider);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Gamma(), mBackEndSliderElementSendingMessageVisitor, true, "Gamma", ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep, mGrayscaleAdjustGammaSlider);
        setUpGrayscaleAdjustButtonsListeners();
        Button exitSpeedOfSoundDialogButton = mDialog.findViewById(R.id.exitContrastViewDialogButton);
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
            checkAutoContrastButtonsStates();
            MauiSlider.setCurrentSliderPosition(mGrayscaleAdjust1stSlider, mBackend.getContrastValue(), ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep);
            MauiSlider.setCurrentSliderPosition(mGrayscaleAdjustGammaSlider, mBackend.getGammaValue(), ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep);
            //MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust1stSlider), mBackend.getContrastValue(), ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep);
            //MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjustGammaSlider), mBackend.getGammaValue(), ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep);
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void checkAutoContrastButtonsStates() {
        TextView grayscale1stTitleTextView = mDialog.findViewById(R.id.grayscale1stTitleTextView);
        TextView grayscale2ndTitleTextView = mDialog.findViewById(R.id.grayscale2ndTitleTextView);
        if (mBackend.getAutoContrastState()) {
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Light Enhance", ParameterLimits.MinAutoBrightness, ParameterLimits.MaxAutoBrightness, ParameterLimits.FloatValueStep, mGrayscaleAdjust2ndSlider);
            MauiSlider.setCurrentSliderPosition(mGrayscaleAdjust2ndSlider, mBackend.getBrightnessValue(), ParameterLimits.MinAutoBrightness, ParameterLimits.MaxAutoBrightness, ParameterLimits.FloatValueStep);
            mDialog.findViewById(R.id.grayscaleAutoButton).setBackgroundColor(Color.WHITE);
            mDialog.findViewById(R.id.grayscaleManualButton).setBackgroundColor(Color.DKGRAY);
            grayscale1stTitleTextView.setText("Dark Enhance");
            grayscale2ndTitleTextView.setText("Light Enhance");
        }
        else {
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Brightness", ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep, mGrayscaleAdjust2ndSlider);
            MauiSlider.setCurrentSliderPosition(mGrayscaleAdjust2ndSlider, mBackend.getBrightnessValue(), ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep);
            mDialog.findViewById(R.id.grayscaleAutoButton).setBackgroundColor(Color.DKGRAY);
            mDialog.findViewById(R.id.grayscaleManualButton).setBackgroundColor(Color.WHITE);
            grayscale1stTitleTextView.setText("Contrast");
            grayscale2ndTitleTextView.setText("Brightness");
        }
    }

    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Contrast View";
    private final Context mContext;
    private Dialog mDialog = null;
    private SeekBar mGrayscaleAdjust1stSlider = null;
    private SeekBar mGrayscaleAdjust2ndSlider = null;
    private SeekBar mGrayscaleAdjustGammaSlider = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean mReadyForCheckRealtimeStates = false;
}
