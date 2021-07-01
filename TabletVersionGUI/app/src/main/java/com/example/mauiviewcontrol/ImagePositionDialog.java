package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class ImagePositionDialog {
    private Context mContext;
    private Dialog mDialog=null;
    private SeekBar zoomSeekBar;

    public ImagePositionDialog(Context context){
        mContext=context;
        mDialog=new Dialog(mContext);
        mDialog.setContentView(R.layout.image_position_view);
        setExitButtonListener();
        setSliderListener();
        setZoomButtonListeners();
    }

    public void showDialog(){
        mDialog.show();
    }

    public void setExitButtonListener(){
        Button exitButton=mDialog.findViewById(R.id.imagePositionExitButton);
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDialog.dismiss();
            }
        });
    }

    private void setSliderListener(){
        Zoom zoom=new Zoom();
        BackEndSliderElementSendingMessageVisitor backEndSliderElementSendingMessageVisitor=new BackEndSliderElementSendingMessageVisitor();
        MauiSlider.setUpSliderListener(mContext, null, mDialog, zoom, backEndSliderElementSendingMessageVisitor, false, "zoom", BeamformerClient.kCenterZoom-5, BeamformerClient.kCenterZoom+5, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.zoomSeekBar));
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
}
