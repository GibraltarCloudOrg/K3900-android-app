package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("AppCompatCustomView")
public class MauiSlider extends SeekBar {
    public MauiSlider(@NonNull Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    static public void setUpSliderListener(Context context, AppCompatActivity appCompatActivity, Dialog dialog, BackEndSliderElement element, BackEndSliderElementSendingMessageVisitor visitor, boolean showToast, String title, float min, float max, float step, SeekBar seekBar) {
        //SeekBar seekBar = getSeekBar(appCompatActivity, dialog, seekBarId);
        if (null == seekBar)
            return;
        seekBar.setMin((int)min);
        seekBar.setMax((int) ((max - min) / step));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //System.out.println("setUpSliderListener() --> onStopTrackingTouch() called.");
                        float value = min + (seekBar.getProgress() * step);
                        element.setValue(value);
                        boolean result = (showToast ? (MauiToastMessage.displayToastMessage(context, element.accept(visitor), title + element.getRuntimeSubText() + ": " + value, Toast.LENGTH_SHORT)) : element.accept(visitor));
                        WidgetUtility.updateBeamformerParameterTextView(dialog.findViewById(R.id.beamformerParameterValueTextView), title, value, result);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        //System.out.println("setUpSliderListener() --> onStartTrackingTouch() called.");
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        float value = min + (seekBar.getProgress() * step);
                        element.setValue(value);
                        boolean result = element.accept(visitor);
                        WidgetUtility.updateBeamformerParameterTextView(dialog.findViewById(R.id.beamformerParameterValueTextView), title, value, result);
                        //System.out.println("setUpSliderListener() --> onProgressChanged() called.");
                        //float value = min + (progress * step);
                        //K3900.BeamformerParametersResponse response = mBackend.onDlcChanged(value);
                    }
                }
        );
    }

    static public void setUpSliderListener(Context context, TextView textView, BackEndSliderElement element, BackEndSliderElementSendingMessageVisitor visitor, boolean showToast, String title, float min, float max, float step, SeekBar seekBar) {
        if (null == seekBar)
            return;
        seekBar.setMin((int)min);
        seekBar.setMax((int) ((max - min) / step));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        float value = min + (seekBar.getProgress() * step);
                        element.setValue(value);
                        boolean result = (showToast ? (MauiToastMessage.displayToastMessage(context, element.accept(visitor), title + element.getRuntimeSubText() + ": " + value, Toast.LENGTH_SHORT)) : element.accept(visitor));
                        WidgetUtility.updateBeamformerParameterTextView(textView, title, value, result);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        float value = min + (seekBar.getProgress() * step);
                        element.setValue(value);
                        boolean result = element.accept(visitor);
                        WidgetUtility.updateBeamformerParameterTextView(textView, title, value, result);
                    }
                }
        );
    }

    public static void waitFor(int seconds) {
        seconds = seconds < 0 ? 0 : seconds;
        while (--seconds >= 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public void setCurrentSliderPosition(SeekBar seekBar, float value, float min, float max, float step) {
        //float floatValue = (max - min) * (value - min) / (step * (max - min));
        //int intValue = (int)floatValue;
        if (seekBar.isEnabled()) {
            //float x = (max - min) * (value - min) / (step * (max - min));
            //seekBar.setProgress((int) ((max - min) * (value - min) / (step * (max - min))));
            seekBar.setProgress((int)((value-min)/step));
        }
    }

    /*static public void setCurrentSliderPosition(SeekBar seekBar, float value, float min, float max, float step, Context context) {
        //float floatValue = (max - min) * (value - min) / (step * (max - min));
        //int intValue = (int)floatValue;
        if (seekBar.isEnabled()) {
            //float x = (max - min) * (value - min) / (step * (max - min));
            //Toast.makeText(context, "" + x , Toast.LENGTH_SHORT).show();
            //seekBar.setProgress((int) ((max - min) * (value - min) / (step * (max - min))));
            seekBar.setProgress((int)((value-min)/step));
        }
    }*/

    static public void setCurrentMauiSliderPosition(MauiSlider seekBar, float value, float min, float max, float step) {
        if (seekBar.isEnabled())
            seekBar.setProgress((int) ((max - min) * (value - min) / (step * (max - min))));
    }

    //static public void setButtonEnabled(Context context, Button button, boolean enabled) {
    //button.setEnabled(enabled);
    //final Drawable buttonActiveColor = context.getResources().getDrawable(R.drawable.element_button_enabled);
    //final Drawable buttonInactiveColor = context.getResources().getDrawable(R.drawable.element_button_disabled);
    //final Drawable buttonActiveColor = context.getResources().getDrawable(R.drawable.button_enabled);
    //final Drawable buttonInactiveColor = context.getResources().getDrawable(R.drawable.button_disabled);
    //button.setBackgroundColor(buttonActiveColor.to);
        /*if (enabled)
            button.setBackground(buttonActiveColor);
        else
            button.setBackground(buttonInactiveColor);*/
        /*if (enabled)
            button.setBackground(Drawable.createFromPath("/drawable/element_button_enabled"));
        else
            button.setBackground(Drawable.createFromPath("/drawable/element_button_disabled"));*/
    //button.setBackgroundResource(R.drawable.button_enabled);
        /*Drawable drawable = context.getResources().getDrawable(R.drawable.button_enabled);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.teal_200));

        button.setCompoundDrawables(null, drawable, null, null);*/
    //button.setBackgroundResource(R.drawable.button_enabled);
    //Drawable img = context.getResources().getDrawable(R.drawable.button_enabled);
    //img.setBounds(0, 0, 60, 60);
    //button.setCompoundDrawables(img, null, null, null);
    //button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.button_enabled, 0, 0, 0);
    //button.setImageResource(R.drawable.button_enabled);
    //}

    static private SeekBar getSeekBar(AppCompatActivity appCompatActivity, Dialog dialog, int seekBarId) {
        if (null != appCompatActivity)
            return appCompatActivity.findViewById(seekBarId);
        else if (null != dialog)
            return dialog.findViewById(seekBarId);
        else
            return null;
    }

    static public void setUpTgcSlider(int sliderId, int index, AppCompatActivity appCompatActivity, Dialog dialog) {
        //float step = (float)0.001;
        //float max = ParameterLimits.MaxTgc;
        //float min = ParameterLimits.MinTgc;
        SeekBar seekBar = null;
        if (null != appCompatActivity)
            seekBar = appCompatActivity.findViewById(sliderId);
        else if (null != dialog)
            seekBar = dialog.findViewById(sliderId);
        else
            return;

        //if (null != view && null == seekBar)
        //seekBar = view.findViewById(sliderId);
        //seekBar.setOutlineAmbientShadowColor(Color.WHITE);
        //seekBar.setOutlineSpotShadowColor(Color.WHITE);
        //seekBar.setBackgroundColor(Color.BLACK);
        //seekBar.setBackgroundColor(Color.BLUE);
        seekBar.setMax((int) ((ParameterLimits.MaxTgc - ParameterLimits.MinTgc) / ParameterLimits.FloatValueStep));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        float value = ParameterLimits.MinTgc + (progress * ParameterLimits.FloatValueStep);
                        boolean result = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().onTgcChanged(index, value);
                    }
                }
        );
    }

    public boolean isEnabled() {
        return mEnalbed;
    }

    public void setEnabled(boolean enabled) {
        mEnalbed = enabled;
        if (mEnalbed) {
            getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }
        else {
            getProgressDrawable().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
            getThumb().setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN);
        }
    }

    private boolean mEnalbed = true;
}
