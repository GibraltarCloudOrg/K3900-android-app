package com.example.mauiviewcontrol;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.protobuf.Any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannelBuilder;
import k3900.BeamformerGrpc;
import k3900.K3900;

public class UnitTestingModel {
    private static UnitTestingModel singletonInstance = null;
    public static UnitTestingModel getUnitTestingModelSingletonInstance() {
        if (null == singletonInstance)
            singletonInstance = new UnitTestingModel();
        return singletonInstance;
    }

    private UnitTestingModel() {
        initializeTxElements();
        initializeRxElements();
    }

    public boolean connected() { return mConnected; }
    public boolean imageAvailable() { return false; }
    public boolean loggedIn() { return mLoggedIn; }
    public boolean paused() { return mPaused; }
    public boolean isDriveTypeNetwork() { return mIsDriveTypeNetwork; }
    public boolean isUnitTypeMetric() { return mIsUnitTypeMetric; }
    public String getUnitName() {
        return "cm";
    }
    public boolean networkMounted() { return mNetworkMounted; }
    public boolean examInProcess() { return mExamInProcess; }
    public boolean isDiagnosticTestRunning() { return mDiagnosticTestRunning; }

    private ArrayList<Boolean> mTxList = null;
    private ArrayList<Boolean> mRxList = null;

    private void initializeTxElements() {
        mTxList = new ArrayList<>();
        for (int index = 0; index < 3 * 64; ++index)
            mTxList.add(true);
    }

    private void initializeRxElements() {
        mRxList = new ArrayList<>();
        for (int index = 0; index < 3 * 64; ++index)
            mRxList.add(false);
    }

    public final ArrayList<Boolean> onGetTxMask(){
        return mTxList;
    }

    public final ArrayList<Boolean>onGetRxMask(){
        return mRxList;
    }

    public GetMaskMsgStreamObserver getTxElementStreamer() {
        return null;
    }

    public GetMaskMsgStreamObserver getRxElementStreamer() {
        return null;
    }

    public boolean isRunning(){
        return false;
    }

    public boolean onChangeRxMask(int elementInt, boolean status){
        mRxList.set(elementInt, status);
        //return "RX mask change done";
        return true;
    }

    public boolean onChangeTxMask(int elementInt, boolean status){
        mTxList.set(elementInt, status);
        //return "TX mask change done";
        return true;
    }

    public String getParameterValuesInString() {
        String text = new String(WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getSelectedDeviceName());
        text += "\n";
        text += LocalDateTime.now();
        text += "\n\n";
        for (int index = 0; index < 9; ++index) {
            float tgcValue = getTgcValue(index);
            text += "TGC " + (index + 1) + ": " + tgcValue + "\n";
        }
        text += "DLC: " + getDlcValue() + "\n";
        text += "VGA Gain: " + getVgaGainValue() + "\n";
        text += "Acoustic Power: " + getAcousticPowerValue() + "\n";
        text += "Gaussian: " + getGaussianValue() + "\n";
        text += "Edge: " + getEdgeValue() + "\n";
        text += "Contrast: " + getContrastValue() + "\n";
        text += "Brightness: " + getBrightnessValue() + "\n";
        text += "Gamma: " + getGammaValue() + "\n";
        text += "Speed of Sound: " + getSpeedOfSoundValue() + "\n";
        return text;
    }

    public boolean onPan(float x, float y) {
        System.out.println("UnitTestingModel.onPan() called");
        return true;
    }

    public boolean onCenterImage() {
        System.out.println("UnitTestingModel.onCenterImage() called");
        return true;
    }

    public boolean onHomeImage() {
        System.out.println("UnitTestingModel.onHomeImage() called");
        return true;
    }

    public boolean onTgcCenter() {
        System.out.println("UnitTestingModel.onTgcCenter() called");
        for (int index = 0; index < 9; ++index)
            mTgcValue[index] = 0.0f;
        return true;
    }

    public boolean resetAll() {
        onUserLogOut();
        mLoggedIn = false;
        return true;
    }

    public String onUserLogIn(String userName, String password) {
        System.out.println("UnitTestingModel.onUserLogIn() called, userName is: " + userName);
        //K3900.ResponseMsg request = K3900.ResponseMsg.getDefaultInstance();
        //K3900.ResponseMsg.Builder responseMsgBuilder = K3900.ResponseMsg.newBuilder(request);
        if (userName.equals("admin") && password.equals("pw"))
            mLoggedIn = true;
        else
            return "Wrong user name or password.";
        return "";
    }

