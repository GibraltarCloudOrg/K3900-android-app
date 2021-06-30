package com.example.mauiviewcontrol;

import android.content.Context;
import android.view.View;
import android.widget.Switch;

import java.util.ArrayList;

public class RxElementMaskingSetup extends ElementMaskingSetup{
    private ArrayList<SetElementMasking> mSetRxElementMaskingArrayList=new ArrayList<>();

    public RxElementMaskingSetup(String name, View view, Context context, Switch leftSwitch, Switch centerSwitch, Switch rightSwitch){
        super(name, view, context, leftSwitch, centerSwitch, rightSwitch);
        mAddBorder=false;
    }

    @Override
    public void setButtonOnClickListener(ElementButton button, Context context, Switch leftSwitch, Switch centerSwitch, Switch rightSwitch){
        //button.alternate();
        if(sSaveButtonHidden) {
            final ArrayList<Boolean> rx=mBackend.onGetRxMask();
            ArrayList <Boolean> rxButtonStatus=rx;

            while(mBackend.isRxRunning()){

            }
            BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
            SetRxElementMasking setRxElementMasking = new SetRxElementMasking();
            mSetRxElementMaskingArrayList.add(setRxElementMasking);
            setRxElementMasking.setSaveButtonHidden(sSaveButtonHidden);
            setRxElementMasking.setIndex(button.getButtonNumber());
            setRxElementMasking.setMask(rxButtonStatus.get(button.getButtonNumber()));
            setRxElementMasking.setSwitches(leftSwitch, centerSwitch, rightSwitch);
            setRxElementMasking.setElementButtonLists(elementButtonList1, elementButtonList2, elementButtonList3);
            WidgetUtility.setUpListener(context, button, null, setRxElementMasking, backEndElementSendingMessageVisitor, false, "", false, "rx changed: " + setRxElementMasking.getIndex(), false);
        }
    }

    @Override
    public void run(){
        completeSetup();
        if(!sSaveButtonHidden) {
            setData();
        }
    }

    @Override
    public void setData(){
        isRunning=true;
        final ArrayList<Boolean> rx=mBackend.onGetRxMask();
        ArrayList <Boolean> rxButtonStatus=rx;

        while(mBackend.isRxRunning() || !mInitializationCompleted){

        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 16; column++) {
                int group1 = column + (row * 16 + ((0) * 64));
                int group2 = column + (row * 16 + ((1) * 64));
                int group3 = column + (row * 16 + ((2) * 64));
                //ElementButton elementButton = elementButtonList1.get(column + (row * 16));
                elementButtonList1.get(column + (row * 16)).setEnabled(rxButtonStatus.get(group1));
                elementButtonList2.get(column + (row * 16)).setEnabled(rxButtonStatus.get(group2));
                elementButtonList3.get(column + (row * 16)).setEnabled(rxButtonStatus.get(group3));
            }
        }
        isRunning=false;
    }

    @Override
    public void setSwitchListener(Context context, ElementButtonList elementButtonList, Switch selectAllSwitch, int switchGroup){
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        SetRxSwitchListener setRxSwitchListener=new SetRxSwitchListener();
        setRxSwitchListener.setElementButtonList(elementButtonList);
        setRxSwitchListener.setSwitch(selectAllSwitch);
        setRxSwitchListener.setSaveButtonHidden(sSaveButtonHidden);
        WidgetUtility.setUpListener(context, selectAllSwitch, switchGroup, mSetRxElementMaskingArrayList, null, setRxSwitchListener, backEndElementSendingMessageVisitor, false, "", false, "rx switch ", false);
    }

    public void printRxData(){
        ArrayList<Boolean>rxButtonStatus=mBackend.onGetRxMask();
        while(mBackend.isRxRunning()){

        }

        for(int i=0;i<rxButtonStatus.size();i++){
            System.out.println(rxButtonStatus.get(i));
        }
    }
}
