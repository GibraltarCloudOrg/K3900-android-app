package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;


public class ViewDialog extends Dialog{
    public ViewDialog(@NonNull Context context){
        super(context);
        mContext=context;
    }

    private Context mContext=null;
    private Dialog mDialog=null;
    private View mfragment_second;
    private View mfragment_third;
    private ElementSetup rxSetup;
    private ElementSetup txSetup;

    public void showDialog(Context context){
        mDialog=new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.engineering_settings_tab_layout);
        mDialog.getWindow().setLayout(2300, 1500);


        Button quitButton=(Button) mDialog.findViewById(R.id.quit_button);
        Button saveButton=(Button) mDialog.findViewById(R.id.save_button);

        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDialog.dismiss();
            }
        });
        //display first tab before tab selection is made
        FrameLayout fl = (FrameLayout) mDialog.findViewById(R.id.simpleFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) ((MainActivity) mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fl.addView(layoutInflater.inflate(R.layout.fragment_first, mDialog.findViewById(R.id.relative_layout), false));

        addTabListener();
        txSetup=new ElementSetup(true);
        Thread t1=new Thread(txSetup);
        t1.start();
        rxSetup=new ElementSetup(false);
        Thread t2=new Thread(rxSetup);
        t2.start();
        txSetup.completeSetup(mfragment_second, mContext);
        rxSetup.completeSetup(mfragment_third, mContext);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DataSaver save=new DataSaver(txSetup, rxSetup);
                //Thread tSave=new Thread(save);
                //tSave.start();
            }
        });

        if(ElementSetup.getSaveButtonHidden()){
            saveButton.setVisibility(View.INVISIBLE);
        }

        while(txSetup.getIsRunning()){
        }
        while(rxSetup.getIsRunning()){

        }

        txSetup.checkSwitches(mfragment_second);
        rxSetup.checkSwitches(mfragment_third);
        mDialog.show();
    }

    public void addTabListener(){
        TabLayout tabLayout = (TabLayout) mDialog.findViewById(R.id.simpleTabLayout);
        LayoutInflater layoutInflater = (LayoutInflater) ((MainActivity) mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mfragment_second=layoutInflater.inflate(R.layout.fragment_second, mDialog.findViewById(R.id.relative_layout),false);
        mfragment_third=layoutInflater.inflate(R.layout.fragment_third, mDialog.findViewById(R.id.relative_layout), false);
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
                        fl.removeViewAt(1);
                        fl.addView(mfragment_second);
                        break;
                    case 2:
                        fl.removeViewAt(1);
                        fl.addView(mfragment_third);
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
}

