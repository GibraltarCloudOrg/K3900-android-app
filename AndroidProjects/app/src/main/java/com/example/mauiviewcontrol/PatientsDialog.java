package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import k3900.K3900;

public class PatientsDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "Patients Dialog";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    int mLastClickId = -1;
    final ArrayList<String> mPatients = mBackend.onGetPatients("");
    StartExam mStartExam = null;

    public PatientsDialog(Context parent)
    {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.patient_view);
        initializeTabPages();
        setUpWidgets();
        setUpListeners();
        showNewPatientPageOnly();
        mDialog.show();
    }

    private void showNewPatientPageOnly() {
        setCurrentTabPage(0);
    }

    private void showLookUpPatientPageOnly() {
        setCurrentTabPage(1);
    }

    private void showSelectedPatientPageOnly() {
        setCurrentTabPage(2);
    }

    private void setCurrentTabPage(int page) {
        View newPatientViewPage = mDialog.findViewById(R.id.newPatientViewPage);
        View lookUpPatientViewPage = mDialog.findViewById(R.id.lookUpPatientViewPage);
        View startExamFromPatientListPage = mDialog.findViewById(R.id.startExamFromPatientListPage);
        switch (page) {
            case 0:
                newPatientViewPage.setVisibility(View.VISIBLE);
                lookUpPatientViewPage.setVisibility(View.INVISIBLE);
                startExamFromPatientListPage.setVisibility(View.INVISIBLE);
                break;
            case 1:
                newPatientViewPage.setVisibility(View.INVISIBLE);
                lookUpPatientViewPage.setVisibility(View.VISIBLE);
                startExamFromPatientListPage.setVisibility(View.INVISIBLE);
                break;
            case 2: // tab only has '0' and '1' pages, but in this case ('2'), the 'Selected' button is clicked on the look up patient page.
                newPatientViewPage.setVisibility(View.INVISIBLE);
                lookUpPatientViewPage.setVisibility(View.INVISIBLE);
                startExamFromPatientListPage.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + page);
        }
    }

    private void initializeTabPages() {
        TabLayout patientsTabLayout = mDialog.findViewById(R.id.patientsTabLayout);
        //LinearLayout newPatientLinearLayout = mDialog.findViewById(R.id.newPatientLinearLayout);
        //LinearLayout lookUpPatientLinearLayout = mDialog.findViewById(R.id.lookUpPatientLinearLayout);
        /*View newPatientViewPage = mDialog.findViewById(R.id.newPatientViewPage);
        View lookUpPatientViewPage = mDialog.findViewById(R.id.lookUpPatientViewPage);
        View startExamFromPatientListPage = mDialog.findViewById(R.id.startExamFromPatientListPage);
        startExamFromPatientListPage.setVisibility(View.INVISIBLE);*/
        patientsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Fragment fragment = null;
                setCurrentTabPage(tab.getPosition());
                /*switch (tab.getPosition()) {
                    case 0:
                        //fragment = new NewPatientFragment();
                        newPatientViewPage.setVisibility(View.VISIBLE);
                        lookUpPatientViewPage.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        //fragment = new LookUpPatientFragment();
                        newPatientViewPage.setVisibility(View.INVISIBLE);
                        lookUpPatientViewPage.setVisibility(View.VISIBLE);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tab.getPosition());
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpWidgets() {
        //ListView listView = mDialog.findViewById(R.id.patientsListView);
        //final ArrayList<String> list = mBackend.onGetPatients("");
        //if (null != list) {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, /*list*/mPatients);
            //ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
            //listView.setAdapter(adapter);
        //}
    }

    private void setUpListeners() {
        setUpStartExamForSelectedPatientViewListener();
        setUpExamNameForSelectedPatientEditTextListener();
        setUpPatientListListener();
        setUpSelectPatientButtonListener();
        EditText patientIdEditText = mDialog.findViewById(R.id.patientIdEditText);
        EditText patientFirstNameEditText = mDialog.findViewById(R.id.patientFirstNameEditText);
        EditText patientLastNameEditText = mDialog.findViewById(R.id.patientLastNameEditText);
        EditText examNameEditText = mDialog.findViewById(R.id.examNameEditText);
        Button startExamButton = mDialog.findViewById(R.id.startExamButton);
        startExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = mBackend.onStartExam(examNameEditText.getText().toString(), "CommentsTest1",
                        patientIdEditText.getText().toString(), patientFirstNameEditText.getText().toString(), patientLastNameEditText.getText().toString(), "09/31/2030", "male");
                String toastMessage;
                if (result) {
                    toastMessage = "Exam Name: " + examNameEditText.getText() + " started.";
                    mDialog.dismiss();
                } else
                    toastMessage = "Exam Name: " + examNameEditText.getText() + " could not start.";
                Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
        Button cancelButton = mDialog.findViewById(R.id.exitNewPatientButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Patient Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
        setUpExitSelectedPatientViewButtonListener();
    }

    private void setUpPatientListListener() {
        ListView patientsListView = mDialog.findViewById(R.id.patientsListView);
        patientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickId);
                mLastClickId = position;
                String line = patientsListView.getAdapter().getItem(mLastClickId).toString();
                if (null != mStartExam)
                    mStartExam.setLine(line);
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        patientsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        patientsListView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, mPatients));
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, /*list*/mPatients);
    }

    private void setUpSelectPatientButtonListener() {
        Button selectPatientButton = mDialog.findViewById(R.id.selectPatientButton);
        selectPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 > mLastClickId || mPatients.size() <= mLastClickId) {
                    Toast.makeText(mContext, "Please select patient on the list.", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(mContext, "Selected Patient: " + mPatients.get(mLastClickId), Toast.LENGTH_LONG).show();
                showSelectedPatientPageOnly();
                selectPatient();
            }
        });
    }

    private void selectPatient() {
        TextView selectedPatientTextView = mDialog.findViewById(R.id.selectedPatientTextView);
        selectedPatientTextView.setText(mPatients.get(mLastClickId));
    }

    private void setUpStartExamForSelectedPatientViewListener() {
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        WidgetUtility.setUpListener(mContext, mDialog, R.id.startExamFromPatientListButton, mStartExam = new StartExam(), backEndElementSendingMessageVisitor, false, "", true, "Start Exam", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.stopExamFromPatientListButton, new StopExam(), backEndElementSendingMessageVisitor, false, "", true, "Stop Exam", false);
    }

    private void setUpExamNameForSelectedPatientEditTextListener() {
        EditText examNameForSelectedPatientEditText = mDialog.findViewById(R.id.examNameForSelectedPatientEditText);
        examNameForSelectedPatientEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStartExam.setExamName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setUpExitSelectedPatientViewButtonListener() {
        Button exitSelectedPatientViewButton = mDialog.findViewById(R.id.exitSelectedPatientViewButton);
        exitSelectedPatientViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Exited Selected Patient View", Toast.LENGTH_LONG).show();
                showLookUpPatientPageOnly();
            }
        });
    }

    public void checkRealtimeStates() {
        if (null != mDialog) {
            //WidgetUtility.setButtonEnabled(mContext, mDialog.findViewById(R.id.startExamFromPatientListButton), !mBackend.examInProcess());
            //WidgetUtility.setButtonEnabled(mContext, mDialog.findViewById(R.id.stopExamFromPatientListButton), mBackend.examInProcess());
            mDialog.findViewById(R.id.startExamFromPatientListButton).setEnabled(!mBackend.examInProcess());
            mDialog.findViewById(R.id.stopExamFromPatientListButton).setEnabled(mBackend.examInProcess());
        }
    }
}
