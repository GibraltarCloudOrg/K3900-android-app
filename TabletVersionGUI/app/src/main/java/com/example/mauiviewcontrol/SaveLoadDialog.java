package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

//import k3900.K3900;

public class SaveLoadDialog {
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Save/Load Dialog";
    final Context mContext;
    private Dialog mDialog = null;
    final String mDefaultPattern = "";
    final ArrayList<String> mPatients = mBackend.onGetPatients(mDefaultPattern);
    //int mLastClickId = -1;
    int mLastClickedFileType = -1;
    int mLastClickedPatientInLoadTabPage = -1;
    int mLastClickedExamInLoadTabPage = -1;
    int mLastClickedFileTypeInLoadTabPage = -1;
    int mLastClickedFileInLoadTabPage = -1;
    int mLastClickedPatientInModifyTabPage = -1;
    int mLeftPinIndex = -1;
    int mRightPinIndex = -1;
    int mCurrentLoadingStep = 0;

    public SaveLoadDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.save_load_view);
        initializeTabPages();
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }


    public void close () {
        mDialog.dismiss();
    }

    public void showSavePage() {
        setCurrentTabPage(0);
    }

    public void showLoadPage() {
        setCurrentTabPage(1);
    }

    public void showModifyPage() {
        setCurrentTabPage(2);
    }

    private void setCurrentTabPage(int page) {
        TabLayout saveLoadModifyTabLayout = mDialog.findViewById(R.id.saveLoadModifyTabLayout);
        saveLoadModifyTabLayout.getTabAt(page).select();
        View savePage = mDialog.findViewById(R.id.savePage);
        //View loadPage1 = mDialog.findViewById(R.id.loadPage1);
        //View loadPage2 = mDialog.findViewById(R.id.loadPage2);
        //View loadPage3 = mDialog.findViewById(R.id.loadPage3);
        View modifyPage = mDialog.findViewById(R.id.modifyPage);
        switch (page) {
            case 0:
                //fragment = new NewPatientFragment();
                savePage.setVisibility(View.VISIBLE);
                //loadPage1.setVisibility(View.INVISIBLE);
                //loadPage2.setVisibility(View.INVISIBLE);
                //loadPage3.setVisibility(View.INVISIBLE);
                showLoadPage(-1);
                modifyPage.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //fragment = new LookUpPatientFragment();
                savePage.setVisibility(View.INVISIBLE);
                showLoadPage(mCurrentLoadingStep);
                //loadPage1.setVisibility(View.VISIBLE);
                //loadPage2.setVisibility(View.INVISIBLE);
                //loadPage3.setVisibility(View.INVISIBLE);
                modifyPage.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //fragment = new LookUpPatientFragment();
                savePage.setVisibility(View.INVISIBLE);
                showLoadPage(-1);
                //loadPage1.setVisibility(View.INVISIBLE);
                //loadPage2.setVisibility(View.INVISIBLE);
                //loadPage3.setVisibility(View.INVISIBLE);
                modifyPage.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + page);
        }
    }

    private void initializeTabPages() {
        TabLayout saveLoadModifyTabLayout = mDialog.findViewById(R.id.saveLoadModifyTabLayout);
        saveLoadModifyTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabPage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        showSavePage();
    }

    private void setUpWidgets() {
        setUpFileTypeListViewInSaveTabPage();
        setUpPatientsListViewInLoad();
        setUpPatientsListViewInModify();
        setUpFileTypeListViewInLoadTabPage();
        //setUpFileListInLoadViewListView();
    }

    private void setUpFileTypeListViewInSaveTabPage() {
        ListView fileTypeListView = mDialog.findViewById(R.id.fileTypeListView);
        ArrayList<String> list = mBackend.onGetFileTypes("", "", "");
        /*if (null != list) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
            fileTypeListView.setAdapter(adapter);
        }*/
        //TextView listItemShow = mDialog.findViewById(R.id.fileTypeTextView);
        //listItemShow.setTextColor(Color.parseColor("#fe00fb"));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
        if (null == list || 0 == list.size()) {
            if (null == list)
                list = new ArrayList<String>();
            list.add("Image");
            list.add("Video");
            list.add("Dataset");
        }
        //if (null != list) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, list);
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, R.id.fileTypeTextView, list);
            //adapter.add("test1");
            fileTypeListView.setAdapter(adapter);
            //ConstraintLayout engineeringViewTopConstraintLayout = mDialog.findViewById(R.id.engineeringViewTopConstraintLayout);
            //engineeringViewTopConstraintLayout.setMinimumWidth(1000);
            //engineeringViewTopConstraintLayout.setMinimumHeight(900);
            //TabLayout engineeringTabLayout = mDialog.findViewById(R.id.engineeringTabLayout);
            //engineeringTabLayout.setMinimumWidth(1000);
        //}
    }

    private void setUpFileTypeListViewInLoadTabPage() {
        ListView fileTypeInLoadViewListView = mDialog.findViewById(R.id.fileTypeInLoadViewListView);
        String pid = mBackend.getPid(mDefaultPattern, mLastClickedPatientInLoadTabPage);
        String examName = mBackend.getExamName(pid, mLastClickedExamInLoadTabPage);
        String examDate = mBackend.getExamDate(pid, mLastClickedExamInLoadTabPage);
        ArrayList<String> fileTypeList = mBackend.onGetFileTypes(pid, examName, examDate);
        if (null == fileTypeList) {
            fileTypeList = new ArrayList<String>();
            fileTypeList.add("Image");
            fileTypeList.add("Video");
            fileTypeList.add("Dataset");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, fileTypeList);
        fileTypeInLoadViewListView.setAdapter(adapter);
    }

    private void setUpFileListInLoadViewListView() {
        ListView fileListInLoadViewListView = mDialog.findViewById(R.id.fileListInLoadViewListView);
        String pid = mBackend.getPid(mDefaultPattern, mLastClickedPatientInLoadTabPage);
        String examName = mBackend.getExamName(pid, mLastClickedExamInLoadTabPage);
        String examDate = mBackend.getExamDate(pid, mLastClickedExamInLoadTabPage);
        final ArrayList<String> list = mBackend.onGetExamStores(pid, examName, examDate, mLastClickedFileType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, list);
        fileListInLoadViewListView.setAdapter(adapter);
    }

    private void setUpPatientsListViewInLoad() {
        ListView patientsListView = mDialog.findViewById(R.id.patientsInLoadTabPageListView);
        //final ArrayList<String> list = mBackend.onGetPatients("");
        //if (null != list) {
            //ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, /*list*/mPatients);
        patientsListView.setAdapter(adapter);
        //}
    }

    private void setUpPatientsListViewInModify() {
        ListView patientsInModifyViewListView = mDialog.findViewById(R.id.patientsInModifyViewListView);
        //final ArrayList<String> list = mBackend.onGetPatients("");
        //if (null != list) {
            //ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, /*list*/mPatients);
            patientsInModifyViewListView.setAdapter(adapter);
        //}
    }

    private void setUpListeners() {
        setUpFileTypeListenerInSaveTabPage();
        setUpFramesStartEndRangeBarListener();
        setUpPatientListListenerInLoadTabPage();
        setUpExamListListener();
        //setUpOpenButtonListenerInLoadTabPage();
        setUpOpenFileListButtonListenerInLoadTabPage();
        setUpLoadButtonListenerInLoadTabPage();
        setUpBackToFileTypeListPageInLoadButtonListener();
        setUpSaveButtonListener();
        setUpShowExamsButtonListener();
        setUpOpenExamButtonListener();
        setUpFileTypeListenerInLoadTabPage();
        setUpFileListListenerInLoadTabPage();
        setUpBackToPatientListButtonListener();
        setUpBackToExamListButtonListener();
        setUpPatientListListenerInModifyTabPage();
        setUpExitSaveDialogButtonListener();
    }

    private void setUpFileTypeListenerInSaveTabPage() {
        ListView fileTypeListView = mDialog.findViewById(R.id.fileTypeListView);
        fileTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedFileType);
                mLastClickedFileType = position;
                String line = fileTypeListView.getAdapter().getItem(mLastClickedFileType).toString();
                /*if (null != mStartExam)
                    mStartExam.setLine(line);*/
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        fileTypeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    private void setUpPatientListListenerInLoadTabPage() {
        ListView patientsListView = mDialog.findViewById(R.id.patientsInLoadTabPageListView);
        patientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedPatientInLoadTabPage);
                mLastClickedPatientInLoadTabPage = position;
                String line = patientsListView.getAdapter().getItem(mLastClickedPatientInLoadTabPage).toString();
                /*if (null != mStartExam)
                    mStartExam.setLine(line);*/
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        patientsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    private void setUpBackToPatientListButtonListener() {
        Button backToPatientsPageInLoadButton = mDialog.findViewById(R.id.backToPatientsPageInLoadButton);
        backToPatientsPageInLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPatientListPage();
            }
        });
    }

    private void setUpBackToExamListButtonListener() {
        Button backToExamsPageInLoadButton = mDialog.findViewById(R.id.backToExamsPageInLoadButton);
        backToExamsPageInLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExamListPage();
            }
        });
    }

    private void setUpExamListListener() {
        ListView examsInLoadTabPageListView = mDialog.findViewById(R.id.examsInLoadTabPageListView);
        examsInLoadTabPageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedExamInLoadTabPage);
                mLastClickedExamInLoadTabPage = position;
                String line = examsInLoadTabPageListView.getAdapter().getItem(mLastClickedExamInLoadTabPage).toString();
                /*if (null != mStartExam)
                    mStartExam.setLine(line);*/
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        examsInLoadTabPageListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    private void setUpOpenFileListButtonListenerInLoadTabPage() {
        Button openFileListPageInLoadViewButton = mDialog.findViewById(R.id.openFileListPageInLoadViewButton);
        openFileListPageInLoadViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileListPage();
            }
        });
    }

    private void setUpLoadButtonListenerInLoadTabPage() {
        Button loadFileListInLoadViewPageButton = mDialog.findViewById(R.id.loadFileListInLoadViewPageButton);
        loadFileListInLoadViewPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid = mBackend.getPids(mDefaultPattern).get(mLastClickedPatientInLoadTabPage);
                String examName = mBackend.getExamName(pid, mLastClickedExamInLoadTabPage);
                String examDate = mBackend.getExamDate(pid, mLastClickedExamInLoadTabPage);
                boolean result = mBackend.onLoadExamFile(pid, examName,  examDate,  mLastClickedFileTypeInLoadTabPage, mBackend.onGetExamStores(pid, examName, examDate, mLastClickedFileTypeInLoadTabPage).get(mLastClickedFileInLoadTabPage));
                MauiToastMessage.displayToastMessage(mContext, result, "Loading:", Toast.LENGTH_LONG);
            }
        });
    }

    private void setUpBackToFileTypeListPageInLoadButtonListener() {
        Button backToFileTypeListPageInLoadButton = mDialog.findViewById(R.id.backToFileTypeListPageInLoadButton);
        backToFileTypeListPageInLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileTypePage();
            }
        });
    }

    private void setUpPatientListListenerInModifyTabPage() {
        ListView patientsListView = mDialog.findViewById(R.id.patientsInModifyViewListView);
        patientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedPatientInModifyTabPage);
                mLastClickedPatientInModifyTabPage = position;
                String line = patientsListView.getAdapter().getItem(mLastClickedPatientInModifyTabPage).toString();
                /*if (null != mStartExam)
                    mStartExam.setLine(line);*/
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        patientsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    private void setUpSelectPatientButtonListener() {
        /*Button selectPatientButton = mDialog.findViewById(R.id.selectPatientButton);
        selectPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 > mLastClickId || mPatients.size() <= mLastClickId) {
                    Toast.makeText(mContext, "Please select patient on the list.", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(mContext, "Selected Patient: " + mPatients.get(mLastClickId), Toast.LENGTH_LONG).show();
                //showSelectedPatientPageOnly();
                //selectPatient();
            }
        });*/
    }

    private void setUpFramesStartEndRangeBarListener() {
        RangeBar rangeBar = mDialog.findViewById(R.id.framesRangeBar);
        rangeBar.setTickStart(1);
        rangeBar.setTickEnd(mBackend.getPlaybackSize());
        TextView frameStartTextView = mDialog.findViewById(R.id.frameStartTextView);
        TextView frameEndTextView = mDialog.findViewById(R.id.frameEndTextView);
        frameStartTextView.setText(Integer.toString(rangeBar.getLeftIndex() + 1));
        frameEndTextView.setText(Integer.toString(rangeBar.getRightIndex() + 1));
        //rangeBar.setRangePinsByIndices(1, mBackend.getPlaybackSize());
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                mLeftPinIndex = leftPinIndex;
                mRightPinIndex = rightPinIndex;
                frameStartTextView.setText(Integer.toString(mLeftPinIndex + 1));
                frameEndTextView.setText(Integer.toString(mRightPinIndex + 1));
            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }
        });
    }

    private void setUpSaveButtonListener() {
        Button saveButton = mDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = mDialog.findViewById(R.id.fileNameEditText);
                boolean result = mBackend.onSave(mLastClickedFileType, nameEditText.getText().toString(), mLeftPinIndex + 1, mRightPinIndex - mLeftPinIndex + 1);
                MauiToastMessage.displayToastMessage(mContext, result, "Saving:", Toast.LENGTH_LONG);
            }
        });
    }

    private void setUpShowExamsButtonListener() {
        Button showExamsButton = mDialog.findViewById(R.id.showExamsButton);
        showExamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExamListPage();
            }
        });
    }

    private void setUpFileTypeListenerInLoadTabPage() {
        ListView fileTypeInLoadViewListView = mDialog.findViewById(R.id.fileTypeInLoadViewListView);
        fileTypeInLoadViewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedFileTypeInLoadTabPage);
                mLastClickedFileTypeInLoadTabPage = position;
                String line = fileTypeInLoadViewListView.getAdapter().getItem(mLastClickedFileTypeInLoadTabPage).toString();
                /*if (null != mStartExam)
                    mStartExam.setLine(line);*/
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        fileTypeInLoadViewListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    private void setUpFileListListenerInLoadTabPage() {
        ListView fileListInLoadViewListView = mDialog.findViewById(R.id.fileListInLoadViewListView);
        fileListInLoadViewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedFileInLoadTabPage);
                mLastClickedFileInLoadTabPage = position;
                String line = fileListInLoadViewListView.getAdapter().getItem(mLastClickedFileInLoadTabPage).toString();
                /*if (null != mStartExam)
                    mStartExam.setLine(line);*/
                Toast.makeText(mContext, "Selected:" + line, Toast.LENGTH_LONG).show();
            }
        });
        fileListInLoadViewListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //patientsListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mPatients));
    }

    private void showPatientListPage() {
        mCurrentLoadingStep = 0;
        showLoadPage(mCurrentLoadingStep);
    }

    private void showExamListPage() {
        mCurrentLoadingStep = 1;
        showLoadPage(mCurrentLoadingStep);
        ListView examsInLoadTabPageListView = mDialog.findViewById(R.id.examsInLoadTabPageListView);
        ArrayList<String> exams = mBackend.onGetExams(mBackend.getPid(mDefaultPattern, mLastClickedPatientInLoadTabPage));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, exams);
        examsInLoadTabPageListView.setAdapter(adapter);
    }

    private void showFileTypePage() {
        mCurrentLoadingStep = 2;
        showLoadPage(mCurrentLoadingStep);
    }

    private void showFileListPage() {
        mCurrentLoadingStep = 3;
        showLoadPage(mCurrentLoadingStep);
    }

    private void showLoadPage(int index) {
        View loadPage1 = mDialog.findViewById(R.id.loadPage1);
        View loadPage2 = mDialog.findViewById(R.id.loadPage2);
        View loadPage3 = mDialog.findViewById(R.id.loadPage3);
        View loadPage4 = mDialog.findViewById(R.id.loadPage4);
        switch (index) {
            case 0:
                loadPage1.setVisibility(View.VISIBLE);
                loadPage2.setVisibility(View.INVISIBLE);
                loadPage3.setVisibility(View.INVISIBLE);
                loadPage4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                loadPage1.setVisibility(View.INVISIBLE);
                loadPage2.setVisibility(View.VISIBLE);
                loadPage3.setVisibility(View.INVISIBLE);
                loadPage4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                loadPage1.setVisibility(View.INVISIBLE);
                loadPage2.setVisibility(View.INVISIBLE);
                loadPage3.setVisibility(View.VISIBLE);
                loadPage4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                loadPage1.setVisibility(View.INVISIBLE);
                loadPage2.setVisibility(View.INVISIBLE);
                loadPage3.setVisibility(View.INVISIBLE);
                loadPage4.setVisibility(View.VISIBLE);
                break;
            case -1:
            default:
                loadPage1.setVisibility(View.INVISIBLE);
                loadPage2.setVisibility(View.INVISIBLE);
                loadPage3.setVisibility(View.INVISIBLE);
                loadPage4.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void setUpOpenExamButtonListener() {
        Button openExamButton = mDialog.findViewById(R.id.openExamButton);
        openExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileTypePage();
            }
        });
    }

    private void setUpExitSaveDialogButtonListener() {
        Button exitSaveDialogButton = mDialog.findViewById(R.id.exitSaveDialogButton);
        exitSaveDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LinearLayout detailedStateLinearLayout = mDialog.findViewById(R.id.detailedStateLinearLayout);
                //detailedStateLinearLayout.setVisibility(View.INVISIBLE);
                mDialog.dismiss();
            }
        });
    }
}
