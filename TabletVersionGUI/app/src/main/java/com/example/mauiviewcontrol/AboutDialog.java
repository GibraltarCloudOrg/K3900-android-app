package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class AboutDialog {
    public static final String TAG = "About Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    final String kGuiVersion = "GUI Tablet Beta2 Build Number: 2033\nBuilt on: June 23rd, 2021" + "\n\n\n";

    public AboutDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.about_view);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        setUpWidgets();
        setUpListeners();
        //ImageStreamer.getImageStreamerSingletonInstance().setImageView(mDialog.findViewById(R.id.engineeringImagingImageView));
        mDialog.show();
        mDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
    }

    private void setUpWidgets() {
        String about = new String();
        try {
            about = mBackend.onGetAbout();
            about += "\n\n\n";
        } catch (Exception e) {
            about += "Get system about failed.";
        } finally {
            about += kGuiVersion;
            ((TextView)mDialog.findViewById(R.id.aboutMessageTextView)).setText(about);
        }
    }

    private void setUpListeners() {
        Button exitAboutDialogButton = mDialog.findViewById(R.id.exitAboutDialogButton);
        exitAboutDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "About Dialog Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
