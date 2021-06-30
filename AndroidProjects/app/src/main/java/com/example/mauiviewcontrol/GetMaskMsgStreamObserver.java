package com.example.mauiviewcontrol;

import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import k3900.K3900;

public class GetMaskMsgStreamObserver implements StreamObserver<K3900.MaskMsg>{
    private static final String TAG="Get Mask Msg Stream Observer";
    private ArrayList<K3900.MaskMsg>elementStatus=new ArrayList<K3900.MaskMsg>();
    private ArrayList<Boolean>elementBooleans=new ArrayList<Boolean>();
    private boolean completed=false;

    public final ArrayList<K3900.MaskMsg>getElementStatus(){
        return elementStatus;
    }
    public final ArrayList<Boolean>getElementBooleans(){
        return elementBooleans;
    }

    @Override
    public void onNext(K3900.MaskMsg maskMsg){
        //elementStatus.add(maskMsg);
        elementBooleans.add(maskMsg.getOn());
        //System.out.println(maskMsg.getOn());
        completed=false;
    }

    @Override
    public void onError(Throwable throwable){
        System.out.println("Error occurred");
    }

    @Override
    public void onCompleted(){
        System.out.println("Completed");
        completed=true;
    }

    public boolean getCompleted(){
        return completed;
    }
}
