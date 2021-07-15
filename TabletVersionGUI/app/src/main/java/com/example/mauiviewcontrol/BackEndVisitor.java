package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Switch;

import java.util.ArrayList;

interface BackEndElement {
    String getRuntimeSubText();
    boolean accept(BackEndElementVisitor visitor);
}

interface BackEndElementMaskingElement{
    void setMask(boolean mask);
}

interface BackEndElementVisitor {
    boolean visit(StartMeasurement element);
    boolean visit(StopMeasurement element);
    boolean visit(SwapMeasurement element);
    boolean visit(CancelMeasurement element);
    boolean visit(DeleteMeasurement element);
    boolean visit(EditMeasurement element);
    boolean visit(StartExam element);
    boolean visit(StopExam element);
    boolean visit(ResetErrors element);
    String visit(DetailedStatus element);
    boolean visit(ConnectNetwork element);
    boolean visit(DisconnectNetwork element);
    boolean visit(CreateUser element);
    boolean visit(DeleteUser element);
    boolean visit(StartDiagnosticTest element);
    boolean visit(AbortDiagnosticTest element);
    boolean visit(IncrementTx3Apertures element);
    boolean visit(DecrementTx3Apertures element);
    boolean visit(IncrementRx3Apertures element);
    boolean visit(DecrementRx3Apertures element);
    boolean visit(IncrementPingPersistence element);
    boolean visit(DecrementPingPersistence element);
    boolean visit(IncrementFramePersistence element);
    boolean visit(DecrementFramePersistence element);
    boolean visit(SetTxElementMasking element);
    boolean visit (SetRxElementMasking element);
    boolean visit(SetTxSwitchListener element);
    boolean visit(SetRxSwitchListener element);
    boolean visit(SetZoomButtonListener element);
    boolean visit(SetPanButtonListener element);
    boolean visit(ClearMeasurements element);
}

class StartMeasurement implements BackEndElement {
    /*private final String name;

    public StartMeasurement(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }*/

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class StopMeasurement implements BackEndElement {
    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class SwapMeasurement implements BackEndElement {
    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class CancelMeasurement implements BackEndElement {
    private MeasureCustomView mMeasureCustomView;

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    public void setMeasureCustomView(MeasureCustomView customView){
        mMeasureCustomView=customView;
    }

    public MeasureCustomView getMeasureCustomView(){
        return mMeasureCustomView;
    }
}

class DeleteMeasurement implements BackEndElement {
    int mIndex;
    public void setIndex(int index) { mIndex = index; }
    public int getIndex() { return mIndex; }
    MeasureCustomView mMeasureCustomView;

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return Integer.toString(mIndex);
    }

    public void setMeasureCustomView(MeasureCustomView customView){
        mMeasureCustomView=customView;
    }

    public MeasureCustomView getMeasureCustomView(){
        return mMeasureCustomView;
    }
}

class EditMeasurement implements BackEndElement {
    int mIndex;
    public void setIndex(int index) { mIndex = index; }
    public int getIndex() { return mIndex; }

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return Integer.toString(mIndex);
    }
}

class StartExam implements BackEndElement {
    String mExamName;
    String mLine;
    public void setExamName(String examName) { mExamName = examName; }
    public void setLine(String line) { mLine = line; }
    public String getExamName() { return mExamName; }
    public String getLine() { return mLine; }

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return mExamName + mLine;
    }
}

