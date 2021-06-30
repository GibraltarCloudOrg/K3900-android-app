package com.example.mauiviewcontrol;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class AutomatedTestingDialog {
    public static final String TAG = "Automated Testing Dialog";
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    AutomatedTestingModel mAutomatedTestingModel = AutomatedTestingModel.getAutomatedTestingModelSingletonInstance();
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    CheckBox mAutomatedSelectAllCheckBox;
    CheckBox mAutomatedMainWindowCheckBox;
    CheckBox mAutomatedLogInCheckBox;
    CheckBox mAutomatedImagingCheckBox;
    CheckBox mAutomatedStatusCheckBox;
    CheckBox mAutomatedPatientCheckBox;
    CheckBox mAutomatedProbeCheckBox;
    CheckBox mAutomatedMeasurementCheckBox;
    CheckBox mAutomatedPresetCheckBox;
    CheckBox mAutomatedSaveCheckBox;
    CheckBox mAutomatedLoadCheckBox;
    CheckBox mAutomatedModifyCheckBox;
    CheckBox mAutomatedVersionCheckBox;
    CheckBox mAutomatedCleanScreenCheckBox;
    CheckBox mAutomatedLogOutCheckBox;

    public AutomatedTestingDialog(MainActivity parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.automated_testing_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    private void setUpWidgets() {
        mAutomatedSelectAllCheckBox = mDialog.findViewById(R.id.automatedSelectAllCheckBox);
        mAutomatedMainWindowCheckBox = mDialog.findViewById(R.id.automatedMainWindowCheckBox);
        mAutomatedLogInCheckBox = mDialog.findViewById(R.id.automatedLogInCheckBox);
        mAutomatedImagingCheckBox = mDialog.findViewById(R.id.automatedImagingCheckBox);
        mAutomatedStatusCheckBox = mDialog.findViewById(R.id.automatedStatusCheckBox);
        mAutomatedPatientCheckBox = mDialog.findViewById(R.id.automatedPatientCheckBox);
        mAutomatedProbeCheckBox = mDialog.findViewById(R.id.automatedProbeCheckBox);
        mAutomatedMeasurementCheckBox = mDialog.findViewById(R.id.automatedMeasurementCheckBox);
        mAutomatedPresetCheckBox = mDialog.findViewById(R.id.automatedPresetCheckBox);
        mAutomatedSaveCheckBox = mDialog.findViewById(R.id.automatedSaveCheckBox);
        mAutomatedLoadCheckBox = mDialog.findViewById(R.id.automatedLoadCheckBox);
        mAutomatedModifyCheckBox = mDialog.findViewById(R.id.automatedModifyCheckBox);
        mAutomatedVersionCheckBox = mDialog.findViewById(R.id.automatedVersionCheckBox);
        mAutomatedCleanScreenCheckBox = mDialog.findViewById(R.id.automatedCleanScreenCheckBox);
        mAutomatedLogOutCheckBox = mDialog.findViewById(R.id.automatedLogOutCheckBox);
    }

    private void setUpListeners()
    {
        setUpSelectAllCheckBoxListener();
        setUpMainWindowCheckBoxListener();
        setUpLogInCheckBoxListener();
        setUpImagingCheckBoxListener();
        setUpStatusCheckBoxListener();
        setUpPatientCheckBoxListener();
        setUpProbeCheckBoxListener();
        setUpMeasurementCheckBoxListener();
        setUpPresetCheckBoxListener();
        setUpSaveCheckBoxListener();
        setUpLoadCheckBoxListener();
        setUpModifyCheckBoxListener();
        setUpVersionCheckBoxListener();
        setUpCleanScreenCheckBoxListener();
        setUpLogOutCheckBoxListener();
        //setUpExitDialogButtonListener();
    }

    private void setUpSelectAllCheckBoxListener() {
        mAutomatedSelectAllCheckBox.setChecked(mAutomatedTestingModel.getAllCasesOn());
        mAutomatedSelectAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedTestingModel.setAllAutomatedTestCasesOn(checked);
                mAutomatedSelectAllCheckBox.setChecked(checked);
                mAutomatedMainWindowCheckBox.setChecked(checked);
                mAutomatedLogInCheckBox.setChecked(checked);
                mAutomatedImagingCheckBox.setChecked(checked);
                mAutomatedStatusCheckBox.setChecked(checked);
                mAutomatedPatientCheckBox.setChecked(checked);
                mAutomatedProbeCheckBox.setChecked(checked);
                mAutomatedMeasurementCheckBox.setChecked(checked);
                mAutomatedPresetCheckBox.setChecked(checked);
                mAutomatedSaveCheckBox.setChecked(checked);
                mAutomatedLoadCheckBox.setChecked(checked);
                mAutomatedModifyCheckBox.setChecked(checked);
                mAutomatedVersionCheckBox.setChecked(checked);
                mAutomatedCleanScreenCheckBox.setChecked(checked);
                mAutomatedLogOutCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpMainWindowCheckBoxListener() {
        mAutomatedMainWindowCheckBox.setChecked(mAutomatedTestingModel.getMainWindowOn());
        mAutomatedMainWindowCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedMainWindowCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpLogInCheckBoxListener() {
        mAutomatedLogInCheckBox.setChecked(mAutomatedTestingModel.getLogInOn());
        mAutomatedLogInCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedLogInCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpImagingCheckBoxListener() {
        mAutomatedImagingCheckBox.setChecked(mAutomatedTestingModel.getImagingOn());
        mAutomatedImagingCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedImagingCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpStatusCheckBoxListener() {
        mAutomatedStatusCheckBox.setChecked(mAutomatedTestingModel.getStatusOn());
        mAutomatedStatusCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedStatusCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpPatientCheckBoxListener() {
        mAutomatedPatientCheckBox.setChecked(mAutomatedTestingModel.getPatientOn());
        mAutomatedPatientCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedPatientCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpProbeCheckBoxListener() {
        mAutomatedProbeCheckBox.setChecked(mAutomatedTestingModel.getProbeOn());
        mAutomatedProbeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedProbeCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpMeasurementCheckBoxListener() {
        mAutomatedMeasurementCheckBox.setChecked(mAutomatedTestingModel.getMeasurementOn());
        mAutomatedMeasurementCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedMeasurementCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpPresetCheckBoxListener() {
        mAutomatedPresetCheckBox.setChecked(mAutomatedTestingModel.getPresetOn());
        mAutomatedPresetCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedPresetCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpSaveCheckBoxListener() {
        mAutomatedSaveCheckBox.setChecked(mAutomatedTestingModel.getSaveOn());
        mAutomatedSaveCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedSaveCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpLoadCheckBoxListener() {
        mAutomatedLoadCheckBox.setChecked(mAutomatedTestingModel.getLoadOn());
        mAutomatedLoadCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedLoadCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpModifyCheckBoxListener() {
        mAutomatedModifyCheckBox.setChecked(mAutomatedTestingModel.getModifyOn());
        mAutomatedModifyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedModifyCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpVersionCheckBoxListener() {
        mAutomatedVersionCheckBox.setChecked(mAutomatedTestingModel.getVersionOn());
        mAutomatedVersionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedVersionCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpCleanScreenCheckBoxListener() {
        mAutomatedCleanScreenCheckBox.setChecked(mAutomatedTestingModel.getCleanScreenOn());
        mAutomatedCleanScreenCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedCleanScreenCheckBox.setChecked(checked);
            }
        });
    }

    private void setUpLogOutCheckBoxListener() {
        mAutomatedLogOutCheckBox.setChecked(mAutomatedTestingModel.getLogOutOn());
        mAutomatedLogOutCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CompoundButton) view).isChecked();
                mAutomatedLogOutCheckBox.setChecked(checked);
            }
        });
    }

    /*private void setUpExitDialogButtonListener() {
        Button exitAutomatedTestingButton = mDialog.findViewById(R.id.exitAutomatedTestingButton);
        exitAutomatedTestingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Automated Testing Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }*/
}
