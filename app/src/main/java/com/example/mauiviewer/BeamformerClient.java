package com.example.mauiviewer;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import k3900.BeamformerGrpc;
import k3900.K3900;

public class BeamformerClient {

    public static final String TAG = "BackEnd";
    ManagedChannel mGrpcChannel;
    BeamformerGrpc.BeamformerBlockingStub mBlockingStub;

    void connect(String address, int port) {
        mGrpcChannel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
        mBlockingStub = BeamformerGrpc.newBlockingStub(mGrpcChannel);
    }

    void disconnect() {
        try {
            Log.d(TAG, "Shutting down channel");
            mGrpcChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.d(TAG, "Failed to shutdown channel");
        }
    }

    String ping() {
        try {
            K3900.ResponseMsg msg =
                    mBlockingStub.withDeadlineAfter(100, TimeUnit.MILLISECONDS).pingServer(K3900.BlankRequest.newBuilder().build());
            return msg.getMsg();
        } catch (Exception e) {
            return "Communication error";
        }
    }
}
