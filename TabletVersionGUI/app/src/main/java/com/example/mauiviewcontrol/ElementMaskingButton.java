package com.example.mauiviewcontrol;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;


public class ElementMaskingButton extends Button {
    private boolean mEnabled = false;
    private final Drawable elementButtonActiveColor;
    private final Drawable elementButtonInactiveColor;

    public ElementMaskingButton(Context context) {
        super(context);
        elementButtonActiveColor =  context.getResources().getDrawable(R.drawable.element_button_enabled);
        elementButtonInactiveColor = context.getResources().getDrawable(R.drawable.element_button_disabled);
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        if (mEnabled)
            setBackground(elementButtonActiveColor);
        else
            setBackground(elementButtonInactiveColor);
    }

    public void alternate() {
        setEnabled(!mEnabled);
    }
}
