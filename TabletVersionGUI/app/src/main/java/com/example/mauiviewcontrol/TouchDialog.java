package com.example.mauiviewcontrol;


import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TouchDialog extends Dialog {
    public TouchDialog(@NonNull Context context, int themeResId, ScaleGestureDetector scaleGestureDetector) {
        super(context, themeResId);
        mScaleGestureDetector = scaleGestureDetector;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return TouchEvent.onTouchEvent(motionEvent, mScaleGestureDetector);
    }

    public void setTgcSliderArray(ArrayList<MauiSlider> tgcSeekBars) {
        TouchEvent.setTgcSliderArray(tgcSeekBars);
    }

    private ScaleGestureDetector mScaleGestureDetector;
}