    public String onUserLogOut() {
        //K3900.ResponseMsg request = K3900.ResponseMsg.getDefaultInstance();
        //K3900.ResponseMsg.Builder responseMsgBuilder = K3900.ResponseMsg.newBuilder(request);
        //responseMsgBuilder.setCode(0);
        mLoggedIn = false;
        return "";
    }

    public String onGetAbout() {
        return TAG + ".onGetAbout()";
    }

    public void setContrast(float delta)
    {
    }

    public boolean onIncrementTx3Apertures() {
        System.out.println("tx_apts incremented.");
        return true;
    }

    public boolean onDecrementTx3Apertures() {
        System.out.println("tx_apts decremented.");
        return true;
    }

    public boolean onIncrementRx3Apertures() {
        System.out.println("rx_apts incremented.");
        return true;
    }

    public boolean onDecrementRx3Apertures() {
        System.out.println("rx_apts decremented");
        return true;
    }

    public boolean onIncrementPingPersistence() {
        System.out.println("ping_mode incremented.");
        return true;
    }

    public boolean onDecrementPingPersistence() {
        System.out.println("ping_mode decremented");
        return true;
    }

    public boolean onIncrementFramePersistence() {
        System.out.println("frame_persistence incremented.");
        return true;
    }

    public boolean onDecrementFramePersistence() {
        System.out.println("frame_persistence decremented.");
        return true;
    }

    public boolean setNumberOfTxElements(int value) {
        System.out.println(TAG + ".setNumberOfTxElements, " + value);
        return true;
    }

    String ping() {
        String testingPingMessage = TAG + "ping() called at: ";
        return testingPingMessage;
    }

    /*K3900.SystemState getSystemState() {
        K3900.SystemState state = K3900.SystemState.getDefaultInstance();
        K3900.SystemState.Builder stateBuilder = K3900.SystemState.newBuilder(state);
        stateBuilder.setSystemError(true);
        return stateBuilder.build();
    }*/

    final String getProbeName()
    {
        return mProbeName;
    }

    boolean getRunState() {
        return mRunState;
    }

    boolean getPlaybackState() {
        mPlaybackState = !mPlaybackState;
        return mPlaybackState;
    }

    boolean getAutoContrastState() {
        return mAutoContrast;
    }

    void updateSystemState() {
        System.out.println("UnitTestingModel.updateSystemState() called.");
        if (100 <= mDiagnosticsProgress) {
            mDiagnosticTestRunning = false;
            mDiagnosticsStatus = "Passed!";
        }
        if (mDiagnosticTestRunning) {
            mDiagnosticsProgress += 20;
            mDiagnosticsStatus = "Running ...";
        }
    }

    public int getFrameRate() {
        return 0;
    }

    public float getTgcValue(int index) {
        //return (ParameterLimits.MaxTgc - ParameterLimits.MinTgc) * (mTgcValue[index] - ParameterLimits.MinTgc) / (ParameterLimits.FloatValueStep * (ParameterLimits.MaxTgc - ParameterLimits.MinTgc));
        return mTgcValue[index];
    }

    public float getDlcValue() {
        return mDlcValue;
    }

    public float getMasterGainValue() {
        return mMasterGainValue;
    }

    public float getVgaGainValue() {
        return mMasterGainValue;
    }

    public float getAcousticPowerValue() {
        return mAcousticPowerValue;
    }

    public float getGaussianValue() {
        return mGaussianValue;
    }

    public float getEdgeValue() {
        return mEdgeValue;
    }

    public float getContrastValue() {
        return mContrastValue;
    }

    public float getBrightnessValue() {
        return mBrightnessValue;
    }

    public float getGammaValue() {
        return mGammaValue;
    }

    public float getSpeedOfSoundValue() {
        return mSpeedOfSoundValue / 1000;
    }

    public boolean onTouchUpdate(int dx, int dy) {
        System.out.println("UnitTestingModel.onTouchUpdate() called.");
        return true;
    }

    public boolean onZoom(float delta) {
        System.out.println("UnitTestingModel.onZoom() called.");
        return true;
    }

