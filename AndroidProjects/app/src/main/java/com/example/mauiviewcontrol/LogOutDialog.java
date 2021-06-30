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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import k3900.K3900;

import static android.content.Intent.getIntent;

public class LogOutDialog implements AutomatedTestingElement {
    public static final String TAG = "Log Out Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    int mCurrentAutomatedTestingStep = 0;

    public LogOutDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.log_out_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    private void setUpWidgets() {
        Button processLogOutButton = mDialog.findViewById(R.id.processLogOutButton);
        processLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = mBackend.onUserLogOut();
                String toastMessage;
                mDialog.dismiss();
                if (0 == result.length())
                    toastMessage = "User Logged Out.";
                else
                    toastMessage = result;
                Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
        Button cancelButton = mDialog.findViewById(R.id.logOutCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext,"Log Out Dialog Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpListeners() {
        //setUpExitDialogButtonListener();
    }

    @Override
    public void executeAutomatedTesting() {
        if (!mAutomatedTestingModel.inProcess())
            return;

        switch (mCurrentAutomatedTestingStep % 3) {
            case 0:
                //System.out.println(LocalDateTime.now() + ": executeAutomatedTesting() called, with case 0");
                //showMainWindowForAutomatedTesting(TestCase.LogIn);
                ((Activity) mContext).runOnUiThread(new ThreadedToasterForAutomatedTesting((AutomatedTestingElement) this, mContext, TAG + ": Logging Out soon....", 5));
                break;
            case 1:
                mBackend.onUserLogOut();
                mDialog.dismiss();
                break;
        }
        ++mCurrentAutomatedTestingStep;
    }

    /*private void setUpExitDialogButtonListener() {
        Button exitMeasurementButton = mDialog.findViewById(R.id.logOutCancelButton);
        exitMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Log Out Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }*/
}
