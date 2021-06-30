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
        if (0 != mOffset)
            return TouchEvent.onTouchEvent(motionEvent, mScaleGestureDetector, mOffset, mScale);
        else
            return TouchEvent.onTouchEvent(motionEvent, mScaleGestureDetector);
    }

    public void setTgcSliderArray(ArrayList<MauiSlider> tgcSeekBars) {
        TouchEvent.setTgcSliderArray(tgcSeekBars);
    }

    public void setOffset(float offset) {
        mOffset = offset;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    private ScaleGestureDetector mScaleGestureDetector;
    private float mOffset = 0;
    private float mScale = 1;
}