    public boolean onStepBackward() {
        System.out.println("UnitTestingModel.onStepBackward() called.");
        return true;
    }

    public boolean onPlayPause() {
        System.out.println("UnitTestingModel.onPlayPause() called.");
        mPaused = !mPaused;
        return true;
    }

    public boolean onStepForward() {
        System.out.println("UnitTestingModel.onStepForward() called.");
        return true;
    }

    public int getPlaybackBufferSize() {
        System.out.println("UnitTestingModel.getPlaybackBufferSize() called.");
        return 1;
    }

    public int getPlaybackStart() {
        System.out.println("UnitTestingModel.getPlaybackStart() called.");
        return 1;
    }

    public int getCurrentFrame() {
        System.out.println("UnitTestingModel.getCurrentFrame() called.");
        return 1;
    }

    public int getPlaybackSize() {
        System.out.println("UnitTestingModel.getPlaybackSize() called.");
        return 62;
    }

    public float getPixelSizeX() {
        return -0.2f;
    }

    public float getPixelSizeY() {
        return 0.3f;
    }

    public float getUpperLeftX() {
        return 30;
    }

    public float getUpperLeftY() {
        return 40;
    }

    public float getScale() {
        return 1.8f;
    }

    public int getTxApertures() {
        return 3;
    }

    public int getRxApertures() {
        return 4;
    }

    public String getFramePersistence() {
        return "Alpha";
    }

    public int getPingMode() {
        return 5;
    }

    public int getFilterSelect() {
        return 8;
    }

    public int getTransmitSize() {
        return 3;
    }

    public float getTxFreq() {
        return 9.5f;
    }

    public boolean onSave(int type, String name, int startFrame, int numberOfFrames) {
        System.out.println(TAG + ".onSave() called.");
        return true;
    }

    public boolean onToggleRunFreeze() {
        System.out.println("UnitTestingModel.onToggleRunFreeze() called.");
        mRunState = !mRunState;
        return true;
    }

    public boolean onToggleLivePlayback() {
        System.out.println("UnitTestingModel.onToggleLivePlayback() called.");
        mRunState = !mRunState;
        mPlaybackState = !mPlaybackState;
        return true;
    }

    public boolean onToggleSos() {
        System.out.println("onToggleSos() called.");
        if (1000 * 1500 < mSpeedOfSoundValue)
            mSpeedOfSoundValue = 1000 * 1449.932f;
        else
            mSpeedOfSoundValue = 1000 * 1539.9423f;
        return true;
    }

    public boolean onFocusChange(float delta) {
        System.out.println("onFocusChange() called, delta: " + delta);
        mSpeedOfSoundValue = mSpeedOfSoundValue + delta;
        //mSpeedOfSoundValue = mSpeedOfSoundValue + delta/50;
        return true;
    }

    public boolean onSpeedOfSoundChanged(float value) {
        System.out.println("onFocusChanged() called, value: " + value);
        mSpeedOfSoundValue = value;
        return true;
    }

    public boolean onTgcChanged(int index, float value) {
        System.out.println("UnitTestingModel.onTgcChanged() called.  index: " + index + ",  value: " + value);
        //K3900.BeamformerParametersResponse response = K3900.BeamformerParametersResponse.getDefaultInstance();
        //K3900.BeamformerParametersResponse.Builder responseBuilder = K3900.BeamformerParametersResponse.newBuilder(response);
        //responseBuilder.setCode(0);
        //responseBuilder.setMessage(TAG + "onTgcChanged() called.  index: " + index + " value: " + value);
        mTgcValue[index] = value;
        return true;
    }

    public boolean onDlcChanged(float value) {
        System.out.println("UnitTestingModel.onDlcChanged() called.  value: " + value);
        mDlcValue = value;
        return true;
    }

    public boolean onMasterGainChanged(float value) {
        System.out.println("UnitTestingModel.onMasterGainChanged() called.  value: " + value);
        mMasterGainValue = value;
        return true;
    }

    public boolean onAcousticPowerChanged(float value) {
        System.out.println("UnitTestingModel.onAcousticPowerChanged() called.  value: " + value);
        mAcousticPowerValue = value;
        return true;
    }

    public void onAutoContrastChanged(boolean toggle) {
        System.out.println("UnitTestingModel.onAutoContrastChanged() called.  toggle: " + toggle);
        mAutoContrast = !mAutoContrast;
    }