class StopExam implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class ResetErrors implements BackEndElement {
    /*private final String name;

    public StartMeasurement(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }*/

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class DetailedStatus implements BackEndElement {
    int mIndex;
    String mDetailedStatus;
    public void setIndex(int index) { mIndex = index; }
    public int getIndex() { return mIndex; }
    public String getDetailedStatus() { return mDetailedStatus; }

    @Override
    public boolean accept(BackEndElementVisitor visitor)
    {
        mDetailedStatus = visitor.visit(this);
        return true;
    }

    @Override
    public String getRuntimeSubText() {
        return Integer.toString(mIndex);
    }
}

class ConnectNetwork implements BackEndElement {
    String mNetworkPath;
    String mUserName;
    String mPassword;
    boolean mAutoConnect;
    public void setNetworkPath(String networkPath) { mNetworkPath = networkPath; }
    public String getNetworkPath() { return mNetworkPath; }
    public void setUserName(String userName) { mUserName = userName; }
    public String getUserName() { return mUserName; }
    public void setPassword(String password) { mPassword = password; }
    public String getPassword() { return mPassword; }
    public void setAutoConnect(boolean autoConnect) { mAutoConnect = autoConnect; }
    public boolean getAutoConnect() { return mAutoConnect; }

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class DisconnectNetwork implements BackEndElement {
    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class CreateUser implements BackEndElement {
    String mUserName;
    String mPassword;
    String mUserType;
    public void setUserName(String userName) { mUserName = userName; }
    public String getUserName() { return mUserName; }
    public void setPassword(String password) { mPassword = password; }
    public String getPassword() { return mPassword; }
    public void setUserType(String userType) { mUserType = userType; }
    public String getUserType() { return mUserType; }
    //int mIndex;
    //public void setIndex(int index) { mIndex = index; }
    //public int getIndex() { return mIndex; }

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
    //public String getRuntimeSubText() {
    //return Integer.toString(mIndex);
    //}
}

class DeleteUser implements BackEndElement {
    int mIndex;
    public void setIndex(int index) { mIndex = index; }
    public int getIndex() { return mIndex; }

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return Integer.toString(mIndex);
    }
}

class StartDiagnosticTest implements BackEndElement {
    String mType;
    public void setType(String type) { mType = type; }
    public String getType() { return mType; }

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class AbortDiagnosticTest implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class IncrementTx3Apertures implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class DecrementTx3Apertures implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class IncrementRx3Apertures implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class DecrementRx3Apertures implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class IncrementPingPersistence implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class DecrementPingPersistence implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class IncrementFramePersistence implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class DecrementFramePersistence implements BackEndElement {

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }
}

class SetElementMasking implements BackEndElement{
    private int mIndex;
    private boolean mMask;
    private boolean mSaveButtonHidden;
    private Switch mLeftSwitch;
    private Switch mCenterSwitch;
    private Switch mRightSwitch;
    private ElementButtonList mElementButtonList1;
    private ElementButtonList mElementButtonList2;
    private ElementButtonList mElementButtonList3;

    @Override
    public boolean accept(BackEndElementVisitor visitor) { return false;
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    public int getIndex(){
        return mIndex;
    }

    public boolean getMask(){
        return mMask;
    }

    public void setMask(boolean mask){
        mMask=mask;
    }

    public void setIndex(int index){
        mIndex=index;
    }

   /* public void checkSwitches(){
        boolean group1=true;
        boolean group2=true;
        boolean group3=true;

        for(int i=0;i<mElementButtonList1.size();i++){
            if(mElementButtonList1.get(i).getEnabled()==false){
                group1=false;
            }
            if(mElementButtonList2.get(i).getEnabled()==false){
                group2=false;
            }
            if(mElementButtonList3.get(i).getEnabled()==false){
                group3=false;
            }
        }

        mLeftSwitch.setChecked(group1);
        mCenterSwitch.setChecked(group2);
        mRightSwitch.setChecked(group3);
    }

    public void setSwitches(Switch leftSwitch, Switch centerSwitch, Switch rightSwitch){
        mLeftSwitch=leftSwitch;
        mCenterSwitch=centerSwitch;
        mRightSwitch=rightSwitch;
    }

    public void setElementButtonLists(ElementButtonList elementButtonList1, ElementButtonList elementButtonList2, ElementButtonList elementButtonList3){
        mElementButtonList1=elementButtonList1;
        mElementButtonList2=elementButtonList2;
        mElementButtonList3=elementButtonList3;
    }*/
}

class SetTxElementMasking extends SetElementMasking{
    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }
}

class SetRxElementMasking extends SetElementMasking {
    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }
}

class SetTxSwitchListener implements BackEndElement{
    Switch mSwitch;
    ElementButtonList mElementButtonList;
    private boolean mSaveButtonHidden;

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    public void setSwitch(Switch switchInput){
        mSwitch=switchInput;
    }

    public void setElementButtonList(ElementButtonList elementButtonList){
        mElementButtonList=elementButtonList;
    }

    public Switch getSwitch(){
        return mSwitch;
    }

    public ElementButtonList getElementButtonList(){
        return mElementButtonList;
    }

    public void setButtonsEnabled(ElementButtonList elementButtonList, boolean enabled){
        for(int i=0;i<elementButtonList.size();i++){
            elementButtonList.get(i).setEnabled(enabled);
        }
    }

    public boolean getIsChecked(){
        return mSwitch.isChecked();
    }

