package com.example.mauiviewcontrol;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import k3900.K3900;

public class GetExamStreamObserver implements StreamObserver<K3900.Exam> {

    final ArrayList<String> mExamSummaryStrings = new ArrayList<String>();
    final ArrayList<String> mExamNameStrings = new ArrayList<String>();
    final ArrayList<String> mExamDateStrings = new ArrayList<String>();

    GetExamStreamObserver() {
    }

    public final ArrayList<String> getExams() {
        return mExamSummaryStrings;
    }

    public final String getExamName(int index) {
        return mExamNameStrings.get(index);
    }

    public final String getExamDate(int index) {
        return mExamDateStrings.get(index);
    }

    @Override
    public void onNext(K3900.Exam exam) {
        System.out.println("GetExamStreamObserver.onNext() called.");
        System.out.println(
                LocalDateTime.now() + " : " + exam.getName() + ", " + exam.getDate()  + ", " + exam.getComments() + ", " + exam.getUrl()
        );
        String examInString = null;
        examInString = exam.getName();
        examInString += "\t";
        examInString += exam.getDate();
        examInString += "\t";
        examInString += exam.getComments();
        examInString += "\t";
        examInString += exam.getUrl();
        mExamSummaryStrings.add(examInString);
        mExamNameStrings.add(exam.getName());
        mExamDateStrings.add(exam.getDate());
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
