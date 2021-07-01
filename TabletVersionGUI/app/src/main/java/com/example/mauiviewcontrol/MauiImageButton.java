package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Timer;

public class MauiImageButton {
    private static Timer mTimer;
    private static ImagePositionTimerTask mImagePositionTimerTask;

    static public void setUpListener(Context context, ImageButton imageButton, Dialog dialog, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean confirmationDialog, String dialogMessage, boolean showToast, String toastMessage, boolean dismissDialog){
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    startPan(context, element, visitor);
                    //speedOfSoundModel.setKeepDecreasing();
                    // start timer here.
                    MauiToastMessage.displayToastMessage(context, true, "Action Down Detected, Decrease Focus:", Toast.LENGTH_LONG);
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

    private static void startPan(Context context, BackEndElement element, BackEndElementSendingMessageVisitor visitor){
        mTimer = new Timer();
        mImagePositionTimerTask = new ImagePositionTimerTask(context, element, visitor);
        mTimer.scheduleAtFixedRate(mImagePositionTimerTask, 0, 250);
    }

    private static void reset(){
        mTimer.cancel();
        mImagePositionTimerTask.cancel();
    }
}
