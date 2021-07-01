package com.example.mauiviewcontrol;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;

import static android.net.wifi.p2p.WifiP2pManager.BUSY;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED;

public class MainActivity extends AppCompatActivity implements AutomatedTestingElement {

    public static final String EXTRA_MESSAGE = "com.example.mauiviewcontrol.MESSAGE";
    IntentFilter mIntentFilter;
    WifiP2pManager mP2pManager;
    WifiP2pManager.Channel mP2pChannel;
    //WifiManager mWifiManager;
    //int mLastClickedWifiDevice = -1;
    private int mDeviceHashCode = -1;
    //boolean mDeviceFound = false;
    //ArrayList<String> m_wifiDirectDeviceList = new ArrayList<String>();
    //ArrayAdapter<String> mWiFiDirectDeviceArrayAdapter = null;
    int mLastClickedFrameRate = -1;
    Button mPingButton;
    BroadcastReceiver mP2pReceiver;
    public static final String TAG = "Maui-viewer";
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;
    Boolean mConnected = false;
    Boolean mConnectionInProgress = false;
    Boolean mDiscovery = false;
    MainActivity mTheOne = this;
    Boolean mAvailableOccurred = false;
    Boolean mDiscoverRequestComplete = false;
    Boolean mDiscoverPeersComplete = false;
    Boolean mConnectionStartRequest = false;
    Boolean mConnectionInfoRequest = false;
    BlockingQueue<List<WifiP2pDevice>>  mPeerUpdateQueue = new LinkedBlockingQueue<>(10);
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    WifiDirectDeviceList mWifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    RelativeLayout mRelativeLayout;
    AutomatedTestingDialog mAutomatedTestingDialog = null;
    int mCurrentAutomatedTestingStep = 0;
    Resources resources;
    RelativeLayout drawLineSampleRelativeLayout;
    Button button;
    ImageView imageView;
    private boolean mActivateCheckSystemStates = true;
    private boolean mActivateGetImage = true;
    private boolean mEnableDisplay = true;
    private MainImagingDialog mMainImagingDialog = null;
    ActionButtonsImagingDialog mActionButtonsImagingDialog = null;
    private SelectProbeDialog mSelectProbeDialog = null;
    private MeasureImagingDialog mMeasureImagingDialog = null;
    private PatientsDialog mPatientDialog = null;
    private EngineeringMenuDialog mEngineeringMenuDialog = null;
    private SystemsDialog mSystemsDialog = null;
    private TgcView mTgcView = null;
    private SpeedOfSoundView mSpeedOfSoundView = null;
    private GainView mGainView = null;
    private FiltersView mFiltersView = null;
    private ContrastView mContrastView = null;
    private boolean mDebugMode = false;
    //private MauiSlider mMauiSlider1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);
        if (mBackend.connectToStaticIp())
            mBackend.connect("192.168.10.236", 50051);
            //mBackend.connect("192.168.222.112", 50051);
            //mBackend.connect("192.168.10.238", 50051);
        setUpProbePage();
        mP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        /*mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            if (!mWifiManager.isWifiEnabled()) {
                Log.d(TAG, "Enable Wifi");
                mWifiManager.setWifiEnabled(true);
            }
        }*/

        if (mP2pManager != null) {
            mP2pChannel = mP2pManager.initialize(this, getMainLooper(), null);
        } else {
            Log.d(TAG, "null p2p pointer");
        }

