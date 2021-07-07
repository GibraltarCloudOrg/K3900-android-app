package com.example.mauiviewcontrol;

import android.util.Log;

import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannelBuilder;
import k3900.BeamformerGrpc;
//import k3900.K3900;

public class SwitchBackEndModel {
    public enum MessageTo { BeamformerClient, BatchMode, UnitTesting }

    public static SwitchBackEndModel getSwitchBackEndModelSingletonInstance() {
        if (null == singletonInstance)
            singletonInstance = new SwitchBackEndModel();
        return singletonInstance;
    }

    public void setMessageTo(MessageTo messageTo) {
        mMessageTo = messageTo;
    }

    public String getParameterValuesInString() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getParameterValuesInString();
            case UnitTesting:
                return mUnitTestingModel.getParameterValuesInString();
            default:
                break;
        }
        return null;
    }

    public final ArrayList<Boolean> onGetTxMask(){
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetTxMask();
            case UnitTesting:
                return mUnitTestingModel.onGetTxMask();
            default:
                break;
        }
        return null;
    }

    public final ArrayList<Boolean>onGetRxMask(){
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetRxMask();
            case UnitTesting:
                return mUnitTestingModel.onGetRxMask();
            default:
                break;
        }
        return null;
    }

    public final void updateRxMask(){
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.updateRxMask();
            case UnitTesting:
                mUnitTestingModel.onGetRxMask();
            default:
                break;
        }
    }

    public final void updateTxMask(){
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.updateTxMask();
            case UnitTesting:
                mUnitTestingModel.onGetTxMask();
            default:
                break;
        }
    }

    public GetMaskMsgStreamObserver getTxElementStreamer() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getTxElementStreamer();
            case UnitTesting:
                return mUnitTestingModel.getTxElementStreamer();
            default:
                break;
        }
        return null;
    }

    public GetMaskMsgStreamObserver getRxElementStreamer() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getRxElementStreamer();
            case UnitTesting:
                return mUnitTestingModel.getRxElementStreamer();
            default:
                break;
        }
        return null;
    }

    public boolean isTxRunning()
    {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.isTxRunning();
            case UnitTesting:
                return mUnitTestingModel.isRunning();
            default:
                break;
        }
        return false;
    }

    public boolean isRxRunning()
    {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.isRxRunning();
            case UnitTesting:
                return mUnitTestingModel.isRunning();
            default:
                break;
        }
        return false;
    }

    public boolean onChangeRxMask(int elementInt, boolean status){
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onChangeRxMask(elementInt, status);
            case UnitTesting:
                return mUnitTestingModel.onChangeRxMask(elementInt, status);
            default:
                break;
        }
        return false;
    }

    public boolean onChangeTxMask(int elementInt, boolean status){
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onChangeTxMask(elementInt, status);
            case UnitTesting:
                return mUnitTestingModel.onChangeTxMask(elementInt, status);
            default:
                break;
        }
        return false;
    }

    public boolean onPan(float x, float y) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onPan(x, y);
            case UnitTesting:
                return mUnitTestingModel.onPan(x, y);
            default:
                break;
        }
        return false;
    }

    public boolean onCenterImage() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onCenterImage();
            case UnitTesting:
                return mUnitTestingModel.onCenterImage();
            default:
                break;
        }
        return false;
    }

    public boolean onHomeImage() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onHomeImage();
            case UnitTesting:
                return mUnitTestingModel.onHomeImage();
            default:
                break;
        }
        return false;
    }

    public boolean onTgcCenter() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onTgcCenter();
            case UnitTesting:
                return mUnitTestingModel.onTgcCenter();
            default:
                break;
        }
        return false;
    }

    public boolean resetAll() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.resetAll();
            case UnitTesting:
                return mUnitTestingModel.resetAll();
            default:
                break;
        }
        return false;
    }

    public String onUserLogIn(String userName, String password) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onUserLogIn(userName, password);
            case UnitTesting:
                return mUnitTestingModel.onUserLogIn(userName, password);
            default:
                break;
        }
        return "";
    }

    public String onUserLogOut() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onUserLogOut();
            case UnitTesting:
                return mUnitTestingModel.onUserLogOut();
            default:
                break;
        }
        return "";
    }

    public boolean loggedIn() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.loggedIn();
            case UnitTesting:
                return mUnitTestingModel.loggedIn();
            default:
                break;
        }
        return false;
    }

    public boolean paused() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.paused();
            case UnitTesting:
                return mUnitTestingModel.paused();
            default:
                break;
        }
        return false;
    }

    public boolean isDriveTypeNetwork() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.isDriveTypeNetwork();
            case UnitTesting:
                return mUnitTestingModel.isDriveTypeNetwork();
            default:
                break;
        }
        return false;
    }

    public boolean isUnitTypeMetric() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.isUnitTypeMetric();
            case UnitTesting:
                return mUnitTestingModel.isUnitTypeMetric();
            default:
                break;
        }
        return false;
    }

    public String getUnitName() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUnitName();
            case UnitTesting:
                return mUnitTestingModel.getUnitName();
            default:
                break;
        }
        return null;
    }

    public boolean networkMounted() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.networkMounted();
            case UnitTesting:
                return mUnitTestingModel.networkMounted();
            default:
                break;
        }
        return false;
    }

    public boolean connected() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.connected();
            case UnitTesting:
                return mUnitTestingModel.connected();
            default:
                break;
        }
        return false;
    }

    public boolean imageAvailable() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.imageAvailable();
            case UnitTesting:
                return mUnitTestingModel.imageAvailable();
            default:
                break;
        }
        return false;
    }

    public boolean examInProcess() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.examInProcess();
            case UnitTesting:
                return mUnitTestingModel.examInProcess();
            default:
                break;
        }
        return false;
    }

    public boolean isDiagnosticTestRunning() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.isDiagnosticTestRunning();
            case UnitTesting:
                return mUnitTestingModel.isDiagnosticTestRunning();
            default:
                break;
        }
        return false;
    }

    public String onGetAbout() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetAbout();
            case UnitTesting:
                return mUnitTestingModel.onGetAbout();
            default:
                break;
        }
        return null;
    }

    public void setContrast(float delta)
    {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.setContrast(delta);
                break;
            case UnitTesting:
                mUnitTestingModel.setContrast(delta);
                break;
            default:
                break;
        }
    }

    public boolean onIncrementTx3Apertures() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onIncrementTx3Apertures();
            case UnitTesting:
                return mUnitTestingModel.onIncrementTx3Apertures();
            default:
                break;
        }
        return false;
    }

    public boolean onDecrementTx3Apertures() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onDecrementTx3Apertures();
            case UnitTesting:
                return mUnitTestingModel.onDecrementTx3Apertures();
            default:
                break;
        }
        return false;
    }

    public boolean onIncrementRx3Apertures() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onIncrementRx3Apertures();
            case UnitTesting:
                return mUnitTestingModel.onIncrementRx3Apertures();
            default:
                break;
        }
        return false;
    }

    public boolean onDecrementRx3Apertures() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onDecrementRx3Apertures();
            case UnitTesting:
                return mUnitTestingModel.onDecrementRx3Apertures();
            default:
                break;
        }
        return false;
    }

    public boolean onIncrementPingPersistence() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onIncrementPingPersistence();
            case UnitTesting:
                return mUnitTestingModel.onIncrementPingPersistence();
            default:
                break;
        }
        return false;
    }

    public boolean onDecrementPingPersistence() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onDecrementPingPersistence();
            case UnitTesting:
                return mUnitTestingModel.onDecrementPingPersistence();
            default:
                break;
        }
        return false;
    }

    public boolean onIncrementFramePersistence() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onIncrementFramePersistence();
            case UnitTesting:
                return mUnitTestingModel.onIncrementFramePersistence();
            default:
                break;
        }
        return false;
    }

    public boolean onDecrementFramePersistence() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onDecrementFramePersistence();
            case UnitTesting:
                return mUnitTestingModel.onDecrementFramePersistence();
            default:
                break;
        }
        return false;
    }

    public boolean setNumberOfTxElements(int value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.setNumberOfTxElements(value);
            case UnitTesting:
                return mUnitTestingModel.setNumberOfTxElements(value);
            default:
                break;
        }
        return false;
    }

    String ping() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.ping();
            case UnitTesting:
                return mUnitTestingModel.ping();
            default:
                break;
        }
        return "";
    }

    /*K3900.SystemState getSystemState() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getSystemState();
            case UnitTesting:
                return mUnitTestingModel.getSystemState();
            default:
                break;
        }
        return null;
    }*/

    String getProbeName() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getProbeName();
            case UnitTesting:
                return mUnitTestingModel.getProbeName();
            default:
                break;
        }
        return "";
    }

    boolean getRunState() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getRunState();
            case UnitTesting:
                return mUnitTestingModel.getRunState();
            default:
                break;
        }
        return false;
    }

    boolean getPlaybackState() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPlaybackState();
            case UnitTesting:
                return mUnitTestingModel.getPlaybackState();
            default:
                break;
        }
        return false;
    }

    boolean getAutoContrastState() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getAutoContrastState();
            case UnitTesting:
                return mUnitTestingModel.getAutoContrastState();
            default:
                break;
        }
        return false;
    }

    void updateSystemState() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.updateSystemState();
                break;
            case UnitTesting:
                mUnitTestingModel.updateSystemState();
                break;
            default:
                break;
        }
    }

    public int getFrameRate() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getFrameRate();
            case UnitTesting:
                return mUnitTestingModel.getFrameRate();
            default:
                break;
        }
        return -1;
    }

    public float getTgcValue(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getTgcValue(index);
            case UnitTesting:
                return mUnitTestingModel.getTgcValue(index);
            default:
                break;
        }
        return -1;
    }

    public float getDlcValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getDlcValue();
            case UnitTesting:
                return mUnitTestingModel.getDlcValue();
            default:
                break;
        }
        return -1;
    }

    public float getZoom() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getZoom();
            case UnitTesting:
                return mUnitTestingModel.getZoom();
            default:
                break;
        }
        return -1;
    }

    public float getMasterGainValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getMasterGainValue();
            case UnitTesting:
                return mUnitTestingModel.getMasterGainValue();
            default:
                break;
        }
        return -1;
    }

    public float getVgaGainValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getVgaGainValue();
            case UnitTesting:
                return mUnitTestingModel.getVgaGainValue();
            default:
                break;
        }
        return -1;
    }

    public float getAcousticPowerValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getAcousticPowerValue();
            case UnitTesting:
                return mUnitTestingModel.getAcousticPowerValue();
            default:
                break;
        }
        return -1;
    }

    public float getGaussianValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getGaussianValue();
            case UnitTesting:
                return mUnitTestingModel.getGaussianValue();
            default:
                break;
        }
        return -1;
    }

    public float getEdgeValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getEdgeValue();
            case UnitTesting:
                return mUnitTestingModel.getEdgeValue();
            default:
                break;
        }
        return -1;
    }

    public float getContrastValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getContrastValue();
            case UnitTesting:
                return mUnitTestingModel.getContrastValue();
            default:
                break;
        }
        return -1;
    }

    public float getBrightnessValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getBrightnessValue();
            case UnitTesting:
                return mUnitTestingModel.getBrightnessValue();
            default:
                break;
        }
        return -1;
    }

    public float getGammaValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getGammaValue();
            case UnitTesting:
                return mUnitTestingModel.getGammaValue();
            default:
                break;
        }
        return -1;
    }

    public float getSpeedOfSoundValue() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getSpeedOfSoundValue();
            case UnitTesting:
                return mUnitTestingModel.getSpeedOfSoundValue();
            default:
                break;
        }
        return -1;
    }

    public boolean onTouchUpdate(int dx, int dy) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onTouchUpdate(dx, dy);
            case UnitTesting:
                return mUnitTestingModel.onTouchUpdate(dx, dy);
            default:
                break;
        }
        return false;
    }

    public boolean onZoom(float delta) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onZoom(delta);
            case UnitTesting:
                return mUnitTestingModel.onZoom(delta);
            default:
                break;
        }
        return false;
    }

    public boolean onZoom(float delta, boolean absolute) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onZoom(delta, absolute);
            case UnitTesting:
                return mUnitTestingModel.onZoom(delta, absolute);
            default:
                break;
        }
        return false;
    }

    public boolean onStepBackward() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onStepBackward();
            case UnitTesting:
                return mUnitTestingModel.onStepBackward();
            default:
                break;
        }
        return false;
    }

    public boolean onPlayPause() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onPlayPause();
            case UnitTesting:
                return mUnitTestingModel.onPlayPause();
            default:
                break;
        }
        return false;
    }

    public boolean onStepForward() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onStepForward();
            case UnitTesting:
                return mUnitTestingModel.onStepForward();
            default:
                break;
        }
        return false;
    }

    public int getPlaybackBufferSize() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPlaybackBufferSize();
            case UnitTesting:
                return mUnitTestingModel.getPlaybackBufferSize();
            default:
                break;
        }
        return -1;
    }

    public int getPlaybackStart() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPlaybackStart();
            case UnitTesting:
                return mUnitTestingModel.getPlaybackStart();
            default:
                break;
        }
        return -1;
    }

    public int getCurrentFrame() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getCurrentFrame();
            case UnitTesting:
                return mUnitTestingModel.getCurrentFrame();
            default:
                break;
        }
        return -1;
    }

    public int getPlaybackSize() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPlaybackSize();
            case UnitTesting:
                return mUnitTestingModel.getPlaybackSize();
            default:
                break;
        }
        return -1;
    }

    public float getPixelSizeX() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPixelSizeX();
            case UnitTesting:
                return mUnitTestingModel.getPixelSizeX();
            default:
                break;
        }
        return 0;
    }

    public float getPixelSizeY() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPixelSizeY();
            case UnitTesting:
                return mUnitTestingModel.getPixelSizeY();
            default:
                break;
        }
        return 0;
    }

    public float getUpperLeftX() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUpperLeftX();
            case UnitTesting:
                return mUnitTestingModel.getUpperLeftX();
            default:
                break;
        }
        return 0;
    }

    public float getUpperLeftY() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUpperLeftY();
            case UnitTesting:
                return mUnitTestingModel.getUpperLeftY();
            default:
                break;
        }
        return 0;
    }

    public float getScale() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getScale();
            case UnitTesting:
                return mUnitTestingModel.getScale();
            default:
                break;
        }
        return 0;
    }

    public int getTxApertures() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getTxApertures();
            case UnitTesting:
                return mUnitTestingModel.getTxApertures();
            default:
                break;
        }
        return 0;
    }

    public int getRxApertures() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getRxApertures();
            case UnitTesting:
                return mUnitTestingModel.getRxApertures();
            default:
                break;
        }
        return 0;
    }

    public String getFramePersistence() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getFramePersistence();
            case UnitTesting:
                return mUnitTestingModel.getFramePersistence();
            default:
                break;
        }
        return null;
    }

    public int getPingMode() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPingMode();
            case UnitTesting:
                return mUnitTestingModel.getPingMode();
            default:
                break;
        }
        return 0;
    }

    public int getFilterSelect() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getFilterSelect();
            case UnitTesting:
                return mUnitTestingModel.getFilterSelect();
            default:
                break;
        }
        return 0;
    }

    public int getTransmitSize() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getTransmitSize();
            case UnitTesting:
                return mUnitTestingModel.getTransmitSize();
            default:
                break;
        }
        return 0;
    }

    public float getTxFreq() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getTxFreq();
            case UnitTesting:
                return mUnitTestingModel.getTxFreq();
            default:
                break;
        }
        return 0;
    }

    public boolean onSave(int type, String name, int startFrame, int numberOfFrames) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSave(type, name, startFrame, numberOfFrames);
            case UnitTesting:
                return mUnitTestingModel.onSave(type, name, startFrame, numberOfFrames);
            default:
                break;
        }
        return false;
    }

    public boolean onToggleRunFreeze() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onToggleRunFreeze();
            case UnitTesting:
                return mUnitTestingModel.onToggleRunFreeze();
            default:
                break;
        }
        return false;
    }

    public boolean onToggleLivePlayback() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onToggleLivePlayback();
            case UnitTesting:
                return mUnitTestingModel.onToggleLivePlayback();
            default:
                break;
        }
        return false;
    }

    public boolean onToggleSos() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onToggleSos();
            case UnitTesting:
                return mUnitTestingModel.onToggleSos();
            default:
                break;
        }
        return false;
    }

    public boolean onFocusChange(float delta) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onFocusChange(delta);
            case UnitTesting:
                return mUnitTestingModel.onFocusChange(delta);
            default:
                break;
        }
        return false;
    }

    public boolean onSpeedOfSoundChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSpeedOfSoundChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onSpeedOfSoundChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onTgcChanged(int index, float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onTgcChanged(index, value);
            case UnitTesting:
                return mUnitTestingModel.onTgcChanged(index, value);
            default:
                break;
        }
        return false;
    }

    public boolean onDlcChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onDlcChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onDlcChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onMasterGainChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onMasterGainChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onMasterGainChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onAcousticPowerChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onAcousticPowerChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onAcousticPowerChanged(value);
            default:
                break;
        }
        return false;
    }

    public void onAutoContrastChanged(boolean toggle) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.onAutoContrastChanged(toggle);
                break;
            case UnitTesting:
                mUnitTestingModel.onAutoContrastChanged(toggle);
                break;
            default:
                break;
        }
    }

    public boolean onGaussianChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGaussianChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onGaussianChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onEdgeChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onEdgeChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onEdgeChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onContrastChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onContrastChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onContrastChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onBrightnessChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onBrightnessChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onBrightnessChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onGammaChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGammaChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onGammaChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onFrequencyChanged(float value) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onFrequencyChanged(value);
            case UnitTesting:
                return mUnitTestingModel.onFrequencyChanged(value);
            default:
                break;
        }
        return false;
    }

    public boolean onLoadExamFile(String pid, String examName, String date, int type, String fileName) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onLoadExamFile(pid, examName, date, type, fileName);
            case UnitTesting:
                return mUnitTestingModel.onLoadExamFile(pid, examName, date, type, fileName);
            default:
                break;
        }
        return false;
    }

    public final ArrayList<String> onGetExamStores(String pid, String examName, String date, int type) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetExamStores(pid, examName, date, type);
            case UnitTesting:
                return mUnitTestingModel.onGetExamStores(pid, examName, date, type);
            default:
                break;
        }
        return null;
    }

    public final ArrayList<String> onGetFileTypes(String pid, String exam, String date) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetFileTypes(pid, exam, date);
            case UnitTesting:
                return mUnitTestingModel.onGetFileTypes(pid, exam, date);
            default:
                break;
        }
        return null;
    }

    public final ArrayList<String> onGetPatients(String pattern) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetPatients(pattern);
            case UnitTesting:
                return mUnitTestingModel.onGetPatients(pattern);
            default:
                break;
        }
        return null;
    }

    public final ArrayList<String> getPids(String pattern) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPids(pattern);
            case UnitTesting:
                return mUnitTestingModel.getPids(pattern);
            default:
                break;
        }
        return null;
    }

    public final String getPid(String pattern, int index) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getPid(pattern, index);
            case UnitTesting:
                return mUnitTestingModel.getPid(pattern, index);
            default:
                break;
        }
        return null;
    }

    public final ArrayList<String> onGetExams(String patientId) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetExams(patientId);
            case UnitTesting:
                return mUnitTestingModel.onGetExams(patientId);
            default:
                break;
        }
        return null;
    }

    public final String getExamName(String patientId, int index) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getExamName(patientId, index);
            case UnitTesting:
                return mUnitTestingModel.getExamName(patientId, index);
            default:
                break;
        }
        return null;
    }

    public final String getExamDate(String patientId, int index) {
        switch (mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getExamDate(patientId, index);
            case UnitTesting:
                return mUnitTestingModel.getExamDate(patientId, index);
            default:
                break;
        }
        return null;
    }

    public boolean onStartExam(String examName, String selectedLine) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onStartExam(examName, selectedLine);
            case UnitTesting:
                return mUnitTestingModel.onStartExam(examName, selectedLine);
            default:
                break;
        }
        return false;
    }

    public boolean onStopExam() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onEndExam();
            case UnitTesting:
                return mUnitTestingModel.onEndExam();
            default:
                break;
        }
        return false;
    }

    public boolean onStartExam(String examName, String comments, String pid, String first, String last, String birth, String sex) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onStartExam(examName, comments, pid, first, last, birth, sex);
            case UnitTesting:
                return mUnitTestingModel.onStartExam(examName, comments, pid, first, last, birth, sex);
            default:
                break;
        }
        return false;
    }

    public boolean onEndExam() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onEndExam();
            case UnitTesting:
                return mUnitTestingModel.onEndExam();
            default:
                break;
        }
        return false;
    }

    public ArrayList<String> getUnitOfMeasureList() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUnitOfMeasureList();
            case UnitTesting:
                return mUnitTestingModel.getUnitOfMeasureList();
            default:
                break;
        }
        return null;
    }

    public ArrayList<String> getUserTypeList() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUserTypeList();
            case UnitTesting:
                return mUnitTestingModel.getUserTypeList();
            default:
                break;
        }
        return null;
    }

    public String getUserName(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUserName(index);
            case UnitTesting:
                return mUnitTestingModel.getUserName(index);
            default:
                break;
        }
        return null;
    }

    public ArrayList<String> getUserList() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getUserList();
            case UnitTesting:
                return mUnitTestingModel.getUserList();
            default:
                break;
        }
        return null;
    }

    boolean createUser(String userName, String password, String userType) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.createUser(userName, password, userType);
            case UnitTesting:
                return mUnitTestingModel.createUser(userName, password, userType);
            default:
                break;
        }
        return false;
    }

    public boolean deleteUser(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.deleteUser(index);
            case UnitTesting:
                return mUnitTestingModel.deleteUser(index);
            default:
                break;
        }
        return false;
    }

    boolean deleteUser(String userName, String password) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.deleteUser(userName, password);
            case UnitTesting:
                return mUnitTestingModel.deleteUser(userName, password);
            default:
                break;
        }
        return false;
    }

    public boolean setDriveTypeNetwork() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.setDriveTypeNetwork();
            case UnitTesting:
                return mUnitTestingModel.setDriveTypeNetwork();
            default:
                break;
        }
        return false;
    }

    public boolean setDriveTypeUsb() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.setDriveTypeUsb();
            case UnitTesting:
                return mUnitTestingModel.setDriveTypeUsb();
            default:
                break;
        }
        return false;
    }

    public boolean setUnitTypeMetric() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.setUnitTypeMetric();
            case UnitTesting:
                return mUnitTestingModel.setUnitTypeMetric();
            default:
                break;
        }
        return false;
    }

    public boolean setUnitTypeInches() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.setUnitTypeInches();
            case UnitTesting:
                return mUnitTestingModel.setUnitTypeInches();
            default:
                break;
        }
        return false;
    }

    public ArrayList<String> getExternalStorageInfo() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getExternalStorageInfo();
            case UnitTesting:
                return mUnitTestingModel.getExternalStorageInfo();
            default:
                break;
        }
        return null;
    }

    public boolean disconnectNetworkStorage() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.disconnectNetworkStorage();
            case UnitTesting:
                return mUnitTestingModel.disconnectNetworkStorage();
            default:
                break;
        }
        return false;
    }

    public boolean connectNetworkStorage(String networkPath, String userName, String password, boolean autoConnect) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.connectNetworkStorage(networkPath, userName, password, autoConnect);
            case UnitTesting:
                return mUnitTestingModel.connectNetworkStorage(networkPath, userName, password, autoConnect);
            default:
                break;
        }
        return false;
    }

    /*public float getProgress(String type) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getProgress(type);
            case UnitTesting:
                return mUnitTestingModel.getProgress(type);
            default:
                break;
        }
        return 0;
    }*/

    public int getDiagnosticsProgress() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getDiagnosticsProgress();
            case UnitTesting:
                return mUnitTestingModel.getDiagnosticsProgress();
            default:
                break;
        }
        return -1;
    }

    public String getDiagnosticsStatus() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getDiagnosticsStatus();
            case UnitTesting:
                return mUnitTestingModel.getDiagnosticsStatus();
            default:
                break;
        }
        return null;
    }

    public ArrayList<String> getDiagnosticTests() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.getDiagnosticTests();
            case UnitTesting:
                return mUnitTestingModel.getDiagnosticTests();
            default:
                break;
        }
        return null;
    }

    public boolean onClearErrors() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onClearErrors();
            case UnitTesting:
                return mUnitTestingModel.onClearErrors();
            default:
                break;
        }
        return false;
    }

    public boolean onPerformDiagnosticTest(String type) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onPerformDiagnosticTest(type);
            case UnitTesting:
                return mUnitTestingModel.onPerformDiagnosticTest(type);
            default:
                break;
        }
        return false;
    }

    public boolean onAbortDiagnosticTest() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onAbortDiagnosticTest();
            case UnitTesting:
                return mUnitTestingModel.onAbortDiagnosticTest();
            default:
                break;
        }
        return false;
    }

    public boolean onSelectFilter(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSelectFilter(index);
            case UnitTesting:
                return mUnitTestingModel.onSelectFilter(index);
            default:
                break;
        }
        return false;
    }

    public final ArrayList<String> onGetFilters() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetFilters();
            case UnitTesting:
                return mUnitTestingModel.onGetFilters();
            default:
                break;
        }
        return null;
    }

    public final ArrayList<String> onGetProbeList() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetProbeList();
            case UnitTesting:
                return mUnitTestingModel.onGetProbeList();
            default:
                break;
        }
        return null;
    }

    public boolean onSelectProbe(String name) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSelectProbe(name);
            case UnitTesting:
                return mUnitTestingModel.onSelectProbe(name);
            default:
                break;
        }
        return false;
    }

    public ArrayList<String> onGetCompressionTypes() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetCompressionTypes();
            case UnitTesting:
                return mUnitTestingModel.onGetCompressionTypes();
            default:
                break;
        }
        return null;
    }

    public boolean onSelectCompressionType(String name) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSelectCompressionType(name);
            case UnitTesting:
                return mUnitTestingModel.onSelectCompressionType(name);
            default:
                break;
        }
        return false;
    }

    public boolean onRequestSystemShutdown() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onRequestSystemShutdown();
            case UnitTesting:
                return mUnitTestingModel.onRequestSystemShutdown();
            default:
                break;
        }
        return false;
    }

    public boolean onStartMeasurement() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onStartMeasurement();
            case UnitTesting:
                return mUnitTestingModel.onStartMeasurement();
            default:
                break;
        }
        return false;
    }

    public boolean onStopMeasurement() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onStopMeasurement();
            case UnitTesting:
                return mUnitTestingModel.onStopMeasurement();
            default:
                break;
        }
        return false;
    }

    public boolean onSwapMeasurementPoints() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSwapMeasurementPoints();
            case UnitTesting:
                return mUnitTestingModel.onSwapMeasurementPoints();
            default:
                break;
        }
        return false;
    }

    public boolean onCaptureMeasurementMark() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onCaptureMeasurementMark();
            case UnitTesting:
                return mUnitTestingModel.onCaptureMeasurementMark();
            default:
                break;
        }
        return false;
    }

    public boolean onCancelMeasurement() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onCancelMeasurement();
            case UnitTesting:
                return mUnitTestingModel.onCancelMeasurement();
            default:
                break;
        }
        return false;
    }

    public boolean onDeleteMeasurement(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onDeleteMeasurement(index);
            case UnitTesting:
                return mUnitTestingModel.onDeleteMeasurement(index);
            default:
                break;
        }
        return false;
    }

    public boolean onEditMeasurement(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onEditMeasurement(index);
            case UnitTesting:
                return mUnitTestingModel.onEditMeasurement(index);
            default:
                break;
        }
        return false;
    }

    public boolean onHighlightMeasurement(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onHighlightMeasurement(index);
            case UnitTesting:
                return mUnitTestingModel.onHighlightMeasurement(index);
            default:
                break;
        }
        return false;
    }

    public boolean onSendCursorMovement(int dx, int dy) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onSendCursorMovement(dx, dy);
            case UnitTesting:
                return mUnitTestingModel.onSendCursorMovement(dx, dy);
            default:
                break;
        }
        return false;
    }

    public final ArrayList<Float> onGetMeasurements() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetMeasurements();
            case UnitTesting:
                return mUnitTestingModel.onGetMeasurements();
            default:
                break;
        }
        return null;
    }

    public final ArrayList<Integer> onGetMeasurementFrames() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetMeasurementFrames();
            case UnitTesting:
                return mUnitTestingModel.onGetMeasurementFrames();
            default:
                break;
        }
        return null;
    }

    public final String onGetSystemState(int index) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetSystemState(index);
            case UnitTesting:
                return mUnitTestingModel.onGetSystemState(index);
            default:
                break;
        }
        return null;
    }

    public final ArrayList<String> onGetSystemStateList() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.onGetSystemStateList();
            case UnitTesting:
                return mUnitTestingModel.onGetSystemStateList();
            default:
                break;
        }
        return null;
    }

    Image getImage(long time) {
        try {
            switch(mMessageTo) {
                case BeamformerClient:
                case BatchMode:
                    return mBeamformerClient.getImage(time);
                case UnitTesting:
                    return mUnitTestingModel.getImage(time);
                default:
                    break;
            }
            return null;
        } catch (ImageNotAvailableException e) {
            throw e;
        } catch (Exception e) {
            throw new LostCommunicationException("Lost Communication: Get image failed", e);
        }
    }

    /*Image getImage(K3900.ImageRequest.Builder request) {
        try {
            switch(mMessageTo) {
                case BeamformerClient:
                case BatchMode:
                    return mBeamformerClient.getImage(request);
                //return mBeamformerClient.onGetImage(request);
                case UnitTesting:
                    return mUnitTestingModel.getImage(request);
                default:
                    break;
            }
            return null;
        } catch (ImageNotAvailableException e) {
            throw e;
        } catch (Exception e) {
            throw new LostCommunicationException("Lost Communication: Get image failed", e);
        }
    }*/

    void connect(String address, int port) {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.connect(address, port);
                break;
            case UnitTesting:
                mUnitTestingModel.connect(address, port);
                break;
            default:
                break;
        }
    }

    void disconnect() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                mBeamformerClient.disconnect();
                break;
            case UnitTesting:
                mUnitTestingModel.disconnect();
                break;
            default:
                break;
        }
    }

    public boolean isBatchMode() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.isBatchMode();
            case UnitTesting:
                return mUnitTestingModel.isBatchMode();
            default:
                break;
        }
        return false;
    }

    public boolean isAvailable() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return mBeamformerClient.connected();
            case UnitTesting:
                return null != mUnitTestingModel;
            default:
                break;
        }
        return false;
    }

    public boolean wifiDeviceConnected() {
        switch(mMessageTo) {
            case BeamformerClient:
            case BatchMode:
                return WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().connected();
            case UnitTesting:
                return true;
            default:
                break;
        }
        return false;
    }

    public boolean connectToStaticIp() {
        return mConnectToStaticIp;
    }

    /*public String getWiFiDeviceName() { return mWiFiDeviceName; }
    public void setWiFiDeviceName(String wiFiDeviceName) { mWiFiDeviceName = wiFiDeviceName; }*/

    private static SwitchBackEndModel singletonInstance = null;
    BeamformerClient mBeamformerClient = BeamformerClient.getBeamformerClientSingletonInstance();
    UnitTestingModel mUnitTestingModel = UnitTestingModel.getUnitTestingModelSingletonInstance();
    MessageTo mMessageTo = MessageTo.BeamformerClient;
    MessageTo messageTo() { return mMessageTo; }
    //private String mWiFiDeviceName;
    boolean mConnectToStaticIp = false;
}
