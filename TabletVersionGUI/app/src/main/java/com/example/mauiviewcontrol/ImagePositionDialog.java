package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;

public class ImagePositionDialog {
    private Context mContext;
    private Dialog mDialog=null;
    private SeekBar zoomSeekBar;
    private static ImagePositionDialog sSingletonInstance=null;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private Timer mTimer;
    private CheckScaleTimerTask mCheckScaleTimerTask;
    private static boolean sZoomDialogOnPinch=true;
    private ImageView mPreviousImageView = null;
    private boolean mImageSet=false;

    private ImagePositionDialog(Context context){
        mContext=context;

        /*mDialog=new Dialog(mContext);
        mDialog.setContentView(R.layout.image_position_view);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.FILL_VERTICAL | Gravity.RIGHT;
        window.setAttributes(wlp);*/

        //added
        mDialog=new TouchDialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen, new ScaleGestureDetector(mContext, new ScaleListener(mContext)));
        mDialog.setContentView(R.layout.main_imaging_view);
        includeImagePositionView();
        mDialog.getWindow().setGravity(Gravity.CENTER);

        setExitButtonListener();
        setSliderListener();
        setZoomButtonListeners();
        setPanButtonListeners();
    }

    private void includeImagePositionView(){
        LinearLayout mainImagingCustomPageLinearLayout = (LinearLayout)mDialog.findViewById(R.id.mainImagingCustomPageLinearLayout);
        ViewGroup viewGroup = mDialog.findViewById(android.R.id.content);
        //View dialogView = LayoutInflater.from(mContext).inflate(R.layout.measurement_view, viewGroup, false);
        mainImagingCustomPageLinearLayout.removeAllViewsInLayout();
        /*View itemInfo1 =*/ ((MainActivity)mContext).getLayoutInflater().inflate(R.layout.image_position_view, mainImagingCustomPageLinearLayout, true);
    }

    public static ImagePositionDialog getSingletonInstance(Context context){
        if (null == sSingletonInstance) {
            sSingletonInstance = new ImagePositionDialog(context);
        }
        return sSingletonInstance;
    }

    public void setImageView(){
        if(!mImageSet) {
            mPreviousImageView = ImageStreamer.getImageStreamerSingletonInstance().getImageView();
            ImageStreamer.getImageStreamerSingletonInstance().setImageView(mDialog.findViewById(R.id.imagingImageView));
        }
        mImageSet=true;
    }

    public void showDialog(){
        startTimer();
        mDialog.show();
    }

    public void setExitButtonListener(){
        Button exitButton=mDialog.findViewById(R.id.imagePositionExitButton);
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mTimer.cancel();
                mCheckScaleTimerTask.cancel();
                mDialog.dismiss();
                ImageStreamer.getImageStreamerSingletonInstance().setImageView(mPreviousImageView);
                mImageSet=false;
            }
        });
    }

    private void setSliderListener(){
        Zoom zoom=new Zoom();
        BackEndSliderElementSendingMessageVisitor backEndSliderElementSendingMessageVisitor=new BackEndSliderElementSendingMessageVisitor();
        MauiSlider.setUpSliderListener(mContext, null, mDialog, zoom, backEndSliderElementSendingMessageVisitor, false, "zoom", ParameterLimits.MinZoom, ParameterLimits.MaxZoom, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.zoomSeekBar));
        SeekBar seekBar=mDialog.findViewById(R.id.zoomSeekBar);
        //seekBar.setProgress(seekBar.getMax()/2);
        //seekBar.setVisibility(View.INVISIBLE);
    }

    private void setZoomButtonListeners(){
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        SetZoomButtonListener setZoomInListener=new SetZoomButtonListener();
        SetZoomButtonListener setZoomOutListener=new SetZoomButtonListener();
        setZoomInListener.setValue(.1f);
        setZoomOutListener.setValue(-.1f);
        MauiFloatingButton.setUpFloatingButtonListener(mContext, mDialog.findViewById(R.id.zoomInButton), mDialog, setZoomInListener, backEndElementSendingMessageVisitor, false, "", true, "zoomed in", false);
        MauiFloatingButton.setUpFloatingButtonListener(mContext, mDialog.findViewById(R.id.zoomOutButton), mDialog, setZoomOutListener, backEndElementSendingMessageVisitor, false, "", true, "zoomed out", false);
    }

    private void setPanButtonListeners(){
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        SetPanButtonListener rightPan=new SetPanButtonListener();
        SetPanButtonListener leftPan=new SetPanButtonListener();
        SetPanButtonListener upPan=new SetPanButtonListener();
        SetPanButtonListener downPan=new SetPanButtonListener();
        rightPan.setX(-.50f);
        rightPan.setY(0f);
        leftPan.setX(.50f);
        leftPan.setY(0f);
        upPan.setX(0f);
        upPan.setY(-.50f);
        downPan.setX(0f);
        downPan.setY(.50f);
        MauiImageButton.setUpListener(mContext, mDialog.findViewById(R.id.imagePositionPanRightButton), mDialog, rightPan, backEndElementSendingMessageVisitor, false, "", true, "panned right", false);
        MauiImageButton.setUpListener(mContext, mDialog.findViewById(R.id.imagePositionPanLeftButton), mDialog, leftPan, backEndElementSendingMessageVisitor, false, "", true, "panned left", false);
        MauiImageButton.setUpListener(mContext, mDialog.findViewById(R.id.imagePositionPanUpButton), mDialog, upPan, backEndElementSendingMessageVisitor, false, "", true, "panned up", false);
        MauiImageButton.setUpListener(mContext, mDialog.findViewById(R.id.imagePositionPanDownButton), mDialog, downPan, backEndElementSendingMessageVisitor, false, "", true, "panned down", false);
    }

    public void updateSliderPosition(){
        if(!mDialog.isShowing()){
            mTimer.cancel();
            mCheckScaleTimerTask.cancel();
        }
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.zoomSeekBar), (mBackend.getZoom()), ParameterLimits.MinZoom, ParameterLimits.MaxZoom, ParameterLimits.FloatValueStep);
    }

    private void startTimer(){
        mTimer=new Timer();
        mCheckScaleTimerTask=new CheckScaleTimerTask(mContext);
        mTimer.scheduleAtFixedRate(mCheckScaleTimerTask, 1000, 1000);
    }

    public static void setZoomDialogOnPinch(boolean zoomOnPinch){
        sZoomDialogOnPinch=zoomOnPinch;
    }

    public static boolean getZoomDialogOnPinch(){
        return sZoomDialogOnPinch;
    }

    public void updateDisplayWidgets() {
        ConvertibleRuler.setUpHorizontalRuler(mDialog.findViewById(R.id.horizontalRulerImageView), mDialog.findViewById(R.id.horizontalMeasurementNumberLabelsTextView), true);
        ConvertibleRuler.setUpVerticalRuler(mDialog.findViewById(R.id.verticalRulerImageView), mDialog.findViewById(R.id.verticalMeasurementNumberLabelsTextView), true);

        CineLoop.update(mDialog.findViewById(R.id.cineLoopSeekBar));
        ((TextView) mDialog.findViewById(R.id.unitNameOfRulersTextView)).setText(mBackend.getUnitName());

        /*else {
            if (View.VISIBLE == mDialog.findViewById(R.id.cineLoopSeekBar).getVisibility())
                mDialog.findViewById(R.id.cineLoopSeekBar).setVisibility(View.INVISIBLE);
            if (View.VISIBLE == mDialog.findViewById(R.id.unitNameOfRulersTextView).getVisibility())
                mDialog.findViewById(R.id.unitNameOfRulersTextView).setVisibility(View.INVISIBLE);
        }*/
        //CineLoop.update(mDialog.findViewById(R.id.cineLoopInMeasureImagingSeekBar));
        //ConvertibleRuler.setUpHorizontalRuler(mDialog.findViewById(R.id.horizontalRulerInMeasureImagingImageView), mDialog.findViewById(R.id.horizontalUnitMeasurementNumberLabelsInMeasureImagingTextView), mEnableDisplayWidgets);
        //ConvertibleRuler.setUpVerticalRuler(mDialog.findViewById(R.id.verticalRulerInMeasureImagingImageView), mDialog.findViewById(R.id.verticalUnitMeasurementNumberLabelsInMeasureImagingTextView), mEnableDisplayWidgets);
    }
}
