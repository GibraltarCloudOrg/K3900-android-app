package com.example.mauiviewcontrol;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public  class ElementSetup implements Runnable {
    public static final String TAG = "Element Setup";
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private ElementButtonList elementButtonList1=new ElementButtonList(1);
    private ElementButtonList elementButtonList2=new ElementButtonList(2);
    private ElementButtonList elementButtonList3=new ElementButtonList(3);
    private boolean isTx;
    private ArrayList <Boolean>buttonStatus;
    private ArrayList<Boolean>mTxButtonStatus;
    private ArrayList<Boolean>mRxButtonStatus;
    //change to non-static
    private boolean isRunning=true;
    private boolean mInitializationCompleted;
    private static boolean sSaveButtonHidden=true;

    public ElementSetup(boolean tx){
        //SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
        isTx=tx;
        //buttonStatus=new ArrayList<Boolean>();
        //txButtonStatus=BeamformerClient.getBeamformerClientSingletonInstance().onGetTxMask();
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

    private TableRow setTableRow(Context context){
        TableRow tableRow=new TableRow(context);
        TableRow.LayoutParams a=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(a);
        return tableRow;
    }

    private void addTableRow(ElementButtonList elementButtonList, int buttonsInRow, int rowNumber, View view, Context context){
        TableLayout tableLayout=setTableLayout(view, elementButtonList.groupNumber());
        TableRow tableRow=setTableRow(context);

        for(int column=0; column<buttonsInRow; column++){
            ElementButton button = new ElementButton(context);
            if((column==2) || (column==6) || (column==10) || (column==14)){
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



            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    button.alternate();
                    checkSwitches(view);

                    if(sSaveButtonHidden){
                        if(isTx) {
                            mBackend.onChangeTxMask(index, button.getEnabled());
                            //BeamformerClient.getBeamformerClientSingletonInstance().onChangeTxMask(index, button.getEnabled());
                        }
                        else{
                            mBackend.onChangeRxMask(index, button.getEnabled());
                            //BeamformerClient.getBeamformerClientSingletonInstance().onChangeRxMask(index, button.getEnabled());
                        }
                        ArrayList<Boolean>  txStatus = mBackend.onGetTxMask();
                        ArrayList<Boolean>  rxStatus = mBackend.onGetRxMask();
                        int dd = 1;
                        dd++;
                    }
                }
            });
            //button.setEnabled(buttonStatus.get(index));
            tableRow.addView(button,120,80);
        }
        tableLayout.addView(tableRow,2000,80);
    }

    private void setButtonsEnabled(ElementButtonList elementButtonList, boolean enabled){
        for(int i=0;i<elementButtonList.size();i++){
            ElementButton button=elementButtonList.get(i);
            button.setEnabled(enabled);
        }
    }

    private void setSwitchListener(ElementButtonList elementButtonList, View view){
        int groupNumber=elementButtonList.groupNumber();
        Switch txSwitch;
        if(groupNumber==1) {
            txSwitch = (Switch) view.findViewById(R.id.txSwitch1);
        }
        else if(groupNumber==2){
            txSwitch=(Switch) view.findViewById(R.id.txSwitch2);
        }
        else{
            txSwitch = (Switch) view.findViewById(R.id.txSwitch3);
        }

        txSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean isChecked=txSwitch.isChecked();
                txSwitch.setChecked(isChecked);
                if(txSwitch.isChecked()){
                    setButtonsEnabled(elementButtonList, true);
                }
                else{
                    setButtonsEnabled(elementButtonList, false);
                }
                if(sSaveButtonHidden){
                    if(isTx) {
                        for (int i = 0; i < elementButtonList.size(); i++) {
                            mBackend.onChangeTxMask(elementButtonList.get(i).getButtonNumber(), elementButtonList.get(i).getEnabled());
                        }
                    }
                    else{
                        for (int i = 0; i < elementButtonList.size(); i++) {
                            mBackend.onChangeRxMask(elementButtonList.get(i).getButtonNumber(), elementButtonList.get(i).getEnabled());
                        }
                    }
                }
            }
        });
    }

    public void completeSetup(View view, Context context){
        mInitializationCompleted=false;
        for(int i=0;i<4;i++) {
            addTableRow(elementButtonList1,16,i, view, context);
            addTableRow(elementButtonList2,16,i, view, context);
            addTableRow(elementButtonList3,16,i, view, context);
        }

        setSwitchListener(elementButtonList1, view);
        setSwitchListener(elementButtonList2, view);
        setSwitchListener(elementButtonList3, view);

        mInitializationCompleted=true;
    }

    public void run(){
        isRunning=true;
        //System.out.println("is running= "+ isRunning);
        if(isTx) {
            //mTxButtonStatus=new ArrayList<Boolean>();
            //mTxButtonStatus = mBackend.onGetTxMask();
            final ArrayList<Boolean> tx = mBackend.onGetTxMask();
            mTxButtonStatus = tx;

            //buttonStatus = BeamformerClient.getBeamformerClientSingletonInstance().onGetTxMask();
        }
        else{
            //buttonStatus=new ArrayList<Boolean>();
            //buttonStatus = mBackend.onGetRxMask();
            //buttonStatus = BeamformerClient.getBeamformerClientSingletonInstance().onGetRxMask();
            final ArrayList<Boolean> rx = mBackend.onGetRxMask();
            mRxButtonStatus = rx;
        }

        if(isTx){
            while(mBackend.isTxRunning() || !mInitializationCompleted){
                //System.out.println("Still running");
            }
        }
        else {
            while (mBackend.isRxRunning() || !mInitializationCompleted) {
                //System.out.println("Still running");
            }
        }
        System.out.println(TAG + "size of element button list 1 is: " + elementButtonList1.size());
        System.out.println(TAG + "size of element button list 2 is: " + elementButtonList1.size());
        System.out.println(TAG + "size of element button list 3 is: " + elementButtonList1.size());
        /*try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if(isTx) {
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 16; column++) {
                    int group1 = column + (row * 16 + ((0) * 64));
                    int group2 = column + (row * 16 + ((1) * 64));
                    int group3 = column + (row * 16 + ((2) * 64));
                    ElementButton elementButton = elementButtonList1.get(column + (row * 16));
                    //Boolean status = buttonStatus.get(group1);
                    elementButtonList1.get(column + (row * 16)).setEnabled(mTxButtonStatus.get(group1));
                    elementButtonList2.get(column + (row * 16)).setEnabled(mTxButtonStatus.get(group2));
                    elementButtonList3.get(column + (row * 16)).setEnabled(mTxButtonStatus.get(group3));
                }
            }
        }
        else{
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 16; column++) {
                    int group1 = column + (row * 16 + ((0) * 64));
                    int group2 = column + (row * 16 + ((1) * 64));
                    int group3 = column + (row * 16 + ((2) * 64));
                    ElementButton elementButton = elementButtonList1.get(column + (row * 16));
                    //Boolean status = buttonStatus.get(group1);
                    elementButtonList1.get(column + (row * 16)).setEnabled(mRxButtonStatus.get(group1));
                    elementButtonList2.get(column + (row * 16)).setEnabled(mRxButtonStatus.get(group2));
                    elementButtonList3.get(column + (row * 16)).setEnabled(mRxButtonStatus.get(group3));
                }
            }
        }

        isRunning=false;
        System.out.println("End of ElementSetup.run()");
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

    public void checkSwitches(View v){
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

        Switch switch1=v.findViewById(R.id.txSwitch1);
        Switch switch2=v.findViewById(R.id.txSwitch2);
        Switch switch3=v.findViewById(R.id.txSwitch3);

        switch1.setChecked(group1);
        switch2.setChecked(group2);
        switch3.setChecked(group3);
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    public static boolean getSaveButtonHidden(){
        return sSaveButtonHidden;
    }
}
