package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    public static final String TAG = "Maui-viewer MainWindowActivity";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private float mScaleFactor = 1.0f;
    private Context mContext;
    private TextView mTextView = null;

    public ScaleListener(Context context) {
        mContext = context;
    }

    public void setTextView(TextView textView) {
        mTextView = textView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector){
        mScaleFactor *= scaleGestureDetector.getScaleFactor();
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
        float scaleFactor = 0;
        final int rateAdjustment = 70;
        if (1 < mScaleFactor)
            scaleFactor = mScaleFactor / rateAdjustment;
        else
            scaleFactor = -1 / (mScaleFactor * rateAdjustment);
        //final float rate = 0.25f;
        ImagePositionDialog imagePositionDialog=new ImagePositionDialog(mContext);
        imagePositionDialog.showDialog();
        boolean result = mBackend.onZoom(scaleFactor/* * rate*/);
        //mImageView.setScaleX(mScaleFactor);
        //mImageView.setScaleY(mScaleFactor);
        //System.out.println("mImageView: x: " + mImageView.getX() + ", y: " + mImageView.getY() + ", w: " + mImageView.getWidth() + " ,h: " + mImageView.getHeight());
        //MauiToastMessage.displayToastMessage(mContext, result, "Zoom: " + scaleFactor, Toast.LENGTH_SHORT);
        WidgetUtility.updateBeamformerParameterOneLineTextView(mTextView, "zoom", scaleFactor, result);
        return result;
    }
}
