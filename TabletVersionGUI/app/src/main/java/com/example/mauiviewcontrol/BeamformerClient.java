package com.example.mauiviewcontrol;

import android.util.Log;
import android.view.View;

import com.google.protobuf.Any;
import com.google.protobuf.AnyProto;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import io.grpc.Deadline;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import k3900.BeamformerGrpc;
import k3900.K3900;
import k3900.K3900.FileType;

import static java.lang.Math.abs;

public class BeamformerClient {

    private static final String TAG = "BeamformerClient";
    private static BeamformerClient singletonInstance = null;
    public static BeamformerClient getBeamformerClientSingletonInstance() {
        if (null == singletonInstance)
            singletonInstance = new BeamformerClient();
        return singletonInstance;
    }
    private int mFrameRate = -1;
    private static int sMonitoringDurationMilliSecondsDefaultIndexForImaging = 0;
    private static int sMonitoringDurationMilliSecondsDefaultIndexForWidgets = 22;
    private static int sDeadlineAfterMilliSecondsDefaultIndex = 13;
    private static int sBatchModeDeadlineAfterMilliSecondsDefaultIndex = 23;
    private static int sGetImageDeadLineInMilliSeconds = 3000;
    public static int getGetImageDeadLineInMilliSeconds() { return sGetImageDeadLineInMilliSeconds; }
    public static void setGetImageDeadLineInMilliSeconds(int milliseconds) { sGetImageDeadLineInMilliSeconds = milliseconds; }
    public static int getMonitoringDurationMilliSecondsDefaultIndexForImaging() { return sMonitoringDurationMilliSecondsDefaultIndexForImaging; }
    public static int getMonitoringDurationMilliSecondsDefaultIndexForWidgets() { return sMonitoringDurationMilliSecondsDefaultIndexForWidgets; }
    public static int getDeadlineAfterMilliSecondsDefaultIndex() { return sDeadlineAfterMilliSecondsDefaultIndex; }
    public static int getBatchModeDeadlineAfterMilliSecondsDefaultIndex() { return sBatchModeDeadlineAfterMilliSecondsDefaultIndex; }
    private static final int[] sMonitoringDurationMilliSecondsList = new int[]{ 0, 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000, 3000, 5000, 10 * 1000, 30 * 1000, 45 * 1000, 60 * 1000, 120 * 1000, 180 * 1000, 300 * 1000 };
    private static final int[] sDeadlineAfterMilliSecondsList = new int[]{ 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 2000, 3000, 5000 };
    private static int sDeadLineMilliSeconds = sDeadlineAfterMilliSecondsList[sDeadlineAfterMilliSecondsDefaultIndex];
    private static int sBatchModeDeadLineMilliSeconds = sDeadlineAfterMilliSecondsList[sBatchModeDeadlineAfterMilliSecondsDefaultIndex];
    private static int sMonitoringDurationMilliSecondsForImaging = sMonitoringDurationMilliSecondsList[sMonitoringDurationMilliSecondsDefaultIndexForImaging];
    private static int sMonitoringDurationMilliSecondsForWidgets = sMonitoringDurationMilliSecondsList[sMonitoringDurationMilliSecondsDefaultIndexForWidgets];
    public static int getMonitoringDurationMilliSecondsForImaging() { return sMonitoringDurationMilliSecondsForImaging; }
    public static int getMonitoringDurationMilliSecondsForWidgets() { return sMonitoringDurationMilliSecondsForWidgets; }
    public static final int[] getMonitoringDurationMilliSecondsList() { return sMonitoringDurationMilliSecondsList; }
    public static final int[] getDeadlineAfterMilliSecondsList() { return sDeadlineAfterMilliSecondsList; }
    private ManagedChannel mGrpcChannel;
    private BeamformerGrpc.BeamformerBlockingStub mBlockingStub;
    private BeamformerGrpc.BeamformerStub mStub;
    boolean mParameterBatch = false;
    K3900.SystemState m_systemState = null;
    private boolean connected = false;
    private static boolean mWifiDirectListChanged = true;
    private String m_address;
    private int m_port;
    private boolean mLoggedIn = false;
    private boolean mPaused = false;
    private boolean mIsDriveTypeNetwork = true;
    private boolean mIsUnitTypeMetric = true;
    private boolean mNetworkMounted = false;
    private boolean mSosBody = false;
    private boolean mExamInProcess = false;
    private GetMaskMsgStreamObserver mStreamObserver;
    private GetMaskMsgStreamObserver mTxStreamObserver;
    private GetMaskMsgStreamObserver mRxStreamObserver;
    private boolean mDiagnosticTestRunning = false;
    K3900.BeamformerParametersRequest.Builder mRequestBuilder = K3900.BeamformerParametersRequest.newBuilder();
    public static final float kCenterZoom=.75f;
    private ArrayList<Boolean>mRxMask;
    private ArrayList<Boolean>mTxMask;

    public boolean connected() { return null != mBlockingStub; }
    //public boolean connected() { return connected; }
    public static void setWifiDirectListChanged(boolean changed) { mWifiDirectListChanged = changed; }
    public boolean loggedIn() { return mLoggedIn; }
    public boolean paused() { return mPaused; }
    public boolean isDriveTypeNetwork() { return mIsDriveTypeNetwork; }
    public boolean isUnitTypeMetric() { return mIsUnitTypeMetric; }
    public String getUnitName() {
        if (mIsUnitTypeMetric)
            return "cm";
        else
            return "inch";
    }
    public boolean networkMounted() { return mNetworkMounted; }
    public boolean examInProcess() { return mExamInProcess; }
    public boolean isDiagnosticTestRunning() { return mDiagnosticTestRunning; }

    public boolean imageAvailable() {
        if (null == m_systemState)
            return false;
        return null != m_systemState.getImage();
    }

    public static void setMonitoringDurationMilliSecondsForImaging(int monitoringDurationMilliSeconds) {
        if (sMonitoringDurationMilliSecondsList[0] > monitoringDurationMilliSeconds) {
            sMonitoringDurationMilliSecondsForImaging = sMonitoringDurationMilliSecondsList[0];
            return;
        }
        sMonitoringDurationMilliSecondsForImaging = monitoringDurationMilliSeconds;
    }

    public static void setMonitoringDurationMilliSecondsForWidgets(int monitoringDurationMilliSeconds) {
        if (sMonitoringDurationMilliSecondsList[0] > monitoringDurationMilliSeconds) {
            sMonitoringDurationMilliSecondsForWidgets = sMonitoringDurationMilliSecondsList[0];
            return;
        }
        sMonitoringDurationMilliSecondsForWidgets = monitoringDurationMilliSeconds;
    }

    public static void setDeadLineMilliSeconds(int milliSeconds) {
        if (sDeadlineAfterMilliSecondsList[0] > milliSeconds) {
            sDeadLineMilliSeconds = sDeadlineAfterMilliSecondsList[0];
            return;
        }
        sDeadLineMilliSeconds = milliSeconds;
    }

    public final ArrayList<Boolean> onGetTxMask(){
        return mTxMask;
    }

    public void updateTxMask(){
        K3900.BlankRequest request=K3900.BlankRequest.getDefaultInstance();;
        mTxStreamObserver=new GetMaskMsgStreamObserver();
        mStub.getTxMask(request, mTxStreamObserver);
        /*while(!mTxStreamObserver.getCompleted()){

        }*/
        mTxMask=mTxStreamObserver.getElementBooleans();
    }

    public final ArrayList<Boolean>onGetRxMask(){
        return mRxMask;
    }

    public final void updateRxMask(){
        K3900.BlankRequest request=K3900.BlankRequest.getDefaultInstance();;
        mRxStreamObserver=new GetMaskMsgStreamObserver();
        mStub.getRxMask(request, mRxStreamObserver);
        /*while(!mRxStreamObserver.getCompleted()){

        }*/
        mRxMask=mRxStreamObserver.getElementBooleans();
    }

    public GetMaskMsgStreamObserver getTxElementStreamer() {
        return mTxStreamObserver;
    }

    public GetMaskMsgStreamObserver getRxElementStreamer() {
        return mRxStreamObserver;
    }

    public boolean isTxRunning(){
        return (!mTxStreamObserver.getCompleted());
    }

    public boolean isRxRunning(){
        return (!mRxStreamObserver.getCompleted());
    }

    public boolean onChangeRxMask(int elementInt, boolean status){
        K3900.MaskMsg request=K3900.MaskMsg.getDefaultInstance();
        K3900.MaskMsg.Builder maskMsgBuilder=K3900.MaskMsg.newBuilder(request);
        maskMsgBuilder.setElement(elementInt);
        maskMsgBuilder.setOn(status);
        try{
            String result="test";
            K3900.ResponseMsg responseMsg;
            responseMsg=mBlockingStub.withDeadlineAfter(3000,TimeUnit.MILLISECONDS).changeRxMask(maskMsgBuilder.build());
            //result=responseMsg.getMsg();
            updateRxMask();
            return true;

        }catch(Exception e){
            return false;
        }
    }

