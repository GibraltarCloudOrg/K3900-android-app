package com.example.mauiviewcontrol;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class ActionButtonsImagingDialog {
    public static final String TAG = "Action Buttons Imaging Dialog";
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private WifiDirectDeviceList mWifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
    private final Context mContext;
    TouchDialog mDialog = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean readyForCheckRealtimeStates = false;
    private ImageView mPreviousImageView = null;
    private boolean mEnableDisplayWidgets = true;
    private boolean mDebugMode = false;
    private String mPreviousConnectionStatusMessage = "";
    private boolean mConnectionStatus = false;
    private ArrayList<MauiSlider> mTgcSliders = new ArrayList();
    private ImageView mImagingImageView = null;

    public ActionButtonsImagingDialog(Context context, boolean enableDisplayWidgets, boolean debugMode) {
        mContext = context;
        mEnableDisplayWidgets = enableDisplayWidgets;
        mDebugMode = debugMode;
        ScaleListener scaleListener = new ScaleListener(mContext);
        mDialog = new TouchDialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen, new ScaleGestureDetector(mContext, scaleListener));
        mDialog.setContentView(R.layout.main_imaging_view);
        scaleListener.setTextView(mDialog.findViewById(R.id.beamformerParameterValueLowerTextView));
        TouchEvent.setTextView(mDialog.findViewById(R.id.beamformerParameterValueTextView));
        includeBeamformerControllerFloatingActionButtonsView();
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

    private void includeBeamformerControllerFloatingActionButtonsView() {
        LinearLayout mainImagingCustomPageLinearLayout = mDialog.findViewById(R.id.mainImagingCustomPageLinearLayout);
        ViewGroup viewGroup = mDialog.findViewById(android.R.id.content);
        mainImagingCustomPageLinearLayout.removeAllViewsInLayout();
        /*View itemInfo1 =*/ ((MainActivity)mContext).getLayoutInflater().inflate(R.layout.beamformer_controller_floating_action_buttons_view, mainImagingCustomPageLinearLayout, true);
    }

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
        //WidgetUtility.setUpExitApplicationButton(mDialog.findViewById(R.id.exitAllControllersViewButton), mContext);
    }

    private void setUpListeners() {
        WidgetUtility.setUpPowerImageView(mDialog.findViewById(R.id.powerImageView), mContext);
        WidgetUtility.setUpCleanScreenButton(mDialog.findViewById(R.id.cleanScreenButton), mContext);
        WidgetUtility.setUpDisconnectServerButton(mDialog.findViewById(R.id.disconnectServerButton), mContext);
        SpeedOfSoundView.setUpDecreaseFocusButtonListener(mDialog, mContext, mDialog.findViewById(R.id.decreaseFocusFloatingActionButton));
        SpeedOfSoundView.setUpIncreaseFocusButtonListener(mDialog, mContext, mDialog.findViewById(R.id.increaseFocusFloatingActionButton));
        SpeedOfSoundView.setUpToggleSosButtonListener(mDialog, mContext, mDialog.findViewById(R.id.toggleSosFloatingActionButton));
        WidgetUtility.setUpMauiLogoImageViewListener(mDialog.findViewById(R.id.mauiLogoImageView), mContext, mDialog);
    }

    void checkRealtimeStates() {
        if (!readyForCheckRealtimeStates)
            return;
        WidgetUtility.updateDateTime(mDialog.findViewById(R.id.currentTimeTextView), mDialog.findViewById(R.id.currentDateTextView));
        checkApplicationStatus();
        checkBeamformerControllerStates();
        checkButtonsStates();
        if(mBackend.connected() && mBackend.imageAvailable())
            updateImagingWidgets();
    }

    private void checkApplicationStatus() {
        TextView debugReleaseModeTextView = mDialog.findViewById(R.id.buildModeTextView);
        int frameRate = mBackend.getFrameRate();
        if (0 < frameRate)
            debugReleaseModeTextView.setText("Frame Rate: " + String.valueOf(mBackend.getFrameRate()) + " fps");
        else
            debugReleaseModeTextView.setText("");
    }

    private void checkBeamformerControllerStates() {
        //if (mBackend.connected())
            //updateSlidersPositions();
    }

    private void checkButtonsStates() {
        checkTopMenuButtonsStates();
        checkFunctionButtonsStates();
        checkRequestMessageButtonsStates();
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
        mDialog.findViewById(R.id.playPauseImageButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.stepBackwardImageButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.stepForwardImageButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.livePlaybackButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        mDialog.findViewById(R.id.runFreezeButton).setEnabled(mBackend.connected() & mBackend.wifiDeviceConnected());
        int livePlaybackButtonVisible = !mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.livePlaybackButton).setVisibility(livePlaybackButtonVisible);
        int runFreezeButtonVisible = mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.runFreezeButton).setVisibility(runFreezeButtonVisible);
        int playPauseImageResource = mBackend.paused() ? R.drawable.right_arrow : R.drawable.pause_bars;
        ((ImageButton)mDialog.findViewById(R.id.playPauseImageButton)).setImageResource(playPauseImageResource);
    }

    private void updateImagingWidgets() {
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
}