        mP2pReceiver = new WifiDirectBroadcastReceiver(mP2pManager,mP2pChannel,this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION);
        }

        //setUpFrameRatesSpinner();
        setUpDurationSpinner(findViewById(R.id.frameRatesSpinner), "between get images", BeamformerClient.getMonitoringDurationMilliSecondsDefaultIndexForImaging());
        setUpDurationSpinner(findViewById(R.id.durationBetweenCheckSystemStateSpinner), "between get system state", BeamformerClient.getMonitoringDurationMilliSecondsDefaultIndexForWidgets());
        setUpTimeoutSpinner(findViewById(R.id.blockingStubDeadlineSpinner), "(timeout)", BeamformerClient.getDeadlineAfterMilliSecondsDefaultIndex(), BeamformerClient.getDeadlineAfterMilliSecondsList());
        setUpTimeoutSpinner(findViewById(R.id.batchModeDeadlineSpinner), "(timeout)", BeamformerClient.getBatchModeDeadlineAfterMilliSecondsDefaultIndex(), BeamformerClient.getDeadlineAfterMilliSecondsList());
        setUpFrameRatesSpinnerListener();
        setUpDurationBetweenCheckSystemStateSpinnerListener();
        setUpBlockingStubDeadlineSpinnerListener();
        //registerReceiver(mP2pReceiver, mIntentFilter);
        //refreshWifiDirectDevicesListView();
        //setUpWifiDirectDevicesListViewListener();
        startTimer();
        mPingButton = findViewById(R.id.pingButton);
        mPingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //mBackend.setContrast(0.2f);
                    String result = mBackend.ping();
                    Log.d(TAG, result);
                    String message = "ping: " + result;
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    //K3900.SystemState state = mBackend.getSystemState();
                    String probeName = mBackend.getProbeName();
                    Log.d(TAG, "Probe Name: " + probeName);
                    //Log.d(TAG, state.getProbeName());
                    //String probeName = "Probe Name: " + state.getProbeName();
                    Toast.makeText(MainActivity.this, "Probe Name: " + probeName, Toast.LENGTH_LONG).show();
                }
                catch (LostCommunicationException le) {
                    Toast.makeText(MainActivity.this, "Ping failed, Lost Communication Exception, le: " + le.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, le.getMessage());
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Ping failed, e: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, e.getMessage());
                }
                /*waitFor(2);
                mMauiSlider1.setProgress(0);
                waitFor(3);
                mMauiSlider1.setProgress(1);
                waitFor(2);
                mMauiSlider1.setProgress(10);
                waitFor(2);
                mMauiSlider1.setProgress(100);
                waitFor(2);
                mMauiSlider1.setProgress(-1);*/
            }
        });

        new GrpcExecutor().execute(new GrpcRunnable(mTheOne));
        mRelativeLayout = findViewById(R.id.wifiDirectStatusRelativeLayout);
        mRelativeLayout.setBackgroundColor(Color.YELLOW);
        Button openGuiButton = findViewById(R.id.connectToServerButton);
        openGuiButton.setBackgroundColor(Color.YELLOW);
        //if (null != mMainImagingDialog)
            //mMainImagingDialog.setConnectionStatus(false);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        //findViewById(android.R.id.content).getRootView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //setUpTgcSliders();
        //setUpDlcSlider();
        /*ProgressBar progressBar = findViewById(R.id.sampleProgressBar);
        progressBar.setScaleY(30f);*/
        resources = getResources();
        drawLineSampleRelativeLayout = findViewById(R.id.drawLineSampleRelativeLayout);
        button = findViewById(R.id.drawLineButton);
        imageView = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = Bitmap.createBitmap(400, 700, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.RED);
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8);
                paint.setAntiAlias(true);
                int offset = 50;
                //canvas.drawLine(offset, canvas.getHeight() / 2, canvas.getWidth() - offset, canvas.getHeight() / 2, paint);
                canvas.drawLine(10, 10, 10, 600, paint);
                canvas.drawLine(0, 50, 20, 50, paint);
                canvas.drawLine(0, 100, 20, 100, paint);
                canvas.drawLine(0, 150, 20, 150, paint);
                canvas.drawLine(0, 200, 20, 200, paint);
                canvas.drawLine(0, 250, 20, 250, paint);
                canvas.drawLine(0, 300, 20, 300, paint);
                imageView.setImageBitmap(bitmap);
            }
        });
        ((CheckBox)findViewById(R.id.activateGetSystemStateCheckBox)).setChecked(mActivateCheckSystemStates);
        ((CheckBox)findViewById(R.id.activateGetImageCheckBox)).setChecked(mActivateGetImage);
        ((CheckBox)findViewById(R.id.enableDisplayWidgetsCheckBox)).setChecked(mEnableDisplay);
        startTimers();
        ImageStreamer.getImageStreamerSingletonInstance().setStopImaging(false);
        setUpGetImageDeadlineListener();
        boolean debugMode = false;
        try {
            debugMode = (boolean) getIntent().getSerializableExtra("DebugMode");
        } catch (Exception e) {
            debugMode = true;
            //debugMode = false;
        }
        setDebuggingWidgetsEnabled(debugMode);
        setUpDebugModeButton();
        showMainWindowPage(null);
        //mMainImagingDialog.start();
        //SelectLogInDialog selectLogInDialog = new SelectLogInDialog(this);
        //if (1 < WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getNumberOfMauiDevices())
        //new SelectMauiServerDialog(this);
    }

    /*private void setUpTgcMauiSliders() {
        LinearLayout getImageDeadlineLinearLayout = findViewById(R.id.getImageDeadlineLinearLayout);
        mMauiSlider1 = new MauiSlider(getApplicationContext());
        mMauiSlider1.setMin(-100);
        mMauiSlider1.setMax(100);
        getImageDeadlineLinearLayout.addView(mMauiSlider1, 1);
    }*/

    /*public void changeTxOnClicked(View view){
        String message = new String();
        try {
            message = mBackend.onChangeTxMask(0, false);
            message="Changed to true";
        } catch (Exception e) {
            message += "Mask change failed.";
        } finally {
            ((TextView) findViewById(R.id.textView)).setText(message);
        }
    }*/

    public void displayTxValue(View view){
        String maskStatus="";
        ArrayList<Boolean> maskMsgs = mBackend.onGetTxMask();
        maskStatus=Integer.toString(maskMsgs.size());
        for(int i=0;i<10;i++) {
            try {
                boolean status=maskMsgs.get(i);
                if(status){
                    maskStatus=maskStatus+ "On, ";
                }
                else{
                    maskStatus=maskStatus+ "Off, ";
                }
                //maskStatus = maskStatus+ ", " + String.valueOf(status);
            } catch (Exception e) {
                maskStatus = e.getMessage();
            } //finally {
            //((TextView) findViewById(R.id.textView3)).setText(maskStatus);
            //}
        }
        ((TextView) findViewById(R.id.textView3)).setText(maskStatus);
    }


    private void setDebuggingWidgetsEnabled(boolean enabled) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(appInfo != null) {
            if( (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0)
                mDebugMode = true;
            else
                mDebugMode = false;
        }
        int visible = enabled ? View.VISIBLE : View.INVISIBLE;
        //findViewById(R.id.batchModeButton).setVisibility(visible);
        //findViewById(R.id.resetAllButton).setVisibility(visible);
        if(!enabled) {
            TableLayout t = findViewById(R.id.mainTableLayout);
            t.removeViewAt(3);
            t.removeViewAt(2);
            t.removeViewAt(1);
        }
    }

    private void setUpDebugModeButton() {
        Button connectToServerButton = findViewById(R.id.connectToServerButton);
        connectToServerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MauiToastMessage.displayToastMessage((Context)MainActivity.this, true, "Activating Debug Mode.", Toast.LENGTH_LONG);
                showDebugModeMain();
                return true;
            }
        });
    }

    public void showDebugModeMain() {
        try {
            mBackend.setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
            Intent intent = new Intent(this, com.example.mauiviewcontrol.MainActivity.class);
            intent.putExtra("DebugMode", true);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Exception at showDebugModeMain", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpGetImageDeadlineListener() {
        EditText getImageDeadlineEditTextNumberDecimal = (EditText)findViewById(R.id.getImageDeadlineEditTextNumberDecimal);

        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                BeamformerClient.setGetImageDeadLineInMilliSeconds(Integer.valueOf(s.toString()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };
        getImageDeadlineEditTextNumberDecimal.addTextChangedListener(textWatcher);
        getImageDeadlineEditTextNumberDecimal.setText(Integer.valueOf(BeamformerClient.getGetImageDeadLineInMilliSeconds()).toString());
    }

    public void onTestCommunication(View view) {
        for (int i = 0; i < 10000; ++i) {
            mBackend.onHomeImage();
            waitInMilliSecondsFor(1);
            //Toast.makeText(MainActivity.this, LocalDateTime.now() + "Sent onHomeImage()", Toast.LENGTH_SHORT).show();
        }
    }

    public void enableDisplayWidgets(View view) {
        mEnableDisplay = !mEnableDisplay;
        ((CheckBox)(findViewById(R.id.enableDisplayWidgetsCheckBox))).setChecked(mEnableDisplay);
    }

    public void writeFileExternalStorage(View v) {
        //File fileDir = new File(MainActivity.this.getFilesDir(), "text");
        File fileDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "MyMauiFile");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        try {
            /*String filename = "myMauifile";
            String string = mBackend.getParameterValuesInString();
            FileOutputStream outputStream;
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();*/
            File filePath = new File(fileDir, "Maui-K3900-ParameterValues" + /*LocalDateTime.now() +*/ ".txt");
            boolean result = Files.deleteIfExists(filePath.toPath());
            FileWriter writer = new FileWriter(filePath, false);
            writer.append(mBackend.getParameterValuesInString());
            writer.flush();
            writer.close();
            showFileSavedDialog(filePath.getAbsoluteFile().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Exception at writeFileExternalStorage: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showFileSavedDialog(String filePath) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                    default:
                        break;
                }
            }
        };
        String additionalInfo = new String("\n\nTry opening \'My Files\' app, and check \'InternalStorage/Documents/MyMauiFile\'");
        //String additionalInfo = new String("\n\n This text file may also be accessible through Maui's One Drive:\n'C:\\Users\\YourUserAccountName\\OneDrive - MAUI Imaging, Inc\\Documents\\AndroidStudio\\DeviceExplorer\\samsung-sm_t860-R52MA11GJDH\\data\\data\\com.example.mauiviewcontrol\\files\\text'");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("File saved to: " + filePath + additionalInfo).setPositiveButton("OK", dialogClickListener).show();
        Toast.makeText(MainActivity.this, "Saved as a text file into Internal Storage.", Toast.LENGTH_LONG).show();
    }

    public void activateGetImage(View view) {
        mActivateGetImage = !mActivateGetImage;
        ((CheckBox)(findViewById(R.id.activateGetImageCheckBox))).setChecked(mActivateGetImage);
    }

    public void activateGetSystemState(View view) {
        mActivateCheckSystemStates = !mActivateCheckSystemStates;
        ((CheckBox)(findViewById(R.id.activateGetSystemStateCheckBox))).setChecked(mActivateCheckSystemStates);
    }

    private void startTimer() {
        Timer timer = new Timer();
        TimerTask controllerWidgetsTimerTask = new MainActivity.MonitoringTimerTask(this);
        timer.scheduleAtFixedRate(controllerWidgetsTimerTask, 0, 2 * 1000);
    }

    /*private void setUpWifiDirectDevicesListViewListener() {
        ListView wifiDirectsListView = findViewById(R.id.wifiDirectsListView);
        wifiDirectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WidgetUtility.changeListViewSelectedItemColor(parent, view, position, mLastClickedWifiDevice);
                if (position != mLastClickedWifiDevice) {
                    try {
                        disconnect();
                    } catch (NullPointerException le) {
                        Log.d(TAG, "Failed to disconnect");
                    }
                    //mConnected = false;
                    //connect();
                    mLastClickedWifiDevice = position;
                    WifiDirectDeviceList wifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
                    wifiDirectDeviceList.setSelected(wifiDirectDeviceList.getDeviceName(mLastClickedWifiDevice));
                    String selected = wifiDirectDeviceList.getString(mLastClickedWifiDevice);
                    Toast.makeText(MainActivity.this, "Selected: " + selected, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "WiFi Direct Device Hash Code: " + wifiDirectDeviceList.getSelectedDevice().getDeviceHashCode() + selected, Toast.LENGTH_LONG).show();
                    //mConnected = false;
                    //mAvailableOccurred = false;
                    //connect();
                }
            }
        });
        wifiDirectsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayList<String> list = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getStringList();
        mWiFiDirectDeviceArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_text_view, R.id.customTextView, list);
        wifiDirectsListView.setAdapter(mWiFiDirectDeviceArrayAdapter);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    public void refreshWiFiDirectDeviceList(View view) {
        mWiFiDirectDeviceArrayAdapter.notifyDataSetChanged();
    }

    public void disconnectWiFiDirect(View view) {
        disconnect();
        //mLastClickedWifiDevice = -1;
    }*/

        /*private void setUpFrameRatesSpinner() {
            setUpDurationSpinner(findViewById(R.id.frameRatesSpinner), "between get images", BeamformerClient.getMonitoringDurationMilliSecondsDefaultIndexForImaging());
        }*/

        private void setUpDurationSpinner(Spinner spinner, String text, int defaultIndex) {
        ArrayList<String> list = new ArrayList<>();
        //String secondsUnitString = " milliseconds per frame";
        String secondsUnitString = " milliseconds " + text;
        for (int index = 0; index < BeamformerClient.getMonitoringDurationMilliSecondsList().length; ++index) {
            int monitoringDuration = BeamformerClient.getMonitoringDurationMilliSecondsList()[index];
            if (1000 <= BeamformerClient.getMonitoringDurationMilliSecondsList()[index]) {
                secondsUnitString = " seconds " + text;
                //secondsUnitString = " seconds per frame";
                monitoringDuration = monitoringDuration / 1000;
            }
            list.add(monitoringDuration + secondsUnitString);
            //list.add(monitoringDuration + secondsUnitString + "\n" + (float) 1000 / BeamformerClient.getMonitoringDurationMilliSecondsList()[index] + " [frame(s) per second]");
        }
        spinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list));
        //final int defaultMillisecondsIndex = 4;
        //frameRatesSpinner.setSelection(defaultMillisecondsIndex);
        spinner.setSelection(defaultIndex);
        //BeamformerClient.setMonitoringDurationMilliSecondsForImaging(BeamformerClient.getMonitoringDurationMilliSecondsList()[defaultIndex]);
    }

    private void setUpTimeoutSpinner(Spinner spinner, String text, int defaultIndex, int[] listInMilliSeconds) {
        ArrayList<String> list = new ArrayList<>();
        String secondsUnitString = " milliseconds " + text;
        for (int index = 0; index < listInMilliSeconds.length; ++index) {
            int timeout = listInMilliSeconds[index];
            if (1000 <= listInMilliSeconds[index]) {
                secondsUnitString = " seconds " + text;
                //secondsUnitString = " seconds per frame";
                timeout = timeout / 1000;
            }
            list.add(timeout + secondsUnitString);
            //list.add(monitoringDuration + secondsUnitString + "\n" + (float) 1000 / BeamformerClient.getMonitoringDurationMilliSecondsList()[index] + " [frame(s) per second]");
        }
        spinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, list));
        //final int defaultMillisecondsIndex = 4;
        //frameRatesSpinner.setSelection(defaultMillisecondsIndex);
        spinner.setSelection(defaultIndex);
        //BeamformerClient.setMonitoringDurationMilliSecondsForImaging(BeamformerClient.getMonitoringDurationMilliSecondsList()[defaultIndex]);
    }

    private void setUpFrameRatesSpinnerListener() {
        Spinner frameRatesSpinner = findViewById(R.id.frameRatesSpinner);
        frameRatesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                BeamformerClient.setMonitoringDurationMilliSecondsForImaging(BeamformerClient.getMonitoringDurationMilliSecondsList()[position]);
                Toast.makeText(MainActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void setUpDurationBetweenCheckSystemStateSpinnerListener() {
        Spinner durationBetweenCheckSystemStateSpinner = findViewById(R.id.durationBetweenCheckSystemStateSpinner);
        durationBetweenCheckSystemStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                BeamformerClient.setMonitoringDurationMilliSecondsForWidgets(BeamformerClient.getMonitoringDurationMilliSecondsList()[position]);
                Toast.makeText(MainActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void setUpBlockingStubDeadlineSpinnerListener() {
        Spinner blockingStubDeadlineSpinner = findViewById(R.id.blockingStubDeadlineSpinner);
        blockingStubDeadlineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                BeamformerClient.setMonitoringDurationMilliSecondsForWidgets(BeamformerClient.getMonitoringDurationMilliSecondsList()[position]);
                Toast.makeText(MainActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void setSaveButtonHidden(View view){
        ElementMaskingSetup.setSaveButtonHidden(!ElementMaskingSetup.sSaveButtonHidden);
    }

    private void setUpProbePage() {
        /*TabLayout testTabLayout = findViewById(R.id.testTabLayout);
        testTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new FirstFragment();
                        break;
                    case 1:
                        fragment = new SecondFragment();
                        break;
                    case 2:
                        fragment = new FirstFragment();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tab.getPosition());
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    /*private void setUpTgcSliders() {
        setUpTgcSlider(R.id.tgc1SeekBar, 0);
        setUpTgcSlider(R.id.tgc2SeekBar, 1);
        setUpTgcSlider(R.id.tgc3SeekBar, 2);
        setUpTgcSlider(R.id.tgc4SeekBar, 3);
        setUpTgcSlider(R.id.tgc5SeekBar, 4);
        setUpTgcSlider(R.id.tgc6SeekBar, 5);
        setUpTgcSlider(R.id.tgc7SeekBar, 6);
        setUpTgcSlider(R.id.tgc8SeekBar, 7);
        setUpTgcSlider(R.id.tgc9SeekBar, 8);
    }

    private void setUpTgcSlider(int sliderId, int index) {
        float step = (float)0.001;
        float max = (float)1.0;
        float min = (float)-1.0;
        SeekBar seekBar = findViewById(sliderId);
        //seekBar.setOutlineAmbientShadowColor(Color.WHITE);
        //seekBar.setOutlineSpotShadowColor(Color.WHITE);
        seekBar.setBackgroundColor(Color.BLUE);
        seekBar.setMax((int) ((max - min) / step));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        float value = min + (progress * step);
                        boolean result = mBackend.onTgcChanged(index, value);
                    }
                }
        );
    }

    private void setUpDlcSlider() {
        float step = (float)0.001;
        float max = (float)1.0;
        float min = (float)-1.0;
        SeekBar seekBar = findViewById(R.id.dlcSeekBar);
        seekBar.setMax((int) ((max - min) / step));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        float value = min + (progress * step);
                        mBackend.onDlcChanged(value);
                    }
                }
        );
    }*/

    public void resetAll(View view) {
        mBackend.resetAll();
    }

    public void showLogInPage(View view) {
        Intent intent = new Intent(this, com.example.mauiviewcontrol.LogInActivity.class);
        startActivity(intent);
    }

    public void showAlphaVersionGui(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("org.qtproject.example.MauiGuiMaster");
        //launchIntent.putExtras(extras);
        //launchIntent.arg
        //Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
        if (launchIntent != null)
            startActivity(launchIntent);
        else
            Toast.makeText(MainActivity.this, "There is no package available in android", Toast.LENGTH_LONG).show();
    }

    public void showBetaVersionGui(View view) {
        try {
            mBackend.setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
            Intent intent = new Intent(this, com.example.mauiviewcontrol.MainWindowActivity.class);
            //intent.putExtra("SendMessageTo", SwitchBackEndModel.MessageTo.BeamformerClient);
            intent.putExtra("TestCase", TestCase.None);
            intent.putExtra("ActivateGetSystemStates", mActivateCheckSystemStates);
            intent.putExtra("ActivateGetImage", mActivateGetImage);
            intent.putExtra("EnableDisplay", mEnableDisplay);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Exception at showBetaVersionGui", Toast.LENGTH_LONG).show();
        }
    }

    public void showBetaVersionGuiWithUnitTestMode(View view) {
        mBackend.setMessageTo(SwitchBackEndModel.MessageTo.UnitTesting);
        Intent intent = new Intent(this, com.example.mauiviewcontrol.MainWindowActivity.class);
        //intent.putExtra("SendMessageTo", SwitchBackEndModel.MessageTo.UnitTesting);
        intent.putExtra("TestCase", TestCase.None);
        startActivity(intent);
    }

    public void showWiFiDirectDeviceList(View view) {
        new SelectWiFiDirectDeviceDialog(this);
    }

    public void showMainWindowPage(View view) {
        mBackend.setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
        //mFullMenuImagingDialog = new FullMenuImagingDialog(this, mEnableDisplay);
        mMainImagingDialog = new MainImagingDialog(this, mEnableDisplay, mDebugMode);
        SelectLogInDialog selectLogInDialog = new SelectLogInDialog(this);
        //if (1 == mWifiDirectDeviceList.getNumberOfMauiDevices())
            //mWifiDirectDeviceList.setSelected(mWifiDirectDeviceList.getDeviceName(0));
        //else
            showSelectMauiServerDialog(null);
        /*try {
            mBackend.setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
            Intent intent = new Intent(this, com.example.mauiviewcontrol.MainWindowActivity.class);
            //intent.putExtra("SendMessageTo", SwitchBackEndModel.MessageTo.BeamformerClient);
            intent.putExtra("TestCase", TestCase.None);
            intent.putExtra("ActivateGetSystemStates", mActivateCheckSystemStates);
            intent.putExtra("ActivateGetImage", mActivateGetImage);
            intent.putExtra("EnableDisplay", mEnableDisplay);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "reloading main window...", Toast.LENGTH_LONG).show();
            showMainWindowPage(null);
        }*/
    }

    public void showFloatingActionButtonsImagingDialog(View view) {
        //mBackend.setMessageTo(SwitchBackEndModel.MessageTo.UnitTesting);
        mActionButtonsImagingDialog = new ActionButtonsImagingDialog(this, mEnableDisplay, mDebugMode);
        //SelectLogInDialog selectLogInDialog = new SelectLogInDialog(this);
        //showSelectMauiServerDialog(null);
    }

    public void showSelectMauiServerDialog(View view) {
        new SelectMauiServerDialog(this);
    }

    public void showElementMaskingDialog(View view){
        //Toast.makeText(getApplicationContext(),"Loading... please wait", Toast.LENGTH_SHORT).show();
        EngineeringSettingsDialog engineeringSettingsDialog=new EngineeringSettingsDialog(getApplicationContext());
        engineeringSettingsDialog.showDialog(getApplicationContext(), 0);
    }

    /*public void showImagePositionDialog(View view){
        ImagePositionDialog imagePositionDialog=new ImagePositionDialog(this);
        imagePositionDialog.showDialog();
    }*/

    public void showMainWindowWithUnitTestingMode(View view) {
        mBackend.setMessageTo(SwitchBackEndModel.MessageTo.UnitTesting);
        //mFullMenuImagingDialog = new FullMenuImagingDialog(this, mEnableDisplay);
        mMainImagingDialog = new MainImagingDialog(this, mEnableDisplay, mDebugMode);
        /*Intent intent = new Intent(this, com.example.mauiviewcontrol.MainWindowActivity.class);
        //intent.putExtra("SendMessageTo", SwitchBackEndModel.MessageTo.UnitTesting);
        intent.putExtra("TestCase", TestCase.None);
        startActivity(intent);*/
    }

    public void showMainWindowForAutomatedTesting(TestCase testCase) {
        Intent intent = new Intent(this, com.example.mauiviewcontrol.MainWindowActivity.class);
        intent.putExtra("TestCase", testCase);
        startActivity(intent);
    }

    public void showSystemStatusDialog(View view) {
        new SystemStatusDialog(this);
    }

    public void showPatientsDialog(View view) {
        mPatientDialog = new PatientsDialog(this);
    }

    public void showProbeDialog(View view) {
        //ProbeDialog dialog = new ProbeDialog(this);
        mSelectProbeDialog = new SelectProbeDialog(this);
    }

    public void showSaveLoadDialog(View view) {
        /*SaveLoadDialog dialog =*/ new SaveLoadDialog(this);
    }

    public void showMeasurementDialog(View view) {
        /*MeasurementDialog dialog =*/ //new MeasurementDialog(this);
        //dialog.process();
        mMeasureImagingDialog = new MeasureImagingDialog(this, mEnableDisplay);
    }

    public void showPresetsDialog(View view) {
        PresetsDialog dialog = new PresetsDialog(this);
        dialog.process();
    }

    public void showPatientsPage(View view) {
        Intent intent = new Intent(this, com.example.mauiviewcontrol.PatientsActivity.class);
        startActivity(intent);
    }

    public void showAutomatedTestingDialog(View view) {
        mAutomatedTestingDialog = new AutomatedTestingDialog(this);
    }

    public void onAutomatedTestingStartedWithServer(View view) {
        mBackend.setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
        mAutomatedTestingModel.setInProcess(true);
        //if (mAutomatedTestingDialog.isAutomatedLogInTestingTurnedOn())
            //startAutomatedLogIn();
        executeAutomatedTesting();
        //stopAutomatedTesting();
    }

    public void onAutomatedTestingStartedWithoutServer(View view) {
        mBackend.setMessageTo(SwitchBackEndModel.MessageTo.UnitTesting);
        mAutomatedTestingModel.setInProcess(true);
        //if (mAutomatedTestingDialog.isAutomatedLogInTestingTurnedOn())
        //startAutomatedLogIn();
        executeAutomatedTesting();
        //stopAutomatedTesting();
    }

    public void onStopAutomatedTesting(View view) {
        mAutomatedTestingModel.setInProcess(false);
    }

    public void onTgcCenter(View view) {
        boolean result = mBackend.onTgcCenter();
        MauiToastMessage.displayToastMessage(this, result, "TGC Center: ", Toast.LENGTH_LONG);
        //for (int index = 0; index < 9; ++index)
            //mBackend.onTgcChanged(index, 0);
    }

    public void onDlcCenter(View view) {
        mBackend.onDlcChanged(0);
    }

    public void onTgc(View view) {
        mTgcView = new TgcView(this, mActionButtonsImagingDialog.getBeamformerParameterValueLowerTextView(), mActionButtonsImagingDialog.getBeamformerParameterValueTextView());
    }

    public void onSpeedOfSound(View view) {
        mSpeedOfSoundView = new SpeedOfSoundView(this);
    }

    public void onGain(View view) {
        mGainView = new GainView(this, mActionButtonsImagingDialog.getBeamformerParameterValueLowerTextView(), mActionButtonsImagingDialog.getBeamformerParameterValueTextView());
    }

    public void onFilters(View view) {
        mFiltersView = new FiltersView(this);
    }

    public void onContrast(View view) {
        mContrastView = new ContrastView(this);
    }

    public void onPinch2Zoom(View view) {
        Intent intent = new Intent(this, com.example.mauiviewcontrol.Pinch2ZoomActivity.class);
        startActivity(intent);
    }

    private void startAutomatedLogIn() {
        //if (!mBackend.automatedTestingInProcess())
            //return;

        runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context)this, "Inputting username: 'usernameTest'", 10));
        /*++mCurrentAutomatedTestingStep;
        //showMainWindowPage(null);
        runOnUiThread(new Toaster(this, (Context)this, "Inputting password: 'passwordTest'", Toast.LENGTH_LONG));
        ++mCurrentAutomatedTestingStep;
        runOnUiThread(new Toaster(this, (Context)this, "Inputting Nothing....'", Toast.LENGTH_LONG));
        ++mCurrentAutomatedTestingStep;*/
        /*Toast.makeText(this, "Inputting username: 'usernameTest'", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.finish();
            }
        }, 20000);
        //waitFor(10);
        showMainWindowPage(null);
        Toast.makeText(this, "Inputting password: 'passwordTest'", Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void executeAutomatedTesting() {
        if (!mAutomatedTestingModel.inProcess())
            return;

        switch (mCurrentAutomatedTestingStep++) {
            case 0:
                //System.out.println(LocalDateTime.now() + ": executeAutomatedTesting() called, with case 0");
                //++mCurrentAutomatedTestingStep;
                if (mAutomatedTestingModel.getMainWindowOn()) {
                    showMainWindowForAutomatedTesting(TestCase.MainWindow);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for MainWindow", 30));
                }
                else
                    executeAutomatedTesting();
                break;
            case 1:
                if (mAutomatedTestingModel.getLogInOn()) {
                    showMainWindowForAutomatedTesting(TestCase.LogIn);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Log In", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 2:
                if (mAutomatedTestingModel.getImagingOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Imaging);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Imaging", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 3:
                if (mAutomatedTestingModel.getStatusOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Status);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Status", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 4:
                if (mAutomatedTestingModel.getPatientOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Patient);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Patient", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 5:
                if (mAutomatedTestingModel.getProbeOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Probe);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Probe", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 6:
                if (mAutomatedTestingModel.getMeasurementOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Measurement);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Measurement", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 7:
                if (mAutomatedTestingModel.getPresetOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Preset);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Preset", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 8:
                if (mAutomatedTestingModel.getSaveOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Save);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Save", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 9:
                if (mAutomatedTestingModel.getLoadOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Load);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Load", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 10:
                if (mAutomatedTestingModel.getModifyOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Modify);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Modify", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 11:
                if (mAutomatedTestingModel.getVersionOn()) {
                    showMainWindowForAutomatedTesting(TestCase.Version);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Version", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 12:
                if (mAutomatedTestingModel.getCleanScreenOn()) {
                    showMainWindowForAutomatedTesting(TestCase.CleanScreen);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Clean Screen", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            case 13:
                if (mAutomatedTestingModel.getLogOutOn()) {
                    showMainWindowForAutomatedTesting(TestCase.LogOut);
                    runOnUiThread(new ThreadedToasterForAutomatedTesting(this, (Context) this, "Automated Testing for Log Out", 20));
                }
                else
                    executeAutomatedTesting();
                break;
            default:
                mAutomatedTestingModel.setInProcess(false);
                Toast.makeText(MainActivity.this, "Completed All Test Cases.", Toast.LENGTH_LONG).show();
                break;
        }
        //++mCurrentAutomatedTestingStep;
    }

    public void waitInMilliSecondsFor(int milliSeconds) {
        milliSeconds = milliSeconds < 0 ? 0 : milliSeconds;
        while (--milliSeconds >= 0) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @Nonnull String[] permissions,
                                           @Nonnull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION) {
            //case PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION:
            if  (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Fine location permission is not granted!");
                finish();
            }
        }
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            Log.d(TAG, "onPeersAvailable?");
            List<WifiP2pDevice> p2pPeers = new ArrayList<>(peerList.getDeviceList());
            //p2pPeers.addAll(peerList.getDeviceList());
            try {
                mPeerUpdateQueue.put(p2pPeers);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    WifiP2pManager.ActionListener actionConnectListener = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {

            Log.d(TAG, "Success Attempting to Connect");
            mConnectionInProgress = true;
            mConnectionStartRequest = true;
        }

        @Override
        public void onFailure(int reason) {
            Log.d(TAG, "Failed Attempting to Connect");
            mConnectionInProgress = false;
            mConnectionStartRequest = true;
        }
    };

    void processPeer(List<WifiP2pDevice> peers) {

        //BeamformerClient.setWifiDirectListChanged(true);
        //Toast.makeText(MainActivity.this, "processPeer() called.", Toast.LENGTH_LONG).show();
        boolean device_found = false;
        //ArrayList<String> list = new ArrayList<String>();

        //disconnect();
        //WifiDirectDeviceList wifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
        mWifiDirectDeviceList.clearAll();

        //if (wifiDirectDeviceList.getCurrentIndex() >= peers.size())
            //wifiDirectDeviceList.setCurrentIndex(-1);
        for (WifiP2pDevice device : peers) {

            //final WifiP2pDevice device = d;
            mWifiDirectDeviceList.add(device);
            Log.d(TAG, "p2p device: " + device.deviceName + ", heap address: " + device);
            //if (!device.deviceName.startsWith("DIRECT-K3900"))
            if (!device.deviceName.startsWith("MAUI"))
                continue;

            String msg = "Discovered PEER : " + device.deviceName + " = ";

            switch (device.status) {
                case WifiP2pDevice.CONNECTED:
                    msg += "mConnected";
                    break;
                case WifiP2pDevice.INVITED:
                    msg += "mInvited";
                    break;
                case WifiP2pDevice.FAILED:
                    msg += "mFailed";
                    break;
                case WifiP2pDevice.AVAILABLE:
                    msg += "mAvailable";
                    mAvailableOccurred = true;
                    break;
                case WifiP2pDevice.UNAVAILABLE:
                    msg += "mUnavailable";
                default:
                    msg += "mUnknown";
                    break;
            }
            Log.d(TAG, msg);

            //connect();

            //if (device.deviceName.compareTo("DIRECT-K3900") == 0) {
            if (0 == device.deviceName.compareTo(mWifiDirectDeviceList.getSelectedDeviceName())) {
                device_found = true;
                if (device.status ==  WifiP2pDevice.AVAILABLE && !mConnected) {
                    if (!mConnectionInProgress) {
                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        config.groupOwnerIntent = 0;
                        Log.d(TAG, "Trying to connect to " + device.deviceAddress + " " + device.deviceName + " Owner: " + device.isGroupOwner());
                        mConnectionStartRequest = false;
                        mP2pManager.connect(mP2pChannel, config, actionConnectListener);
                        while (!mConnectionStartRequest) {
                            SystemClock.sleep(1000);
                        }
                    }
                }
                else if (device.status ==  WifiP2pDevice.CONNECTED && !mAvailableOccurred) {
                    if (!mConnectionInProgress) {
                        this.mP2pManager.requestConnectionInfo(this.mP2pChannel, this.connectionInfoListener);
                        while(!this.mConnectionInfoRequest)
                            SystemClock.sleep(500);
                        mAvailableOccurred = true;
                    }
                }
            }
            else if (0 == device.deviceName.length()) {
                mP2pManager.removeGroup(mP2pChannel, null);
                mConnected = false;  // ToDo: do we need this line?  Maybe not???
            }
        }

        if (!device_found) {
            mConnectionInProgress = false;
        }
    }

    /*private void connect() {
        WifiDirectDeviceList wifiDirectDeviceList = WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance();
        WifiDirectDevice wifiDirectDevice = wifiDirectDeviceList.getSelectedDevice();
        if (null == wifiDirectDevice)
            return;
        WifiP2pDevice device = wifiDirectDevice.getWifiP2pDevice();
        if (null == device)
            return;
        if (wifiDirectDevice.getDeviceHashCode() != mDeviceHashCode) {
            BeamformerClient.setWifiDirectListChanged(true);
            String message = "WiFi Direct Device Hash Code Changed, " + LocalDateTime.now() + " name: " + device.deviceName + " OLD: " + mDeviceHashCode + " NEW: " + wifiDirectDevice.getDeviceHashCode();
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
            mDeviceHashCode = wifiDirectDevice.getDeviceHashCode();
        }
        //if (device.deviceName.compareTo("DIRECT-K3900") == 0) {
        //if (device.deviceName.startsWith("DIRECT-K3900")) {
        if (0 == device.deviceName.compareTo(wifiDirectDeviceList.getSelectedDeviceName())) {
            mDeviceFound = true;
            if (device.status == WifiP2pDevice.AVAILABLE && !mConnected) {
                if (!mConnectionInProgress) {
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = device.deviceAddress;
                    config.groupOwnerIntent = 0;
                    Log.d(TAG, "Trying to connect to " + device.deviceAddress + " " + device.deviceName + " Owner: " + device.isGroupOwner());
                    mConnectionStartRequest = false;
                    mP2pManager.connect(mP2pChannel, config, actionConnectListener);
                    while (!mConnectionStartRequest) {
                        SystemClock.sleep(1000);
                    }
                }
            } else if (device.status == WifiP2pDevice.CONNECTED && !mAvailableOccurred) {
                if (!mConnectionInProgress) {
                    this.mP2pManager.requestConnectionInfo(this.mP2pChannel, this.connectionInfoListener);
                    while (!this.mConnectionInfoRequest)
                        SystemClock.sleep(500);
                    mAvailableOccurred = true;
                }
            }
        }
    }*/

    WifiP2pManager.ActionListener actionPeerListener = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "Started discovery");
            mDiscoverPeersComplete = true;
        }

        @Override
        public void onFailure(int i) {
            if (i == BUSY) {
                Log.d(TAG, "Discovery Busy");
            }
            else {
                Log.d(TAG, "Discovery Error");
            }
            mDiscoverPeersComplete = true;
        }
    };

    WifiP2pManager.ActionListener actionDiscoverStop = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "DISCOVERY SHOULD BE STOPPED");
        }

        @Override
        public void onFailure(int i) {
            if (i == BUSY) {
                Log.d(TAG, "Discovery Busy");
            }
            else {
                Log.d(TAG, "Discovery Error");
            }
        }
    };

    WifiP2pManager.DiscoveryStateListener discoveryStateListener = new WifiP2pManager.DiscoveryStateListener() {
        @Override
        public void onDiscoveryStateAvailable(int state) {

            if (state == WIFI_P2P_DISCOVERY_STARTED) {
                Log.d(TAG, "Discovery started");
                mDiscovery = true;
            } else if (state == WIFI_P2P_DISCOVERY_STOPPED) {
                Log.d(TAG, "Discovery stopped");
                mDiscovery = false;
            }
            mDiscoverRequestComplete = true;
        }
    };



    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if (info.groupFormed){
                if (!mConnected) {
                    mConnected = true;
                    mRelativeLayout.setBackgroundColor(Color.GREEN);
                    Button openGuiButton = findViewById(R.id.connectToServerButton);
                    openGuiButton.setBackgroundColor(Color.GREEN);
                    Log.d(TAG, "groupOwnerAddress.getHostAddress(): " + groupOwnerAddress.getHostAddress());
                    mBackend.connect(groupOwnerAddress.getHostAddress(), 50051);
                    //mGrpcChannel = ManagedChannelBuilder.forAddress(groupOwnerAddress.getHostAddress(), 50051).usePlaintext().build();
                    //mBlockingStub = BeamformerGrpc.newBlockingStub(mGrpcChannel);
                    Log.d(TAG, "Group Formed, Stub created");
                    BeamformerClient.setWifiDirectListChanged(false);
                    //if (null != mMainImagingDialog)
                        //mMainImagingDialog.setConnectionStatus(true);
                }
            }
            else {
                if (mConnected)
                    disconnect();
            }
            mConnectionInfoRequest = true;
        }
    };

    private void disconnect() {
        Log.d(TAG, "Lost Connection");
        mConnected = false;
        mRelativeLayout.setBackgroundColor(Color.RED);
        Button openGuiButton = findViewById(R.id.connectToServerButton);
        openGuiButton.setBackgroundColor(Color.RED);
        mConnectionInProgress = false;
        //mDeviceFound = false;
        //mAvailableOccurred = false;
        //if (mBackend.connected())
            mBackend.disconnect();
        //if (null != mMainImagingDialog)
            //mMainImagingDialog.setConnectionStatus(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");
        registerReceiver(mP2pReceiver, mIntentFilter);
        //mP2pManager.cancelConnect(mP2pChannel, actionConnectListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pause");
        unregisterReceiver(mP2pReceiver);
    }

    private void checkRealTimeStates() {
        //if (mConnected && mBackend.connected() /*&& mDeviceFound*/ /*&& -1 != mLastClickedWifiDevice*/) {
            /*int playbackBufferSize = mBackend.getPlaybackBufferSize();
            int playbackStart = mBackend.getPlaybackStart();
            int currentFrame = mBackend.getCurrentFrame();
            int playbackSize = mBackend.getPlaybackSize();
            String message = LocalDateTime.now() + ":  playbackBufferSize: " + playbackBufferSize + ", playbackStart: " + playbackStart + ", currentFrame: " + currentFrame + ", playbackSize: " + playbackSize;
            Log.d(TAG, message);
            //Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }*/
        /*if (WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getSize() <= mLastClickedWifiDevice) {
            mDeviceFound = false;
            mLastClickedWifiDevice = -1;
        }
        mWiFiDirectDeviceArrayAdapter.notifyDataSetChanged();*/
    }

    private void startTimers() {
        Timer controllerWidgetsTimer = new Timer();
        TimerTask controllerWidgetsTimerTask = new WidgetsTimerTask(this);
        controllerWidgetsTimer.scheduleAtFixedRate(controllerWidgetsTimerTask, 0, BeamformerClient.getMonitoringDurationMilliSecondsForWidgets());

        Timer imagingTimer = new Timer();
        TimerTask imagingTimerTask = new MainImagingTimerTask(this);
        //imagingTimer.scheduleAtFixedRate(imagingTimerTask, 3000, BeamformerClient.getMonitoringDurationMilliSeconds());
        imagingTimer.scheduleAtFixedRate(imagingTimerTask, 0, 3000);
    }

    public void imagePositionCenter(View view) {
        boolean result = mBackend.onCenterImage();
        MauiToastMessage.displayToastMessage(this, result, "Image Position Center: ", Toast.LENGTH_LONG);
    }

    public void imagePositionHome(View view) {
        boolean result = mBackend.onHomeImage();
        MauiToastMessage.displayToastMessage(this, result, "Image Position Home: ", Toast.LENGTH_LONG);
    }

    public void onStepBackward(View view) {
        boolean result = mBackend.onStepBackward();
        MauiToastMessage.displayToastMessage(this, result, "Step Backward:", Toast.LENGTH_SHORT);
    }

    public void onPlayPause(View view) {
        boolean result = mBackend.onPlayPause();
        MauiToastMessage.displayToastMessage(this, result, "Play/Pause:", Toast.LENGTH_SHORT);
    }

    public void onStepForward(View view) {
        boolean result = mBackend.onStepForward();
        MauiToastMessage.displayToastMessage(this, result, "Step Forward:", Toast.LENGTH_SHORT);
    }

    public void onRunFreeze(View view) {
        boolean result = mBackend.onToggleRunFreeze();
        MauiToastMessage.displayToastMessage(this, result, "Toggle Run Freeze:", Toast.LENGTH_SHORT);
        //findViewById(R.id.imagingFrameInMainWindowImageView).setBackgroundColor(Color.GREEN);
        //findViewById(R.id.runFreezeInMainWindowButton).setVisibility(View.INVISIBLE);
        //findViewById(R.id.livePlaybackInMainWindowButton).setVisibility(View.VISIBLE);
    }

    public void onLivePlayback(View view) {
        boolean result = mBackend.onToggleLivePlayback();
        MauiToastMessage.displayToastMessage((Context)this, result, "Toggle Live Playback:", Toast.LENGTH_SHORT);
        //hideMauiLogo();
        //findViewById(R.id.runFreezeInMainWindowButton).setVisibility(View.VISIBLE);
        //findViewById(R.id.livePlaybackInMainWindowButton).setVisibility(View.INVISIBLE);
    }

    public void showLogInLogOutDialog(View view) {
        if(mBackend.loggedIn())
            new LogOutDialog((Context)this);
        else
            new LogInDialog((Context)this);
    }

    public void showSystemPopupMenu(View view) {
        showSystemPopupMenu(view, false, R.style.MyPopupStyle);
    }

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
                        new AboutDialog((Context)MainActivity.this);
                        Toast.makeText((Context)MainActivity.this, "System > About clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_configuration:
                        (mSystemsDialog = new SystemsDialog((Context)MainActivity.this)).showConfigurationPage();
                        Toast.makeText((Context)MainActivity.this, "System > Configuration clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_users:
                        (mSystemsDialog = new SystemsDialog((Context)MainActivity.this)).showCreateUserPage();
                        Toast.makeText((Context)MainActivity.this, "System > Users clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_diagnostics:
                        (mSystemsDialog = new SystemsDialog((Context)MainActivity.this)).showDiagnosticsPage();
                        Toast.makeText((Context)MainActivity.this, "System > Diagnostics clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_engineering:
                        Toast.makeText((Context)MainActivity.this, "System Engineering clicked", Toast.LENGTH_SHORT).show();
                        mEngineeringMenuDialog = new EngineeringMenuDialog((Context)MainActivity.this);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public class MonitoringTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();

        public MonitoringTimerTask(Context con) {
            this.mContext = con;
        }

        @Override
        public void run() {
            if (!mActivateCheckSystemStates)
                return;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.checkRealTimeStates();
                        }
                    });
                }
            }).start();
        }
    }

    private static class GrpcExecutor implements Executor {

        @Override
        public void execute(@Nonnull Runnable r) {
            new Thread(r).start();
        }
    }

    class GrpcRunnable implements Runnable {

        private final WeakReference<MainActivity> activityReference;

        GrpcRunnable(MainActivity activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public void run() {
            if (!mActivateCheckSystemStates)
                return;

            MainActivity activity = activityReference.get();
            long nextTime = 0;
            //K3900.ImageRequest.Builder imageRequest;



            //Log.d(TAG, "Stop peer discovery");
            //activity.mP2pManager.stopPeerDiscovery(mP2pChannel, actionDiscoverStop);
            //SystemClock.sleep(5000);

            while (true) {

                if (!mPeerUpdateQueue.isEmpty())
                    Log.d(TAG, "got " + activity.mPeerUpdateQueue.size() + " peer updates");
                while(!mPeerUpdateQueue.isEmpty()) {
                    try {
                        activity.processPeer(activity.mPeerUpdateQueue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (!activity.mConnected && !activity.mConnectionInProgress) {

                    if (activity.mConnected)
                        Log.d(TAG, "Connected so check discovery");
                    else
                        Log.d(TAG, "NOT Connected so check discovery");

                    activity.mDiscoverRequestComplete = false;
                    activity.mP2pManager.requestDiscoveryState(activity.mP2pChannel, activity.discoveryStateListener);
                    while(!activity.mDiscoverRequestComplete)
                        SystemClock.sleep(500);

                    if (!activity.mDiscovery || !mConnected) {
                        //mDiscoveryInProgress = true;
                        activity.mDiscoverPeersComplete = false;
                        Log.d(TAG, "activate PEER Listener");
                        activity.mP2pManager.discoverPeers(mP2pChannel, actionPeerListener);
                        while(!activity.mDiscoverPeersComplete)
                            SystemClock.sleep(500);
                    }
                    //activity.mPingButton.setBackgroundColor(Color.RED);
                    //activity.mRelativeLayout.setBackgroundColor(Color.RED);

                }
                else {
                    //imageRequest = K3900.ImageRequest.newBuilder().setTime(nextTime);
                    try {
                        //Image img = mBackend.getImage(imageRequest);
                        final ImageView image = (ImageView) findViewById(R.id.bfImageView);
                        if (null == image)
                            return;
                        int size = 512*512;

                        byte[] byteArray = new byte[size];
                        for (int i = 0; i < size; i++) {
                            byteArray[i] = (byte)(i%256);
                        }
                        int[] color = new int[size];
                        for (int i = 0; i < size; i++) {
                            //int b = ((int)byteArray[i])&0xff;
                            //byteArray = Arrays.copyOf(img.getData(), size);
                            int b = ((int)(byteArray[i]))&0xff;
                            //color[i] = (int)img.getData()[i];
                            //color[i] = 0xFF000000 | (((int)byteArray[i])&0xff << 16) | (byteArray[i] << 8) | (byteArray[i]);
                            color[i] = 0xFF000000 | (b << 16) | (b << 8) | b;
                        }
                        final Bitmap bmap = Bitmap.createBitmap(color, 512, 512, Bitmap.Config.ARGB_8888);
                        //Bitmap bmap = BitmapFactory.decodeFile("C:\\Users\\Hitoshi.Gotani\\Work\\ThirdPartyTools\\boost_1_74_0\\boost_1_74_0\\libs\\gil\\test\\extension\\io\\images\\bmp\\g04p4.bmp");
                        //BitmapFactory.decode
                        //ByteArrayInputStream isBm = new ByteArrayInputStream(byteArray);
                        //Bitmap bm = BitmapFactory.decodeStream(isBm, null, null);
                        //Bitmap bmap = BitmapFactory.decodeByteArray(img.getData(), 0, img.getData().length);
                        //Bitmap bmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                        image.post(new Runnable() {
                            @Override
                            public void run() {
                                image.setImageBitmap(bmap);
                            }
                        });
                        //ImageView image = (ImageView) findViewById(R.id.bfImageView);
                        //image.setImageBitmap(Bitmap.createScaledBitmap(bmap, image.getWidth(), image.getHeight(), false));
                        //size = image.getMaxHeight()*image.getMaxWidth();
                        //Log.d(TAG, "size = (" + image.getWidth() + ", " + image.getHeight() + ")");

                        /*if (img.getTime() == Long.MAX_VALUE) {
                            nextTime = 1;
                        } else {
                            nextTime = img.getTime() + 1;
                        }*/
                    } catch (LostCommunicationException le) {
                        Log.d(TAG, le.getCause().getMessage());
                        Log.d(TAG, le.getMessage());
                    } catch (ImageNotAvailableException ie) {
                        Log.d(TAG, ie.getMessage());
                    }

                    /*activity.mConnectionInfoRequest = false;
                    activity.mP2pManager.requestConnectionInfo(activity.mP2pChannel, activityReference.get().connectionInfoListener);
                    while(!activity.mConnectionInfoRequest)
                        SystemClock.sleep(500);*/

                }
                SystemClock.sleep(500);
            }
        }
    }

    public class WidgetsTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public WidgetsTimerTask(Context con) {
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
                            if (!mActivateCheckSystemStates)
                                return;
                            MainActivity.this.checkRealTimeStates();
                            ElapseTimeStatisticsModel.start(-1, "getSystemState() RPC <Started>");
                            mBackend.updateSystemState();
                            ElapseTimeStatisticsModel.end(-1, "getSystemState() RPC <Completed>");
                            if (null != mSelectProbeDialog)
                                mSelectProbeDialog.checkRealtimeStates();
                            if (null != mPatientDialog)
                                mPatientDialog.checkRealtimeStates();
                            if (null != mEngineeringMenuDialog)
                                mEngineeringMenuDialog.checkRealtimeStates();
                            if (null != mSystemsDialog)
                                mSystemsDialog.checkRealtimeStates();
                            if (null != mMainImagingDialog)
                                mMainImagingDialog.checkRealtimeStates();
                            if (null != mActionButtonsImagingDialog)
                                mActionButtonsImagingDialog.checkRealtimeStates();
                            if (null != mMeasureImagingDialog)
                                mMeasureImagingDialog.checkRealtimeStates();
                            if (null != mTgcView)
                                mTgcView.checkRealtimeStates();
                            if (null != mSpeedOfSoundView)
                                mSpeedOfSoundView.checkRealtimeStates();
                            if (null != mGainView)
                                mGainView.checkRealtimeStates();
                            if (null != mFiltersView)
                                mFiltersView.checkRealtimeStates();
                            if (null != mContrastView)
                                mContrastView.checkRealtimeStates();
                            //updateDisplayWidgets();
                            ImageStreamer imageStreamer = ImageStreamer.getImageStreamerSingletonInstance();
                            //if (null == mEngineeringMenuDialog || null == imageStreamer.getImageView() || !mEngineeringMenuDialog.isEngineeringImagingDialogVisible())
                                //imageStreamer.setImageView(findViewById(R.id.mainImageView));
                        }
                    });
                }
            }).start();
        }
    }

    public class MainImagingTimerTask extends TimerTask {

        private Context mContext;
        private Handler mHandler = new Handler();
        public MainImagingTimerTask(Context con) {
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
                            if (!mActivateCheckSystemStates)
                                return;
                            if (!mBackend.isAvailable())
                                return;
                            ImageStreamer imageStreamer = ImageStreamer.getImageStreamerSingletonInstance();
                            if (!imageStreamer.getStopTimer()) {
                                imageStreamer.updateImaging();
                                //imageStreamer.setStopTimer(true);
                                //imageStreamer.setStopImaging(false);
                            }
                        }
                    });
                }
            }).start();
        }
    }
}