package com.example.mauiviewcontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import k3900.K3900;

public class GetImageStreamObserver implements StreamObserver<K3900.ImageChunk> {

    /*final ArrayList<String> mExamSummaryStrings = new ArrayList<String>();
    final ArrayList<String> mExamNameStrings = new ArrayList<String>();
    final ArrayList<String> mExamDateStrings = new ArrayList<String>();*/

    GetImageStreamObserver() {
    }

    /*public final ArrayList<String> getImages() {
        return mExamSummaryStrings;
    }

    public final String getImageName(int index) {
        return mExamNameStrings.get(index);
    }

    public final String getImageDate(int index) {
        return mExamDateStrings.get(index);
    }*/

    public Image getImage() {
        return new Image(mOutput.toByteArray(), mTime);
    }

    @Override
    public void onNext(K3900.ImageChunk imageChunk) {
        System.out.println("getImageStreamObserver.onNext() called.");
        if(0 == mTime)
            mTime = imageChunk.getTime();
        try {
            mOutput.write(imageChunk.getPixels().toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*System.out.println(
                LocalDateTime.now() + " : " + exam.getName() + ", " + exam.getDate()  + ", " + exam.getComments() + ", " + exam.getUrl()
        );*/
        /*String examInString = null;
        examInString = exam.getName();
        examInString += "\t";
        examInString += exam.getDate();
        examInString += "\t";
        examInString += exam.getComments();
        examInString += "\t";
        examInString += exam.getUrl();
        mExamSummaryStrings.add(examInString);
        mExamNameStrings.add(exam.getName());
        mExamDateStrings.add(exam.getDate());*/
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("getImageStreamObserver.onError() called.");
    }

    @Override
    public void onCompleted() {
        System.out.println("getImageStreamObserver.onCompleted() called.");
        mTime = 0;
        mOutput.reset();
    }

    private static long mTime = 0;
    private static ByteArrayOutputStream mOutput = new ByteArrayOutputStream();
}
