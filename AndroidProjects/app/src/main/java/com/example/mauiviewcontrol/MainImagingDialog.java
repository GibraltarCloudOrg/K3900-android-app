package com.example.mauiviewcontrol;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.method.Touch;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class MainImagingDialog {
    public static final String TAG = "Main Imaging Dialog";
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private WifiDirectDeviceList mWifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
    private final Context mContext;
    /*final*/ TouchDialog mDialog = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean readyForCheckRealtimeStates = false;
    private ImageView mPreviousImageView = null;
    private boolean mEnableDisplayWidgets = true;
    private boolean mDebugMode = false;
    private String mPreviousConnectionStatusMessage = "";
    private boolean mConnectionStatus = false;
    private ArrayList<MauiSlider> mTgcSliders = new ArrayList();
    private ImageView mImagingImageView = null;

    public MainImagingDialog(Context context, boolean enableDisplayWidgets, boolean debugMode) {
        mContext = context;
        mEnableDisplayWidgets = enableDisplayWidgets;
        mDebugMode = debugMode;
        ScaleListener scaleListener = new ScaleListener(mContext);
        mDialog = new TouchDialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen, new ScaleGestureDetector(mContext, scaleListener));
        mDialog.setContentView(R.layout.main_imaging_view);
        scaleListener.setTextView(mDialog.findViewById(R.id.beamformerParameterValueLowerTextView));
        TouchEvent.setTextView(mDialog.findViewById(R.id.beamformerParameterValueTextView));
        setUpWidgets();
        setUpListeners();
        mPreviousImageView = ImageStreamer.getImageStreamerSingletonInstance().getImageView();
        mImagingImageView = mDialog.findViewById(R.id.imagingImageView);
        ImageStreamer.getImageStreamerSingletonInstance().setImageView(mImagingImageView);
        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.CENTER);
        updateConnectionStatus();
        startTimers();
        readyForCheckRealtimeStates = true;
    }

    /*public void start() {
        SelectLogInDialog selectLogInDialog = new SelectLogInDialog(mContext);
        //if (1 < WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getNumberOfMauiDevices())
            new SelectMauiServerDialog(mContext);
    }*/

    public void updateConnectionStatus() {
        mConnectionStatus = mBackend.isAvailable() & WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().connected();
        TextView connectionStatusTextView = mDialog.findViewById(R.id.connectionStatusTextView);
        String message;
        if (mConnectionStatus) {
            connectionStatusTextView.setBackgroundColor(Color.BLACK);
            connectionStatusTextView.setTextColor(Color.GREEN);
            message = mWifiDirectDeviceList.getSelectedDeviceName();
            //message = "Connected to: " + mWifiDirectDeviceList.getSelectedDeviceName();
        }
        else if (mBackend.messageTo().UnitTesting == mBackend.messageTo()) {
            connectionStatusTextView.setBackgroundColor(Color.YELLOW);
            connectionStatusTextView.setTextColor(Color.BLACK);
            message = "Gui Unit Test Mode";
        }
        else {
            connectionStatusTextView.setBackgroundColor(Color.YELLOW);
            connectionStatusTextView.setTextColor(Color.BLACK);
            message = "  Not Ready";
        }
        if (0 != message.compareTo(mPreviousConnectionStatusMessage)) {
            mPreviousConnectionStatusMessage = message;
            connectionStatusTextView.setText(message);
            if (mConnectionStatus) {
                Toast.makeText(mContext, "Almost there.  Please wait....", Toast.LENGTH_LONG).show();
                message = "Connecting to: " + message;
            }
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

    private void startTimers() {
        Timer statusTimer = new Timer();
        TimerTask statusTimerTask = new StatusTimerTask(mContext);
        statusTimer.scheduleAtFixedRate(statusTimerTask, 3000, 1500);

        Timer clearMessageTimer = new Timer();
        TimerTask clearMessageTimerTask = new ClearMessageTimerTask(mContext);
        clearMessageTimer.scheduleAtFixedRate(clearMessageTimerTask, 10 * 1000, 30 * 1000);
    }

    public void checkRealTimeApplicationStates() {
        updateConnectionStatus();
        TextView connectionStatusTextView = mDialog.findViewById(R.id.connectionStatusTextView);
        if (!mConnectionStatus)
            blinkConnectionStatus();
        else if (View.INVISIBLE == connectionStatusTextView.getVisibility())
            connectionStatusTextView.setVisibility(View.VISIBLE);
        if (mDebugMode)
            blinkDebugModeText();
    }

    private void blinkConnectionStatus() {
        TextView connectionStatusTextView = mDialog.findViewById(R.id.connectionStatusTextView);
        if (View.INVISIBLE == connectionStatusTextView.getVisibility())
            connectionStatusTextView.setVisibility(View.VISIBLE);
        else
            connectionStatusTextView.setVisibility(View.INVISIBLE);
    }

    private void blinkDebugModeText() {
        TextView buildModeTextView = mDialog.findViewById(R.id.buildModeTextView);
        if (View.INVISIBLE == buildModeTextView.getVisibility())
            buildModeTextView.setVisibility(View.VISIBLE);
        else
            buildModeTextView.setVisibility(View.INVISIBLE);
    }

    private void clearMessages() {
        ((TextView)mDialog.findViewById(R.id.beamformerParameterValueTextView)).setText("");
        ((TextView)mDialog.findViewById(R.id.beamformerParameterValueLowerTextView)).setText("");
    }

    public class StatusTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public StatusTimerTask(Context con) {
            this.mContext = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //updateDateTime();
                            //if (!mBackend.isAvailable())
                                //return;
                            checkRealTimeApplicationStates();
                        }
                    });
                }
            }).start();
        }
    }

    public class ClearMessageTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public ClearMessageTimerTask(Context con) {
            this.mContext = con;
        }

        @Override
        public void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            clearMessages();
                        }
                    });
                }
            }).start();
        }
    }

    private void setUpWidgets() {
        setUpTgcMauiSliders();
        WidgetUtility.setUpExitApplicationButton(mDialog.findViewById(R.id.exitAllControllersViewButton), mContext);
        //LinearLayout tgcDlcLinearLayout = mDialog.findViewById(R.id.tgcDlcLinearLayout);
        //MauiSeekBar mauiSeekBar = new MauiSeekBar(mContext);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 260);
        //mauiSeekBar.setLayoutParams(params);
        //tgcDlcLinearLayout.addView(mauiSeekBar);

        //TextView debugReleaseModeTextView = mDialog.findViewById(R.id.buildModeTextView);
        /*if (mDebugMode) {
            debugReleaseModeTextView.setText("Debug Mode");
            debugReleaseModeTextView.setBackgroundColor(Color.BLACK);
            debugReleaseModeTextView.setTextColor(Color.WHITE);
        }
        else {
            debugReleaseModeTextView.setText("Release Mode");
            debugReleaseModeTextView.setBackgroundColor(Color.YELLOW);
            debugReleaseModeTextView.setTextColor(Color.BLACK);
        }*/
        /*int frameRate = mBackend.getFrameRate();
        if (0 < frameRate)
            debugReleaseModeTextView.setText("Frame Rate: " + String.valueOf(mBackend.getFrameRate()) + " fps");
        else
            debugReleaseModeTextView.setText("");*/
        /*mTgcSliders.add(mDialog.findViewById(R.id.tgc1SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc2SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc3SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc4SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc5SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc6SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc7SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc8SeekBar));
        tgcSeekBars.add(mDialog.findViewById(R.id.tgc9SeekBar));
        mDialog.setTgcSliderArray(tgcSeekBars);*/
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
        LinearLayout tgcDlcLinearLayout = mDialog.findViewById(R.id.tgcDlcLinearLayout);
        for (int index = 0; index < 9; ++index) {
            MauiSlider tgcSlider = new MauiSlider(mContext);
            tgcSlider.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            tgcSlider.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);            //tgcSlider.setTint
            //Drawable thumb = tgcSlider.getThumb();
            //thumb.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            mTgcSliders.add(tgcSlider);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(380, 87);
            //params.setMargins(0, 0, 0, 77);
            //tgcSlider.setBackgroundColor(Color.WHITE);
            tgcSlider.setLayoutParams(params);
            tgcDlcLinearLayout.addView(tgcSlider, index + 2);
        }
        mDialog.setTgcSliderArray(mTgcSliders);
        /*MauiSlider tgcSlider = new MauiSlider(mContext);
        tgcSlider.setId(R.id.tgc1SeekBar);
        tgcSlider.setMin(-100);
        tgcSlider.setMax(100);
        getImageDeadlineLinearLayout.addView(tgcSlider, 1);*/
    }

    private void setUpListeners() {
        WidgetUtility.setUpPowerImageView(mDialog.findViewById(R.id.powerImageView), mContext);
        WidgetUtility.setUpCleanScreenButton(mDialog.findViewById(R.id.cleanScreenButton), mContext);
        WidgetUtility.setUpDisconnectServerButton(mDialog.findViewById(R.id.disconnectServerButton), mContext);
        SpeedOfSoundView.setUpDecreaseFocusButtonListener(mDialog, mContext, mDialog.findViewById(R.id.decreaseFocusFloatingActionButton));
        SpeedOfSoundView.setUpIncreaseFocusButtonListener(mDialog, mContext, mDialog.findViewById(R.id.increaseFocusFloatingActionButton));
        SpeedOfSoundView.setUpToggleSosButtonListener(mDialog, mContext, mDialog.findViewById(R.id.toggleSosFloatingActionButton));
        setUpGrayscaleAdjustButtonsListeners();
        setUpSlidersListeners();
        //setUpExitButtonListener();
        WidgetUtility.setUpMauiLogoImageViewListener(mDialog.findViewById(R.id.mauiLogoImageView), mContext, mDialog);
    }

    private void setUpSlidersListeners() {
        mBackEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(0), mBackEndSliderElementSendingMessageVisitor, true, "Tgc1", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(0));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(1), mBackEndSliderElementSendingMessageVisitor, true, "Tgc2", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(1));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(2), mBackEndSliderElementSendingMessageVisitor, true, "Tgc3", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(2));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(3), mBackEndSliderElementSendingMessageVisitor, true, "Tgc4", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(3));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(4), mBackEndSliderElementSendingMessageVisitor, true, "Tgc5", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(4));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(5), mBackEndSliderElementSendingMessageVisitor, true, "Tgc6", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(5));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(6), mBackEndSliderElementSendingMessageVisitor, true, "Tgc7", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(6));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(7), mBackEndSliderElementSendingMessageVisitor, true, "Tgc8", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(7));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(8), mBackEndSliderElementSendingMessageVisitor, true, "Tgc9", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, mTgcSliders.get(8));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Dlc(), mBackEndSliderElementSendingMessageVisitor, true, "Dlc", ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.dlcSeekBar));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new MasterGain(), mBackEndSliderElementSendingMessageVisitor, true, "VGA Gain", ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.masterGainSeekBar));
        //WidgetUtility.setUpSliderListener(mContext, null, mDialog, new MasterGain(), mBackEndSliderElementSendingMessageVisitor, true, "MasterGain value updated", ParameterLimits.MinMasterGain, ParameterLimits.MaxMasterGain, ParameterLimits.FloatValueStep, R.id.masterGainSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new AcousticPower(), mBackEndSliderElementSendingMessageVisitor, true, "Acoustic Power", ParameterLimits.MinAcousticPower, ParameterLimits.MaxAcousticPower, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.acousticPowerSeekBar));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Gaussian(), mBackEndSliderElementSendingMessageVisitor, true, "Gaussian", ParameterLimits.MinGaussianFilter, ParameterLimits.MaxGaussianFilter, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.gaussianSeekBar));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Edge(), mBackEndSliderElementSendingMessageVisitor, true, "Edge", ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.edgeSeekBar));
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Contrast(), mBackEndSliderElementSendingMessageVisitor, true, "Contrast", ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.grayscaleAdjust1stSeekBar));
        //WidgetUtility.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Brightness value updated", ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep, R.id.grayscaleAdjust2ndSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Gamma(), mBackEndSliderElementSendingMessageVisitor, true, "Gamma", ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.grayscaleAdjustGammaSeekBar));
    }

    private void setUpGrayscaleAdjustButtonsListeners() {
        Button autoGrayscaleButton = mDialog.findViewById(R.id.autoGrayscaleButton);
        autoGrayscaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackend.onAutoContrastChanged(false);
            }
        });

        Button manualGrayscaleButton = mDialog.findViewById(R.id.manualGrayscaleButton);
        manualGrayscaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBackend.onAutoContrastChanged(true);
            }
        });
    }

    private void setUpExitButtonListener() {
        ((Button)mDialog.findViewById(R.id.exitAllControllersViewButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ImageStreamer.getImageStreamerSingletonInstance().setImageView(mPreviousImageView);
                Toast.makeText(mContext, "Main Imaging Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean isVisible() {
        return mDialog.isShowing();
    }

    void checkRealtimeStates() {
        if (!readyForCheckRealtimeStates)
            return;
        WidgetUtility.updateDateTime(mDialog.findViewById(R.id.currentTimeTextView), mDialog.findViewById(R.id.currentDateTextView));
        checkApplicationStatus();
        //if (!mBackend.connected())
            //return;
        checkBeamformerControllerStates();
        checkButtonsStates();
        if(mBackend.connected() && mBackend.imageAvailable())
            updateImagingWidgets();
    }

    private void checkApplicationStatus() {
        //((TextView)mDialog.findViewById(R.id.probeNameTextView)).setText(mBackend.getProbeName());
        //((Button)mDialog.findViewById(R.id.probeButton)).setText("Probe: " + mBackend.getProbeName());
        TextView debugReleaseModeTextView = mDialog.findViewById(R.id.buildModeTextView);
        int frameRate = mBackend.getFrameRate();
        if (0 < frameRate)
            debugReleaseModeTextView.setText("Frame Rate: " + String.valueOf(mBackend.getFrameRate()) + " fps");
        else
            debugReleaseModeTextView.setText("");
    }

    private void checkBeamformerControllerStates() {
        setSlidersEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        if (mBackend.connected())
            updateSlidersPositions();

        setSpeedOfSoundEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        ((TextView)mDialog.findViewById(R.id.speedOfSoundValueTextView)).setText(Float.toString(mBackend.getSpeedOfSoundValue()) + " m/s");
    }

    private void setSlidersEnabled(boolean enabled) {
        mTgcSliders.get(0).setEnabled(enabled);
        mTgcSliders.get(1).setEnabled(enabled);
        mTgcSliders.get(2).setEnabled(enabled);
        mTgcSliders.get(3).setEnabled(enabled);
        mTgcSliders.get(4).setEnabled(enabled);
        mTgcSliders.get(5).setEnabled(enabled);
        mTgcSliders.get(6).setEnabled(enabled);
        mTgcSliders.get(7).setEnabled(enabled);
        mTgcSliders.get(8).setEnabled(enabled);
        mDialog.findViewById(R.id.dlcSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.masterGainSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.acousticPowerSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.gaussianSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.edgeSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.grayscaleAdjust1stSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar).setEnabled(enabled);
        mDialog.findViewById(R.id.grayscaleAdjustGammaSeekBar).setEnabled(enabled);
    }

    private void setSpeedOfSoundEnabled(boolean enabled) {
        mDialog.findViewById(R.id.speedOfSoundTitleTextView).setEnabled(enabled);
        mDialog.findViewById(R.id.decreaseFocusFloatingActionButton).setEnabled(enabled);
        mDialog.findViewById(R.id.increaseFocusFloatingActionButton).setEnabled(enabled);
        mDialog.findViewById(R.id.toggleSosFloatingActionButton).setEnabled(enabled);
    }

    private void updateSlidersPositions() {
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(0), mBackend.getTgcValue(0), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(1), mBackend.getTgcValue(1), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(2), mBackend.getTgcValue(2), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(3), mBackend.getTgcValue(3), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(4), mBackend.getTgcValue(4), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(5), mBackend.getTgcValue(5), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(6), mBackend.getTgcValue(6), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(7), mBackend.getTgcValue(7), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mTgcSliders.get(8), mBackend.getTgcValue(8), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.dlcSeekBar), mBackend.getDlcValue(), ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.masterGainSeekBar), mBackend.getVgaGainValue(), ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep);
        //MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.masterGainSeekBar), mBackend.getMasterGainValue(), ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.acousticPowerSeekBar), mBackend.getAcousticPowerValue(), ParameterLimits.MinAcousticPower, ParameterLimits.MaxAcousticPower, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.gaussianSeekBar), mBackend.getGaussianValue(), ParameterLimits.MinGaussianFilter, ParameterLimits.MaxGaussianFilter, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.edgeSeekBar), mBackend.getEdgeValue(), ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust1stSeekBar), mBackend.getContrastValue(), ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjustGammaSeekBar), mBackend.getGammaValue(), ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep);
    }

    private void checkButtonsStates() {
        checkTopMenuButtonsStates();
        checkFunctionButtonsStates();
        checkRequestMessageButtonsStates();
        checkAutoContrastButtonsStates();
    }

    private void checkTopMenuButtonsStates() {
        mDialog.findViewById(R.id.logInButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        String buttonText = !mBackend.loggedIn() ? "Log In" : "Log Out";
        ((Button)mDialog.findViewById(R.id.logInButton)).setText(buttonText);
        mDialog.findViewById(R.id.powerImageView).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
    }

    private void checkFunctionButtonsStates() {
        mDialog.findViewById(R.id.probeButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.measureButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.presetsButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.patientButton).setEnabled(mBackend.loggedIn() & mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.saveLoadButton).setEnabled(mBackend.loggedIn() & mBackend.connected() & mBackend.wifiDeviceConnected());
        ((Button)mDialog.findViewById(R.id.probeButton)).setText("Probe: " + mBackend.getProbeName());
    }

    private void checkRequestMessageButtonsStates() {
        mDialog.findViewById(R.id.imagePositionHomeButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.imagePositionCenterButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.autoGrayscaleButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.manualGrayscaleButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.playPauseImageButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.stepBackwardImageButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.stepForwardImageButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.livePlaybackButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.runFreezeButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.increaseFocusFloatingActionButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.decreaseFocusFloatingActionButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.toggleSosFloatingActionButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        //int livePlaybackButtonVisible = mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        //mDialog.findViewById(R.id.livePlaybackButton).setVisibility(livePlaybackButtonVisible);
        int livePlaybackButtonVisible = !mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.livePlaybackButton).setVisibility(livePlaybackButtonVisible);
        int runFreezeButtonVisible = mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.runFreezeButton).setVisibility(runFreezeButtonVisible);
        int playPauseImageResource = mBackend.paused() ? R.drawable.right_arrow : R.drawable.pause_bars;
        ((ImageButton)mDialog.findViewById(R.id.playPauseImageButton)).setImageResource(playPauseImageResource);
    }

    private void checkAutoContrastButtonsStates() {
        TextView grayscale1stTextView = mDialog.findViewById(R.id.grayscale1stTextView);
        TextView grayscale2ndTextView = mDialog.findViewById(R.id.grayscale2ndTextView);
        if (mBackend.getAutoContrastState()) {
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Light Enhance", ParameterLimits.MinAutoBrightness, ParameterLimits.MaxAutoBrightness, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar));
            MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar), mBackend.getBrightnessValue(), ParameterLimits.MinAutoBrightness, ParameterLimits.MaxAutoBrightness, ParameterLimits.FloatValueStep);
            mDialog.findViewById(R.id.autoGrayscaleButton).setBackgroundColor(Color.WHITE);
            mDialog.findViewById(R.id.manualGrayscaleButton).setBackgroundColor(Color.DKGRAY);
            grayscale1stTextView.setText("Dark Enhance");
            grayscale2ndTextView.setText("Light Enhance");
        }
        else {
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Brightness", ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar));
            MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar), mBackend.getBrightnessValue(), ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep);
            mDialog.findViewById(R.id.autoGrayscaleButton).setBackgroundColor(Color.DKGRAY);
            mDialog.findViewById(R.id.manualGrayscaleButton).setBackgroundColor(Color.WHITE);
            grayscale1stTextView.setText("Contrast");
            grayscale2ndTextView.setText("Brightness");
        }
    }

    private void updateImagingWidgets() {
        //WidgetUtility.updateCineLoop(mDialog.findViewById(R.id.cineLoopProgressBar));
        //if (!mAreDisplayWidgetsVisible)
        //showDisplayWidgets(true);
        //WidgetUtility.setCentimeterText(mDialog.findViewById(R.id.cmTextView));
        //setUpHorizontalCentimetersMeasurementNumbers();
        //setUpVerticalCentimetersMeasurementNumbers();
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
        updateShakaImage();
    }

    private void updateShakaImage() {
        float pixelSizeMmX = 1024 * mBackend.getPixelSizeX() / (mBackend.getScale() * mImagingImageView.getWidth());
        float pixelSizeMmY = 1024 * mBackend.getPixelSizeY() / (mBackend.getScale() * mImagingImageView.getHeight());
        float scaleX = pixelSizeMmX < 0 ? 1.0f : -1.0f;
        float scaleY = pixelSizeMmY > 0 ? 1.0f : -1.0f;
        mDialog.findViewById(R.id.shakaRelativeLayout).setScaleX(scaleX);
        mDialog.findViewById(R.id.shakaRelativeLayout).setScaleY(scaleY);
    }

    private void setWidgetEnabled(boolean enabled) {
        setSlidersEnabled(enabled);
    }

    /*private void updateDateTime() {
        ((TextView)mDialog.findViewById(R.id.currentTimeTextView)).setText(new SimpleDateFormat("HH:mm z", Locale.getDefault()).format(new Date()));
        ((TextView)mDialog.findViewById(R.id.currentDateTextView)).setText(new SimpleDateFormat("EEE, MM-dd-yyyy", Locale.getDefault()).format(new Date()));
    }*/
}
