package com.example.mauiviewcontrol;

import android.content.Context;
import android.widget.Toast;

public class MauiToastMessage {
    static public boolean displayToastMessage(Context context, boolean result, String message, int length) {
        if (result)
            message += " Success.";
        else
            message += " Failed.";
        Toast.makeText(context,  message, length).show();
        return null == context;
    }

    static public boolean displayToastMessageForCompoundButton(Context context, boolean result, String message, int length) {
        if (result)
            message += " on.";
        else
            message += " off.";
        Toast.makeText(context,  message, length).show();
        return null == context;
    }

}
