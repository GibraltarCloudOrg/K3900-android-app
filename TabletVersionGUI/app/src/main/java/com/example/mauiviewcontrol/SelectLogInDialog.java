package com.example.mauiviewcontrol;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SelectLogInDialog implements AutomatedTestingElement {
    public static final String TAG = "Select Log In Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    //int mSelectedListItem = -1;//getIntent().getIntExtra("PositionInList", -1);
    int mCurrentAutomatedTestingStep = 0;
    private String mSelectedDeviceName;

    public SelectLogInDialog(Context context, String selectedDeviceName) {
        mContext = context;
        mSelectedDeviceName = selectedDeviceName;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.select_log_in_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mDialog.setCancelable(false); // to make it like a modal dialog
    }

    private void setUpWidgets() {
        Button guestModeButton = mDialog.findViewById(R.id.guestModeButton);
        guestModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Guest Mode Selected.", Toast.LENGTH_LONG).show();
                WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().setSelected(mSelectedDeviceName);
                /*((MainActivity)mContext).finish();
                Intent intent = new Intent(mContext, com.example.mauiviewcontrol.MainActivity.class);
                intent.putExtra("ShowServerListDialog", false);
                intent.putExtra("ServerName", mSelectedDeviceName);
                ((MainActivity)mContext).startActivity(intent);*/
            }
        });

        Button openLogInDialogButton = mDialog.findViewById(R.id.openLogInDialogButton);
        openLogInDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogInDialog(mContext);
                mDialog.dismiss();
                WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().setSelected(mSelectedDeviceName);
                /*((MainActivity)mContext).finish();
                Intent intent = new Intent(mContext, com.example.mauiviewcontrol.MainActivity.class);
                intent.putExtra("ShowServerListDialog", false);
                intent.putExtra("ServerName", mSelectedDeviceName);
                ((MainActivity)mContext).startActivity(intent);*/
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
        ++mCurrentAutomatedTestingStep;
    }
}
