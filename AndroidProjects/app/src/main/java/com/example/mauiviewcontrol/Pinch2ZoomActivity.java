package com.example.mauiviewcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import java.time.LocalDateTime;

public class Pinch2ZoomActivity extends AppCompatActivity {
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinch2_zoom);
        mImageView=(ImageView)findViewById(R.id.imageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    float mLastTouchX;
    float mLastTouchY;
    final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    float mPosX;
    float mPosY;

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);

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
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);

                final float x = MotionEventCompat.getX(motionEvent, pointerIndex);
                final float y = MotionEventCompat.getY(motionEvent, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                //invalidate();

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;
                System.out.println(LocalDateTime.now() + ": onTouchEvent() called, with case: ACTION_MOVE Position(" + mPosX + ", " + mPosY + "),   Last Touch(" +mLastTouchX + ", " + mLastTouchY + ")");

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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            mImageView.setScaleX(mScaleFactor);
            mImageView.setScaleY(mScaleFactor);
            //System.out.println("mImageView: x: " + mImageView.getX() + ", y: " + mImageView.getY() + ", w: " + mImageView.getWidth() + " ,h: " + mImageView.getHeight());
            return true;
        }
    }
}

