package com.example.mauiviewcontrol;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import k3900.K3900;

public class GetExamStoresStreamObserver implements StreamObserver<K3900.ExamStore> {

    final ArrayList<String> mFileNameStrings = new ArrayList<String>();

    GetExamStoresStreamObserver() {
    }

    public final ArrayList<String> getFileNameList() {
        return mFileNameStrings;
    }

    public final String getFileName(int index) {
        return mFileNameStrings.get(index);
    }

    @Override
    public void onNext(K3900.ExamStore examStore) {
        System.out.println("GetExamStoresStreamObserver.onNext() called.");
        System.out.println(LocalDateTime.now() + " : " + examStore.getName());
        mFileNameStrings.add(examStore.getName());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("GetExamStreamObserver.onError() called.");
    }

    @Override
    public void onCompleted() {
        System.out.println("GetExamStreamObserver.onCompleted() called.");
    }
}
