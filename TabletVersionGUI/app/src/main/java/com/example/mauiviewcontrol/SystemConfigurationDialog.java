package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class SystemConfigurationDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "System Configuration Dialog";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    int mLastClickId = -1;

    public SystemConfigurationDialog(MainWindowActivity parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.system_configuration_view);
        initializeTabPages();
        setUpWidgets();
        setUpListeners();
        mDialog.show();
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
        /*TabLayout systemConfigurationTabLayout = mDialog.findViewById(R.id.systemConfigurationTabLayout);
        systemConfigurationTabLayout.getTabAt(page).select();
        LinearLayout systemConfigurationLinearLayout = mDialog.findViewById(R.id.systemConfigurationLinearLayout);
        LinearLayout systemConfigurationCreateUserLinearLayout = mDialog.findViewById(R.id.systemConfigurationCreateUserLinearLayout);
        LinearLayout systemConfigurationUsersListLinearLayout = mDialog.findViewById(R.id.systemConfigurationUsersListLinearLayout);
        LinearLayout systemConfigurationDiagnosticsLinearLayout = mDialog.findViewById(R.id.systemConfigurationDiagnosticsLinearLayout);
        switch (page) {
            case 0:
                systemConfigurationLinearLayout.setVisibility(View.VISIBLE);
                systemConfigurationCreateUserLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationUsersListLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationDiagnosticsLinearLayout.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //fragment = new LookUpPatientFragment();
                systemConfigurationLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationCreateUserLinearLayout.setVisibility(View.VISIBLE);
                systemConfigurationUsersListLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationDiagnosticsLinearLayout.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //fragment = new LookUpPatientFragment();
                systemConfigurationLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationCreateUserLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationUsersListLinearLayout.setVisibility(View.INVISIBLE);
                systemConfigurationDiagnosticsLinearLayout.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + page);
        }*/
    }

    private void initializeTabPages() {
        /*TabLayout systemConfigurationTabLayout = mDialog.findViewById(R.id.systemConfigurationTabLayout);
        systemConfigurationTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabPage(tab.getPosition());*/
                //Fragment fragment = null;
                /*switch (tab.getPosition()) {
                    case 0:
                        //fragment = new NewPatientFragment();
                        systemConfigurationLinearLayout.setVisibility(View.VISIBLE);
                        systemConfigurationCreateUserLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationUsersListLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationDiagnosticsLinearLayout.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        //fragment = new LookUpPatientFragment();
                        systemConfigurationLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationCreateUserLinearLayout.setVisibility(View.VISIBLE);
                        systemConfigurationUsersListLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationDiagnosticsLinearLayout.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        //fragment = new LookUpPatientFragment();
                        systemConfigurationLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationCreateUserLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationUsersListLinearLayout.setVisibility(View.INVISIBLE);
                        systemConfigurationDiagnosticsLinearLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tab.getPosition());
                }
            }*/

            /*@Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    private void setUpWidgets() {
    }

    private void setUpListeners() {
        Button activeUsersButton = mDialog.findViewById(R.id.activeUsersButton);
        activeUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.findViewById(R.id.systemConfigurationUsersListLinearLayout).setVisibility(View.VISIBLE);
                mDialog.findViewById(R.id.systemConfigurationCreateUserLinearLayout).setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "Active Users Button clicked.", Toast.LENGTH_LONG).show();
            }
        });

        Button exitUsersListButton = mDialog.findViewById(R.id.exitUsersListButton);
        exitUsersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.findViewById(R.id.systemConfigurationUsersListLinearLayout).setVisibility(View.INVISIBLE);
                mDialog.findViewById(R.id.systemConfigurationCreateUserLinearLayout).setVisibility(View.VISIBLE);
                Toast.makeText(mContext, "Exit Users List Button clicked.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
