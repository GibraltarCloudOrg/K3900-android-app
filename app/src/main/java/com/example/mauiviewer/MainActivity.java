package com.example.mauiviewer;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;

import static android.net.wifi.p2p.WifiP2pManager.BUSY;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED;

public class MainActivity extends AppCompatActivity {

    IntentFilter mIntentFilter;
    WifiP2pManager mP2pManager;
    WifiP2pManager.Channel mP2pChannel;
    Button mPingButton;
    BroadcastReceiver mP2pReceiver;
    public static final String TAG = "Maui-viewer";
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001;
    Boolean mConnected = false;
    Boolean mConnectionInProgress = false;
    Boolean mDiscovery = false;
    MainActivity mTheOne = this;
    Boolean mDiscoverRequestComplete = false;
    Boolean mDiscoverPeersComplete = false;
    Boolean mConnectionStartRequest = false;
    Boolean mConnectionInfoRequest = false;
    BlockingQueue<List<WifiP2pDevice>>  mPeerUpdateQueue = new LinkedBlockingQueue<>(10);
    BeamformerClient mBackend = new BeamformerClient();
    RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        if (mP2pManager != null) {
            mP2pChannel = mP2pManager.initialize(this, getMainLooper(), null);
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

        //registerReceiver(mP2pReceiver, mIntentFilter);
        mPingButton = findViewById(R.id.pingButton);
        mPingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBackend.ping();
                Log.d(TAG, mBackend.ping());
            }
        });

        new GrpcExecutor().execute(new GrpcRunnable(mTheOne));
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mRelativeLayout.setBackgroundColor(Color.YELLOW);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        //findViewById(android.R.id.content).getRootView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE | SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
                //break;
        }
    }

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {


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

        boolean device_found = false;

        for (WifiP2pDevice device : peers) {

            //final WifiP2pDevice device = d;

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
                    break;
                case WifiP2pDevice.UNAVAILABLE:
                    msg += "mUnavailable";
                default:
                    msg += "mUnknown";
                    break;
            }
            Log.d(TAG, msg);

            if (device.deviceName.compareTo("DIRECT-K3900") == 0) {
                device_found = true;
                if (device.status ==  WifiP2pDevice.AVAILABLE) {
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
            }
        }

        if (!device_found) {
            mConnectionInProgress = false;
        }
    }

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

    WifiP2pManager.DiscoveryStateListener discoveryStateListener = new WifiP2pManager.DiscoveryStateListener() {
        @Override
        public void onDiscoveryStateAvailable(int state) {

            if (state == WIFI_P2P_DISCOVERY_STARTED) {
                mDiscovery = true;
            }
            else if (state == WIFI_P2P_DISCOVERY_STOPPED) {
                Log.d(TAG, "discovery stopped");
                mDiscovery = false;
            }
            mDiscoverRequestComplete = true;
        }
    };



    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            final InetAddress groupOwnerAddress = info.groupOwnerAddress;
            //Log.d(TAG, "Address = " + groupOwnerAddress);
            if (info.groupFormed){

                if (!mConnected) {
                    mConnected = true;
                    mRelativeLayout.setBackgroundColor(Color.GREEN);
                    mBackend.connect(groupOwnerAddress.getHostAddress(), 50051);
                    //mGrpcChannel = ManagedChannelBuilder.forAddress(groupOwnerAddress.getHostAddress(), 50051).usePlaintext().build();
                    //mBlockingStub = BeamformerGrpc.newBlockingStub(mGrpcChannel);
                    Log.d(TAG, "Group Formed, Stub created");
                }
            }
            else {
                if (mConnected)
                {
                    Log.d(TAG, "Lost Connection");
                    mConnected = false;
                    mRelativeLayout.setBackgroundColor(Color.RED);
                    mConnectionInProgress = false;
                    mBackend.disconnect();
                }
            }
            mConnectionInfoRequest = true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");
        registerReceiver(mP2pReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pause");
        unregisterReceiver(mP2pReceiver);
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

        public void run() {

            MainActivity activity = activityReference.get();

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

                    if (!activity.mDiscovery) {
                        //mDiscoveryInProgress = true;
                        activity.mDiscoverPeersComplete = false;
                        activity.mP2pManager.discoverPeers(mP2pChannel, actionPeerListener);
                        while(!activity.mDiscoverPeersComplete)
                            SystemClock.sleep(500);
                    }
                    //activity.mPingButton.setBackgroundColor(Color.RED);
                    //activity.mRelativeLayout.setBackgroundColor(Color.RED);

                }
                else {
                    activity.mConnectionInfoRequest = false;
                    activity.mP2pManager.requestConnectionInfo(activity.mP2pChannel, activityReference.get().connectionInfoListener);
                    while(!activity.mConnectionInfoRequest)
                        SystemClock.sleep(500);

                }
                SystemClock.sleep(500);
            }
        }
    }
}
