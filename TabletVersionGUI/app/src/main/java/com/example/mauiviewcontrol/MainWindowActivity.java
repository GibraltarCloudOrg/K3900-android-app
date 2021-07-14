package com.example.mauiviewcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
//import static com.example.mauiviewcontrol.R.id.view;

public class MainWindowActivity extends AppCompatActivity implements AutomatedTestingElement {
    public static final String TAG = "Maui-viewer MainWindowActivity";
    private boolean mActivateCheckSystemStates = false;
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    final Context mContext = this;
    PatientsDialog mPatientDialog = null;
    EngineeringMenuDialog mEngineeringMenuDialog = null;
    SystemsDialog mSystemsDialog = null;
    private boolean mAreDisplayWidgetsVisible = false;
    private boolean mMauiLogoHidden = false;
    int mCurrentAutomatedTestingStep = 0;

    // for touch pad
    private ScaleGestureDetector mScaleGestureDetector;
    //private ImageView mImageView;
    /*float mLastTouchX;
    float mLastTouchY;
    final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    float mPosX;
    float mPosY;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //getSupportActionBar().setHideOnContentScrollEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.main_window_layout);
        Button runFreezeInMainWindowButton = findViewById(R.id.runFreezeInMainWindowButton);
        runFreezeInMainWindowButton.setVisibility(View.INVISIBLE);
        //showDisplayWidgets(false);
        mAreDisplayWidgetsVisible = false;
        showMauiLogo();
        //setUpHorizontalRuler();
        //setUpVerticalRuler();
        WidgetUtility.updateCineLoop(findViewById(R.id.cineLoopProgressBar));
        //if (SwitchBackEndModel.MessageTo.UnitTesting == mBackend.messageTo())
            //run();
        WidgetUtility.setUpPowerImageView(findViewById(R.id.powerImageView), mContext);
        WidgetUtility.setUpCleanScreenButton(findViewById(R.id.cleanScreenButton), mContext);
        setUpUnitOfMeasurementTextView(findViewById(R.id.unitLabelInMainWindowTextView));
        //setUpLogInLogOut();
        //setUpPatientsTabPages();
        //setUpTgcSliders();
        //setUpDlcSlider();
        setUpSliders();
        startTimers();
        if (mAutomatedTestingModel.inProcess()) {
            TestCase testCase = (TestCase) getIntent().getSerializableExtra("TestCase");
            startAutomatedTesting(testCase);
        }
        SwitchBackEndModel.MessageTo messageTo = mBackend.messageTo();
        //SwitchBackEndModel.MessageTo messageTo = (SwitchBackEndModel.MessageTo)getIntent().getSerializableExtra("SendMessageTo");
        ImageStreamer imageStreamer = ImageStreamer.getImageStreamerSingletonInstance();
        if (SwitchBackEndModel.MessageTo.UnitTesting == messageTo) {
            mActivateCheckSystemStates = true;
            imageStreamer.setActivateGetImage(false);
            imageStreamer.setEnableImaging(false);
        } else {
            mActivateCheckSystemStates = (boolean) getIntent().getSerializableExtra("ActivateGetSystemStates");
            imageStreamer.setActivateGetImage((boolean) getIntent().getSerializableExtra("ActivateGetImage"));
            //mActivateGetImage = (boolean) getIntent().getSerializableExtra("ActivateGetImage");
            imageStreamer.setEnableImaging((boolean) getIntent().getSerializableExtra("EnableDisplay"));
            //mEnableDisplay = (boolean) getIntent().getSerializableExtra("EnableDisplay");
        }
        //mImageView=(ImageView)findViewById(R.id.mainImageView);
        //mImageView=(ImageView)findViewById(R.id.imageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener(mContext));
        imageStreamer.setStopImaging(false);
        //updateImaging();
        /*Button button1 = findViewById(R.id.cineLoop1Button);
        Button button2 = findViewById(R.id.cineLoop2Button);
        button1.setWidth(100);
        button2.setWidth(200);*/

