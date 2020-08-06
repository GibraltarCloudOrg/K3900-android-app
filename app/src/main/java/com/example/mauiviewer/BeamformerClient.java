package com.example.mauiviewer;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.Deadline;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import k3900.BeamformerGrpc;
import k3900.K3900;

class BeamformerClient {

    private static final String TAG = "BackEnd";
    private ManagedChannel mGrpcChannel;
    private BeamformerGrpc.BeamformerBlockingStub mBlockingStub;

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
            throw new LostCommunicationException("Ping failed", e);
            //return "Communication error";
        }
    }

    K3900.SystemState getSystemState() {
        K3900.SystemState state;
        try {
            state =  mBlockingStub.withDeadlineAfter(100, TimeUnit.MILLISECONDS).getSystemState(K3900.SystemStateRequest.newBuilder().build());
            return state;
        } catch (Exception e) {
            throw new LostCommunicationException("Get system state failed", e);
        }
    }

    Image getImage(K3900.ImageRequest.Builder request) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (request.getChunkSize() == 0) {
            request.setChunkSize(64);
        }
        try {
            long time;
            Iterator<K3900.ImageChunk> it =  mBlockingStub.withDeadlineAfter(100, TimeUnit.MILLISECONDS).sendBeamformedImageStream(request.build());

            K3900.ImageChunk first_chunk = it.next();
            Log.d(TAG, "GOT HERE");
            time = first_chunk.getTime();
            Log.d(TAG, "Time = " + time);
            output.write(first_chunk.getPixels().toByteArray());
            while (it.hasNext()) {
                K3900.ImageChunk chunk = it.next();
                output.write(chunk.getPixels().toByteArray());
            }
            if (output.size() == 0) {
                throw new ImageNotAvailableException("no image");
            }

            return new Image(output.toByteArray(), time);
        } catch (ImageNotAvailableException e) {
            throw e;
        } catch (Exception e) {
            throw new LostCommunicationException("Lost Communication: Get image failed", e);
        }
    }
}
