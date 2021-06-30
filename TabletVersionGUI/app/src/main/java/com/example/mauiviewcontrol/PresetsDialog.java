package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PresetsDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "Presets Dialog";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    /*final*/ AutoCompleteTextView mSelectedProbeView = null;

    public PresetsDialog(Context parent) {
        mContext = parent;
    }

    public void process() {
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.presets_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    private void setUpWidgets() {
        LinearLayout presetsTopLevelLinearLayout = mDialog.findViewById(R.id.presetsTopLevelLinearLayout);
        presetsTopLevelLinearLayout.setMinimumWidth(1000);
    }

    private void setUpListeners() {
        Button cancelPresetsButton = mDialog.findViewById(R.id.exitInPresetsButton);
        cancelPresetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Presets Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