    /*public void setSaveButtonHidden(boolean hidden){
        mSaveButtonHidden=hidden;
    }

    public boolean getSaveButtonHidden(){
        return mSaveButtonHidden;
    }
    */
}

class SetRxSwitchListener implements BackEndElement{
    Switch mSwitch;
    ElementButtonList mElementButtonList;
    private boolean mSaveButtonHidden;

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    public void setSwitch(Switch switchInput){
        mSwitch=switchInput;
    }

    public void setElementButtonList(ElementButtonList elementButtonList){
        mElementButtonList=elementButtonList;
    }

    public Switch getSwitch(){
        return mSwitch;
    }

    public ElementButtonList getElementButtonList(){
        return mElementButtonList;
    }

    public boolean getIsChecked(){
        return mSwitch.isChecked();
    }

    /*public void setSaveButtonHidden(boolean hidden){
        mSaveButtonHidden=hidden;
    }

    public boolean getSaveButtonHidden(){
        return mSaveButtonHidden;
    }*/
}

class SetZoomButtonListener implements BackEndElement{
    private float mValue;

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    public void setValue(float value){
        mValue=value;
    }

    public float getValue(){
        return mValue;
    }
}

class SetPanButtonListener implements BackEndElement{
    private float mX;
    private float mY;

    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    public void setX(float x){
        mX=x;
    }

    public void setY(float y){
        mY=y;
    }

    public float getX(){
        return mX;
    }

    public float getY(){
        return mY;
    }
}

class ClearMeasurements implements BackEndElement{
    @Override
    public boolean accept(BackEndElementVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getRuntimeSubText() {
        return "";
    }

    private MeasureCustomView mMeasureCustomView;
    private FrameLayout mFrameLayout;
    private Dialog mDialog;

    /*public void setDialog(Dialog dialog){
        mDialog=dialog;
    }

    public void refreshDialog(){
        mDialog.dismiss();
        mDialog.show();
    }*/

    public void setMeasureCustomView(MeasureCustomView customView){
        mMeasureCustomView=customView;
    }

    public MeasureCustomView getMeasureCustomView(){
        return mMeasureCustomView;
    }

   /* public void setFrameLayout(FrameLayout frameLayout){
        mFrameLayout=frameLayout;
    }

    public FrameLayout getFrameLayout(){
        return mFrameLayout;
    }*/
}

class BackEndElementSendingMessageVisitor implements BackEndElementVisitor {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();

    @Override
    public boolean visit(StartMeasurement element) {
        return mBackend.onStartMeasurement();
    }

    @Override
    public boolean visit(StopMeasurement element) {
        return mBackend.onStopMeasurement();
    }

    @Override
    public boolean visit(SwapMeasurement element) {
        return mBackend.onSwapMeasurementPoints();
    }

    @Override
    public boolean visit(CancelMeasurement element) {
        element.getMeasureCustomView().cancelMeasurement();
        return mBackend.onCancelMeasurement();
    }

    @Override
    public boolean visit(DeleteMeasurement element) {
        boolean backend=mBackend.onDeleteMeasurement(element.getIndex());
        MeasureImagingDialog.getSingletonInstance(null, true).setMeasurementListView();
        element.getMeasureCustomView().deleteMeasurement(element.getIndex());
        return backend;
    }

    @Override
    public boolean visit(EditMeasurement element) {
        return mBackend.onEditMeasurement(element.getIndex());
    }

    @Override
    public boolean visit(StartExam element) {
        if (null == element.getExamName())
            return false;
        return mBackend.onStartExam(element.getExamName(), element.getLine());
    }

    @Override
    public boolean visit(StopExam element) {
        return mBackend.onStopExam();
    }

    @Override
    public boolean visit(ResetErrors element) {
        return mBackend.onClearErrors();
    }

    @Override
    public String visit(DetailedStatus element) {
        return mBackend.onGetSystemState(element.getIndex());
    }

    @Override
    public boolean visit(ConnectNetwork element) {
        return mBackend.connectNetworkStorage(element.getNetworkPath(), element.getUserName(), element.getPassword(), element.getAutoConnect());
    }

    @Override
    public boolean visit(DisconnectNetwork element) {
        return mBackend.disconnectNetworkStorage();
    }

    @Override
    public boolean visit(CreateUser element) {
        return mBackend.createUser(element.getUserName(), element.getPassword(), element.getUserType());
    }

    @Override
    public boolean visit(DeleteUser element) {
        return mBackend.deleteUser(element.getIndex());
    }

