package com.example.mauiviewcontrol;

import android.app.AlertDialog;
import android.content.Context;

public class LoadingDialog implements Runnable{
    private Context mContext;
    AlertDialog.Builder builder;
    private AlertDialog alert;
    public LoadingDialog(Context context){
        mContext=context;
        builder=new AlertDialog.Builder(mContext);
        alert=builder.create();
        alert.setTitle("Loading");
        alert.setMessage("Please wait.");
    }

    @Override
    public void run(){
        alert.show();
    }

    public void cancel(){
        alert.cancel();
    }
}
