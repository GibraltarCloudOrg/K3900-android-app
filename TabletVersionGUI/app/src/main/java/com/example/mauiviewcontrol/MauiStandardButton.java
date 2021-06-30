package com.example.mauiviewcontrol;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

@SuppressLint("AppCompatCustomView")
public class MauiStandardButton extends Button {
    private boolean mEnabled = false;
    private final Drawable standardButtonActiveColor;
    private final Drawable standardButtonInactiveColor;

    public MauiStandardButton(Context context) {
        super(context);
        standardButtonActiveColor =  context.getResources().getDrawable(R.drawable.button_enabled);
        standardButtonInactiveColor = context.getResources().getDrawable(R.drawable.button_disabled);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        if (mEnabled) {
            setBackground(standardButtonActiveColor);
        }
        else {
            setBackground(standardButtonInactiveColor);
        }
    }

    /*public void alternate() {
        setEnabled(!mEnabled);
    }*/
}
