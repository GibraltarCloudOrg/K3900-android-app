package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;

public class MauiFloatingButton {
    private static Timer mTimer;
    private static ImagePositionTimerTask mImagePositionTimerTask;

    static public void setUpFloatingButtonListener(Context context, FloatingActionButton floatingActionButton, Dialog dialog, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean confirmationDialog, String dialogMessage, boolean showToast, String toastMessage, boolean dismissDialog){
        floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    startZoom(context, element, visitor);
                    //speedOfSoundModel.setKeepDecreasing();
                    // start timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Down Detected, Decrease Focus:", Toast.LENGTH_LONG);
                    System.out.println("Pixel size X "+SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().getPixelSizeX());
                    System.out.println("Pixel size Y " +SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().getPixelSizeY());
                    //System.out.println("Scale "+SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().getPixel());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //speedOfSoundModel.decrease();
                    //speedOfSoundModel.reset();
                    reset();
                    // stop timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Up Detected, Decrease Focus:", Toast.LENGTH_LONG);
                }
                return true;
            }
        });
    }
    
    private static void startZoom(Context context, BackEndElement element, BackEndElementSendingMessageVisitor visitor){
        mTimer = new Timer();
        mImagePositionTimerTask = new ImagePositionTimerTask(context, element, visitor);
        mTimer.scheduleAtFixedRate(mImagePositionTimerTask, 0, 500);
    }
    
    private static void reset(){
        mTimer.cancel();
        mImagePositionTimerTask.cancel();
    }
}
