package com.example.mauiviewcontrol;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.grpc.stub.StreamObserver;
import k3900.K3900;

public class GetPatientStreamObserver implements StreamObserver<K3900.Patient> {

    private static final String TAG = "Get Patient Stream Observer";
    final ArrayList<String> mPatientStrings = new ArrayList<String>();
    final ArrayList<String> mPids = new ArrayList<String>();

    GetPatientStreamObserver() {
    }

    public final ArrayList<String> getPatients() {
        return mPatientStrings;
    }

    public final ArrayList<String> getPids() {
        return mPids;
    }

    public final String getPid(int index) {
        try {
            return mPids.get(index);
        } catch (Exception e) {
            Log.e(TAG, "getPid failed, index: " + index);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onNext(K3900.Patient patient) {
        System.out.println("GetPatientStreamObserver.onNext() called.");
        System.out.println(
                LocalDateTime.now() + " : " + patient.getId() + ": " + patient.getFirst()  + " " + patient.getLast() + " " +  patient.getBirth()
        );
        mPids.add(patient.getId());
        String patientInString = null;
        patientInString = patient.getId();
        patientInString += "\t";
        patientInString += patient.getFirst();
        patientInString += "\t";
        patientInString += patient.getLast();
        patientInString += "\t";
        patientInString += patient.getBirth();
        patientInString += "\t";
        patientInString += patient.getSex();
        mPatientStrings.add(patientInString);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("GetPatientStreamObserver.onError() called.");
    }

    @Override
    public void onCompleted() {
        System.out.println("GetPatientStreamObserver.onCompleted() called.");
    }
}