    public boolean onGaussianChanged(float value) {
        System.out.println("UnitTestingModel.onGaussianChanged() called.  value: " + value);
        mGaussianValue = value;
        return true;
    }

    public boolean onEdgeChanged(float value) {
        System.out.println("UnitTestingModel.onEdgeChanged() called.  value: " + value);
        mEdgeValue = value;
        return true;
    }

    public boolean onContrastChanged(float value) {
        System.out.println("UnitTestingModel.onContrastChanged() called.  value: " + value);
        mContrastValue = value;
        return true;
    }

    public boolean onBrightnessChanged(float value) {
        System.out.println("UnitTestingModel.onBrightnessChanged() called.  value: " + value);
        mBrightnessValue = value;
        return true;
    }

    public boolean onGammaChanged(float value) {
        System.out.println("UnitTestingModel.onGammaChanged() called.  value: " + value);
        mGammaValue = value;
        return true;
    }

    public boolean onFrequencyChanged(float value) {
        System.out.println("UnitTestingModel.onFrequencyChanged() called.  value: " + value);
        return true;
    }

    public boolean onLoadExamFile(String pid, String examName, String date, int type, String fileName) {
        System.out.println(TAG + ".onLoadExamFile() called, pid: " + pid + ", examName:" + examName + ", date: " + date + ", type: " + type + ", fileName: " + fileName);
        return true;
    }

    public final ArrayList<String> onGetExamStores(String pid, String examName, String date, int type) {
        System.out.println(TAG + ".onGetExamStores() called, pid: " + pid + " exam:" + examName + "date: " + date);
        final ArrayList<String> fileNameList = new ArrayList<String>();
        fileNameList.add("SampleFile01");
        fileNameList.add("SampleFile02");
        fileNameList.add("SampleFile03");
        fileNameList.add("SampleFile04");
        fileNameList.add("SampleFile05");
        return fileNameList;
    }

    public final ArrayList<String> onGetFileTypes(String pid, String exam, String date) {
        System.out.println(TAG + ".onGetFileTypes() called, pid: " + pid + " exam:" + exam + "date: " + date);
        final ArrayList<String> fileTypeList = new ArrayList<String>();
        fileTypeList.add("Image");
        fileTypeList.add("Video");
        fileTypeList.add("Dataset");
        return fileTypeList;
    }

    public final ArrayList<String> onGetPatients(String pattern) {
        System.out.println(TAG + ".onGetPatients() called, pattern is: " + pattern);
        final ArrayList<String> patients = new ArrayList<String>();
        patients.add("pid1    patient 001");
        patients.add("pid2    patient 002");
        patients.add("pid3    patient 003");
        patients.add("pid4    patient 004");
        patients.add("pid5    patient 005");
        patients.add("pid6    patient 006");
        patients.add("pid7    patient 007");
        patients.add("pid8    patient 008");
        patients.add("pid9    patient 009");
        patients.add("pid10   patient 010");
        return patients;
    }

    public final ArrayList<String> getPids(String pattern) {
        System.out.println(TAG + ".onGetPids() called, pattern is: " + pattern);
        ArrayList<String> pids = new ArrayList<String>();
        pids.add("pid1");
        pids.add("pid2");
        pids.add("pid3");
        pids.add("pid4");
        pids.add("pid5");
        pids.add("pid6");
        pids.add("pid7");
        pids.add("pid8");
        pids.add("pid9");
        pids.add("pid10");
        return pids;
    }

    public final String getPid(String pattern, int index) {
        System.out.println(TAG + ".onGetPid() called, pattern is: " + pattern + ", index: " + index);
        return "pid1";
    }

    public final ArrayList<String> onGetExams(String patientId) {
        System.out.println(TAG + ".onGetExams() called, pid is: " + patientId);
        ArrayList<String> exams = new ArrayList<String>();
        exams.add("Lung        John    2021-01-23  07:39:19");
        exams.add("Kidney      John    2019-09-22  11:30:37");
        exams.add("Leg         John    2019-09-26  08:28:21");
        return exams;
    }

    public final String getExamName(String patientId, int index) {
        System.out.println(TAG + ".getExamName() called, pid is: " + patientId);
        return "Kidney";
    }

