package com.example.mauiviewcontrol;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import io.grpc.stub.StreamObserver;

import static java.lang.Thread.sleep;

public  class ElementMaskingSetup implements Runnable {
    public static final String TAG = "Element Setup";
    protected SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private String mName = null;
    protected ElementButtonList elementButtonList1=new ElementButtonList(1);
    protected ElementButtonList elementButtonList2=new ElementButtonList(2);
    protected ElementButtonList elementButtonList3=new ElementButtonList(3);
    protected boolean mAddBorder = false;
    private ArrayList <Boolean>buttonStatus;
    protected ArrayList<Boolean> mElementButtonStatus;
    protected boolean isRunning=true;
    protected boolean mInitializationCompleted;
    protected static boolean sSaveButtonHidden=true;
    private View mView;
    protected Context mContext;
    protected Switch mLeftSwitch;
    protected Switch mCenterSwitch;
    protected Switch mRightSwitch;


    public ElementMaskingSetup(String name, View view, Context context, Switch leftSwitch, Switch centerSwitch, Switch rightSwitch){
        mName = name;
        //SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
        mView=view;
        mContext=context;
        mLeftSwitch=leftSwitch;
        mCenterSwitch=centerSwitch;
        mRightSwitch=rightSwitch;
    }

    private TableLayout setTableLayout(View view, int groupNumber){
        TableLayout tableLayout;
        if(groupNumber==1) {
            tableLayout = (TableLayout) view.findViewById(R.id.tableLayout1);
        }
        else if(groupNumber==2) {
            tableLayout = (TableLayout) view.findViewById(R.id.tableLayout2);
        }
        else{
            tableLayout = (TableLayout) view.findViewById(R.id.tableLayout3);
        }
        return tableLayout;
    }

    private TableRow setTableRow(){
        TableRow tableRow=new TableRow(mContext);
        TableRow.LayoutParams a=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(a);
        return tableRow;
    }