    public boolean onChangeTxMask(int elementInt, boolean status){
        K3900.MaskMsg request=K3900.MaskMsg.getDefaultInstance();
        K3900.MaskMsg.Builder maskMsgBuilder=K3900.MaskMsg.newBuilder(request);
        maskMsgBuilder.setElement(elementInt);
        maskMsgBuilder.setOn(status);
        try{
            String result="test";
            K3900.ResponseMsg responseMsg;
            responseMsg=mBlockingStub.withDeadlineAfter(3000,TimeUnit.MILLISECONDS).changeTxMask(maskMsgBuilder.build());
            //result=responseMsg.getMsg();
            updateTxMask();
            return true;

        }catch(Exception e){
            return false;
        }
    }

    /*public static BeamformerClient getBeamformerClientSingletonInstance(){
        if(null==singletonInstance){
            singletonInstance=new BeamformerClient();
        }
        return singletonInstance;
    }*/

    void connect(String address, int port) {
        m_address = address;
        m_port = port;
        mGrpcChannel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
        mBlockingStub = BeamformerGrpc.newBlockingStub(mGrpcChannel);
        mStub = BeamformerGrpc.newStub(mGrpcChannel);
    }

    void disconnect() {
        /*try {
            Log.d(TAG, "Shutting down channel");
            mGrpcChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.d(TAG, "Failed to shutdown channel");
        }*/
    }

    public boolean isBatchMode() {
        return mParameterBatch;
    }

    public void startParameterBatch() {
        mParameterBatch = true;
    }

    public void endParameterBatch() {
        mParameterBatch = false;
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
        if (mParameterBatch)
            return false;
        startParameterBatch();
        addToBeamformerParameterList("x_position", x, false);
        addToBeamformerParameterList("y_position", y, false);
        boolean result = sendBeamformerParameterListInBatchMode();
        endParameterBatch();
        return result;
    }

    public boolean onCenterImage() {
        startParameterBatch();
        addToBeamformerParameterList("x_position", 0.0f, true);
        boolean result = sendBeamformerParameterListInBatchMode();
        endParameterBatch();
        return result;
    }

    public boolean onHomeImage() {
        startParameterBatch();
        addToBeamformerParameterList("x_position", 0.0f, true);
        addToBeamformerParameterList("y_position", 42.0f, true);
        addToBeamformerParameterList("zoom", kCenterZoom, true);
        boolean result = sendBeamformerParameterListInBatchMode();
        endParameterBatch();
        return result;
    }

    public boolean onTgcCenter() {
        startParameterBatch();
        for (int index = 0; index < 9; ++index)
            addToBeamformerParameterList("tgc", index, 0.0f, true);
        boolean result = sendBeamformerParameterListInBatchMode();
        endParameterBatch();
        return result;
    }

    public boolean resetAll() {
        onUserLogOut();
        mLoggedIn = false;
        return true;
    }

