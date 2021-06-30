package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import k3900.K3900;

public class SystemsDialog {
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Systems Dialog";
    final Context mContext;
    private Dialog mDialog = null;
    private int mLastClickedUserIndex = -1;
    private ArrayAdapter<String> m_activeUserListAdapter = null;
    private boolean readyForCheckRealtimeStates = false;
    private boolean mHasDiagnosticTestStarted = false;

    public SystemsDialog(Context context) {
        mContext = context;
        System.out.println(TAG + ".SystemsDialog() step 1.");
        mDialog = new Dialog(mContext);
        System.out.println(TAG + ".SystemsDialog() step 2.");
        mDialog.setContentView(R.layout.systems_view);
        System.out.println(TAG + ".SystemsDialog() step 3.");
        initializeTabPages();
        System.out.println(TAG + ".SystemsDialog() step 4.");
        setUpWidgets();
        System.out.println(TAG + ".SystemsDialog() step 5.");
        setUpListeners();
        System.out.println(TAG + ".SystemsDialog() step 6.");
        mDialog.show();
        System.out.println(TAG + ".SystemsDialog() step 7.");
        readyForCheckRealtimeStates = true;
        System.out.println(TAG + ".SystemsDialog() step 8.");
    }

    public void showConfigurationPage() {
        setCurrentTabPage(0);
    }

    public void showCreateUserPage() {
        setCurrentTabPage(1);
    }

    public void showDiagnosticsPage() {
        setCurrentTabPage(2);
    }