    private void addTableRow(ElementButtonList elementButtonList, int buttonsInRow, int rowNumber){
        TableLayout tableLayout=setTableLayout(mView, elementButtonList.groupNumber());
        TableRow tableRow=setTableRow();

        for(int column=0; column<buttonsInRow; column++){
            ElementButton button = new ElementButton(mContext);
            if(mAddBorder && ((column==2) || (column==6) || (column==10) || (column==14))){
                button.addBorder();
            }
            elementButtonList.add(button);
            int index=column+(rowNumber*buttonsInRow+((elementButtonList.groupNumber()-1)*64));
            button.setButtonNumber(index);
            String buttonNumber=Integer.toString(index);
            button.setTextSize(15);
            button.setGravity(Gravity.TOP | Gravity.CENTER);
            button.setText(buttonNumber);
            button.setWidth(120);
            button.setHeight(80);

            if(sSaveButtonHidden) {
                setButtonOnClickListener(button, mContext);
            }
            else{
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        button.alternate();
                        checkSwitches();
                    }
                });
            }
            tableRow.addView(button,120,80);
        }
        tableLayout.addView(tableRow,2000,80);
    }

    protected void setButtonOnClickListener(ElementButton button, Context context){
    }

    private void setButtonsEnabled(ElementButtonList elementButtonList, boolean enabled){
        for(int i=0;i<elementButtonList.size();i++){
            ElementButton button=elementButtonList.get(i);
            button.setEnabled(enabled);
        }
    }

    protected void setSwitchListener(Context context, ElementButtonList elementButtonList, Switch selectAllSwitch, int switchGroup){
        selectAllSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean isChecked=selectAllSwitch.isChecked();
                setButtonsEnabled(elementButtonList, isChecked);
            }
        });
    }

    public void completeSetup(){
        mInitializationCompleted=false;
        for(int i=0;i<4;i++) {
            addTableRow(elementButtonList1,16,i);
        }
        for(int i=0;i<4;i++){
            addTableRow(elementButtonList2,16,i);
        }
        for(int i=0;i<4;i++){
            addTableRow(elementButtonList3,16,i);
        }

        setSwitchListener(mContext, elementButtonList1, mLeftSwitch, 1);
        setSwitchListener(mContext, elementButtonList2, mCenterSwitch, 2);
        setSwitchListener(mContext, elementButtonList3, mRightSwitch, 3);

        mInitializationCompleted=true;
    }

    public void run(){

        //isRunning=true;
        //System.out.println("is running= "+ isRunning);
        //if(isTx) {
            //mTxButtonStatus=new ArrayList<Boolean>();
            //mTxButtonStatus = mBackend.onGetTxMask();
           // final ArrayList<Boolean> tx = mStreamObserver.getElementBooleans();
            //final ArrayList<Boolean> tx = mBackend.onGetTxMask();
           // mElementButtonStatus = tx;

            //buttonStatus = BeamformerClient.getBeamformerClientSingletonInstance().onGetTxMask();
        /*}
        else{
            //buttonStatus=new ArrayList<Boolean>();
            //buttonStatus = mBackend.onGetRxMask();
            //buttonStatus = BeamformerClient.getBeamformerClientSingletonInstance().onGetRxMask();
            final ArrayList<Boolean> rx = mBackend.onGetRxMask();
            mElementButtonStatus = rx;
        }*/

        //if(isTx){
            //while((null != mStreamObserver && (!mStreamObserver.getCompleted())) || !mInitializationCompleted){
                //System.out.println("Still running");
           // }
        /*}
        else {
            while (mBackend.isRxRunning() || !mInitializationCompleted) {
                //System.out.println("Still running");
            }
        }*/
        //System.out.println(TAG + "size of element button list 1 is: " + elementButtonList1.size());
        //System.out.println(TAG + "size of element button list 2 is: " + elementButtonList1.size());
        //System.out.println(TAG + "size of element button list 3 is: " + elementButtonList1.size());
        /*try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //if(isTx) {
            /*for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 16; column++) {
                    int group1 = column + (row * 16 + ((0) * 64));
                    int group2 = column + (row * 16 + ((1) * 64));
                    int group3 = column + (row * 16 + ((2) * 64));
                    //ElementButton elementButton = elementButtonList1.get(column + (row * 16));
                    //Boolean status = buttonStatus.get(group1);
                    elementButtonList1.get(column + (row * 16)).setEnabled(mElementButtonStatus.get(group1));
                    elementButtonList2.get(column + (row * 16)).setEnabled(mElementButtonStatus.get(group2));
                    elementButtonList3.get(column + (row * 16)).setEnabled(mElementButtonStatus.get(group3));
                }
            }/*
        /*}
        else{
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 16; column++) {
                    int group1 = column + (row * 16 + ((0) * 64));
                    int group2 = column + (row * 16 + ((1) * 64));
                    int group3 = column + (row * 16 + ((2) * 64));
                    ElementButton elementButton = elementButtonList1.get(column + (row * 16));
                    //Boolean status = buttonStatus.get(group1);
                    elementButtonList1.get(column + (row * 16)).setEnabled(mElementButtonStatus.get(group1));
                    elementButtonList2.get(column + (row * 16)).setEnabled(mElementButtonStatus.get(group2));
                    elementButtonList3.get(column + (row * 16)).setEnabled(mElementButtonStatus.get(group3));
                }
            }
        }*/

        //isRunning=false;
        //System.out.println("End of ElementSetup.run()");
        //System.out.println("is running= "+ isRunning);

    }

    public ArrayList<Boolean>getSetButtonStatus(){
        ArrayList<Boolean>status=new ArrayList<Boolean>();
        for(int i=0;i<elementButtonList1.size();i++){
            status.add(elementButtonList1.get(i).getEnabled());
        }
        for(int i=0;i<elementButtonList2.size();i++){
            status.add(elementButtonList2.get(i).getEnabled());
        }
        for(int i=0;i<elementButtonList3.size();i++){
            status.add(elementButtonList3.get(i).getEnabled());
        }
        return status;
    }

    public void checkSwitches(){
        boolean group1=true;
        boolean group2=true;
        boolean group3=true;

        for(int i=0;i<elementButtonList1.size();i++){
            if(elementButtonList1.get(i).getEnabled()==false){
                group1=false;
            }
            if(elementButtonList2.get(i).getEnabled()==false){
                group2=false;
            }
            if(elementButtonList3.get(i).getEnabled()==false){
                group3=false;
            }
        }

        mLeftSwitch.setChecked(group1);
        mCenterSwitch.setChecked(group2);
        mRightSwitch.setChecked(group3);
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    public static boolean getSaveButtonHidden(){
        return sSaveButtonHidden;
    }

    public void setData(){}

    public static void setSaveButtonHidden(boolean saveButtonHidden){
        sSaveButtonHidden=saveButtonHidden;
    }
}
