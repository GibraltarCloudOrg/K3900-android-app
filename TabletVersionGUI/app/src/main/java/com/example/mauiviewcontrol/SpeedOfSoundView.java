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


public class SpeedOfSoundView {

    public SpeedOfSoundView(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.speed_of_sound_view);
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
        setUpSosMauiSlider();
    }

    private void setUpSosMauiSlider() {
        try {
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
        }
    }

    private void setUpListeners() {
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new SpeedOfSound(), new BackEndSliderElementSendingMessageVisitor(), true, "Speed of Sound", ParameterLimits.MinSpeedOfSound, ParameterLimits.MaxSpeedOfSound, 100/*ParameterLimits.FloatValueStep*/, mSpeedOfSoundSlider);
        SpeedOfSoundView.setUpDecreaseFocusButtonListener(mDialog, mContext, mDialog.findViewById(R.id.decreaseSpeedOfSoundFloatingActionButton));
        SpeedOfSoundView.setUpIncreaseFocusButtonListener(mDialog, mContext, mDialog.findViewById(R.id.increaseSpeedOfSoundFloatingActionButton));
        SpeedOfSoundView.setUpToggleSosButtonListener(mDialog, mContext, mDialog.findViewById(R.id.toggleSpeedOfSoundFloatingActionButton));
        Button exitSpeedOfSoundDialogButton = mDialog.findViewById(R.id.exitSpeedOfSoundDialogButton);
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
            float speedOfSoundValueInMeterPerSecond = mBackend.getSpeedOfSoundValue();
            ((TextView)mDialog.findViewById(R.id.speedOfSoundCurrentValueTextView)).setText(Float.toString(speedOfSoundValueInMeterPerSecond) + " m/s");
            MauiSlider.setCurrentSliderPosition(mSpeedOfSoundSlider, speedOfSoundValueInMeterPerSecond * 1000, ParameterLimits.MinSpeedOfSound, ParameterLimits.MaxSpeedOfSound, 100/*ParameterLimits.FloatValueStep*/);
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    static public void setUpDecreaseFocusButtonListener(Dialog dialog, Context context, FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            SpeedOfSoundModel speedOfSoundModel = SpeedOfSoundModel.getSpeedOfSoundModelSingletonInstance();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    speedOfSoundModel.setKeepDecreasing();
                    // start timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Down Detected, Decrease Focus:", Toast.LENGTH_LONG);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    speedOfSoundModel.decrease();
                    speedOfSoundModel.reset();
                    // stop timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Up Detected, Decrease Focus:", Toast.LENGTH_LONG);
                }
                return true;
            }
        });
    }

    static public void setUpIncreaseFocusButtonListener(Dialog dialog, Context context, FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SpeedOfSoundModel speedOfSoundModel = SpeedOfSoundModel.getSpeedOfSoundModelSingletonInstance();
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    speedOfSoundModel.setKeepIncreasing();
                    // start timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Down Detected, Increase Focus:", Toast.LENGTH_LONG);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    speedOfSoundModel.increase();
                    speedOfSoundModel.reset();
                    // stop timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Up Detected, Increase Focus:", Toast.LENGTH_LONG);
                }
                return true;
            }
        });
    }

    static public void setUpToggleSosButtonListener(Dialog dialog, Context context, FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().onToggleSos();
                MauiToastMessage.displayToastMessage(context, result, "Toggle Sos:", Toast.LENGTH_LONG);
            }
        });
    }

    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Speed of Sound Dialog";
    private final Context mContext;
    private Dialog mDialog = null;
    private SeekBar mSpeedOfSoundSlider = null;
    //private MauiSlider mSpeedOfSoundSlider = null;
    private boolean mReadyForCheckRealtimeStates = false;
}
