package com.example.mauiviewcontrol;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import k3900.K3900;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

public class ParameterValuesDialog implements AutomatedTestingElement {
    public static final String TAG = "Parameter Values Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    //int mSelectedListItem = -1;//getIntent().getIntExtra("PositionInList", -1);
    int mCurrentAutomatedTestingStep = 0;

    public ParameterValuesDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.parameter_values_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        //mDialog.setCancelable(false); // to make it like a modal dialog
    }

    private void setUpWidgets() {
        /*String text = new String(WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().getSelectedDeviceName());
        text += "\n";
        text += LocalDateTime.now();
        text += "\n\n";
        for (int index = 0; index < 9; ++index) {
            float tgcValue = mBackend.getTgcValue(index);
            text += "TGC " + (index + 1) + ": " + tgcValue + "\n";
        }
        text += "DLC: " + mBackend.getDlcValue() + "\n";
        text += "VGA Gain: " + mBackend.getVgaGainValue() + "\n";
        text += "Acoustic Power: " + mBackend.getAcousticPowerValue() + "\n";
        text += "Gaussian: " + mBackend.getGaussianValue() + "\n";
        text += "Edge: " + mBackend.getEdgeValue() + "\n";
        text += "Contrast: " + mBackend.getContrastValue() + "\n";
        text += "Brightness: " + mBackend.getBrightnessValue() + "\n";
        text += "Gamma: " + mBackend.getGammaValue() + "\n";
        text += "Speed of Sound: " + mBackend.getSpeedOfSoundValue() + "\n";
        ((EditText)mDialog.findViewById(R.id.parameterNamesAndValuesEditText)).setText(text);
        writeFileExternalStorage();*/
        ((EditText)mDialog.findViewById(R.id.parameterNamesAndValuesEditText)).setText(mBackend.getParameterValuesInString());
    }

    /*public void writeFileExternalStorage() {

        //Text of the Document
        String textToWrite = "bla bla bla";

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        //Create a new file that points to the root directory, with the given name:
        //String filenameExternal = "Sample01";
        //File file = new File(Environment.getExternalStorageDirectory(), filenameExternal);
        File lroot = Environment.getDataDirectory();
        File file = new File(lroot, "K3900-ParameterValues.txt");
        //File file = new File(getExternalFilesDir(null), filenameExternal);
        OutputStream os = openFileOutput("samplefile.txt", MODE_PRIVATE);
        BufferedWriter lout = new BufferedWriter(new OutputStreamWriter(os));
        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);

            outputStream.write(textToWrite.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void setUpListeners() {
        //setUpExitDialogButtonListener();
    }

    /*private void setUpExitDialogButtonListener() {
        Button exitMeasurementButton = mDialog.findViewById(R.id.logInCancelButton);
        exitMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Log In Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }*/

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