    public String onUserLogIn(String userName, String password) {
        //System.out.println(TAG + ".onUserLogIn() called, userName is: " + userName);
        K3900.Credentials request = K3900.Credentials.getDefaultInstance();
        //request.toBuilder()
        try {
            String result = "";
            //request.newBuilderForType().setUsername(userName);
            //request.newBuilderForType().setPassword(password);
            //String test = request.getUsername();
            K3900.Credentials.Builder builder = K3900.Credentials.newBuilder(request);
            //K3900.Credentials.Builder builder = request.toBuilder();
            //K3900.Credentials.Builder builder = request.newBuilderForType();
            builder.setUsername(userName);
            builder.setPassword(password);
            K3900.ResponseMsg responseMsg;
            //responseMsg = mBlockingStub.userLogin(builder.build());
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).userLogin(builder.build());
            if (0 == responseMsg.getCode())
                mLoggedIn = true;
            else
                result = responseMsg.getMsg();
            return result;
        } catch (Exception e) {
            return "Log In failed.";
            //throw new LostCommunicationException("Log in failed", e);
        }
    }

    public String onUserLogOut() {
        K3900.BlankRequest blankRequest = K3900.BlankRequest.getDefaultInstance();
        try {
            String result = "";
            //K3900.Credentials.Builder builder = K3900.BlankRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            //responseMsg = mBlockingStub.userLogout(blankRequest);
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).userLogout(blankRequest);
            if (0 == responseMsg.getCode())
                mLoggedIn = false;
            else
                result = responseMsg.getMsg();
            return result;
        } catch (Exception e) {
            return "Log Out failed.";
            //throw new LostCommunicationException("Log Out failed", e);
        }
    }

    public boolean onStartExam(String examName, String selectedLine) {
        String[] stringList = selectedLine.split("\t");
        return onStartExam(examName, "", stringList[0], stringList[1], stringList[2], stringList[3], stringList[4]);
    }

    public boolean onStartExam(String examName, String comments, String pid, String first, String last, String birth, String sex) {
        K3900.ExamRequest request = K3900.ExamRequest.getDefaultInstance();
        K3900.Exam exam = K3900.Exam.getDefaultInstance();
        K3900.Patient patient = K3900.Patient.getDefaultInstance();
        try {
            K3900.Exam.Builder examBuilder = K3900.Exam.newBuilder();
            examBuilder.setName(examName);
            examBuilder.setComments(comments);

            K3900.Patient.Builder patientBuilder = K3900.Patient.newBuilder();
            patientBuilder.setId(pid);
            patientBuilder.setFirst(first);
            patientBuilder.setLast(last);
            patientBuilder.setBirth(birth);
            patientBuilder.setSex(sex);

            K3900.ExamRequest.Builder examRequestBuilder = K3900.ExamRequest.newBuilder();
            examRequestBuilder.setExam(examBuilder.build());
            examRequestBuilder.setPatient(patientBuilder.build());

            //K3900.Exam exam = request.getExam();
            //K3900.ExamRequest.Builder builder = K3900.ExamRequest.newBuilder(request);
            //exam.toBuilder().setName(examName);
            //exam.toBuilder().setComments(comments);
            //K3900.Patient patient = K3900.Patient.getDefaultInstance();
            //patient.toBuilder().setId(pid);
            //patient.toBuilder().setFirst(first);
            //patient.toBuilder().setLast(last);
            //patient.toBuilder().setBirth(birth);
            //patient.toBuilder().setSex(sex);
            //request.toBuilder().setPatient(patient);
            //request.toBuilder().setExam(exam);
            K3900.ResponseMsg responseMsg;
            responseMsg = mBlockingStub.beginExam(examRequestBuilder.build());
            //responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).userLogin(request);
            //return responseMsg;
            boolean result = (0 == responseMsg.getCode());
            if (result)
                mExamInProcess = true;
            return result;
        } catch (Exception e) {
            //throw new LostCommunicationException("Start Exam failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Start Exam failed.");
            return false;
        }
    }

    public boolean onEndExam() {
        K3900.ExamRequest request = K3900.ExamRequest.getDefaultInstance();
        try {
            K3900.ResponseMsg responseMsg;
            //responseMsg = mBlockingStub.endExam(request);
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).endExam(request);
            boolean result = 0 == responseMsg.getCode();
            if (result)
                mExamInProcess = false;
            return result;
        } catch (Exception e) {
            //throw new LostCommunicationException("End Exam failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "End Exam failed.");
            return false;
        }
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
        System.out.println(TAG + ".getUserName() called, index: " + index);
        return null;
    }

    public ArrayList<String> getUserList() {
        System.out.println(TAG + ".getUserList() called.");
        ArrayList<String> list = new ArrayList<String>();
        return list;
    }

    public boolean createUser(String userName, String password, String userType) {
        System.out.println(TAG + ".createUser() called.");
        return false;
    }

    public boolean deleteUser(int index) {
        System.out.println(TAG + ".deleteUser() called.");
        return false;
    }

    public boolean deleteUser(String userName, String password) {
        System.out.println(TAG + ".deleteUser() called.");
        return false;
    }

    public boolean setDriveTypeNetwork() {
        System.out.println(TAG + ".setDriveTypeNetwork() called.");
        return false;
    }

    public boolean setDriveTypeUsb() {
        System.out.println(TAG + ".setDriveTypeUsb() called.");
        return false;
    }

    public boolean setUnitTypeMetric() {
        System.out.println(TAG + ".setUnitTypeMetric() called.");
        return false;
    }

    public boolean setUnitTypeInches() {
        System.out.println(TAG + ".setUnitTypeInches() called.");
        return false;
    }

    public ArrayList<String> getExternalStorageInfo() {
        System.out.println(TAG + ".getExternalStorageInfo() called.");
        try {
            K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
            K3900.ExternalStorageMsg externalStorageMsg;
            externalStorageMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).externalStorageInfo(request);
            final ArrayList<String> usbDrives = new ArrayList<String>();
            for (int index = 0; index < externalStorageMsg.getUsbDrivesCount(); ++index)
                usbDrives.add(externalStorageMsg.getUsbDrives(index));
            return usbDrives;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get External Storage Info failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Get External Storage Info failed.");
            return null;
        }
    }

    public boolean disconnectNetworkStorage() {
        System.out.println(TAG + ".disconnectNetworkStorage() called.");
        try {
            K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).unmountExternalStorage(request);
            if (0 == responseMsg.getCode())
                mNetworkMounted = false;
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Unmount External Storage failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Unmount External Storage failed.");
            return false;
        }
    }

    public boolean connectNetworkStorage(String networkPath, String userName, String password, boolean autoConnect) {
        System.out.println(TAG + ".connectNetworkStorage() called, networkPath is: " + networkPath);
        try {
            /*
            K3900.CursorMoveRequest request = K3900.CursorMoveRequest.getDefaultInstance();
            K3900.CursorMoveRequest.Builder builder = K3900.CursorMoveRequest.newBuilder(request);
            K3900.Pixel pixel = K3900.Pixel.getDefaultInstance();
            K3900.Pixel.Builder pixelBuilder = K3900.Pixel.newBuilder(pixel);
            pixelBuilder.setX(dx);
            pixelBuilder.setY(dy);
            builder.setDelta(pixelBuilder);
                         */
            K3900.StorageRequest request = K3900.StorageRequest.getDefaultInstance();
            K3900.StorageRequest.Builder builder = K3900.StorageRequest.newBuilder(request);
            builder.setName(networkPath);
            builder.setAutoConnect(autoConnect);
            K3900.Credentials credentials = K3900.Credentials.getDefaultInstance();
            K3900.Credentials.Builder credentialsBuilder = K3900.Credentials.newBuilder(credentials);
            credentialsBuilder.setUsername(userName);
            credentialsBuilder.setPassword(password);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).mountExternalStorage(builder.build());
            if (0 == responseMsg.getCode())
                mNetworkMounted = true;
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Mount External Storage failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Mount External Storage failed.");
            return false;
        }
    }

    public int getDiagnosticsProgress() {
        return (int)getProgress("Diagnostics");
    }

    public String getDiagnosticsStatus() {
        return getStatus("Diagnostics");
    }

    private float getProgress(String type) {
        System.out.println(TAG + ".getProgress() called.");
        try {
            K3900.ProgressRequest request = K3900.ProgressRequest.getDefaultInstance();
            K3900.ProgressRequest.Builder builder = K3900.ProgressRequest.newBuilder(request);
            if (0 == type.compareTo("Media"))
                builder.setType(K3900.ProgressType.MEDIA);
            else if (0 == type.compareTo("Diagnostics"))
                builder.setType(K3900.ProgressType.DIAGNOSTICS);
            K3900.Progress responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getProgress(builder.build());
            if (100 <= responseMsg.getPercentComplete())
                mDiagnosticTestRunning = false;
            return responseMsg.getPercentComplete();
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Progress failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Get Progress failed.");
            return -1;
        }
    }

    private String getStatus(String type) {
        System.out.println(TAG + ".getStatus() called.");
        try {
            K3900.ProgressRequest request = K3900.ProgressRequest.getDefaultInstance();
            K3900.ProgressRequest.Builder builder = K3900.ProgressRequest.newBuilder(request);
            if (0 == type.compareTo("Media"))
                builder.setType(K3900.ProgressType.MEDIA);
            else if (0 == type.compareTo("Diagnostics"))
                builder.setType(K3900.ProgressType.DIAGNOSTICS);
            K3900.Progress responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getProgress(builder.build());
            if (100 <= responseMsg.getPercentComplete())
                mDiagnosticTestRunning = false;
            if (0 != responseMsg.getCode())
                return "Aborted";
            return responseMsg.getMsg();
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Get Status failed.");
            return null;
        }
    }

    public ArrayList<String> getDiagnosticTests() {
        System.out.println(TAG + ".getDiagnosticTests() called.");
        try {
            K3900.DiagnosticTestsRequest request = K3900.DiagnosticTestsRequest.getDefaultInstance();
            K3900.DiagnosticTestsRequest.Builder builder = K3900.DiagnosticTestsRequest.newBuilder(request);
            K3900.DiagnosticTestList responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getDiagnosticTests(builder.build());
            ArrayList<String> tests = new ArrayList<String>();
            for (int index = 0; index < responseMsg.getTestCount(); ++index)
                tests.add(responseMsg.getTest(index));
            return tests;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Diagnostic Tests failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Get Diagnostic Tests failed.");
            return null;
        }
    }

    public boolean onClearErrors() {
        System.out.println(TAG + ".onClearErrors() called.");
        try {
            K3900.ClearErrorsRequest request = K3900.ClearErrorsRequest.getDefaultInstance();
            K3900.ClearErrorsRequest.Builder builder = K3900.ClearErrorsRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).clearErrors(builder.build());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Clear Errors failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Clear Errors failed.");
            return false;
        }
    }

    public boolean onPerformDiagnosticTest(String type) {
        System.out.println(TAG + ".onPerformDiagnosticTest() called.");
        try {
            K3900.DiagnosticRequest request = K3900.DiagnosticRequest.getDefaultInstance();
            K3900.DiagnosticRequest.Builder builder = K3900.DiagnosticRequest.newBuilder(request);
            builder.setTest(type);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).performDiagnosticTest(builder.build());
            if (0 == responseMsg.getCode())
                mDiagnosticTestRunning = true;
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Perform Diagnostic Test failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Perform Diagnostic Test failed.");
            return false;
        }
    }

    public boolean onAbortDiagnosticTest() {
        System.out.println(TAG + ".onAbortDiagnosticTest() called.");
        try {
            K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
            K3900.BlankRequest.Builder builder = K3900.BlankRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).abortDiagnosticTest(builder.build());
            if (0 == responseMsg.getCode())
                mDiagnosticTestRunning = false;
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Abort Diagnostic Test failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Abort Diagnostic Test failed.");
            return false;
        }
    }


    /*

QVariantList BackEnd::onSave(const QVariantList save_info)
bool BeamformerClient::Save(int type, const std::string & name,
                                           uint32_t frame, uint32_t n_frames, std::string & result)

void BackEnd::onClearErrors()
void BeamformerClient::ClearErrors()
*/


    /*K3900.BeamformerParametersResponse setBeamformerParameter(String name, float ap) {
        K3900.FloatParam fparam = K3900.FloatParam.getDefaultInstance();
        K3900.FloatParam.Builder fparamBuilder = fparam.toBuilder();
        fparamBuilder.setAbsolute(true);
        fparamBuilder.setValue(ap);
        K3900.BeamformerParametersRequest request = K3900.BeamformerParametersRequest.getDefaultInstance();
        K3900.BeamformerParameter parameter = K3900.BeamformerParameter.getDefaultInstance();
        try {
            K3900.BeamformerParametersRequest.Builder parametersRequestBuilder = request.toBuilder();
            //K3900.BeamformerParametersRequest.Builder parametersRequestBuilder = K3900.BeamformerParametersRequest.newBuilder(request);
            K3900.BeamformerParameter.Builder parameterBuilder = parameter.toBuilder();
            //K3900.BeamformerParameter.Builder parameterBuilder = K3900.BeamformerParameter.newBuilder(parameter);
            parameterBuilder.setName(name);
            com.google.protobuf.Any value = com.google.protobuf.Any.getDefaultInstance();
            com.google.protobuf.Any.Builder valueBuilder = value.toBuilder();
            String stringValue = String.valueOf(ap);
            //String stringValue = parameterBuilder.toString();
            valueBuilder.setValue(ByteString.copyFrom(stringValue.getBytes()));
            //valueBuilder.mergeFrom(ByteString.copyFrom(stringValue.getBytes()));
            //valueBuilder.mergeFrom();
            parameterBuilder.setValue(valueBuilder.build());
            String test = parameterBuilder.getValue().toString();
            parametersRequestBuilder.addParameters(parameterBuilder);
            //parametersRequestBuilder.mergeFrom(fparamBuilder.build());
            //Any any = Any.pack(foo);
            K3900.BeamformerParametersResponse responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameter(parametersRequestBuilder.build());
            return responseMsg;
        } catch (Exception e) {
            throw new LostCommunicationException("Acoustic Power Change failed", e);
        }
    }*/

    boolean clearBeamformerParameterList() {
        //mRequestBuilder.clearParameters();
        mRequestBuilder.clear();
        return true;
    }

    boolean addToBeamformerParameterList(String name, float value, boolean absolute) {
        System.out.println("addToBeamformerParameterList() called.  name: " + name + "value: " + value);
        K3900.FloatParam.Builder paramBuilder = K3900.FloatParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        paramBuilder.setValue(value);
        paramBuilder.setAbsolute(absolute);
        //paramBuilder.setAbsolute(false);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName(name);
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        mRequestBuilder.addParameters(bfparamBuilder.build());
        return true;
    }

    boolean addToBeamformerParameterList(String name, int index, float value, boolean absolute) {
        System.out.println("addToBeamformerParameterList() called.  name: " + name + "value: " + value);
        K3900.FloatArrayParam.Builder paramBuilder = K3900.FloatArrayParam.newBuilder();
        paramBuilder.setIndex(index);
        paramBuilder.setValue(value);
        paramBuilder.setAbsolute(absolute);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName(name);
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        mRequestBuilder.addParameters(bfparamBuilder.build());
        return true;
    }

    boolean sendBeamformerParameterListInBatchMode() {
        try {
            K3900.BeamformerParametersResponse response = mBlockingStub.withDeadlineAfter(sBatchModeDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(mRequestBuilder.build());
            clearBeamformerParameterList();
            return 0 == response.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Send Beamformer Parameter List in Batch Mode failed, parameter name: ");
            return false;
        }
    }

    boolean setBeamformerParameter(String name, float value, boolean absolute) {
        System.out.println("setBeamformerParameter() called.  name: " + name + "value: " + value);
        K3900.FloatParam.Builder paramBuilder = K3900.FloatParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        paramBuilder.setValue(value);
        paramBuilder.setAbsolute(absolute);
        //paramBuilder.setAbsolute(false);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName(name);
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse response = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == response.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Set Beamformer Parameter failed, parameter name: " + name);
            //throw new LostCommunicationException("Set Beamformer Parameters Failed", e);
            return false;
        }
    }

    boolean setBeamformerIntParameter(String name, int value, boolean absolute) {
        K3900.IntParam.Builder paramBuilder = K3900.IntParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        paramBuilder.setValue(value);
        paramBuilder.setAbsolute(absolute);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName(name);
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse response = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == response.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Set Beamformer Parameter failed, parameter name: " + name);
            return false;
        }
    }

    boolean setBeamformerParameter(String name, float value) {
        return setBeamformerParameter(name, value, true);
        /*System.out.println("setBeamformerParameter() called.  name: " + name + "value: " + value);
        K3900.FloatParam.Builder paramBuilder = K3900.FloatParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        paramBuilder.setValue(value);
        paramBuilder.setAbsolute(true);
        //paramBuilder.setAbsolute(false);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName(name);
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse response = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == response.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Set Beamformer Parameter failed, parameter name: " + name);
            //throw new LostCommunicationException("Set Beamformer Parameters Failed", e);
            return false;
        }*/
    }

    boolean setBeamformerParameter(String name, boolean toggle) {
        System.out.println("SetBeafmormerParameters() called.  name: " + name + "toggle: " + toggle);
        K3900.ToggleParam.Builder paramBuilder = K3900.ToggleParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        //paramBuilder.    // ToDo: set toggle here?
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName(name);
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse response = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == response.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Set Beamformer Parameter failed, parameter name: " + name);
            //throw new LostCommunicationException("Set Beamformer Parameters Failed", e);
        }
        return false;
    }

    public boolean onTouchUpdate(int dx, int dy) {
        return onSendCursorMovement(dx, dy);
    }

    public boolean onZoom(float delta) {
        return onZoom(delta, false);
        /*K3900.FloatParam.Builder paramBuilder = K3900.FloatParam.newBuilder();
        paramBuilder.setValue(delta);
        paramBuilder.setAbsolute(false);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName("zoom");
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse msg =
                    mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == msg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Zoom Failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Zoom failed.");
            //return "Communication error";
            return false;
        }*/
    }

    public boolean onZoom(float delta, boolean absolute) {
        startParameterBatch();
        addToBeamformerParameterList("zoom", delta, absolute);
        boolean result = sendBeamformerParameterListInBatchMode();
        endParameterBatch();
        return result;
    }

    public boolean onStepBackward() {
        return stepFrame(-1);
    }

    public boolean onPlayPause() {
        mPaused = !mPaused;
        return onToggleRunFreeze();
    }

    public boolean onStepForward() {
        return stepFrame(1);
    }

    public int getPlaybackBufferSize() {
        return getSystemState().getImage().getPbBufferSize();
    }

    public int getPlaybackStart() {
        return getSystemState().getImage().getPbStart();
    }

    public int getCurrentFrame() {
         return getSystemState().getImage().getCurrentFrame();
    }

    public int getPlaybackSize() {
         return getSystemState().getImage().getPbSize();
    }

    public float getPixelSizeX() { return -getSystemState().getImage().getXSpacing(); }

    public float getPixelSizeY() { return getSystemState().getImage().getYSpacing(); }

    public float getZoom(){return getSystemState().getZoom();}

    public float getUpperLeftX() {
        return getSystemState().getImage().getXPosition() - getPixelSizeX() * getSystemState().getImage().getWidth()/2 + getPixelSizeX() / 2;
    }

    public float getUpperLeftY() {
        return getSystemState().getImage().getYPosition() - getPixelSizeY() * getSystemState().getImage().getHeight()/2 + getPixelSizeY() / 2;
    }

    public float getScale() {
        return getSystemState().getImage().getScale();
    }

    public int getTxApertures() {
        return getSystemState().getTxApts();
    }

    public int getRxApertures() {
        return getSystemState().getRxApts();
    }

    public String getFramePersistence() {
        return getSystemState().getFramePersistence();
    }

    public int getPingMode() {
        return getSystemState().getPingMode();
    }

    public int getFilterSelect() {
        return getSystemState().getFilterSelect();
    }

    public int getTransmitSize() {
        return getSystemState().getTransmitSize();
    }

    public float getTxFreq() {
        return getSystemState().getTxFreq();
    }

    public boolean onSave(int type, String name, int startFrame, int numberOfFrames) {
        System.out.println(TAG + ".onSave() called.");
        try {
            K3900.SaveRequest request = K3900.SaveRequest.getDefaultInstance();
            K3900.SaveRequest.Builder builder = K3900.SaveRequest.newBuilder(request);
            builder.setName(name);
            builder.setType(FileType.values()[type]);
            builder.setStartFrame(startFrame);
            builder.setNFrames(numberOfFrames);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).save(/*request*/builder.build());
            System.out.println(TAG + ".onSave(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Save failed.");
            //throw new LostCommunicationException("Save failed", e);
            return false;
        }
    }

    public boolean onToggleRunFreeze() {
        //setBeamformerParameter("live_playback", true);
        setBeamformerParameter("run_freeze", true);
         /*if (livePlaybackRequested) {
            if (!systemBoots)
                onToggleRunFreeze();
            onToggleLivePlayback();
        }
        else {
            onToggleLivePlayback();
            onToggleRunFreeze();
        }*/
        return true;
    }

    public boolean onToggleLivePlayback() {
        setBeamformerParameter("run_freeze", true);
        //setBeamformerParameter("live_playback", true);
        return true;
    }

    public boolean onToggleSos() {
        System.out.println("onToggleSos() called.");
        K3900.FloatParam.Builder paramBuilder = K3900.FloatParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        if (mSosBody) {
            paramBuilder.setValue(1450000.0f);
            mSosBody = false;
        }
        else {
            paramBuilder.setValue(1540000.0f);
            mSosBody = true;
        }
        paramBuilder.setAbsolute(true);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName("focus");
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse response = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == response.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Toggle Sos failed.");
            //throw new LostCommunicationException("Set Beamformer Parameters Failed", e);
            return false;
        }
    }

    public boolean onFocusChange(float delta) {
        return setBeamformerParameter("focus", delta, false);
    }

    public boolean onSpeedOfSoundChanged(float value) {
        return setBeamformerParameter("focus", value, true);
    }

    public boolean onTgcChanged(int index, float value) {
        System.out.println("onTgcChanged() called.  index: " + index + ",  value: " + value);
        K3900.FloatArrayParam.Builder paramBuilder = K3900.FloatArrayParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        paramBuilder.setIndex(index);
        paramBuilder.setValue(value);
        paramBuilder.setAbsolute(true);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName("tgc");
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        //K3900.BeamformerParametersResponse responseMessage = mBlockingStub.setBeamformerParameters(request.build());
        //return 0 == responseMessage.getCode();
        try {
            K3900.BeamformerParametersResponse responseMessage = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == responseMessage.getCode();
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onTgcChanged()");
            //throw new LostCommunicationException("onTgcChanged Failed", e);
            return false;
        }
    }

    public boolean onDlcChanged(float value) {
        return setBeamformerParameter("agc_offset", value);
    }

    /*public boolean onSpeedOfSoundChanged(float value) {
        return setBeamformerParameter("agc_offset", value);
    }*/

    public boolean onMasterGainChanged(float value) {
        return setBeamformerParameter("master_gain", value);
    }

    public boolean onAcousticPowerChanged(float value) {
        return setBeamformerParameter("acoustic_power", value);
    }

    public boolean onGaussianChanged(float value) {
        return setBeamformerParameter("gaussian_filter", value);
    }

    public boolean onEdgeChanged(float value) {
        return setBeamformerParameter("edge_filter", value);
    }

    public boolean onContrastChanged(float value) {
        return setBeamformerParameter("contrast", value);
    }

    public boolean onBrightnessChanged(float value) {
        return setBeamformerParameter("brightness", value);
    }

    public boolean onGammaChanged(float value) {
        return setBeamformerParameter("gamma", value);
    }

    public boolean onFrequencyChanged(float value) {
        return setBeamformerParameter("tx_freq", value);
    }

    public void onAutoContrastChanged(boolean toggle) {
        setBeamformerParameter("auto_contrast", toggle);
    }

    public boolean onLoadExamFile(String pid, String examName, String date, int type, String fileName) {
        try {
            K3900.ExamFileRequest request = K3900.ExamFileRequest.getDefaultInstance();
            K3900.ExamFileRequest.Builder builder = K3900.ExamFileRequest.newBuilder(request);
            builder.setName(fileName);
            builder.setType(FileType.values()[type]);
            builder.setPatientId(pid);
            K3900.Exam exam = K3900.Exam.getDefaultInstance();
            K3900.Exam.Builder examBuilder = K3900.Exam.newBuilder(exam);
            examBuilder.setName(examName);
            examBuilder.setDate(date);
            builder.setExam(examBuilder);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).loadExamFile(builder.build());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Load Exam File failed.");
            return false;
        }
    }

    public final ArrayList<String> onGetExamStores(String pid, String examName, String date, int type) {
        try {
            System.out.println(TAG + ".onGetExamStores() called, pid is: " + pid);
            K3900.ExamStoresRequest request = K3900.ExamStoresRequest.newBuilder().setPatientId(pid).build();
            GetExamStoresStreamObserver streamObserver;
            mStub.getExamStores(request, streamObserver = new GetExamStoresStreamObserver());
            return streamObserver.getFileNameList();
            /*K3900.ExamStore message;
            K3900.ExamStoresRequest request = K3900.ExamStoresRequest.getDefaultInstance();
            K3900.ExamStoresRequest.Builder builder = K3900.ExamStoresRequest.newBuilder(request);
            builder.setPatientId(pid);
            K3900.Exam exam = K3900.Exam.getDefaultInstance();
            K3900.Exam.Builder examBuilder = K3900.Exam.newBuilder(exam);
            examBuilder.setName(examName);
            examBuilder.setDate(date);
            builder.setExam(examBuilder);
            message =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getExamStores(builder.build();
            final ArrayList<String> fileList = new ArrayList<String>();
            fileList.add(message)*/
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetExamStores()");
        }
        return null;
    }

    public final ArrayList<String> onGetFileTypes(String pid, String examName, String date) {
        //System.out.println(TAG + ".onGetFileTypes() called, pid: " + pid + " exam:" + exam + "date: " + date);
        //K3900.ExamStoresTypeRequest request = K3900.ExamStoresTypeRequest.getDefaultInstance();
        try {
            K3900.ExamStoresTypes message;
            //probeListMsg = mBlockingStub.getProbeList(request);
            K3900.ExamStoresTypeRequest request = K3900.ExamStoresTypeRequest.getDefaultInstance();
            K3900.ExamStoresTypeRequest.Builder builder = K3900.ExamStoresTypeRequest.newBuilder(request);
            builder.setPatientId(pid);
            K3900.Exam exam = K3900.Exam.getDefaultInstance();
            K3900.Exam.Builder examBuilder = K3900.Exam.newBuilder(exam);
            examBuilder.setName(examName);
            examBuilder.setDate(date);
            builder.setExam(examBuilder);
            //K3900.ResponseMsg responseMsg;
            message =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getExamStoresTypes(/*request*/builder.build());
            final ArrayList<String> fileTypeList = new ArrayList<String>();
            if (message.getStills())
                fileTypeList.add("Image");
            if (message.getVideos())
                fileTypeList.add("Video");
            if (message.getDatasets())
                fileTypeList.add("Dataset");
            return fileTypeList;
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetFileTypes()");
            //throw new LostCommunicationException("Get File Types failed", e);
        }
        return null;
    }

        //    public java.util.Iterator<k3900.K3900.Patient> getPatients(
