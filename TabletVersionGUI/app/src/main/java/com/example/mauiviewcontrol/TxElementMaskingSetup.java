package com.example.mauiviewcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;

import java.util.ArrayList;

public class TxElementMaskingSetup extends ElementMaskingSetup{
    ArrayList<SetElementMasking>mSetTxElementMasking=new ArrayList<>();

    public TxElementMaskingSetup(String name, View view, Context context, Switch leftSwitch, Switch centerSwitch, Switch rightSwitch){
        super(name, view, context, leftSwitch, centerSwitch, rightSwitch);
        mAddBorder=true;
    }

    @Override
    public void setButtonOnClickListener(ElementButton button, Context context){
        //button.alternate();
        if(sSaveButtonHidden) {
            final ArrayList<Boolean> tx=mBackend.onGetTxMask();
            ArrayList <Boolean> txButtonStatus=tx;
            while(mBackend.isTxRunning()){

            }
            BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
            SetTxElementMasking setTxElementMasking = new SetTxElementMasking();
            mSetTxElementMasking.add(setTxElementMasking);
            setTxElementMasking.setIndex(button.getButtonNumber());
            setTxElementMasking.setMask(txButtonStatus.get(button.getButtonNumber()));
            //setTxElementMasking.setSaveButtonHidden(sSaveButtonHidden);
            //setTxElementMasking.setSwitches(leftSwitch, centerSwitch, rightSwitch);
            //setTxElementMasking.setElementButtonLists(elementButtonList1,elementButtonList2,elementButtonList3);
            WidgetUtility.setUpListener(context, button, null, setTxElementMasking, backEndElementSendingMessageVisitor, false, "", false, "tx changed: " + setTxElementMasking.getIndex(), false);
        }
    }

    @Override
    public void run(){
        Looper.prepare();
        ((MainActivity)mContext).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                completeSetup();
            }
        });
        if(!sSaveButtonHidden) {
            setData();
            while(isRunning){

            }
            checkSwitches();
        }
    }

    @Override
    public void setData(){
        isRunning=true;
        final ArrayList<Boolean> tx=mBackend.onGetTxMask();
        ArrayList <Boolean> txButtonStatus=tx;

        while(mBackend.isTxRunning() || !mInitializationCompleted){

        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 16; column++) {
                int group1 = column + (row * 16 + ((0) * 64));
                int group2 = column + (row * 16 + ((1) * 64));
                int group3 = column + (row * 16 + ((2) * 64));
                //ElementButton elementButton = elementButtonList1.get(column + (row * 16));
                elementButtonList1.get(column + (row * 16)).setEnabled(txButtonStatus.get(group1));
                elementButtonList2.get(column + (row * 16)).setEnabled(txButtonStatus.get(group2));
                elementButtonList3.get(column + (row * 16)).setEnabled(txButtonStatus.get(group3));
            }
        }

        isRunning=false;
    }

    @Override
    public void setSwitchListener(Context context, ElementButtonList elementButtonList, Switch selectAllSwitch, int switchGroup){
        if(!sSaveButtonHidden){
            super.setSwitchListener(context, elementButtonList, selectAllSwitch, switchGroup);
        }
        else {
            BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
            SetTxSwitchListener setTxSwitchListener = new SetTxSwitchListener();
            setTxSwitchListener.setElementButtonList(elementButtonList);
            setTxSwitchListener.setSwitch(selectAllSwitch);
            //setTxSwitchListener.setSaveButtonHidden(sSaveButtonHidden);
            WidgetUtility.setUpListener(context, selectAllSwitch, switchGroup, mSetTxElementMasking, null, setTxSwitchListener, backEndElementSendingMessageVisitor, false, "", false, "tx switch ", false);
        }
    }

    public void printTxData(){
        ArrayList<Boolean>txButtonStatus=mBackend.onGetTxMask();
        while(mBackend.isTxRunning()){

        }

        for(int i=0; i<txButtonStatus.size();i++){
            System.out.println(txButtonStatus.get(i));
        }
    }
}
