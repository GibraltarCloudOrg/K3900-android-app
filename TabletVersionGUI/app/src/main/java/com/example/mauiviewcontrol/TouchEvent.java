package com.example.mauiviewcontrol;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import k3900.K3900;

public class TouchEvent {
    private static final String TAG = "TouchEvent";
    static private float mLastTouchX;
    static private float mLastTouchY;
    static private final int INVALID_POINTER_ID = -1;
    static private int mActivePointerId = INVALID_POINTER_ID;
    static private float mPosX;
    static private float mPosY;
    static private final int sfImagingMinXPosition = 300;
    static private final int sfImagingMaxXPosition = 1600;
    static private final int sfTgcMinXPosition = 2150;
    static private final int sfTgcMaxXPosition = 2460;
    static private final int sfTgcMinYPosition = 200;
    static private final int sfTgcMaxYPosition = 1010;
    static private ArrayList<MauiSlider> mTgcSeekBars = null;
    static private TextView mBeamformerParameterValueTextView = null;

    static public void setTextView(TextView textView) {
        mBeamformerParameterValueTextView = textView;
    }

    public static void setTgcSliderArray(ArrayList<MauiSlider> tgcSeekBars) {
        mTgcSeekBars = tgcSeekBars;
    }

    static public boolean onTouchEvent(MotionEvent motionEvent, ScaleGestureDetector scaleGestureDetector) {
        return onTouchEvent(motionEvent, scaleGestureDetector, 0, 1);
    }

    static public boolean onTouchEvent(MotionEvent motionEvent, ScaleGestureDetector scaleGestureDetector, float offset, float scale) {
        scaleGestureDetector.onTouchEvent(motionEvent);

        final int action = MotionEventCompat.getActionMasked(motionEvent);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_DOWN");
                final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                final float y = MotionEventCompat.getY(motionEvent, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_DOWN Position(" + x + ", " + y + "), " + mPosX + ", " + mPosY + "),   Last Touch(" +mLastTouchX + ", " + mLastTouchY + ")");
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);

                final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                final float y = MotionEventCompat.getY(motionEvent, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                float relativeX = offset + scale * x;
                if (sfImagingMinXPosition < relativeX && sfImagingMaxXPosition > relativeX) {
                    boolean result = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().onPan((int) dx / 10, -(int) dy / 10);
                    WidgetUtility.updateBeamformerParameterTextView(mBeamformerParameterValueTextView, "move", "(" + String.format("%.4f", dx / 10) + ", " + String.format("%.4f", dy / 10) + ")", result);
                }
                else if (sfTgcMinXPosition < relativeX && sfTgcMaxXPosition > relativeX && sfTgcMinYPosition < y && sfTgcMaxYPosition > y)
                    updateTgcValue(relativeX, y);
                //mBackend.onTouchUpdate((int)dx / 100, (int)dy / 100);

                //invalidate();
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_MOVE Position(" + x + ", " + y + "), " + mPosX + ", " + mPosY + "),   Last Touch(" +mLastTouchX + ", " + mLastTouchY + ")");

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_UP");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_CANCEL");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_POINTER_UP");
                final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                final int pointerId = MotionEventCompat.getPointerId(motionEvent, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(motionEvent, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(motionEvent, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(motionEvent, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    static private void updateTgcValue(float xPosition, float yPosition) {
        //setTgcSlidersEnabled(false);
        float value = (ParameterLimits.MaxTgc - ParameterLimits.MinTgc) * (xPosition - sfTgcMinXPosition ) / (sfTgcMaxXPosition - sfTgcMinXPosition) - 1;
        setTgcValue(yPosition, value);
        //SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().onTgcChanged(0, value);
        //setTgcSlidersEnabled(true);
    }

    static private void setTgcValue(float yPosition, float value) {
        try {
            int width = (sfTgcMaxYPosition - sfTgcMinYPosition) / 9;
            int index = (int)((yPosition - sfTgcMinYPosition) / width);
            MauiSlider.setCurrentMauiSliderPosition(mTgcSeekBars.get(index), value, ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
            boolean result = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().onTgcChanged(index, value);
            String title = "tgc " + (index + 1) + ": ";
            WidgetUtility.updateBeamformerParameterTextView(mBeamformerParameterValueTextView, title, value, result);
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "setTgcValue() failed.");
        }
    }

    static private void setTgcSlidersEnabled(boolean enabled) {
        try {
            for (int index = 0; index < mTgcSeekBars.size(); ++index) {
                SeekBar seekBar = mTgcSeekBars.get(index);
                seekBar.setEnabled(enabled);
                //mTgcSeekBars.get(index).getProgressDrawable().setAlpha(255);
                seekBar.post(new Runnable(){
                    @Override
                    public void run()
                    {
                        seekBar.getProgressDrawable().setAlpha(255);
                        seekBar.setEnabled(true);
                    }
                });
            }
        }
        catch (Exception e) {
            Log.d(TAG, LocalDateTime.now() + " : " + "setTgcSlidersEnabled() failed.");
        }
    }
}