    public final String getExamDate(String patientId, int index) {
        System.out.println(TAG + ".getExamDate() called, pid is: " + patientId);
        return "2019-09-26";
    }

    public boolean onStartExam(String examName, String selectedLine) {
        System.out.println("UnitTestingModel.onStartExam() called.  examName: " + examName + " selectedLine: " + selectedLine);
        return true;
    }

    public boolean onStartExam(String examName, String comments, String pid, String first, String last, String birth, String sex) {
        System.out.println("UnitTestingModel.onStartExam() called.  examName: " + examName + " comments: " + comments + " pid: " + pid + " first: " + first + " last: " + last);
        /*K3900.ResponseMsg request = K3900.ResponseMsg.getDefaultInstance();
        K3900.ResponseMsg.Builder responseMsgBuilder = K3900.ResponseMsg.newBuilder(request);
        responseMsgBuilder.setCode(0);
        responseMsgBuilder.setMsg(TAG + " onStartExam() called.");*/
        mExamInProcess = true;
        return true;
    }

    public boolean onEndExam() {
        System.out.println("UnitTestingModel.onEndExam() called.");
        /*K3900.ResponseMsg request = K3900.ResponseMsg.getDefaultInstance();
        K3900.ResponseMsg.Builder responseMsgBuilder = K3900.ResponseMsg.newBuilder(request);
        responseMsgBuilder.setCode(0);
        responseMsgBuilder.setMsg(TAG + " onEndExam() called.");*/
        mExamInProcess = false;
        return true;
    }

