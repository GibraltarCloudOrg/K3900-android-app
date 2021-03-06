package com.example.mauiviewcontrol;

import java.util.ArrayList;

public class ElementDataSaver /*implements Runnable*/{
    private ElementMaskingSetup txElementSetup;
    private ElementMaskingSetup rxElementSetup;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();

    public ElementDataSaver(ElementMaskingSetup tx, ElementMaskingSetup rx){
        //SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().setMessageTo(SwitchBackEndModel.MessageTo.BeamformerClient);
        txElementSetup=tx;
        rxElementSetup=rx;
    }

    public void save(){
        ArrayList<Boolean> txStatus=txElementSetup.getSetButtonStatus();
        ArrayList<Boolean> rxStatus=rxElementSetup.getSetButtonStatus();

        for(int i=0;i<192;i++){
            mBackend.onChangeTxMask(i,txStatus.get(i));
            mBackend.onChangeRxMask(i,rxStatus.get(i));
            //BeamformerClient.getBeamformerClientSingletonInstance().onChangeTxMask(i,txStatus.get(i));
            //BeamformerClient.getBeamformerClientSingletonInstance().onChangeRxMask(i, rxStatus.get(i));
        }

        ArrayList<Boolean>txAfterChange=mBackend.onGetTxMask();
        ArrayList<Boolean>rxAfterChange=mBackend.onGetRxMask();
    }
}
