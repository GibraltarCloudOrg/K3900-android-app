package com.example.mauiviewcontrol;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Constraints;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import k3900.K3900;

import static android.content.Intent.getIntent;

public class CleanScreenDialog implements AutomatedTestingElement {
    public static final String TAG = "Clean Screen Dialog";
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    int mCurrentAutomatedTestingStep = 0;

    public CleanScreenDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.clean_screen_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mDialog.setCancelable(false); // to make it like a modal dialog
    }

    private void setUpWidgets() {
    }

    private void setUpListeners() {
        Button reactivateInCleanScreenButton = mDialog.findViewById(R.id.reactivateInCleanScreenButton);
        reactivateInCleanScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MauiToastMessage.displayToastMessage(mContext, true, "Please press and hold to unlock the screen.", Toast.LENGTH_LONG);
            }
        });
        reactivateInCleanScreenButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean result = true;
                MauiToastMessage.displayToastMessage(mContext, result, "Done cleaning, the screen is reactivated.", Toast.LENGTH_LONG);
                mDialog.dismiss();
                return result;
            }
        });
    }

    @Override
    public void executeAutomatedTesting() {
        /*if (!mAutomatedTestingModel.inProcess())
            return;

        EditText userNameEditText = mDialog.findViewById(R.id.userNameEditText);
        EditText passwordEditText = mDialog.findViewById(R.id.passwordEditText);
        switch (mCurrentAutomatedTestingStep % 3) {
            case 0:
                //System.out.println(LocalDateTime.now() + ": executeAutomatedTesting() called, with case 0");
                //++mCurrentAutomatedTestingStep;
                //showMainWindowForAutomatedTesting(TestCase.LogIn);
                ((Activity)mContext).runOnUiThread(new ThreadedToasterForAutomatedTesting((AutomatedTestingElement)this, mContext, TAG + ": user name and password set", 5));
                userNameEditText.setText("admin");
                passwordEditText.setText("pw");
                break;
            case 1:
                //System.out.println(LocalDateTime.now() + ": executeAutomatedTesting() called, with case 1");
                //++mCurrentAutomatedTestingStep;
                //showMainWindowForAutomatedTesting(TestCase.Probe);
                ((Activity)mContext).runOnUiThread(new ThreadedToasterForAutomatedTesting((AutomatedTestingElement)this, mContext, TAG + ": User Log In Processed.", 5));
                //passwordEditText.setText("test");
                mBackend.onUserLogIn(userNameEditText.getText().toString(), passwordEditText.getText().toString());
                //showAlphaVersionGui(null);
                break;
            case 2:
                //System.out.println(LocalDateTime.now() + ": executeAutomatedTesting() called, with case 2");
                //++mCurrentAutomatedTestingStep;
                //showMainWindowForAutomatedTesting(TestCase.Probe);
                mDialog.dismiss();
                ((Activity)mContext).runOnUiThread(new ThreadedToasterForAutomatedTesting((AutomatedTestingElement)mContext, mContext, TAG + ": Completed Log In testing.", 5));
                //userNameEditText.setText("");
                //passwordEditText.setText("");
                //mBackend.setAutomatedTestingInProcess(false);
                break;
        }
        ++mCurrentAutomatedTestingStep;*/
    }
}