    public ArrayList<String> getUnitOfMeasureList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Metric");
        list.add("US (inches)");
        return list;
    }

    public ArrayList<String> getUserTypeList() {
        System.out.println(TAG + ".getUserTypeList() called.");
        ArrayList<String> list = new ArrayList<String>();
        list.add("Standard");
        list.add("Administrator");
        return list;
    }

    public String getUserName(int index) {
        if (0 <= index && mUserList.size() > index)
            return mUserNameList.get(index);
        else
            return null;
    }

    public ArrayList<String> getUserList() {
        System.out.println(TAG + ".getUserList() called.");
        if (mUserNameList.size() == 0) {
            mUserNameList.add("Johnson123");
            mUserNameList.add("Johnson456");
            mUserNameList.add("Smith777");
        }
        if (mUserList.size() == 0) {
            mUserList.add("Johnson123       password1      Engineer");
            mUserList.add("Johnson456       pw2                 Standard");
            mUserList.add("Smith777            pw3                 Administrator");
        }
        return mUserList;
    }

    public boolean createUser(String userName, String password, String userType) {
        System.out.println(TAG + ".createUser() called.");
        mUserNameList.add(userName);
        mUserList.add(userName + "            " + password + "            " + userType);
        return true;
    }

    public boolean deleteUser(int index) {
        if (0 <= index && mUserList.size() > index) {
            mUserList.remove(index);
        }
        if (0 <= index && mUserNameList.size() > index) {
            mUserNameList.remove(index);
            return true;
        }
        return false;
    }

    public boolean deleteUser(String userName, String password) {
        System.out.println(TAG + ".deleteUser() called.");
        return true;
    }

    public boolean setDriveTypeNetwork() {
        System.out.println(TAG + ".setDriveTypeNetwork() called.");
        mIsDriveTypeNetwork = true;
        return true;
    }

    public boolean setDriveTypeUsb() {
        System.out.println(TAG + ".setDriveTypeUsb() called.");
        mIsDriveTypeNetwork = false;
        return true;
    }

    public boolean setUnitTypeMetric() {
        System.out.println(TAG + ".setUnitTypeMetric() called.");
        mIsUnitTypeMetric = true;
        return true;
    }

    public boolean setUnitTypeInches() {
        System.out.println(TAG + ".setUnitTypeInches() called.");
        mIsUnitTypeMetric = false;
        return true;
    }

    public ArrayList<String> getExternalStorageInfo() {
        System.out.println(TAG + ".getExternalStorageInfo() called.");
        final ArrayList<String> usbDrives = new ArrayList<String>();
        usbDrives.add("Network");
        usbDrives.add("USB");
        return usbDrives;
    }

    public boolean disconnectNetworkStorage() {
        System.out.println(TAG + ".disconnectNetworkStorage() called.");
        mNetworkMounted = false;
        return true;
    }

    public boolean connectNetworkStorage(String networkPath, String userName, String password, boolean autoConnect) {
        System.out.println(TAG + ".connectNetworkStorage() called, networkPath is: " + networkPath + ", userName is:  " + userName + ", password is:  " + password + ", autoConnect is:  " + autoConnect);
        mNetworkMounted = true;
        return true;
    }

    /*public float getProgress(String type) {
        System.out.println(TAG + ".getProgress() called, type: " + type);
        return mProgress;
    }*/
    public int getDiagnosticsProgress() {
        System.out.println(TAG + ".getDiagnosticsProgress() called.");
        return mDiagnosticsProgress;
    }

    public String getDiagnosticsStatus() {
        return mDiagnosticsStatus;
    }

    public ArrayList<String> getDiagnosticTests() {
        System.out.println(TAG + ".getDiagnosticTests() called.");
        ArrayList<String> tests = new ArrayList<String>();
        tests.add("MD2134 Registers");
        tests.add("Memory Short");
        tests.add("Memory Long");
        return tests;
    }

    public boolean onClearErrors() {
        System.out.println(TAG + ".onClearErrors() called.");
        return true;
    }

    public boolean onPerformDiagnosticTest(String type) {
        System.out.println(TAG + ".onPerformDiagnosticTest() called.");
        mDiagnosticTestRunning = true;
        mDiagnosticsProgress = 0;
        return true;
    }

    public boolean onAbortDiagnosticTest() {
        System.out.println(TAG + ".onAbortDiagnosticTest() called.");
        mDiagnosticsStatus = "Aborted";
        mDiagnosticTestRunning = false;
        return true;
    }

    public boolean onSelectFilter(int index) {
        System.out.println(TAG + ".onSelectFilter() called, index: " + index);
        return true;
    }

    public ArrayList<String> onGetFilters() {
        System.out.println(TAG + ".onGetFilters() called.");
        final ArrayList<String> filterList = new ArrayList<String>();
        filterList.add("1.5625 / 0.600");
        filterList.add("2.0833 / 0.750");
        filterList.add("2.0833 / 1.400");
        filterList.add("2.5000 / 0.900");
        filterList.add("2.5000 / 1.400");
        filterList.add("2.7770 / 1.400");
        filterList.add("3.1250 / 1.200");
        filterList.add("3.1250 / 1.400");
        filterList.add("10.0000 / 3.500");
        return filterList;
    }

    public final ArrayList<String> onGetProbeList() {
        System.out.println(TAG + ".onGetProbeList() called.");
        final ArrayList<String> probeList = new ArrayList<String>();
        probeList.add(TAG + ": Maui 3 SS");
        probeList.add(TAG + ": Maui 5");
        probeList.add(TAG + ": Maui 5L");
        probeList.add(TAG + ": Maui 3D SP");
        probeList.add(TAG + ": Maui 3D Mouse");
        return probeList;
    }

    public boolean onSelectProbe(String name) {
        System.out.println(TAG + ".onSelectProbe() called, name: " + name);
        mProbeName = name;
        return true;
    }

    public ArrayList<String> onGetCompressionTypes() {
        System.out.println(TAG + ".onGetCompressionTypes() called.");
        ArrayList<String> compressionTypes = new ArrayList<String>();
        compressionTypes.add(TAG + " Compression Type 1");
        compressionTypes.add(TAG + " Compression Type 2");
        compressionTypes.add(TAG + " Compression Type 3");
        return compressionTypes;
    }

    public boolean onSelectCompressionType(String name) {
        System.out.println(TAG + ".onSelectCompressionType() called, name: " + name);
        return true;
    }

    public boolean onRequestSystemShutdown() {
        System.out.println(TAG + ".onRequestSystemShutdown() called.");
        return true;
    }

    public boolean onStartMeasurement() {
        System.out.println(TAG + ".onStartMeasurement() called.");
        return false;
    }

    public boolean onStopMeasurement() {
        System.out.println(TAG + ".onStopMeasurement() called.");
        return false;
    }

    public boolean onSwapMeasurementPoints() {
        System.out.println(TAG + ".onSwapMeasurementPoints() called.");
        return false;
    }

    public boolean onCaptureMeasurementMark() {
        System.out.println(TAG + ".onCaptureMeasurementMark() called.");
        return false;
    }

    public boolean onCancelMeasurement() {
        System.out.println(TAG + ".onCancelMeasurement() called.");
        return false;
    }

    public boolean onDeleteMeasurement(int index) {
        System.out.println(TAG + ".onDeleteMeasurement() called, index: " + index);
        return false;
    }

    public boolean onEditMeasurement(int index) {
        System.out.println(TAG + ".onEditMeasurement() called, index: " + index);
        return true;
    }

    public boolean onHighlightMeasurement(int index) {
        System.out.println(TAG + ".onHighlightMeasurement() called, index: " + index);
        return false;
    }

    public boolean onSendCursorMovement(int dx, int dy) {
        System.out.println(TAG + ".onHighlightMeasurement() called, dx: " + dx + ", dy: " + dy);
        return false;
    }

    public final ArrayList<Float> onGetMeasurements() {
        System.out.println(TAG + ".onGetMeasurements() called.");
        final ArrayList<Float> measurements = new ArrayList<Float>();
        for (int index = 0; index < 5; ++index)
            measurements.add((float) (index * 2 + index));
        return measurements;
    }

    public final ArrayList<Integer> onGetMeasurementFrames() {
        System.out.println(TAG + ".onGetMeasurementFrames() called.");
        final ArrayList<Integer> measurements = new ArrayList<Integer>();
        for (int index = 0; index < 5; ++index)
            measurements.add(index * 2 + index);
        return measurements;
    }

    public final String onGetSystemState(int index) {
        System.out.println(TAG + ".onGetSystemState() called, index: " + index);
        String state = TAG;
        state += ".onGetSystemState(), index: ";
        state += index;
        state += "\n";
        state += "Build Type:    Stable\n";
        state += "Version:       3.7.4\n";
        state += "Branch:        Master\n";
        return state;
    }

    public final ArrayList<String> onGetSystemStateList() {
        System.out.println(TAG + ".getSystemStateList() called.");
        final ArrayList<String> states = new ArrayList<String>();
        for (int index = 0; index < 7; ++index) {
            String state = "Data Coming from Unit Testing Model, Sample Device ";
            state += index;
            state += "    OK    74.36C";
            states.add(state);
        }
        return states;
    }

    Image getImage(long time)
    {
        int size = 512*512;

        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = (byte)(i%2500);
        }
        //Bitmap bitmap = BitmapFactory.decodeFile("C:\\Users\\Hitoshi.Gotani\\Work\\ThirdPartyTools\\boost_1_74_0\\boost_1_74_0\\libs\\gil\\test\\extension\\io\\images\\bmp\\g04p4.bmp");
        //byte[] data = bitmap.getRowBytes();
        //Image image =
        //return (new Bitmap);
        return new Image(byteArray, 0);
    }

    void connect(String address, int port) {
        System.out.println("UnitTestingModel.connect() called.  address: " + address + ",  port: " + port);
    }

    void disconnect() {
        System.out.println("UnitTestingModel.disconnect() called");
    }

    public boolean isBatchMode() {
        return false;
    }

    private void waitFor(int seconds) {
        seconds = seconds < 0 ? 0 : seconds;
        while (--seconds >= 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final String TAG = "UnitTestingModel";
    private boolean mConnected = true;
    private boolean mLoggedIn = false;
    private boolean mIsDriveTypeNetwork = true;
    private boolean mIsUnitTypeMetric = true;
    private boolean mNetworkMounted = false;
    private boolean mExamInProcess = false;
    private String mProbeName = "Maui 5L";
    private float mTgcValue[] = new float[9];
    private float mDlcValue;
    private float mMasterGainValue;
    private float mAcousticPowerValue;
    private float mGaussianValue;
    private float mEdgeValue;
    private float mContrastValue;
    private float mBrightnessValue;
    private float mGammaValue;
    private float mSpeedOfSoundValue = 1535 * 1000;
    private ArrayList<String> mUserNameList = new ArrayList<String>();
    private ArrayList<String> mUserList = new ArrayList<String>();
    private boolean mDiagnosticTestRunning = false;
    private int mDiagnosticsProgress = 0;
    private String mDiagnosticsStatus;
    private boolean mRunState = false;
    private boolean mPlaybackState = false;
    private boolean mPaused = false;
    private boolean mAutoContrast = false;
}
