package com.example.mauiviewcontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;


public class EngineeringSettingsDialog extends Dialog{
    public EngineeringSettingsDialog(@NonNull Context context){
        super(context);
        mContext=context;
    }

    private Context mContext=null;
    private Dialog mDialog=null;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private View mFragmentTxView;
    private View mFragmentRxView;
    private RxElementMaskingSetup rxSetup;
    private TxElementMaskingSetup txSetup;

    public void showDialog(Context context){
        mDialog=new Dialog(context);
        mDialog.setContentView(R.layout.tab_layout);
        //mDialog.show();
        //mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //mDialog.setCancelable(false);
        //mDialog.setContentView(R.layout.tab_layout);
        mDialog.getWindow().setLayout(2300, 1500);

        addTabListener();
        //txSetup=new ElementMaskingSetup(true);
        txSetup=new TxElementMaskingSetup("Tx", mFragmentTxView, mContext, mFragmentTxView.findViewById(R.id.txLeftSwitch), mFragmentTxView.findViewById(R.id.txCenterSwitch), mFragmentTxView.findViewById(R.id.txRightSwitch));
        Thread t1=new Thread(txSetup);
        t1.start();
        //rxSetup=new ElementMaskingSetup(false);
        rxSetup=new RxElementMaskingSetup("Rx", mFragmentRxView, mContext, mFragmentRxView.findViewById(R.id.rxLeftSwitch), mFragmentRxView.findViewById(R.id.rxCenterSwitch), mFragmentRxView.findViewById(R.id.rxRightSwitch));
        Thread t2=new Thread(rxSetup);
        t2.start();
        //txSetup.completeSetup(mFragmentTxView, mContext, mFragmentTxView.findViewById(R.id.txLeftSwitch), mFragmentTxView.findViewById(R.id.txCenterSwitch), mFragmentTxView.findViewById(R.id.txRightSwitch));
        //rxSetup.completeSetup(mFragmentRxView, mContext, mFragmentRxView.findViewById(R.id.rxLeftSwitch), mFragmentRxView.findViewById(R.id.rxCenterSwitch), mFragmentRxView.findViewById(R.id.rxRightSwitch));


        Button quitButton=(Button) mDialog.findViewById(R.id.quit_button);
        Button saveButton=(Button) mDialog.findViewById(R.id.save_button);

        //display first tab before tab selection is made
        FrameLayout fl = (FrameLayout) mDialog.findViewById(R.id.simpleFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) ((MainActivity) mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fl.addView(layoutInflater.inflate(R.layout.fragment_first, mDialog.findViewById(R.id.relative_layout), false));


        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ElementDataSaver save=new ElementDataSaver(txSetup, rxSetup);
                Thread tSave=new Thread(save);
                tSave.start();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                txSetup.printTxData();
                mDialog.dismiss();
            }
        });

        if(ElementSetup.getSaveButtonHidden()){
            saveButton.setVisibility(View.INVISIBLE);
        }

        /*while(txSetup.getIsRunning()){
        }
        while(rxSetup.getIsRunning()){

        }*/

        //txSetup.checkSwitches();
        //rxSetup.checkSwitches();
        mDialog.show();

        if(txSetup.getSaveButtonHidden()) {
            startTimer(txSetup, rxSetup);
        }
    }

    public void addTabListener(){
        TabLayout tabLayout = (TabLayout) mDialog.findViewById(R.id.simpleTabLayout);
        LayoutInflater layoutInflater = (LayoutInflater) ((MainActivity) mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFragmentTxView=layoutInflater.inflate(R.layout.element_masking_tx_view, mDialog.findViewById(R.id.relative_layout),false);
        mFragmentRxView=layoutInflater.inflate(R.layout.element_masking_rx_view, mDialog.findViewById(R.id.relative_layout), false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                FrameLayout fl = (FrameLayout) mDialog.findViewById(R.id.simpleFrameLayout);
                switch (tab.getPosition()) {
                    case 0:
                        fl.removeViewAt(1);
                        fl.addView(layoutInflater.inflate(R.layout.fragment_first, mDialog.findViewById(R.id.relative_layout), false));
                        break;
                    case 1:
                        Toast.makeText(mContext, "Loading... please wait", Toast.LENGTH_SHORT).show();
                        fl.removeViewAt(1);
                        fl.addView(mFragmentTxView);
                        break;
                    case 2:
                        Toast.makeText(mContext, "Loading... please wait", Toast.LENGTH_SHORT).show();
                        fl.removeViewAt(1);
                        fl.addView(mFragmentRxView);
                        break;
                    case 3:
                        fl.removeViewAt(1);
                        fl.addView(layoutInflater.inflate(R.layout.fragment_fourth, mDialog.findViewById(R.id.relative_layout), false));
                }

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void startTimer(TxElementMaskingSetup txSetup, RxElementMaskingSetup rxSetup){
        Timer timer=new Timer();
        TimerTask checkElementStatusTimerTask=new CheckElementStatusTimerTask(mContext, txSetup, rxSetup);
        timer.scheduleAtFixedRate(checkElementStatusTimerTask, 0, 1*1000);
    }
}

