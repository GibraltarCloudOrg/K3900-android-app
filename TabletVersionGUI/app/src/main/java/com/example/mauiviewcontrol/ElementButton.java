package com.example.mauiviewcontrol;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ElementButton extends androidx.appcompat.widget.AppCompatButton {

    //int mGroup;
    //int mXPosition;
    //int mYPosition;
    private int mButtonNumber;
    private Drawable maskedColor;
    private Drawable unmaskedColor;
    private boolean mEnabled=false;
    private Context mContext;

    public ElementButton(Context context) {
        super(context);
        maskedColor=context.getResources().getDrawable(R.drawable.button_disabled);
        unmaskedColor=context.getResources().getDrawable(R.drawable.button_enabled);
        mContext=context;
    }

    public void setEnabled(boolean enabled){
        mEnabled=enabled;
        if(enabled){
            setBackground(unmaskedColor);
        }
        else{
            setBackground(maskedColor);
        }
    }

    public void alternate(){
        setEnabled(!mEnabled);
    }

    public void setButtonNumber(int n){
        mButtonNumber=n;
    }

    public int getButtonNumber(){
        return mButtonNumber;
    }

    public boolean getEnabled(){
        return mEnabled;
    }

    public void addBorder(){
        maskedColor=mContext.getResources().getDrawable(R.drawable.bordered_button_disabled);
        unmaskedColor=mContext.getResources().getDrawable(R.drawable.bordered_button_enabled);
    }
}