    private void setCurrentTabPage(int page) {
        TabLayout systemsTabLayout = mDialog.findViewById(R.id.systemsTabLayout);
        systemsTabLayout.getTabAt(page).select();
        View systemsConfigurationPage = mDialog.findViewById(R.id.systemsConfigurationPage);
        View systemsCreateUserPage = mDialog.findViewById(R.id.systemsCreateUserPage);
        View systemsActiveUsersPage = mDialog.findViewById(R.id.systemsActiveUsersPage);
        View systemsDiagnosticsPage = mDialog.findViewById(R.id.systemsDiagnosticsPage);
        switch (page) {
            case 0:
                systemsConfigurationPage.setVisibility(View.VISIBLE);
                systemsCreateUserPage.setVisibility(View.INVISIBLE);
                systemsActiveUsersPage.setVisibility(View.INVISIBLE);
                systemsDiagnosticsPage.setVisibility(View.INVISIBLE);
                break;
            case 1:
                systemsConfigurationPage.setVisibility(View.INVISIBLE);
                systemsCreateUserPage.setVisibility(View.VISIBLE);
                systemsActiveUsersPage.setVisibility(View.INVISIBLE);
                systemsDiagnosticsPage.setVisibility(View.INVISIBLE);
                break;
            case 2:
                systemsConfigurationPage.setVisibility(View.INVISIBLE);
                systemsCreateUserPage.setVisibility(View.INVISIBLE);
                systemsActiveUsersPage.setVisibility(View.INVISIBLE);
                systemsDiagnosticsPage.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + page);
        }
    }

    private void initializeTabPages() {
        TabLayout systemsTabLayout = mDialog.findViewById(R.id.systemsTabLayout);
        systemsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    private void setUpWidgets() {
        setUpWidgetsInConfigurationPage();
        setUpWidgetsInCreateUserPage();
        setUpWidgetsInActiveUsersListPage();
        setUpWidgetsInDiagnosticsPage();
    }

    private void setUpWidgetsInDiagnosticsPage() {
        ((Spinner)mDialog.findViewById(R.id.diagnosticTestSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mBackend.getDiagnosticTests()));
        showDiagnosticTestProgress(false);
    }

    private void setUpWidgetsInCreateUserPage() {
        ((Spinner)mDialog.findViewById(R.id.userTypeSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mBackend.getUserTypeList()));
    }

    private void setUpWidgetsInActiveUsersListPage() {
        ((ListView)mDialog.findViewById(R.id.activeUsersListView)).setAdapter(m_activeUserListAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, mBackend.getUserList()));
    }

    private void setUpActiveUsersListViewListener() {
        ListView activeUsersListView = mDialog.findViewById(R.id.activeUsersListView);
        activeUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickedUserIndex);
                if (position != mLastClickedUserIndex) {
                    Toast.makeText(mContext, "Selected User Name: " + mBackend.getUserName(position), Toast.LENGTH_LONG).show();
                    mLastClickedUserIndex = position;
                }
            }
        });
        activeUsersListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void setUpWidgetsInConfigurationPage() {
        /*ArrayList<String> list = mBackend.getExternalStorageInfo();
        Spinner driveTypeSpinner = mDialog.findViewById(R.id.driveTypeSpinner);
        driveTypeSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list));*/

        ((Spinner)mDialog.findViewById(R.id.driveTypeSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mBackend.getExternalStorageInfo()));
        ((Spinner)mDialog.findViewById(R.id.unitOfMeasureSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mBackend.getUnitOfMeasureList()));
        mDialog.findViewById(R.id.unitOfMeasureSpinner).setEnabled(false);

        ArrayList<String> userLogOutMinList = new ArrayList<String>();
        userLogOutMinList.add("5");
        userLogOutMinList.add("10");
        userLogOutMinList.add("15");
        userLogOutMinList.add("20");
        userLogOutMinList.add("30");
        userLogOutMinList.add("45");
        ((Spinner)mDialog.findViewById(R.id.userLogOutMinSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, userLogOutMinList));

        ArrayList<String> timeoutMinList = new ArrayList<String>();
        timeoutMinList.add("10");
        timeoutMinList.add("20");
        timeoutMinList.add("30");
        timeoutMinList.add("45");
        timeoutMinList.add("60");
        ((Spinner)mDialog.findViewById(R.id.timeoutSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, timeoutMinList));
    }

    private void setUpListeners() {
        setUpDriveTypeSpinnerListener();
        WidgetUtility.setUpUnitOfMeasureSpinnerListener(mDialog.findViewById(R.id.unitOfMeasureSpinner), mBackend);
        //setUpUnitOfMeasureSpinnerListener();
        setUpMountNetworkButtonListener();
        setUpExitConfigurationViewButtonListener();
        setUpCreateUserButtonListener();
        setUpActiveUsersButtonListener();
        setUpActiveUsersListViewListener();
        setUpDeleteUserButtonListener();
        setUpDiagnosticsListeners();
        setUpExitUsersListButtonListener();
        setUpExitDiagnosticsButtonListener();
    }

    private void setUpDiagnosticsListeners() {
        BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
        WidgetUtility.setUpListener(mContext, mDialog, R.id.startDiagnosticTestButton, new StartDiagnosticTest(), backEndElementSendingMessageVisitor, false, "", true, "Perform Diagnostic Test", false);
        WidgetUtility.setUpListener(mContext, mDialog, R.id.abortDiagnosticTestButton, new AbortDiagnosticTest(), backEndElementSendingMessageVisitor, true, "Are you sure?", true, "Abort Diagnostic Test", false);
        Button diagnosticsTestMessageAreaOkButton = mDialog.findViewById(R.id.diagnosticsTestMessageAreaOkButton);
        diagnosticsTestMessageAreaOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHasDiagnosticTestStarted = false;
                showDiagnosticTestProgress(false);
            }
        });
    }

    private void setUpDriveTypeSpinnerListener() {
        Spinner driveTypeSpinner = mDialog.findViewById(R.id.driveTypeSpinner);
        driveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                if (0 == selectedString.compareTo("Network"))
                    mBackend.setDriveTypeNetwork();
                else if (0 == selectedString.compareTo("USB"))
                    mBackend.setDriveTypeUsb();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /*private void setUpUnitOfMeasureSpinnerListener() {
        WidgetUtility.setUpUnitOfMeasureSpinnerListener(mDialog.findViewById(R.id.unitOfMeasureSpinner), mBackend);
        Spinner unitOfMeasureSpinner = mDialog.findViewById(R.id.unitOfMeasureSpinner);
        unitOfMeasureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                if (0 == selectedString.compareTo("Metric"))
                    mBackend.setUnitTypeMetric();
                else if (selectedString.contains("inches"))
                    mBackend.setUnitTypeInches();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }*/

    private void setUpMountNetworkButtonListener() {
        Button mountNetworkButton = mDialog.findViewById(R.id.mountNetworkButton);
        mountNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toastMessage;
                boolean result = false;
                if (mBackend.networkMounted()) {
                    result = mBackend.disconnectNetworkStorage();
                    if (result)
                        toastMessage = "Network storage unmounted.";
                    else
                        toastMessage = "Network disconnection failed.";
                }
                else {
                    result = mBackend.connectNetworkStorage(((EditText)(mDialog.findViewById(R.id.networkPathEditText))).getText().toString(), ((EditText)(mDialog.findViewById(R.id.networkUserNameEditText))).getText().toString(), ((EditText)(mDialog.findViewById(R.id.networkPasswordEditText))).getText().toString(), true);
                    if (result)
                        toastMessage = "Network storage mounted to " + ((EditText)(mDialog.findViewById(R.id.networkPathEditText))).getText().toString() + ", user name: " + ((EditText)(mDialog.findViewById(R.id.networkUserNameEditText))).getText().toString();
                    else
                        toastMessage = "Network connection failed.";
                }
                Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpExitUsersListButtonListener() {
        Button exitUsersListButton = mDialog.findViewById(R.id.exitUsersListButton);
        exitUsersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDialog.findViewById(R.id.systemConfigurationUsersListLinearLayout).setVisibility(View.INVISIBLE);
                //mDialog.findViewById(R.id.systemConfigurationCreateUserLinearLayout).setVisibility(View.VISIBLE);
                mDialog.findViewById(R.id.systemsCreateUserPage).setVisibility(View.VISIBLE);
                mDialog.findViewById(R.id.systemsActiveUsersPage).setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "Exit Users List Button clicked.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpCreateUserButtonListener() {
        Button createUserButton = mDialog.findViewById(R.id.createUserButton);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameInCreateUserEditText = mDialog.findViewById(R.id.userNameInCreateUserEditText);
                EditText passwordInCreateUserEditText = mDialog.findViewById(R.id.passwordInCreateUserEditText);
                boolean result = mBackend.createUser(userNameInCreateUserEditText.getText().toString(), passwordInCreateUserEditText.getText().toString(), ((Spinner)mDialog.findViewById(R.id.userTypeSpinner)).getSelectedItem().toString());
                MauiToastMessage.displayToastMessage(mContext, result, "Create User: ", Toast.LENGTH_LONG);
                if (result) {
                    userNameInCreateUserEditText.setText("");
                    passwordInCreateUserEditText.setText("");
                }
            }
        });
    }

    private void setUpActiveUsersButtonListener() {
        Button activeUsersButton = mDialog.findViewById(R.id.activeUsersButton);
        activeUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDialog.findViewById(R.id.systemConfigurationUsersListLinearLayout).setVisibility(View.VISIBLE);
                //mDialog.findViewById(R.id.systemConfigurationCreateUserLinearLayout).setVisibility(View.INVISIBLE);
                mDialog.findViewById(R.id.systemsCreateUserPage).setVisibility(View.INVISIBLE);
                mDialog.findViewById(R.id.systemsActiveUsersPage).setVisibility(View.VISIBLE);
                //m_activeUserListAdapter.notifyDataSetChanged();
                Toast.makeText(mContext, "Active Users Button clicked.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpDeleteUserButtonListener() {
        Button deleteUserButton = mDialog.findViewById(R.id.deleteUserButton);
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mBackend.getUserName(mLastClickedUserIndex);
                boolean result = mBackend.deleteUser(mLastClickedUserIndex);
                MauiToastMessage.displayToastMessage(mContext, result, "Delete User: " + userName + ", ", Toast.LENGTH_LONG);
                if (result)
                    m_activeUserListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setUpExitConfigurationViewButtonListener() {
        Button exitConfigurationButton = mDialog.findViewById(R.id.exitConfigurationButton);
        exitConfigurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void setUpExitDiagnosticsButtonListener() {
        Button exitDiagnosticTestButton = mDialog.findViewById(R.id.exitDiagnosticTestButton);
        exitDiagnosticTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void showNetworkWidgetsInConfigurationPage(boolean show) {
        int visible = show ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.networkPathTextView).setVisibility(visible);
        mDialog.findViewById(R.id.networkPathEditText).setVisibility(visible);
        mDialog.findViewById(R.id.networkUserNameTextView).setVisibility(visible);
        mDialog.findViewById(R.id.networkUserNameEditText).setVisibility(visible);
        mDialog.findViewById(R.id.networkPasswordTextView).setVisibility(visible);
        mDialog.findViewById(R.id.networkPasswordEditText).setVisibility(visible);
        mDialog.findViewById(R.id.mountNetworkButton).setVisibility(visible);
    }

    private void setUpHorizontalRuler() {
        /*ImageView horizontalRulerImageView = findViewById(R.id.horizontalRulerImageView);
        Bitmap bitmap = Bitmap.createBitmap(512, 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        final int offset = 15;
        //canvas.drawLine(offset, canvas.getHeight() / 2, canvas.getWidth() - offset, canvas.getHeight() / 2, paint);
        canvas.drawLine(0, 10, 512, 10, paint);
        for (int index = 0; index < 100; ++index)
            canvas.drawLine(offset + 5 * index, 5, offset + 5 * index, 15, paint);
        horizontalRulerImageView.setImageBitmap(bitmap);*/
    }

    private void showDiagnosticTestProgress(boolean show) {
        if (mHasDiagnosticTestStarted) {
            boolean isRunning = mBackend.isDiagnosticTestRunning();
            mDialog.findViewById(R.id.diagnosticProgressAreaImageView).setVisibility(View.VISIBLE);
            TextView diagnosticsTestMessageAreaTitleTextView = mDialog.findViewById(R.id.diagnosticsTestMessageAreaTitleTextView);
            diagnosticsTestMessageAreaTitleTextView.setVisibility(View.VISIBLE);
            String testResults = "Diagnostic Test " + mBackend.getDiagnosticsStatus();
            String title = isRunning ? "Running Diagnostic Test" : testResults;
            diagnosticsTestMessageAreaTitleTextView.setText(title);
            int showOkButton = isRunning ? View.INVISIBLE : View.VISIBLE;
            int showProgressBar = isRunning ? View.VISIBLE : View.INVISIBLE;
            mDialog.findViewById(R.id.diagnosticsTestMessageAreaOkButton).setVisibility(showOkButton);
            mDialog.findViewById(R.id.runningDiagnosticsTestProgressBar).setVisibility(showProgressBar);
            return;
        }
        int visible = show ? View.VISIBLE : View.INVISIBLE;
        mDialog.findViewById(R.id.diagnosticsTestMessageAreaTitleTextView).setVisibility(visible);
        mDialog.findViewById(R.id.diagnosticsTestMessageAreaOkButton).setVisibility(visible);
        mDialog.findViewById(R.id.runningDiagnosticsTestProgressBar).setVisibility(visible);
        mDialog.findViewById(R.id.diagnosticProgressAreaImageView).setVisibility(visible);
    }

    private void checkRealtimeStatesInConfigurationPage() {
        Spinner driveTypeSpinner = mDialog.findViewById(R.id.driveTypeSpinner);
        if (mBackend.isDriveTypeNetwork()) {
            driveTypeSpinner.setSelection(0);
            showNetworkWidgetsInConfigurationPage(true);
        }
        else {
            driveTypeSpinner.setSelection(1);
            showNetworkWidgetsInConfigurationPage(false);
        }

        Spinner unitOfMeasureSpinner = mDialog.findViewById(R.id.unitOfMeasureSpinner);
        if (mBackend.isUnitTypeMetric())
            unitOfMeasureSpinner.setSelection(0);
        else
            unitOfMeasureSpinner.setSelection(1);

        Button mountNetworkButton = mDialog.findViewById(R.id.mountNetworkButton);
        if (mBackend.networkMounted())
            mountNetworkButton.setText("Disconnect");
        else
            mountNetworkButton.setText("Connect");
    }

    private void checkRealtimeStatesInDiagnosticsPage() {
        if (mBackend.isDiagnosticTestRunning()) {
            mHasDiagnosticTestStarted = true;
            mDialog.findViewById(R.id.startDiagnosticTestButton).setVisibility(View.INVISIBLE);
            mDialog.findViewById(R.id.abortDiagnosticTestButton).setVisibility(View.VISIBLE);
            ProgressBar runningDiagnosticsTestProgressBar = mDialog.findViewById(R.id.runningDiagnosticsTestProgressBar);
            runningDiagnosticsTestProgressBar.setProgress(mBackend.getDiagnosticsProgress());
        }
        else {
            mDialog.findViewById(R.id.startDiagnosticTestButton).setVisibility(View.VISIBLE);
            mDialog.findViewById(R.id.abortDiagnosticTestButton).setVisibility(View.INVISIBLE);
        }
        showDiagnosticTestProgress(mBackend.isDiagnosticTestRunning());
    }

    public void checkRealtimeStates() {
        System.out.println(TAG + ".checkRealtimeStates() step 1.");
        if (!readyForCheckRealtimeStates)
            return;
        System.out.println(TAG + ".checkRealtimeStates() step 2.");
        checkRealtimeStatesInConfigurationPage();
        System.out.println(TAG + ".checkRealtimeStates() step 3.");
        checkRealtimeStatesInDiagnosticsPage();
        System.out.println(TAG + ".checkRealtimeStates() step 4.");
    }
}
