package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

@SuppressLint("AppCompatCustomView")
public class MauiSeekBar extends SeekBar {
    public MauiSeekBar(@NonNull Context context) {
        super(context);
    }
}