//            k3900.K3900.QueryRequest request)
    public final ArrayList<String> onGetPatients(String pattern) {
        System.out.println(TAG + ".onGetPatients() called, pattern is: " + pattern);
        /*try {
            final ArrayList<String> patients = new ArrayList<String>();
            K3900.QueryRequest request = K3900.QueryRequest.newBuilder().setPattern(pattern).build();
            Iterator<K3900.Patient> it =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getPatients(request);

            K3900.Patient responseMsg = null;
            do {
                responseMsg = it.next();
                String patient = null;
                patient = responseMsg.getId();
                patient += ",    ";
                patient += responseMsg.getFirst();
                patient += ",    ";
                patient += responseMsg.getLast();
                patient += ",    ";
                patient += responseMsg.getBirth();
                patient += ",    ";
                patient += responseMsg.getSex();
                patients.add(patient);
                System.out.println(LocalDateTime.now() + " : " + TAG + ".onGetPatients(), patient is: " + patient + "\n");
            } while(it.hasNext());*/
            /*K3900.Patient first_chunk = it.next();
            while (it.hasNext()) {
                K3900.Patient chunk = it.next();
            }*/

            /*while(responseMsg.hasNext()) {
                k3900.K3900.Patient current = responseMsg.next();
                String patient = null;
                patient = current.getId();
                patient += "    ";
                patient += current.getFirst();
                patient += "    ";
                patient += current.getLast();
                patient += "    ";
                patient += current.getBirth();
                patient += "    ";
                patient += current.getSex();
                patients.add(patient);
            }*/
            /*return patients;
        } catch (ImageNotAvailableException e) {
            throw e;
        } catch (Exception e) {
            throw new LostCommunicationException("Lost Communication: Get Patients failed", e);
        }*/
        K3900.QueryRequest request = K3900.QueryRequest.newBuilder().setPattern(pattern).build();
        GetPatientStreamObserver streamObserver;
        mStub.getPatients(request, streamObserver = new GetPatientStreamObserver());
        return streamObserver.getPatients();
                //.getPatients(request,, new GetPatientStreamObserver());
        /*final ArrayList<String> patients = new ArrayList<String>();
        k3900.K3900.QueryRequest request = K3900.QueryRequest.getDefaultInstance();
        try {
            request.toBuilder().setPattern(pattern);
            java.util.Iterator<k3900.K3900.Patient> responseMsg;
            responseMsg = mBlockingStub.getPatients(request);
            while(responseMsg.hasNext()) {
                k3900.K3900.Patient current = responseMsg.next();
                String patient = null;
                patient = current.getId();
                patient += "    ";
                patient += current.getFirst();
                patient += "    ";
                patient += current.getLast();
                patient += "    ";
                patient += current.getBirth();
                patient += "    ";
                patient += current.getSex();
                patients.add(patient);
            }
            //responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).userLogin(request);
            return patients;
        } catch (Exception e) {
            throw new LostCommunicationException("Get Patients failed", e);
        }*/
    }

    public final ArrayList<String> getPids(String pattern) {
        System.out.println(TAG + ".onGetPids() called, pattern is: " + pattern);
        K3900.QueryRequest request = K3900.QueryRequest.newBuilder().setPattern(pattern).build();
        GetPatientStreamObserver streamObserver;
        mStub.getPatients(request, streamObserver = new GetPatientStreamObserver());
        return streamObserver.getPids();
    }

    public final String getPid(String pattern, int index) {
        System.out.println(TAG + ".onGetPid() called, pattern is: " + pattern + ", index: " + index);
        K3900.QueryRequest request = K3900.QueryRequest.newBuilder().setPattern(pattern).build();
        GetPatientStreamObserver streamObserver;
        mStub.getPatients(request, streamObserver = new GetPatientStreamObserver());
        return streamObserver.getPid(index);
    }

    public final ArrayList<String> onGetExams(String patientId) {
        System.out.println(TAG + ".onGetExams() called, pid is: " + patientId);
        K3900.PatientRequest request = K3900.PatientRequest.newBuilder().setPatientId(patientId).build();
        GetExamStreamObserver streamObserver;
        mStub.getExams(request, streamObserver = new GetExamStreamObserver());
        return streamObserver.getExams();
    }

    public final String getExamName(String patientId, int index) {
        System.out.println(TAG + ".getExamName() called, pid is: " + patientId);
        try {
            K3900.PatientRequest request = K3900.PatientRequest.newBuilder().setPatientId(patientId).build();
            GetExamStreamObserver streamObserver;
            mStub.getExams(request, streamObserver = new GetExamStreamObserver());
            return streamObserver.getExamName(index);
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "getExamName() failed, e: " + e.getMessage());
            return null;
        }
    }

    public final String getExamDate(String patientId, int index) {
        System.out.println(TAG + ".getExamDate() called, pid is: " + patientId);
        try {
            K3900.PatientRequest request = K3900.PatientRequest.newBuilder().setPatientId(patientId).build();
            GetExamStreamObserver streamObserver;
            mStub.getExams(request, streamObserver = new GetExamStreamObserver());
            return streamObserver.getExamDate(index);
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "getExamDate() failed, e: " + e.getMessage());
            return null;
        }
    }

    public boolean onSelectFilter(int index) {
        System.out.println(TAG + ".onSelectFilter() called.");
        try {
            K3900.FilterRequest request = K3900.FilterRequest.getDefaultInstance();
            K3900.FilterRequest.Builder builder = K3900.FilterRequest.newBuilder(request);
            builder.setIndex(index);
            K3900.ResponseMsg responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).selectFilter(/*request*/builder.build());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Select Filter failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onSelectFilter()");
            return false;
        }
    }

    public ArrayList<String> onGetFilters() {
        System.out.println(TAG + ".onGetFilters() called.");
        try {
            K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
            K3900.BlankRequest.Builder builder = K3900.BlankRequest.newBuilder(request);
            K3900.FilterList filterList =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getFilterList(request);
            ArrayList<String> list = new ArrayList<String>();
            for (int index = 0; index < filterList.getFilterCount(); ++index)
                list.add(filterList.getFilter(index));
            return list;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Filter List failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetFilters()");
            return null;
        }
    }

    /*
void BackEnd::onFrequencyChanged(const qreal b)
{
    k3900::FloatParam fparam;
    fparam.set_absolute(true);
    fparam.set_value(b);
    m_BeamformerClient->SetBeamformerParameter("tx_freq", fparam);
}


BackEnd
    onFrequencyChanged(Number(f[0]))
    onSelectFilter(currentIndex)

    onGetFilters()
    QVariantList flist;
    try
    {
        auto freqs = m_BeamformerClient->GetFilters();
        for(auto & f: freqs)
        {
            flist << f.c_str();
        }
    }

BeamformerClient
        m_BeamformerClient->SelectFilter(index);
*/

    public final ArrayList<String> onGetProbeList() {
        System.out.println(TAG + ".onGetProbeList() called.");
        K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
        try {
            K3900.ProbeList probeListMsg;
            //probeListMsg = mBlockingStub.getProbeList(request);
            probeListMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getProbeList(request);
            final ArrayList<String> probeList = new ArrayList<String>();
            for (int index = 0; index < probeListMsg.getNamesCount(); ++index)
                probeList.add(probeListMsg.getNames(index));
            return probeList;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Probe List failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetProbeList()");
        }
        return null;
    }

    public boolean onSelectProbe(String name) {
        System.out.println(TAG + ".onSelectProbe() called.");
        try {
            K3900.ProbeRequest request = K3900.ProbeRequest.getDefaultInstance();
            K3900.ProbeRequest.Builder builder = K3900.ProbeRequest.newBuilder(request);
            builder.setName(name);
            K3900.ResponseMsg responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).selectProbe(/*request*/builder.build());
            System.out.println(TAG + ".onSelectProbe(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Select Probe failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onSelectProbe()");
            return false;
        }
    }

    public ArrayList<String> onGetCompressionTypes() {
        System.out.println(TAG + ".onGetCompressionTypes() called.");
        try {
            K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
            K3900.BlankRequest.Builder builder = K3900.BlankRequest.newBuilder(request);
            K3900.StringList responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getCompressionTypeList(request);
            ArrayList<String> compressionTypes = new ArrayList<String>();
            for (int index = 0; index < responseMsg.getNamesCount(); ++index)
                compressionTypes.add(responseMsg.getNames(index));
            return compressionTypes;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Compression Types failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetCompressionTypes()");
            return null;
        }
    }

    public boolean onSelectCompressionType(String name) {
        System.out.println(TAG + ".onSelectCompressionType() called.");
        try {
            K3900.NameRequest request = K3900.NameRequest.getDefaultInstance();
            K3900.NameRequest.Builder builder = K3900.NameRequest.newBuilder(request);
            builder.setName(name);
            K3900.ResponseMsg responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).selectCompressionType(/*request*/builder.build());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Select Compression Type failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onSelectCompressionType()");
            return false;
        }
    }

    public boolean onRequestSystemShutdown() {
        System.out.println(TAG + ".onRequestSystemShutdown() called.");
        try {
            K3900.ShutdownRequest request = K3900.ShutdownRequest.getDefaultInstance();
            K3900.ShutdownRequest.Builder builder = K3900.ShutdownRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).systemShutdown(request);
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Shutdown Request failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onRequestSystemShutdown()");
            return false;
        }
    }

    public boolean onStartMeasurement() {
        System.out.println(TAG + ".onStartMeasurement() called.");
        try {
            K3900.MeasurementRequest request = K3900.MeasurementRequest.getDefaultInstance();
            K3900.MeasurementRequest.Builder builder = K3900.MeasurementRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).startMeasurement(request);
            System.out.println(TAG + ".onStartMeasurement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Start Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onStartMeasurement()");
            return false;
        }
    }

    public boolean onStopMeasurement() {
        System.out.println(TAG + ".onStopMeasurement() called.");
        try {
            K3900.MeasurementRequest request = K3900.MeasurementRequest.getDefaultInstance();
            K3900.MeasurementRequest.Builder builder = K3900.MeasurementRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).stopMeasurement(request);
            System.out.println(TAG + ".onStopMeasurement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Stop Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onStopMeasurement()");
            return false;
        }
    }

    public boolean onSwapMeasurementPoints() {
        System.out.println(TAG + ".onSwapMeasurement() called.");
        try {
            K3900.MeasurementRequest request = K3900.MeasurementRequest.getDefaultInstance();
            K3900.MeasurementRequest.Builder builder = K3900.MeasurementRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).swapMeasurementPoints(request);
            System.out.println(TAG + ".onSwapMeasurement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Swap Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onSwapMeasurementPoints()");
            return false;
        }
    }

    public boolean onCaptureMeasurementMark() {
        System.out.println(TAG + ".onCaptureMeasurementMark() called.");
        try {
            K3900.MeasurementRequest request = K3900.MeasurementRequest.getDefaultInstance();
            K3900.MeasurementRequest.Builder builder = K3900.MeasurementRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).captureMeasurementMark(request);
            System.out.println(TAG + ".onCaptureMeasurementMark(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Capture Measurement Mark failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onCaptureMeasurementMark()");
            return false;
        }
    }

    public boolean onCancelMeasurement() {
        System.out.println(TAG + ".onCancelMeasurement() called.");
        try {
            K3900.MeasurementRequest request = K3900.MeasurementRequest.getDefaultInstance();
            K3900.MeasurementRequest.Builder builder = K3900.MeasurementRequest.newBuilder(request);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).cancelMeasurement(request);
            System.out.println(TAG + ".onCancelMeasurement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Cancel Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onCancelMeasurement()");
            return false;
        }
    }

    public boolean onDeleteMeasurement(int index) {
        System.out.println(TAG + ".onDeleteMeasurement() called.");
        try {
            K3900.EditMeasurementRequest request = K3900.EditMeasurementRequest.getDefaultInstance();
            K3900.EditMeasurementRequest.Builder builder = K3900.EditMeasurementRequest.newBuilder(request);
            builder.setIdx(index);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).deleteMeasurement(request);
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Delete Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onDeleteMeasurement()");
            return false;
        }
    }

    public boolean onEditMeasurement(int index) {
        System.out.println(TAG + ".onEditMeasurement() called.");
        try {
            K3900.EditMeasurementRequest request = K3900.EditMeasurementRequest.getDefaultInstance();
            K3900.EditMeasurementRequest.Builder builder = K3900.EditMeasurementRequest.newBuilder(request);
            builder.setIdx(index);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).editMeasurement(request);
            System.out.println(TAG + ".onEditMeasurement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Edit Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onEditMeasurement()");
            return false;
        }
    }

    public boolean onHighlightMeasurement(int index) {
        System.out.println(TAG + ".onHighlightMeasurement() called.");
        try {
            K3900.EditMeasurementRequest request = K3900.EditMeasurementRequest.getDefaultInstance();
            K3900.EditMeasurementRequest.Builder builder = K3900.EditMeasurementRequest.newBuilder(request);
            builder.setIdx(index);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).highlightMeasurement(request);
            System.out.println(TAG + ".onHighlightMeasurement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Highlight Measurement failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onHighlightMeasurement()");
            return false;
        }
    }

    public boolean onSendCursorMovement(int dx, int dy) {
        System.out.println(TAG + ".onSendCursorMovement() called, dx: " + dx + ", dy: " + dy);
        try {
            K3900.CursorMoveRequest request = K3900.CursorMoveRequest.getDefaultInstance();
            K3900.CursorMoveRequest.Builder builder = K3900.CursorMoveRequest.newBuilder(request);
            K3900.Pixel pixel = K3900.Pixel.getDefaultInstance();
            K3900.Pixel.Builder pixelBuilder = K3900.Pixel.newBuilder(pixel);
            pixelBuilder.setX(dx);
            pixelBuilder.setY(dy);
            builder.setDelta(pixelBuilder.build());
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).sendCursorMovement(builder.build());
            System.out.println(TAG + ".onSendCursorMovement(), server returned code: " + responseMsg.getCode());
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onSendCursorMovement()");
            //throw new LostCommunicationException("Send Cursor Movement failed", e);
            return false;
        }
    }

    public final ArrayList<Float> onGetMeasurements() {
        System.out.println(TAG + ".onGetMeasurements() called.");
        try {
            K3900.MeasurementRequest request = K3900.MeasurementRequest.getDefaultInstance();
            K3900.MeasurementRequest.Builder builder = K3900.MeasurementRequest.newBuilder(request);
            K3900.Measurements responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getMeasurements(request);
            final ArrayList<Float> measurements = new ArrayList<Float>();
            for (int index = 0; index < responseMsg.getMeasurementsCount(); ++index)
                measurements.add(responseMsg.getMeasurements(index).getLength());
            return measurements;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Measurements failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetMeasurements()");
            return null;
        }
    }

    public final ArrayList<Integer> onGetMeasurementFrames() {
        System.out.println(TAG + ".onGetMeasurementFrames() called.");
        try {
            K3900.FrameRequest request = K3900.FrameRequest.getDefaultInstance();
            K3900.FrameRequest.Builder builder = K3900.FrameRequest.newBuilder(request);
            K3900.FrameMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getMeasurementFrames(request);
            final ArrayList<Integer> measurements = new ArrayList<Integer>();
            for (int index = 0; index < responseMsg.getFramesCount(); ++index)
                measurements.add(responseMsg.getFrames(index));
            return measurements;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get Measurement Frames failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetMeasurementFrames()");
            return null;
        }
    }

    public boolean onChangeTxMask(boolean on, int element) {
        System.out.println(TAG + ".onChangeTxMask() called.");
        try {
            K3900.MaskMsg request = K3900.MaskMsg.getDefaultInstance();
            K3900.MaskMsg.Builder builder = K3900.MaskMsg.newBuilder(request);
            builder.setOn(on);
            builder.setElement(element);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).changeTxMask(request);
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Change Tx Mask failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onChangeTxMask()");
            return false;
        }
    }

    public boolean onChangeRxMask(boolean on, int element) {
        System.out.println(TAG + ".onChangeRxMask() called.");
        try {
            K3900.MaskMsg request = K3900.MaskMsg.getDefaultInstance();
            K3900.MaskMsg.Builder builder = K3900.MaskMsg.newBuilder(request);
            builder.setOn(on);
            builder.setElement(element);
            K3900.ResponseMsg responseMsg;
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).changeRxMask(request);
            return 0 == responseMsg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Change Rx Mask failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onChangeRxMask()");
            return false;
        }
    }

    //public ArrayList<String> onGetTxMask() {
        /*ArrayList<String> txMaskArrayList = new ArrayList<String>();
        K3900.MaskMsg maskMsg = null;
        final StreamObserver<K3900.MaskMsg> maskMsgStreamObserver = new StreamObserver<K3900.K3900.MaskMsg>();
        StreamObserver<K3900.BlankRequest> streamObserver = getTxMask(maskMsgStreamObserver);
        maskMsgStreamObserver.
        return txMaskArrayList;*/
        //return null;
    //}

    //@Override
    public StreamObserver<K3900.BlankRequest> getTxMask(final StreamObserver<K3900.MaskMsg> responseObserver) {
        /*return new StreamObserver<K3900.BlankRequest>() {
            int pointCount;
            ArrayList<Iterator<K3900.MaskMsg>> maskMessageIteratorArrayList;
            int featureCount;
            int distance;
            K3900.BlankRequest previous;
            long startTime = System.nanoTime();

            public ArrayList<Iterator<K3900.MaskMsg>> getTxMask() {
                return maskMessageIteratorArrayList;
            }

            @Override
            public void onNext(K3900.BlankRequest point) {
                pointCount++;
                maskMessageIteratorArrayList.add(mBlockingStub.getTxMask(point));*/
                /*if (RouteGuideUtil.exists(checkFeature(point))) {
                    featureCount++;
                }*/
                // For each point after the first, add the incremental distance from the previous point
                // to the total distance value.
                /*if (previous != null) {
                    distance += calcDistance(previous, point);
                }*/
                /*previous = point;
            }

            @Override
            public void onError(Throwable t) {
                //logger.log(Level.WARNING, "Encountered error in recordRoute", t);
                System.out.println(TAG + "Error.");
            }

            @Override
            public void onCompleted() {
                //long seconds = NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                responseObserver.onNext(K3900.MaskMsg.getDefaultInstance());
                responseObserver.onCompleted();
            }
        };*/
        return null;
    }

    public String onGetAbout() {
        K3900.BlankRequest request = K3900.BlankRequest.getDefaultInstance();
        try {
            K3900.AboutMsg responseMsg;
            //aboutMsg = mBlockingStub.getAbout(request);
            responseMsg =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getAbout(request);
            return responseMsg.getText();
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in onGetAbout()");
            //throw new LostCommunicationException("Get About failed", e);
        }
        return "Failed in getting system information.";
    }

    public void setContrast(float delta)
    {
        K3900.FloatParam.Builder paramBuilder = K3900.FloatParam.newBuilder();//.setValue(delta).setAbsolute(false).build();
        paramBuilder.setValue(delta);
        paramBuilder.setAbsolute(false);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName("contrast");
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse msg =
                    mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
        } catch (Exception e) {
            //throw new LostCommunicationException("Set Contrast Failed", e);
            //return "Communication error";
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in setContrast()");
        }
    }

    private boolean stepFrame(int delta)
    {
        K3900.IntParam.Builder paramBuilder = K3900.IntParam.newBuilder();
        paramBuilder.setValue(delta);
        paramBuilder.setAbsolute(false);
        K3900.BeamformerParameter.Builder bfparamBuilder = K3900.BeamformerParameter.newBuilder();
        bfparamBuilder.setName("step_frame");
        bfparamBuilder.setValue(Any.pack(paramBuilder.build()));
        K3900.BeamformerParametersRequest.Builder request = K3900.BeamformerParametersRequest.newBuilder();
        request.addParameters(bfparamBuilder.build());

        try {
            K3900.BeamformerParametersResponse msg =
                    mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).setBeamformerParameters(request.build());
            return 0 == msg.getCode();
        } catch (Exception e) {
            //throw new LostCommunicationException("Step Frame Failed", e);
            //return "Communication error";
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in stepFrame()");
            return false;
        }
    }

    public boolean onIncrementTx3Apertures() {
        return setBeamformerIntParameter("tx_apts", 1, false);
    }

    public boolean onDecrementTx3Apertures() {
        return setBeamformerIntParameter("tx_apts", -1, false);
    }

    public boolean onIncrementRx3Apertures() {
        return setBeamformerIntParameter("rx_apts", 1, false);
    }

    public boolean onDecrementRx3Apertures() {
        return setBeamformerIntParameter("rx_apts", -1, false);
    }

    public boolean onIncrementPingPersistence() {
        return setBeamformerIntParameter("ping_mode", 1, false);
    }

    public boolean onDecrementPingPersistence() {
        return setBeamformerIntParameter("ping_mode", -1, false);
    }

    public boolean onIncrementFramePersistence() {
        return setBeamformerIntParameter("frame_persistence", 1, false);
    }

    public boolean onDecrementFramePersistence() {
        return setBeamformerIntParameter("frame_persistence", -1, false);
    }

    public boolean setNumberOfTxElements(int value) {
        return setBeamformerIntParameter("transmit_size", value, true);
    }

    String ping() {
        try {
            K3900.ResponseMsg msg =
                    mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).pingServer(K3900.BlankRequest.newBuilder().build());
            return msg.getMsg();
        } catch (Exception e) {
            //throw new LostCommunicationException("Ping failed", e);
            //return "Communication error";
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in ping()");
            return null;
        }
    }

    String getProbeName() {
        try {
            return getSystemState().getProbeName();
        } catch (Exception e) {
            return "";
        }
    }

    boolean getRunState() {
        try {
            return getSystemState().getRun();
        } catch (Exception e) {
            return false;
        }
    }

    boolean getPlaybackState() {
        try {
            return getSystemState().getPlayback();
        } catch (Exception e) {
            return false;
        }
    }

    boolean getAutoContrastState() {
        try {
            return getSystemState().getAutoContrast();
        } catch (Exception e) {
            return false;
        }
    }

    K3900.SystemState getSystemState() {
        try {
            //if (null == mBlockingStub)
                //throw new java.lang.NullPointerException();
            if (null == m_systemState)
                m_systemState = mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getSystemState(K3900.SystemStateRequest.newBuilder().build());
            return m_systemState;
        } catch (NullPointerException e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost communication in getSystemState()");
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Get system state failed.");
            //throw new LostCommunicationException("Get system state failed", e);
        }
        return null;
    }

    void updateSystemState() {
        try {
            if (mParameterBatch)
                return;
            m_systemState =  mBlockingStub.withDeadlineAfter(sDeadLineMilliSeconds, TimeUnit.MILLISECONDS).getSystemState(K3900.SystemStateRequest.newBuilder().build());
            if (null != m_systemState)
                mSosBody = abs(m_systemState.getSos() - 1540.0) < abs(m_systemState.getSos() - 1450.0);
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Update system state failed.");
            //throw new LostCommunicationException("Update system state failed", e);
        }
    }

    public int getFrameRate() {
        try {
            int frameRate = getSystemState().getFps();
            if (0 < frameRate)
                mFrameRate = frameRate;
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "get frame rate failed.");
        }
        return mFrameRate;
    }

    /*private float getPosition(float value, float min, float max) {
        K3900.SystemState state = getSystemState();
        return (value - min) / (max - min);
    }*/

    public float getTgcValue(int index) {
        try {
            return getSystemState().getTgc(index);
        } catch (Exception e) {
            return -1;
        }
    }

    public float getDlcValue() {
        try {
            return getSystemState().getAgcOffset();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getMasterGainValue() {
        try {
            return getSystemState().getMasterGain();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getVgaGainValue() {
        try {
            return getSystemState().getVgaGain();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getAcousticPowerValue() {
        try {
            return getSystemState().getAcousticPower();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getGaussianValue() {
        try {
            return getSystemState().getGaussianFilter();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getEdgeValue() {
        try {
            return getSystemState().getEdgeFilter();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getContrastValue() {
        try {
            return getSystemState().getContrast();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getBrightnessValue() {
        try {
            return getSystemState().getBrightness();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getGammaValue() {
        try {
            return getSystemState().getGamma();
        } catch (Exception e) {
            return -1;
        }
    }

    public float getSpeedOfSoundValue() {
        try {
            return getSystemState().getSos();
        } catch (Exception e) {
            return -1;
        }
    }

    public final String onGetSystemState(int index) {
        try {
            K3900.SystemState systemState = getSystemState();
            Any device = systemState.getDeviceStatus(index);
            String status = null;
            if (device.is(K3900.FpgaStatus.class)) {
                K3900.FpgaStatus.Builder fpgaBuilder = K3900.FpgaStatus.newBuilder();
                K3900.FpgaStatus ds = null;
                device.unpack(ds.getClass());
                status = "FPCA";
                status += "\n";
                status += ds.getId();
                status += "\n";
                String errors = null;
                for (int errorIndex = 0; errorIndex < ds.getErrorsCount(); ++errorIndex) {
                    errors += ds.getErrors(errorIndex);
                    errors += "\n";
                }
                status += errors;
                status += "\n";
            }
            else if (device.is(K3900.SystemStatus.class)) {
                K3900.SystemStatus.Builder systemStatusBuilder = K3900.SystemStatus.newBuilder();
                K3900.SystemStatus ss = null;
                device.unpack(ss.getClass());
                status = "SYSTEM";
                status += "\n";
                status += ss.getName();
                String errors = null;
                for (int errorIndex = 0; errorIndex < ss.getErrorsCount(); ++errorIndex) {
                    errors += ss.getErrors(errorIndex);
                    errors += "\n";
                }
                status += errors;
                status += "\n";
            }
            return status;
        } catch (Exception e) {
            //throw new LostCommunicationException("Get System State failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Get system state failed.");
            return null;
        }
    }

    public final ArrayList<String> onGetSystemStateList() {
        K3900.SystemState systemState = getSystemState();
        final ArrayList<String> states = new ArrayList<String>();
        try {
            for (int index = 0; index < systemState.getDeviceStatusCount(); ++index) {
                Any device = systemState.getDeviceStatus(index);
                if (device.is(K3900.FpgaStatus.class)) {
                    K3900.FpgaStatus.Builder fpgaBuilder = K3900.FpgaStatus.newBuilder();
                    K3900.FpgaStatus ds = null;
                    device.unpack(ds.getClass());
                    String deviceStatus = "FPCA";
                    deviceStatus += "    ";
                    deviceStatus += ds.getId();
                    deviceStatus += "    ";
                    deviceStatus += ds.getTemp();
                    deviceStatus += "    ";
                    deviceStatus += ds.getVccint();
                    deviceStatus += "    ";
                    deviceStatus += ds.getVccaux();
                    deviceStatus += "    ";
                    deviceStatus += ds.getVccbram();
                    deviceStatus += "    ";
                    deviceStatus += ds.getVersion();
                    String errors = null;
                    for (int errorIndex = 0; errorIndex < ds.getErrorsCount(); ++errorIndex) {
                        errors += ds.getErrors(errorIndex);
                        errors += "    ";
                    }
                    states.add(errors);
                    states.add(deviceStatus);
                }
                else if (device.is(K3900.SystemStatus.class)) {
                    K3900.SystemStatus.Builder systemStatusBuilder = K3900.SystemStatus.newBuilder();
                    K3900.SystemStatus ss = null;
                    device.unpack(ss.getClass());
                    String deviceStatus = "SYSTEM";
                    deviceStatus += "    ";
                    deviceStatus += ss.getName();
                    String errors = null;
                    for (int errorIndex = 0; errorIndex < ss.getErrorsCount(); ++errorIndex) {
                        errors += ss.getErrors(errorIndex);
                        errors += "    ";
                    }
                    states.add(errors);
                    states.add(deviceStatus);
                }
            }
        } catch (Exception e) {
            //throw new LostCommunicationException("Get System State List failed", e);
            Log.d(TAG, LocalDateTime.now() + " : " + "Get system state list failed.");
        }
        return states;
    }

    //Image getImage(K3900.ImageRequest.Builder request) {
    Image getImage(long nextTime) {
        Log.d(TAG, "getImage() time: " + nextTime);
        //Log.d(TAG, "getImage() nextTime: " + request.getTime());
        K3900.ImageRequest.Builder imageRequest;
        imageRequest = K3900.ImageRequest.newBuilder().setTime(nextTime);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (imageRequest.getChunkSize() == 0) {
            imageRequest.setChunkSize(64);
        }
        long time = 0;
        try {
            /*if (mWifiDirectListChanged) {
                disconnect();
                connect(m_address, m_port);
                mWifiDirectListChanged = false;
            }*/
            Log.d(TAG, "mBlockingStub: " + mBlockingStub);
            Iterator<K3900.ImageChunk> it =  mBlockingStub.withDeadlineAfter(sGetImageDeadLineInMilliSeconds, TimeUnit.MILLISECONDS).sendBeamformedImageStream(imageRequest.build());
            Log.d(TAG, "it: " + it);

            K3900.ImageChunk first_chunk = it.next();
            Log.d(TAG, "first_chunk: " + first_chunk);
            Log.d(TAG, "GOT HERE");
            time = first_chunk.getTime();
            Log.d(TAG, "Time = " + time);
            output.write(first_chunk.getPixels().toByteArray());
            //Log.d(TAG, "Before while()");
            while (it.hasNext()) {
                //Log.d(TAG, "Inside while()");
                K3900.ImageChunk chunk = it.next();
                Log.d(TAG, "chunk: " + chunk);
                output.write(chunk.getPixels().toByteArray());
                /*mPbBufferSize = chunk.getPbBufferSize();
                mPbSize = chunk.getPbSize();
                mPbStart = chunk.getPbStart();
                mCurrentFrame = chunk.getCurrentFrame();*/
                //break;
                updateCineLoop(chunk);
            }
            if (output.size() == 0) {
                //throw new ImageNotAvailableException("no image");
                Log.d(TAG, LocalDateTime.now() + " : " + "Image Not Available at Time: " + nextTime + " output size is 0.");
                //Log.d(TAG, LocalDateTime.now() + " : " + "Image Not Available at Time: " + request.getTime() + " output size is 0.");
                return null;
            }
            return new Image(output.toByteArray(), time);
        } catch (ImageNotAvailableException e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Image Not Available at Time: " + nextTime);
            //Log.d(TAG, LocalDateTime.now() + " : " + "Image Not Available at Time: " + request.getTime());
            //throw e;
        } catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "Lost Communication: Get image failed at Time: " + nextTime);
            return null;
            //throw new LostCommunicationException("Lost Communication: Get image failed", e);
        }
        return null;
    }

    public final Image onGetImage(K3900.ImageRequest.Builder request) {
        System.out.println(TAG + ".onGetImage() called.");
        //K3900.ImageRequest request = K3900.ImageRequest.newBuilder().build();
        GetImageStreamObserver streamObserver;
        if (null != mStub) {
            mStub.sendBeamformedImageStream(request.build(), streamObserver = new GetImageStreamObserver());
            return streamObserver.getImage();
        }
        return null;
    }

    private void updateCineLoop(final K3900.ImageChunk imageChunk) {
        CineLoop.setPbBuffSize(imageChunk.getPbBufferSize());
        CineLoop.setPbSize(imageChunk.getPbSize());
        CineLoop.setPbStart(imageChunk.getPbStart());
        CineLoop.setCurrentFrame(imageChunk.getCurrentFrame());
        //CineLoop.update();
    }

}
