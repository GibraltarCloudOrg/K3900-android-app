package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EngineeringImagingDialog {
    public static final String TAG = "Engineering Imaging Dialog";
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private final Context mContext;
    private /*final*/ TouchDialog mDialog = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean mReadyForCheckRealtimeStates = false;
    private ImageView mPreviousImageView = null;
    private boolean mEnableDisplayWidgets = true;

    public EngineeringImagingDialog(MainWindowActivity parent) {
        mContext = parent;
        mDialog = new TouchDialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen, new ScaleGestureDetector(mContext, new ScaleListener(mContext)));
        //mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.engineering_imaging_view);
        /*Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);*/
        setUpWidgets();
        setUpListeners();
        mPreviousImageView = ImageStreamer.getImageStreamerSingletonInstance().getImageView();
        ImageStreamer.getImageStreamerSingletonInstance().setImageView(mDialog.findViewById(R.id.engineeringImagingImageView));
        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.CENTER);
        //mDialog.getWindow().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        mReadyForCheckRealtimeStates = true;
    }

    /*public void run() {
        K3900.ImageRequest.Builder imageRequest;
        long nextTime = 0;
        imageRequest = K3900.ImageRequest.newBuilder().setTime(nextTime);
        try {
            Runnable runnable = new Runnable() {
                public void run() {
                    Image img = mBackend.getImage(imageRequest);
                    if (null == img)
                        return;
                    final ImageView image = mDialog.findViewById(R.id.engineeringImagingImageView);
                    int size = 512*512;
                    //int size = 1024*1024;

                    byte[] byteArray = new byte[size];
                    //byteArray = img.getData();
                    //for (int i = 0; i < size; i++) {
                        //byteArray[i] = (byte)(i%256);
                    //}
                    int[] color = new int[size];
                    byteArray = Arrays.copyOf(img.getData(), size);
                    for (int i = 0; i < size; i++) {
                        //int b = ((int)byteArray[i])&0xff;
                        int b = ((int)(byteArray[i]))&0xff;
                        //color[i] = 0xFF000000 | (((int)byteArray[i])&0xff << 16) | (byteArray[i] << 8) | (byteArray[i]);
                        //color[i] = 0xFF000000 | b;
                        color[i] = 0xFF000000 | (b << 16) | (b << 8) | b;
                    }
                    final Bitmap bmap = Bitmap.createBitmap(color, 512, 512, Bitmap.Config.ARGB_8888);
                    //ByteArrayInputStream isBm = new ByteArrayInputStream(byteArray);
                    //Bitmap bm = BitmapFactory.decodeStream(isBm, null, null);
                    //Bitmap bmap = BitmapFactory.decodeByteArray(img.getData(), 0, img.getData().length);
                    //Bitmap bmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    image.post(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(bmap);
                        }
                    });
                    //ImageView image = (ImageView) findViewById(R.id.bfImageView);
                    //image.setImageBitmap(Bitmap.createScaledBitmap(bmap, image.getWidth(), image.getHeight(), false));
                    //size = image.getMaxHeight()*image.getMaxWidth();
                    //Log.d(TAG, "size = (" + image.getWidth() + ", " + image.getHeight() + ")");

                        //if (img.getTime() == Long.MAX_VALUE) {
                        //    nextTime = 1;
                        //} else {
                        //    nextTime = img.getTime() + 1;
                        //}
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();
        } catch (LostCommunicationException le) {
            Log.d(TAG, le.getCause().getMessage());
            Log.d(TAG, le.getMessage());
        } catch (ImageNotAvailableException ie) {
            Log.d(TAG, ie.getMessage());
        }
    }*/

    private void setUpWidgets() {
        //WidgetUtility.setUpTgcSlider(R.id.tgc1InImagingSeekBar, 0, null, mDialog);
        //WidgetUtility.setUpTgcSlider(R.id.tgc2InImagingSeekBar, 1, null, mDialog);
        //WidgetUtility.setUpTgcSlider(R.id.tgc3InImagingSeekBar, 2, null, mDialog);
        /*WidgetUtility.setUpTgcSlider(R.id.tgc4InImagingSeekBar, 3, null, mDialog);
        WidgetUtility.setUpTgcSlider(R.id.tgc5InImagingSeekBar, 4, null, mDialog);
        WidgetUtility.setUpTgcSlider(R.id.tgc6InImagingSeekBar, 5, null, mDialog);
        WidgetUtility.setUpTgcSlider(R.id.tgc7InImagingSeekBar, 6, null, mDialog);
        WidgetUtility.setUpTgcSlider(R.id.tgc8InImagingSeekBar, 7, null, mDialog);
        WidgetUtility.setUpTgcSlider(R.id.tgc9InImagingSeekBar, 8, null, mDialog);*/
    }

    private void setUpListeners() {
        setUpDecreaseFocusButtonListener();
        setUpIncreaseFocusButtonListener();
        //setUpStepBackwardButtonListener();
        //setUpStepForwardButtonListener();
        //setUpPlayPauseButtonListener();
        setUpToggleSosButtonListener();
        setUpGrayscaleAdjustButtonsListeners();
        setUpSlidersListeners();
        setUpExitButtonListener();
    }

    /*private void setUpStepBackwardButtonListener() {
        ImageButton stepBackwardInEngineeringImagingImageButton = mDialog.findViewById(R.id.stepBackwardInEngineeringImagingImageButton);
        stepBackwardInEngineeringImagingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onStepBackward();
                WidgetUtility.displayToastMessage(mContext, result, "Step Backward:", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setUpStepForwardButtonListener() {
        ImageButton stepForwardInEngineeringImagingImageButton = mDialog.findViewById(R.id.stepForwardInEngineeringImagingImageButton);
        stepForwardInEngineeringImagingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onStepForward();
                WidgetUtility.displayToastMessage(mContext, result, "Step Forward:", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setUpPlayPauseButtonListener() {
        ImageButton stepForwardInEngineeringImagingImageButton = mDialog.findViewById(R.id.playPauseInEngineeringImagingImageButton);
        stepForwardInEngineeringImagingImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onPlayPause();
                WidgetUtility.displayToastMessage(mContext, result, "Play Pause:", Toast.LENGTH_SHORT);
            }
        });
    }*/

    private void setUpSlidersListeners() {
        /*mBackEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(0), mBackEndSliderElementSendingMessageVisitor, true, "Tgc1 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc1InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(1), mBackEndSliderElementSendingMessageVisitor, true, "Tgc2 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc2InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(2), mBackEndSliderElementSendingMessageVisitor, true, "Tgc3 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc3InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(3), mBackEndSliderElementSendingMessageVisitor, true, "Tgc4 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc4InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(4), mBackEndSliderElementSendingMessageVisitor, true, "Tgc5 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc5InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(5), mBackEndSliderElementSendingMessageVisitor, true, "Tgc6 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc6InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(6), mBackEndSliderElementSendingMessageVisitor, true, "Tgc7 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc7InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(7), mBackEndSliderElementSendingMessageVisitor, true, "Tgc8 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc8InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Tgc(8), mBackEndSliderElementSendingMessageVisitor, true, "Tgc9 value updated", ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep, R.id.tgc9InImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Dlc(), mBackEndSliderElementSendingMessageVisitor, true, "Dlc value updated", ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep, R.id.dlcInImagingSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new MasterGain(), mBackEndSliderElementSendingMessageVisitor, true, "VGA Gain value updated", ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep, R.id.masterGainSeekBar);
        //WidgetUtility.setUpSliderListener(mContext, null, mDialog, new MasterGain(), mBackEndSliderElementSendingMessageVisitor, true, "MasterGain value updated", ParameterLimits.MinMasterGain, ParameterLimits.MaxMasterGain, ParameterLimits.FloatValueStep, R.id.masterGainSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new AcousticPower(), mBackEndSliderElementSendingMessageVisitor, true, "AcousticPower value updated", ParameterLimits.MinAcousticPower, ParameterLimits.MaxAcousticPower, ParameterLimits.FloatValueStep, R.id.acousticPowerSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Gaussian(), mBackEndSliderElementSendingMessageVisitor, true, "Gaussian value updated", ParameterLimits.MinGaussianFilter, ParameterLimits.MaxGaussianFilter, ParameterLimits.FloatValueStep, R.id.gaussianSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Edge(), mBackEndSliderElementSendingMessageVisitor, true, "Edge value updated", ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep, R.id.edgeSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Contrast(), mBackEndSliderElementSendingMessageVisitor, true, "Contrast value updated", ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep, R.id.grayscaleAdjust1stSeekBar);
        //WidgetUtility.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Brightness value updated", ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep, R.id.grayscaleAdjust2ndSeekBar);
        MauiSlider.setUpSliderListener(mContext, null, mDialog, new Gamma(), mBackEndSliderElementSendingMessageVisitor, true, "Gamma value updated", ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep, R.id.grayscaleAdjustGammaSeekBar);*/
    }

    private void setUpDecreaseFocusButtonListener() {
        FloatingActionButton decreaseFocusFloatingActionButton = mDialog.findViewById(R.id.decreaseFocusFloatingActionButton);
        decreaseFocusFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onFocusChange(-1000.0f);
                MauiToastMessage.displayToastMessage(mContext, result, "Decrease Focus:", Toast.LENGTH_LONG);
            }
        });
        decreaseFocusFloatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // probably not needed anymore.
                boolean result = mBackend.onFocusChange(-250.0f);
                MauiToastMessage.displayToastMessage(mContext, result, "Long Click Detected, Decrease Focus:", Toast.LENGTH_LONG);
                return result;
            }
        });
        decreaseFocusFloatingActionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    // start timer here.
                    MauiToastMessage.displayToastMessage(mContext, true, "Action Down Detected, Decrease Focus:", Toast.LENGTH_LONG);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // stop timer here.
                    MauiToastMessage.displayToastMessage(mContext, true, "Action Up Detected, Decrease Focus:", Toast.LENGTH_LONG);
                }
                return true;
            }
        });
    }

    private void setUpIncreaseFocusButtonListener() {
        FloatingActionButton increaseFocusFloatingActionButton = mDialog.findViewById(R.id.increaseFocusFloatingActionButton);
        increaseFocusFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onFocusChange(1000.0f);
                MauiToastMessage.displayToastMessage(mContext, result, "Increase Focus:", Toast.LENGTH_LONG);
            }
        });
    }

    private void setUpToggleSosButtonListener() {
        FloatingActionButton toggleSosFloatingActionButton = mDialog.findViewById(R.id.toggleSosFloatingActionButton);
        toggleSosFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onToggleSos();
                MauiToastMessage.displayToastMessage(mContext, result, "Toggle Sos:", Toast.LENGTH_LONG);
            }
        });
    }

    private void setUpGrayscaleAdjustButtonsListeners() {
        //TextView grayscale1stTextView = mDialog.findViewById(R.id.grayscale1stTextView);
        //TextView grayscale2ndTextView = mDialog.findViewById(R.id.grayscale2ndTextView);

        Button autoGrayscaleButton = mDialog.findViewById(R.id.autoGrayscaleButton);
        autoGrayscaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grayscale1stTextView.setText("Dark Enhance");
                //grayscale2ndTextView.setText("Light Enhance");
                mBackend.onAutoContrastChanged(false);
            }
        });

        Button manualGrayscaleButton = mDialog.findViewById(R.id.manualGrayscaleButton);
        manualGrayscaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grayscale1stTextView.setText("Contrast");
                //grayscale2ndTextView.setText("Brightness");
                mBackend.onAutoContrastChanged(true);
            }
        });
    }

    private void setUpExitButtonListener() {
        ((Button)mDialog.findViewById(R.id.exitEngineeringImagingButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                ImageStreamer.getImageStreamerSingletonInstance().setImageView(mPreviousImageView);
                Toast.makeText(mContext, "Engineering Imaging Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean isVisible() {
        return mDialog.isShowing();
    }

    public void checkRealtimeStates() {
        if (!mReadyForCheckRealtimeStates)
            return;
        checkSlidersStates();
        checkButtonsStates();
        updateDisplayWidgets();
    }

    private void checkSlidersStates() {
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc1InImagingSeekBar), mBackend.getTgcValue(0), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc2InImagingSeekBar), mBackend.getTgcValue(1), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc3InImagingSeekBar), mBackend.getTgcValue(2), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc4InImagingSeekBar), mBackend.getTgcValue(3), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc5InImagingSeekBar), mBackend.getTgcValue(4), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc6InImagingSeekBar), mBackend.getTgcValue(5), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc7InImagingSeekBar), mBackend.getTgcValue(6), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc8InImagingSeekBar), mBackend.getTgcValue(7), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.tgc9InImagingSeekBar), mBackend.getTgcValue(8), ParameterLimits.MinTgc, ParameterLimits.MaxTgc, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.dlcInImagingSeekBar), mBackend.getDlcValue(), ParameterLimits.MinAgcOffset, ParameterLimits.MaxAgcOffset, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.masterGainSeekBar), mBackend.getMasterGainValue(), ParameterLimits.MinVgaGain, ParameterLimits.MaxVgaGain, ParameterLimits.FloatValueStep);
        //WidgetUtility.setCurrentSliderPosition(mDialog.findViewById(R.id.masterGainSeekBar), mBackend.getMasterGainValue(), ParameterLimits.MinMasterGain, ParameterLimits.MaxMasterGain, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.acousticPowerSeekBar), mBackend.getAcousticPowerValue(), ParameterLimits.MinAcousticPower, ParameterLimits.MaxAcousticPower, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.gaussianSeekBar), mBackend.getGaussianValue(), ParameterLimits.MinGaussianFilter, ParameterLimits.MaxGaussianFilter, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.edgeSeekBar), mBackend.getEdgeValue(), ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust1stSeekBar), mBackend.getContrastValue(), ParameterLimits.MinContrast, ParameterLimits.MaxContrast, ParameterLimits.FloatValueStep);
        //WidgetUtility.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar), mBackend.getBrightnessValue(), ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep);
        MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjustGammaSeekBar), mBackend.getGammaValue(), ParameterLimits.MinGamma, ParameterLimits.MaxGamma, ParameterLimits.FloatValueStep);
    }

    private void checkButtonsStates() {
        //int livePlaybackButtonVisible = mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        //mDialog.findViewById(R.id.livePlaybackInEngineeringImagingButton).setVisibility(livePlaybackButtonVisible);
        int livePlaybackButtonVisible = !mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.livePlaybackInEngineeringImagingButton).setVisibility(livePlaybackButtonVisible);
        int runFreezeButtonVisible = mBackend.getRunState() ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.runFreezeInEngineeringImagingButton).setVisibility(runFreezeButtonVisible);
        int playPauseImageResource = mBackend.paused() ? R.drawable.right_arrow : R.drawable.pause_bars;
        ((ImageButton)mDialog.findViewById(R.id.playPauseInEngineeringImagingImageButton)).setImageResource(playPauseImageResource);

        checkAutoContrastButtonsStates();
    }

    private void checkAutoContrastButtonsStates() {
        TextView grayscale1stTextView = mDialog.findViewById(R.id.grayscale1stTextView);
        TextView grayscale2ndTextView = mDialog.findViewById(R.id.grayscale2ndTextView);
        if (mBackend.getAutoContrastState()) {
            //MauiSlider.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Light Enhance value updated", ParameterLimits.MinAutoBrightness, ParameterLimits.MaxAutoBrightness, ParameterLimits.FloatValueStep, R.id.grayscaleAdjust2ndSeekBar);
            MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar), mBackend.getBrightnessValue(), ParameterLimits.MinAutoBrightness, ParameterLimits.MaxAutoBrightness, ParameterLimits.FloatValueStep);
            mDialog.findViewById(R.id.autoGrayscaleButton).setBackgroundColor(Color.WHITE);
            mDialog.findViewById(R.id.manualGrayscaleButton).setBackgroundColor(Color.DKGRAY);
            grayscale1stTextView.setText("Dark Enhance");
            grayscale2ndTextView.setText("Light Enhance");
        }
        else {
            //MauiSlider.setUpSliderListener(mContext, null, mDialog, new Brightness(), mBackEndSliderElementSendingMessageVisitor, true, "Brightness value updated", ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep, R.id.grayscaleAdjust2ndSeekBar);
            MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.grayscaleAdjust2ndSeekBar), mBackend.getBrightnessValue(), ParameterLimits.MinBrightness, ParameterLimits.MaxBrightness, ParameterLimits.FloatValueStep);
            mDialog.findViewById(R.id.autoGrayscaleButton).setBackgroundColor(Color.DKGRAY);
            mDialog.findViewById(R.id.manualGrayscaleButton).setBackgroundColor(Color.WHITE);
            grayscale1stTextView.setText("Contrast");
            grayscale2ndTextView.setText("Brightness");
        }
    }

    private void updateDisplayWidgets() {
        CineLoop.update(mDialog.findViewById(R.id.cineLoopInEngineeringImagingSeekBar));
        //WidgetUtility.updateCineLoop(mDialog.findViewById(R.id.cineLoopProgressBar));
        //if (!mAreDisplayWidgetsVisible)
            //showDisplayWidgets(true);
        //WidgetUtility.setCentimeterText(mDialog.findViewById(R.id.cmInEngineeringImagingTextView));
        //setUpHorizontalCentimetersMeasurementNumbers();
        //setUpVerticalCentimetersMeasurementNumbers();
        ConvertibleRuler.setUpHorizontalRuler(mDialog.findViewById(R.id.horizontalRulerInEngineeringImagingImageView), mDialog.findViewById(R.id.horizontalUnitMeasurementNumberLabelsInEngineeringImagingTextView), mEnableDisplayWidgets);
        ConvertibleRuler.setUpVerticalRuler(mDialog.findViewById(R.id.verticalRulerInEngineeringImagingImageView), mDialog.findViewById(R.id.verticalUnitMeasurementNumberLabelsInEngineeringImagingTextView), mEnableDisplayWidgets);
    }
}