    @Override
    public boolean visit(StartDiagnosticTest element) {
        return mBackend.onPerformDiagnosticTest(element.getType());
    }

    @Override
    public boolean visit(AbortDiagnosticTest element) {
        return mBackend.onAbortDiagnosticTest();
    }

    @Override
    public boolean visit(IncrementTx3Apertures element) {
        return mBackend.onIncrementTx3Apertures();
    }

    @Override
    public boolean visit(DecrementTx3Apertures element) {
        return mBackend.onDecrementTx3Apertures();
    }

    @Override
    public boolean visit(IncrementRx3Apertures element) {
        return mBackend.onIncrementRx3Apertures();
    }

    @Override
    public boolean visit(DecrementRx3Apertures element) {
        return mBackend.onDecrementRx3Apertures();
    }

    @Override
    public boolean visit(IncrementPingPersistence element) {
        return mBackend.onIncrementPingPersistence();
    }

    @Override
    public boolean visit(DecrementPingPersistence element) {
        return mBackend.onDecrementPingPersistence();
    }

    @Override
    public boolean visit(IncrementFramePersistence element) {
        return mBackend.onIncrementFramePersistence();
    }

    @Override
    public boolean visit(DecrementFramePersistence element) {
        return mBackend.onDecrementFramePersistence();
    }

    @Override
    public boolean visit(SetTxElementMasking element){
        element.setMask(!element.getMask());
        mBackend.onChangeTxMask(element.getIndex(), element.getMask());
        //element.checkSwitches();
        return true;
    }

    @Override
    public boolean visit(SetRxElementMasking element){
        element.setMask(!element.getMask());
        mBackend.onChangeRxMask(element.getIndex(), element.getMask());
        //element.checkSwitches();
        return true;
    }

    @Override
    public boolean visit(SetTxSwitchListener element){
        //element.setButtonsEnabled(element.getElementButtonList(),element.getIsChecked());
        for (int i = 0; i < element.getElementButtonList().size(); i++) {
            //mBackend.onChangeTxMask(element.getElementButtonList().get(i).getButtonNumber(), element.getElementButtonList().get(i).getEnabled());
            mBackend.onChangeTxMask(element.getElementButtonList().get(i).getButtonNumber(), element.getSwitch().isChecked());
        }
        //return mBackend.onChangeTxMask(element.getElementButtonList().get((element.getElementButtonList().size()) - 1).getButtonNumber(), element.getElementButtonList().get(element.getElementButtonList().size() - 1).getEnabled());
        return mBackend.onChangeTxMask(element.getElementButtonList().get((element.getElementButtonList().size()) - 1).getButtonNumber(),element.getSwitch().isChecked());
    }

    public boolean visit(SetRxSwitchListener element){
        //element.setButtonsEnabled(element.getElementButtonList(),element.getIsChecked());
        for (int i = 0; i < element.getElementButtonList().size(); i++) {
            mBackend.onChangeRxMask(element.getElementButtonList().get(i).getButtonNumber(), element.getSwitch().isChecked());
        }
        //return mBackend.onChangeRxMask(element.getElementButtonList().get((element.getElementButtonList().size()) - 1).getButtonNumber(), element.getElementButtonList().get(element.getElementButtonList().size() - 1).getEnabled());
        return mBackend.onChangeRxMask(element.getElementButtonList().get((element.getElementButtonList().size()) - 1).getButtonNumber(),element.getSwitch().isChecked());
    }

    public boolean visit(SetZoomButtonListener element){
        return mBackend.onZoom(element.getValue());
    }

    public boolean visit(SetPanButtonListener element){
        return mBackend.onPan(element.getX(), element.getY());
    }

    public boolean visit(ClearMeasurements element){
        element.getMeasureCustomView().clearMeasurements();
        //element.getMeasureCustomView().setBackgroundColor(Color.RED);
        //ViewGroup.LayoutParams params=element.getMeasureCustomView().getLayoutParams();
        //params.height=0;
        //params.width=0;
        //element.getMeasureCustomView().setLayoutParams(params);
        //element.getMeasureCustomView().requestLayout();
        //element.getMeasureCustomView().postInvalidate();
        //element.getFrameLayout().removeAllViewsInLayout();
        //element.getFrameLayout().addView(element.getMeasureCustomView(), params);
        //element.refreshDialog();
        boolean backend=mBackend.onClearMeasurements();
        MeasureImagingDialog.getSingletonInstance(null, true).setMeasurementListView();
        return backend;
    }
}