        SeekBar cineLoopSeekBar = (SeekBar) findViewById(R.id.cineLoopSeekBar);
        TextView cineProgressTextView = (TextView) findViewById(R.id.cineProgressTextView);
        // Set default value to 0
        //cineProgressTextView.setText("Progress : "+cineLoopSeekBar.getProgress() + "/" + cineLoopSeekBar.getMax());
        cineLoopSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress = 0;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        cineProgressTextView.setText("Frame : " + progress +  "/" + seekBar.getMax());
                    }
                });
    }

    private void startAutomatedTesting(TestCase testCase) {
        switch (testCase) {
            case None:
                break;
            case MainWindow:
                executeAutomatedTesting();
                break;
            case LogIn:
                LogInDialog logInDialog = new LogInDialog(this);
                logInDialog.executeAutomatedTesting();
                break;
            case Imaging:
                break;
            case Status:
                break;
            case Patient:
                showPatientsDialog(null);
                break;
            case Probe:
                showProbeDialog(null);
                break;
            case Measurement:
                showMeasurementDialog(null);
                break;
            case Preset:
                showPresetsDialog(null);
                break;
            case Save:
                break;
            case Load:
                break;
            case Modify:
                break;
            case Version:
                break;
            case CleanScreen:
                break;
            case LogOut:
                LogOutDialog logOutDialog = new LogOutDialog(this);
                logOutDialog.executeAutomatedTesting();
                break;
        }
    }

    private void startTimers() {
        Timer controllerWidgetsTimer = new Timer();
        TimerTask controllerWidgetsTimerTask = new ControllerWidgetsTimerTask(mContext);
        controllerWidgetsTimer.scheduleAtFixedRate(controllerWidgetsTimerTask, 0, BeamformerClient.getMonitoringDurationMilliSecondsForWidgets());

        Timer imagingTimer = new Timer();
        TimerTask imagingTimerTask = new ImagingTimerTask(mContext);
        //imagingTimer.scheduleAtFixedRate(imagingTimerTask, 3000, BeamformerClient.getMonitoringDurationMilliSeconds());
        imagingTimer.scheduleAtFixedRate(imagingTimerTask, 0, 3000);
    }

    public void imagePositionCenter(View view) {
        mBackend.onCenterImage();
    }

    public void imagePositionHome(View view) {
        mBackend.onHomeImage();
    }

    public void onStepBackward(View view) {
        boolean result = mBackend.onStepBackward();
        MauiToastMessage.displayToastMessage(mContext, result, "Step Backward:", Toast.LENGTH_SHORT);
    }

    public void onPlayPause(View view) {
        boolean result = mBackend.onPlayPause();
        MauiToastMessage.displayToastMessage(mContext, result, "Play/Pause:", Toast.LENGTH_SHORT);
    }

    public void onStepForward(View view) {
        boolean result = mBackend.onStepForward();
        MauiToastMessage.displayToastMessage(mContext, result, "Step Forward:", Toast.LENGTH_SHORT);
    }

    public void onRunFreeze(View view) {
        boolean result = mBackend.onToggleRunFreeze();
        MauiToastMessage.displayToastMessage(mContext, result, "Toggle Run Freeze:", Toast.LENGTH_SHORT);
        findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.GREEN);
        //findViewById(R.id.runFreezeInMainWindowButton).setVisibility(View.INVISIBLE);
        //findViewById(R.id.livePlaybackInMainWindowButton).setVisibility(View.VISIBLE);
    }

    public void onLivePlayback(View view) {
        boolean result = mBackend.onToggleLivePlayback();
        MauiToastMessage.displayToastMessage(mContext, result, "Toggle Live Playback:", Toast.LENGTH_SHORT);
        hideMauiLogo();
        //findViewById(R.id.runFreezeInMainWindowButton).setVisibility(View.VISIBLE);
        //findViewById(R.id.livePlaybackInMainWindowButton).setVisibility(View.INVISIBLE);
    }

    public void showLogInLogOutDialog(View view) {
        if(mBackend.loggedIn())
            new LogOutDialog(MainWindowActivity.this);
        else
            new LogInDialog(MainWindowActivity.this);
    }

    public void showSystemPopupMenu(View view) {
        showSystemPopupMenu(view, false, R.style.MyPopupStyle);
    }

    public void showCustomPopupMenu(View view) {
        showSystemPopupMenu(view, true, R.style.MyPopupStyle);
    }

    public void showStyledPopupMenu(View view) {
        showSystemPopupMenu(view, false, R.style.MyPopupOtherStyle);
    }

    /*public void showEngineeringPopupMenu(View view) {
        showEngineeringPopupMenu(view, false, R.style.MyPopupStyle);
    }*/

    /**
     * method responsible to show popup menu
     *
     * @param anchor      is a view where the popup will be shown
     * @param isWithIcons flag to check if icons to be shown or not
     * @param style       styling for popup menu
     */
    private void showSystemPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        popup.getMenuInflater().inflate(R.menu.system_popup_menu, popup.getMenu());
        //popup.getMenu().findItem(R.id.action_users).setVisible(false);

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //SystemConfigurationDialog systemConfigurationDialog = new SystemConfigurationDialog(MainWindowActivity.this);
                switch (menuItem.getItemId()) {
                    case R.id.action_about:
                        new AboutDialog(MainWindowActivity.this);
                        Toast.makeText(MainWindowActivity.this, "System > About clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_configuration:
                        (mSystemsDialog = new SystemsDialog(MainWindowActivity.this)).showConfigurationPage();
                        Toast.makeText(MainWindowActivity.this, "System > Configuration clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_users:
                        (mSystemsDialog = new SystemsDialog(MainWindowActivity.this)).showCreateUserPage();
                        Toast.makeText(MainWindowActivity.this, "System > Users clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_diagnostics:
                        (mSystemsDialog = new SystemsDialog(MainWindowActivity.this)).showDiagnosticsPage();
                        Toast.makeText(MainWindowActivity.this, "System > Diagnostics clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_engineering:
                        Toast.makeText(MainWindowActivity.this, "System Engineering clicked", Toast.LENGTH_SHORT).show();
                        mEngineeringMenuDialog = new EngineeringMenuDialog(MainWindowActivity.this);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    /*private void showEngineeringPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        //  The below code in try catch is responsible to display icons
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.engineering_popup_menu, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_engineering_general:
                        Toast.makeText(MainWindowActivity.this, "System > Engineering > General clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_engineering_element_masking:
                        Toast.makeText(MainWindowActivity.this, "System > Engineering > Element clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_engineering_configure_presets:
                        Toast.makeText(MainWindowActivity.this, "System > Engineering > Configure Presets clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_engineering_imaging:
                        Toast.makeText(MainWindowActivity.this, "System > Engineering > Imaging clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();

    }*/

    public void setUpUnitOfMeasurementTextView(TextView unitLabelTextView) {
        /*unitLabelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Please press and hold the Unit Label (cm or inch) to change unit of measurement.", Toast.LENGTH_LONG).show();
            }
        });
        unitLabelTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mBackend.isUnitTypeMetric())
                    WidgetUtility.displayToastMessage(mContext, mBackend.setUnitTypeInches(), "Changed unit of measurement to Inches", Toast.LENGTH_LONG);
                else
                    WidgetUtility.displayToastMessage(mContext, mBackend.setUnitTypeMetric(), "Changed unit of measurement to Metric.", Toast.LENGTH_LONG);
                return true;
            }
        });*/
    }

    public void showSystemStatusDialog(View view) {
        new SystemStatusDialog(this);
    }

    public void showPatientsDialog(View view) {
        mPatientDialog = new PatientsDialog(this);
    }

    public void showProbeDialog(View view) {
        ProbeDialog dialog = new ProbeDialog(this);
        //dialog.process();
    }

    public void showSaveLoadDialog(View view) {
        /*SaveLoadDialog dialog =*/ new SaveLoadDialog(this);
    }

    public void showMeasurementDialog(View view) {
        /*MeasurementDialog dialog =*/ //new MeasurementDialog(this);
        //dialog.process();
        MeasureImagingDialog.getSingletonInstance(this, true);
    }

    public void showPresetsDialog(View view) {
        PresetsDialog dialog = new PresetsDialog(this);
        dialog.process();
    }

    /*public void onStartMeasurement(View v) {
        displayToastMessage(mBackend.onStartMeasurement(), "Start Measurement", Toast.LENGTH_LONG);
    }

    public void onStopMeasurement(View v) {
        displayToastMessage(mBackend.onStopMeasurement(), "Stop Measurement", Toast.LENGTH_LONG);
    }*/

    /*private void setUpPatientsTabPages() {
        Button button = (Button) findViewById(R.id.patientButton);
        // add button listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setContentView(R.layout.main_window_layout);
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.patient_view);
                dialog.show();
                TabLayout patientsTabLayout = dialog.findViewById(R.id.patientsTabLayout);
                LinearLayout newPatientLinearLayout = dialog.findViewById(R.id.newPatientLinearLayout);
                LinearLayout lookUpPatientLinearLayout = dialog.findViewById(R.id.lookUpPatientLinearLayout);
                patientsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        setContentView(R.layout.main_window_layout);
                        Fragment fragment = null;
                        switch (tab.getPosition()) {
                            case 0:
                                fragment = new NewPatientFragment();
                                newPatientLinearLayout.setVisibility(View.VISIBLE);
                                lookUpPatientLinearLayout.setVisibility(View.INVISIBLE);

                                break;
                            case 1:
                                fragment = new LookUpPatientFragment();
                                newPatientLinearLayout.setVisibility(View.INVISIBLE);
                                lookUpPatientLinearLayout.setVisibility(View.VISIBLE);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + tab.getPosition());
                        }*/
                        /*FragmentManager fm = getSupportFragmentManager();
                        //FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        FrameLayout frameLayout = dialog.findViewById(R.id.patientsTabPageFrameLayout);
                        //ft.replace(R.id.simpleFrameLayout, fragment);
                        ft.replace(R.id.patientsTabPageFrameLayout, fragment);
                        //ft.replace(R.id.patientsTabPageFrameLayout, fragment);
                        //ft.addToBackStack(null);
                        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();*/
                    /*}

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                EditText patientIdEditText = dialog.findViewById(R.id.patientIdEditText);
                EditText patientFirstNameEditText = dialog.findViewById(R.id.patientFirstNameEditText);
                EditText patientLastNameEditText = dialog.findViewById(R.id.patientLastNameEditText);
                EditText examNameEditText = dialog.findViewById(R.id.examNameEditText);
                Button startExamButton = dialog.findViewById(R.id.startExamButton);
                startExamButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        K3900.ResponseMsg responseMsg = mBackend.onStartExam(examNameEditText.getText().toString(), "CommentsTest1",
                                patientIdEditText.getText().toString(), patientFirstNameEditText.getText().toString(), patientLastNameEditText.getText().toString(), "09/31/2030", "male");
                        String toastMessage;
                        if (0 == responseMsg.getCode()) {
                            toastMessage = "Exam Name: " + examNameEditText.getText() + " started.";
                            dialog.dismiss();
                        } else
                            toastMessage = responseMsg.getMsg();
                        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
                    }
                });
                Button cancelButton = dialog.findViewById(R.id.exitNewPatientButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Patient Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            }
        });
    }*/

    /*private void setUpLogInLogOut() {
        Button button = (Button) findViewById(R.id.logInButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(mBackend.loggedIn())
                    new LogOutDialog(MainWindowActivity.this);
                else
                    new LogInDialog(MainWindowActivity.this);
            }
        });
    }*/

    private void setUpSliders() {
        /*BackEndSliderElementSendingMessageVisitor backEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(0), backEndSliderElementSendingMessageVisitor, true, "Tgc1 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc1InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(1), backEndSliderElementSendingMessageVisitor, true, "Tgc2 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc2InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(2), backEndSliderElementSendingMessageVisitor, true, "Tgc3 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc3InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(3), backEndSliderElementSendingMessageVisitor, true, "Tgc4 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc4InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(4), backEndSliderElementSendingMessageVisitor, true, "Tgc5 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc5InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(5), backEndSliderElementSendingMessageVisitor, true, "Tgc6 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc6InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(6), backEndSliderElementSendingMessageVisitor, true, "Tgc7 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc7InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(7), backEndSliderElementSendingMessageVisitor, true, "Tgc8 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc8InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Tgc(8), backEndSliderElementSendingMessageVisitor, true, "Tgc9 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc9InMainWindowSeekBar);
        MauiSlider.setUpSliderListener(mContext, this, null, new Dlc(), backEndSliderElementSendingMessageVisitor, true, "Dlc value updated", ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep, R.id.dlcInMainWindowSeekBar);*/
    }

    /*private void setUpTgcSliders() {
        WidgetUtility.setUpTgcSlider(R.id.tgc1InMainWindowSeekBar, 0, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc2InMainWindowSeekBar, 1, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc3InMainWindowSeekBar, 2, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc4InMainWindowSeekBar, 3, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc5InMainWindowSeekBar, 4, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc6InMainWindowSeekBar, 5, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc7InMainWindowSeekBar, 6, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc8InMainWindowSeekBar, 7, this, null);
        WidgetUtility.setUpTgcSlider(R.id.tgc9InMainWindowSeekBar, 8, this, null);
    }*/

    /*private void setUpDlcSlider() {
        float step = (float)0.001;
        float max = (float)1.0;
        float min = (float)-1.0;
        SeekBar seekBar = findViewById(R.id.dlcInMainWindowSeekBar);
        seekBar.setMax((int) ((max - min) / step));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        System.out.println("setUpDlcSlider() --> onStopTrackingTouch() called.");
                        float value = min + (seekBar.getProgress() * step);
                        mBackend.onDlcChanged(value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        System.out.println("setUpDlcSlider() --> onStartTrackingTouch() called.");
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        System.out.println("setUpDlcSlider() --> onProgressChanged() called.");
                        //float value = min + (progress * step);
                        //K3900.BeamformerParametersResponse response = mBackend.onDlcChanged(value);
                    }
                }
        );
    }*/

    private void showMauiLogo() {
        findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.BLACK);
        final ImageView image = findViewById(R.id.mainImageView);
        image.setImageDrawable(getResources().getDrawable(R.drawable.splash));
    }

    private void hideMauiLogo() {
        if (mMauiLogoHidden)
            return;
        findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.BLACK);
        final ImageView image = findViewById(R.id.mainImageView);
        image.setImageDrawable(null);
        findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.GREEN);
        mMauiLogoHidden = true;
    }

    /*private void showDisplayWidgets(boolean show) {
        Log.d(TAG, "show: " + show);
        Log.d(TAG, "showDisplayWidgets() step 1");
        int visible = (show ? View.VISIBLE : View.INVISIBLE);
        Log.d(TAG, "showDisplayWidgets() step 2");
        TextView cinePlusTextView = findViewById(R.id.cinePlusTextView);
        Log.d(TAG, "showDisplayWidgets() step 3");
        cinePlusTextView.setVisibility(visible);
        Log.d(TAG, "showDisplayWidgets() step 4");
        ImageButton stepBackwardInMainWindowImageButton = findViewById(R.id.stepBackwardInMainWindowImageButton);
        Log.d(TAG, "showDisplayWidgets() step 5");
        stepBackwardInMainWindowImageButton.setVisibility(visible);
        Log.d(TAG, "showDisplayWidgets() step 6");
        ImageButton playPauseInMainWindowImageButton = findViewById(R.id.playPauseInMainWindowImageButton);
        Log.d(TAG, "showDisplayWidgets() step 7");
        playPauseInMainWindowImageButton.setVisibility(visible);
        Log.d(TAG, "showDisplayWidgets() step 8");
        ImageButton stepForwardInMainWindowImageButton = findViewById(R.id.stepForwardInMainWindowImageButton);
        Log.d(TAG, "showDisplayWidgets() step 9");
        stepForwardInMainWindowImageButton.setVisibility(visible);
        Log.d(TAG, "showDisplayWidgets() step 10");
        ProgressBar cineLoopProgressBar = findViewById(R.id.cineLoopProgressBar);
        Log.d(TAG, "showDisplayWidgets() step 11");
        cineLoopProgressBar.setVisibility(visible);
        Log.d(TAG, "showDisplayWidgets() step 12");
        mAreDisplayWidgetsVisible = show;
    }*/

    private void setUpHorizontalCentimetersMeasurementNumbers() {
        //TextView horizontalCentimetersMeasurementNumbersTextView = findViewById(R.id.horizontalCentimetersMeasurementNumbersTextView);
        //horizontalCentimetersMeasurementNumbersTextView.setText("8       7       6       5       4       3       2       1       0      -1      -2      -3      -4      -5      -6      -7      -8");
    }

    private void setUpVerticalCentimetersMeasurementNumbers() {
        //TextView verticalCentimetersMeasurementNumbersTextView = findViewById(R.id.verticalCentimetersMeasurementNumbersTextView);
        //verticalCentimetersMeasurementNumbersTextView.setText("\n\n  0\n\n  1\n\n  2\n\n  3\n\n  4\n\n  5\n\n  6\n\n  7\n\n  8\n\n  9\n\n10\n\n11\n\n12\n\n13\n\n14\n\n15\n\n16");
    }

    private void updateDisplayWidgets() {
        if (!mAreDisplayWidgetsVisible)
            mAreDisplayWidgetsVisible  = true;
        if (ImageStreamer.getImageStreamerSingletonInstance().getStopImaging()) {
            findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.BLACK);
            findViewById(R.id.cineLoopSeekBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.cineLoopProgressBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.cineProgressTextView).setVisibility(View.INVISIBLE);
        }
        else {
            CineLoop.update(findViewById(R.id.cineLoopSeekBar));
            WidgetUtility.updateCineLoop(findViewById(R.id.cineLoopProgressBar));
            //WidgetUtility.setCentimeterText(findViewById(R.id.unitLabelInMainWindowTextView));
            //setUpHorizontalCentimetersMeasurementNumbers();
            //setUpVerticalCentimetersMeasurementNumbers();
            ConvertibleRuler.setUpHorizontalRuler(findViewById(R.id.horizontalRulerImageView), findViewById(R.id.horizontalUnitMeasurementNumberLabelsTextView), true);
            ConvertibleRuler.setUpVerticalRuler(findViewById(R.id.verticalRulerImageView), findViewById(R.id.verticalMeasurementNumberLabelsTextView), true);
            //hideMauiLogo();
            //findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.GREEN);
            findViewById(R.id.cineLoopSeekBar).setVisibility(View.VISIBLE);
            findViewById(R.id.cineLoopProgressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.cineProgressTextView).setVisibility(View.VISIBLE);
        }
    }


    /*public void run() {
        K3900.ImageRequest.Builder imageRequest;
        long nextTime = 0;
        imageRequest = K3900.ImageRequest.newBuilder().setTime(nextTime);
        try {
            Runnable runnable = new Runnable() {
                public void run() {
                    Image img = mBackend.getImage(imageRequest);
                    if (null == img)
                        return;
                    final ImageView image = findViewById(R.id.mainImageView);
                    int size = 512*512;
                    byte[] byteArray = new byte[size];
                    int[] color = new int[size];
                    byteArray = Arrays.copyOf(img.getData(), size);
                    for (int i = 0; i < size; i++) {
                        //int b = ((int)byteArray[i])&0xff;
                        int b = ((int)(byteArray[i]))&0xff;
                        color[i] = 0xFF000000 | (b << 16) | (b << 8) | b;
                    }
                    final Bitmap bmap = Bitmap.createBitmap(color, 512, 512, Bitmap.Config.ARGB_8888);
                    image.post(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(bmap);
                        }
                    });
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getCause().getMessage());
            Log.d(TAG, le.getMessage());
        } catch (ImageNotAvailableException ie) {
            Log.d(TAG, ie.getMessage());
        }
    }*/

    private void checkRealTimeStates() {
        if (mActivateCheckSystemStates) {
            checkRealTimeButtonStates();
            checkRealTimeSliderStates();
            updateUnitLabel(findViewById(R.id.unitLabelInMainWindowTextView));
            //run();
        }
    }

    private void checkRealTimeButtonStates() {
        Button button = (Button) findViewById(R.id.logInButton);
        String buttonText = "";
        buttonText = !mBackend.loggedIn() ? "Log In" : "Log Out";
        button.setText(buttonText);

        findViewById(R.id.patientButton).setEnabled(mBackend.loggedIn() & mBackend.connected());
        //findViewById(R.id.probeButton).setEnabled(mBackend.loggedIn() & mBackend.connected());
        //findViewById(R.id.measureButton).setEnabled(mBackend.loggedIn() & mBackend.connected());
        //findViewById(R.id.presetsButton).setEnabled(mBackend.loggedIn() & mBackend.connected());
        findViewById(R.id.saveLoadButton).setEnabled(mBackend.loggedIn() & mBackend.connected());

        //Toast.makeText(this, "System Run State: " + mBackend.getRunState(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "System Playback State: " + mBackend.getPlaybackState(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "System Auto Contrast State: " + mBackend.getAutoContrastState(), Toast.LENGTH_SHORT).show();
        int livePlaybackButtonVisible = !mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        findViewById(R.id.livePlaybackInMainWindowButton).setVisibility(livePlaybackButtonVisible);
        int runFreezeButtonVisible = mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        findViewById(R.id.runFreezeInMainWindowButton).setVisibility(runFreezeButtonVisible);
        int playPauseImageResource = mBackend.paused() ? R.drawable.right_arrow : R.drawable.pause_bars;
        ((ImageButton)findViewById(R.id.playPauseInMainWindowImageButton)).setImageResource(playPauseImageResource);
    }

    private void updateDateTime() {
        //((TextView)findViewById(R.id.currentTimeTextView)).setText(LocalDateTime.now().format().toString());
        //String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        //tvDateTime.setText(currentDateTimeString);
        //String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        ((TextView)findViewById(R.id.currentTimeTextView)).setText(new SimpleDateFormat("EEE, MM-dd-yyyy    HH:mm z", Locale.getDefault()).format(new Date()));
        //((TextView)findViewById(R.id.currentTimeTextView)).setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

    private void checkRealTimeSliderStates() {
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc1InMainWindowSeekBar), mBackend.getTgcValue(0), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc2InMainWindowSeekBar), mBackend.getTgcValue(1), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc3InMainWindowSeekBar), mBackend.getTgcValue(2), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc4InMainWindowSeekBar), mBackend.getTgcValue(3), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc5InMainWindowSeekBar), mBackend.getTgcValue(4), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc6InMainWindowSeekBar), mBackend.getTgcValue(5), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc7InMainWindowSeekBar), mBackend.getTgcValue(6), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc8InMainWindowSeekBar), mBackend.getTgcValue(7), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.tgc9InMainWindowSeekBar), mBackend.getTgcValue(8), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(findViewById(R.id.dlcInMainWindowSeekBar), mBackend.getDlcValue(), ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep);
    }

    private void updateUnitLabel(TextView unitLabelTextView) {
        if (mBackend.isUnitTypeMetric())
            unitLabelTextView.setText("cm");
        else
            unitLabelTextView.setText("inch");
    }

    @Override
    public void executeAutomatedTesting() {
        if (!mAutomatedTestingModel.inProcess())
            return;

        switch (mCurrentAutomatedTestingStep++) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                mBackend.onTgcChanged(mCurrentAutomatedTestingStep-1, (float)(-0.1 * mCurrentAutomatedTestingStep));
                ((Activity) mContext).runOnUiThread(new ThreadedToasterForAutomatedTesting((AutomatedTestingElement) this, mContext, TAG + ": Tgc " + mCurrentAutomatedTestingStep + "Value changed to: " + -0.1 * mCurrentAutomatedTestingStep, 3));
                break;
            case 9:
                mBackend.onDlcChanged(1);
                ((Activity) mContext).runOnUiThread(new ThreadedToasterForAutomatedTesting((AutomatedTestingElement) this, mContext, TAG + ": Dlc Value changed to 1", 3));
                break;
        }
    }

    public class ControllerWidgetsTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public ControllerWidgetsTimerTask(Context con) {
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
                            updateDateTime();
                            if (!mBackend.isAvailable())
                                return;
                            MainWindowActivity.this.checkRealTimeStates();
                            mBackend.updateSystemState();
                            if (null != mPatientDialog)
                                mPatientDialog.checkRealtimeStates();
                            if (null != mEngineeringMenuDialog)
                                mEngineeringMenuDialog.checkRealtimeStates();
                            if (null != mSystemsDialog)
                                mSystemsDialog.checkRealtimeStates();
                            updateDisplayWidgets();
                            ImageStreamer imageStreamer = ImageStreamer.getImageStreamerSingletonInstance();
                            if (null == mEngineeringMenuDialog || null == imageStreamer.getImageView() || !mEngineeringMenuDialog.isEngineeringImagingDialogVisible())
                                imageStreamer.setImageView(findViewById(R.id.mainImageView));
                        }
                    });
                }
            }).start();
        }
    }

    public class ImagingTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public ImagingTimerTask(Context con) {
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
                            if (!mBackend.isAvailable())
                                return;
                            ImageStreamer imageStreamer = ImageStreamer.getImageStreamerSingletonInstance();
                            if (!imageStreamer.getStopTimer()) {
                                imageStreamer.updateImaging();
                                imageStreamer.setStopTimer(true);
                                //imageStreamer.setStopImaging(false);
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return TouchEvent.onTouchEvent(motionEvent, mScaleGestureDetector);
        /*mScaleGestureDetector.onTouchEvent(motionEvent);

        final int action = MotionEventCompat.getActionMasked(motionEvent);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_DOWN");
                final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                final float y = MotionEventCompat.getY(motionEvent, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);

                final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                final float y = MotionEventCompat.getY(motionEvent, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;
                mBackend.onPan((int)dx / 10, -(int)dy / 10);
                //mBackend.onTouchUpdate((int)dx / 100, (int)dy / 100);

                //invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_MOVE Position(" + mPosX + ", " + mPosY + "),   Last Touch(" +mLastTouchX + ", " + mLastTouchY + ")");

                break;
            }

            case MotionEvent.ACTION_UP: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_UP");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_CANCEL");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_POINTER_UP");
                final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                final int pointerId = MotionEventCompat.getPointerId(motionEvent, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(motionEvent, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(motionEvent, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(motionEvent, newPointerIndex);
                }
                break;
            }
        }
        return true;*/
    }
}
