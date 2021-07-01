package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class EngineeringSettingsDialog extends Dialog{
    public EngineeringSettingsDialog(@NonNull Context context){
        super(context);
        mContext=context;
        mDialog=new Dialog(context);
        mDialog.setContentView(R.layout.engineering_settings_tab_layout);
        mDialog.getWindow().setLayout(2300, 1500);
        ElementMaskingSetup.setTextView(mDialog.findViewById(R.id.LoadingText));
        addTabListener();
        setUpProbeList();
        setUpFilterList();
        setUpTxSizeList();
        setUpWidgets();
        setUpListenersInGeneralPage();
        //SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().setMessageTo(SwitchBackEndModel.MessageTo.UnitTesting);

        mReadyForCheckRealtimeStates = true;
        //int txSize = mBackend.getTransmitSize();
        ((Spinner)mFragmentGeneral.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setSelection(mTxSizeList.indexOf(String.valueOf(mBackend.getTransmitSize())));

        txSetup=new TxElementMaskingSetup("Tx", mFragmentTxView, mContext, mFragmentTxView.findViewById(R.id.txLeftSwitch), mFragmentTxView.findViewById(R.id.txCenterSwitch), mFragmentTxView.findViewById(R.id.txRightSwitch));
        Thread t1=new Thread(txSetup);
        t1.start();
        //rxSetup=new ElementMaskingSetup(false);
        rxSetup=new RxElementMaskingSetup("Rx", mFragmentRxView, mContext, mFragmentRxView.findViewById(R.id.rxLeftSwitch), mFragmentRxView.findViewById(R.id.rxCenterSwitch), mFragmentRxView.findViewById(R.id.rxRightSwitch));
        Thread t2=new Thread(rxSetup);
        t2.start();
    }

    private Context mContext=null;
    private Dialog mDialog=null;
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private View mFragmentTxView;
    private View mFragmentRxView;
    private View mFragmentGeneral;
    private View mFragmentPresets;
    private ArrayList<String> mTxSizeList = null;
    private RxElementMaskingSetup rxSetup;
    private TxElementMaskingSetup txSetup;
    private ArrayList<String> mProbeList = null;
    private ArrayList<String> mFilterList = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean mDebugMode = false;
    private static final String TAG = "Engineering Settings Dialog";
    private boolean mReadyForCheckRealtimeStates = false;
    private boolean mUpdateLockNumberOfTxElements = true;
    private Timer mTimer=null;
    private TimerTask mCheckElementStatusTimerTask=null;


    public void showDialog(Context context, int firstTab){
        /*mDialog=new Dialog(context);
        mDialog.setContentView(R.layout.tab_layout);
        mDialog.getWindow().setLayout(2300, 1500);*/
        //mDialog.show();
        //mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //mDialog.setCancelable(false);
        //mDialog.setContentView(R.layout.tab_layout);


        //addTabListener();
        //txSetup=new ElementMaskingSetup(true);
        /*txSetup=new TxElementMaskingSetup("Tx", mFragmentTxView, mContext, mFragmentTxView.findViewById(R.id.txLeftSwitch), mFragmentTxView.findViewById(R.id.txCenterSwitch), mFragmentTxView.findViewById(R.id.txRightSwitch));
        Thread t1=new Thread(txSetup);
        t1.start();
        //rxSetup=new ElementMaskingSetup(false);
        rxSetup=new RxElementMaskingSetup("Rx", mFragmentRxView, mContext, mFragmentRxView.findViewById(R.id.rxLeftSwitch), mFragmentRxView.findViewById(R.id.rxCenterSwitch), mFragmentRxView.findViewById(R.id.rxRightSwitch));
        Thread t2=new Thread(rxSetup);
        t2.start();*/
        //txSetup.completeSetup(mFragmentTxView, mContext, mFragmentTxView.findViewById(R.id.txLeftSwitch), mFragmentTxView.findViewById(R.id.txCenterSwitch), mFragmentTxView.findViewById(R.id.txRightSwitch));
        //rxSetup.completeSetup(mFragmentRxView, mContext, mFragmentRxView.findViewById(R.id.rxLeftSwitch), mFragmentRxView.findViewById(R.id.rxCenterSwitch), mFragmentRxView.findViewById(R.id.rxRightSwitch));


        Button quitButton=(Button) mDialog.findViewById(R.id.quit_button);
        Button saveButton=(Button) mDialog.findViewById(R.id.save_button);

        //display first tab before tab selection is made
        showFirstTab(firstTab);


        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ElementDataSaver elementDataSaver=new ElementDataSaver(txSetup, rxSetup);
                //Thread tSave=new Thread(save);
                //tSave.start();
                elementDataSaver.save();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                txSetup.printTxData();
                mDialog.dismiss();
                if(ElementMaskingSetup.sSaveButtonHidden){
                    mTimer.cancel();
                    mCheckElementStatusTimerTask.cancel();
                }
            }
        });

        if(ElementMaskingSetup.getSaveButtonHidden()){
            saveButton.setVisibility(View.INVISIBLE);
        }

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
        mFragmentGeneral=layoutInflater.inflate(R.layout.engineering_generalpage_view, mDialog.findViewById(R.id.relative_layout), false);
        mFragmentPresets=layoutInflater.inflate(R.layout.engineering_presetpage_view, mDialog.findViewById(R.id.relative_layout), false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                FrameLayout fl = (FrameLayout) mDialog.findViewById(R.id.simpleFrameLayout);
                switch (tab.getPosition()) {
                    case 0:
                        mDialog.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
                        fl.removeViewAt(1);
                        fl.addView(mFragmentGeneral);
                        break;
                    case 1:
                        if(!ElementMaskingSetup.sSaveButtonHidden){
                            mDialog.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
                        }
                        fl.removeViewAt(1);
                        fl.addView(mFragmentTxView);
                        //alert.cancel();
                        break;
                    case 2:
                        if(!ElementMaskingSetup.sSaveButtonHidden){
                            mDialog.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
                        }
                        //oast.makeText(mContext, "Loading... please wait", Toast.LENGTH_SHORT).show();
                        fl.removeViewAt(1);
                        fl.addView(mFragmentRxView);
                        break;
                    case 3:
                        mDialog.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
                        fl.removeViewAt(1);
                        fl.addView(mFragmentPresets);
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
        mTimer=new Timer();
        mCheckElementStatusTimerTask=new CheckElementStatusTimerTask(mContext, txSetup, rxSetup);
        mTimer.scheduleAtFixedRate(mCheckElementStatusTimerTask, 0, 1*1000);
    }

    private void showFirstTab(int tabNumber){
        TabLayout tabLayout = (TabLayout) mDialog.findViewById(R.id.simpleTabLayout);
        FrameLayout fl = (FrameLayout) mDialog.findViewById(R.id.simpleFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) ((MainActivity) mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (tabNumber){
            case 0:
                mDialog.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
                fl.addView(mFragmentGeneral);
                break;
            case 1:
                if(!ElementMaskingSetup.sSaveButtonHidden){
                    mDialog.findViewById(R.id.save_button).setVisibility(View.VISIBLE);
                }
                fl.addView(mFragmentTxView);
                tabLayout.getTabAt(1).select();
                //tabLayout.setScrollPosition(1,0f,true, true);

                break;
            case 2:
                mDialog.findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
                fl.addView(mFragmentPresets);
                tabLayout.getTabAt(3).select();
                break;
        }
    }

    private void setUpProbeList() {
        if (null == mProbeList) {
            mProbeList = mBackend.onGetProbeList();
        }
    }

    private void setUpFilterList() {
        if (null == mFilterList) {
            mFilterList = mBackend.onGetFilters();
        }
    }

    private void setUpWidgets() {
        //FrameLayout fl=(FrameLayout)mDialog.findViewById(R.id.layout);
        //LayoutInflater layoutInflater=(LayoutInflater)((MainActivity)mContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //fl.addView(layoutInflater.inflate(R.layout.fragment_pager, new LinearLayout(mContext), false), 1);

        if (null == mBackEndSliderElementSendingMessageVisitor)
            mBackEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
        //ConstraintLayout engineeringViewTopConstraintLayout = mDialog.findViewById(R.id.engineeringViewTopConstraintLayout);
        //engineeringViewTopConstraintLayout.setMinimumWidth(1000);
        //engineeringViewTopConstraintLayout.setMinimumHeight(900);
        //TabLayout engineeringTabLayout = mDialog.findViewById(R.id.engineeringTabLayout);
        //engineeringTabLayout.setMinimumWidth(1000);
        setUpWidgetsInGeneralPage();
        //setUpWidgetsInPresetsPage();
    }

    private void setUpWidgetsInGeneralPage() {
        try {
            ((Spinner)mFragmentGeneral.findViewById(R.id.selectCompressionInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_look_and_feel_item, R.id.spinnerTextView, mBackend.onGetCompressionTypes()));

            ((Spinner)mFragmentGeneral.findViewById(R.id.selectProbeInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_look_and_feel_item, R.id.spinnerTextView, mProbeList));
            ((Spinner)mFragmentGeneral.findViewById(R.id.selectProbeInEngineeringGeneralSpinner)).setSelection(mProbeList.indexOf(mBackend.getProbeName()), false);

            ((Spinner)mFragmentGeneral.findViewById(R.id.selectFrequencyInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_item, R.id.spinnerTextView, mFilterList));
            ((Spinner)mFragmentGeneral.findViewById(R.id.selectFrequencyInEngineeringGeneralSpinner)).setSelection(mBackend.getFilterSelect(), false);

            ((Spinner)mFragmentGeneral.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_look_and_feel_item, R.id.spinnerTextView, mBackend.getUnitOfMeasureList()));
            if (mBackend.isUnitTypeMetric())
                ((Spinner)mFragmentGeneral.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner)).setSelection(0, false);
            else
                ((Spinner)mFragmentGeneral.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner)).setSelection(1, false);

            ((Spinner)mFragmentGeneral.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_item, R.id.spinnerTextView, mTxSizeList));
            //((Spinner)mDialog.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setSelection(mTxSizeList.indexOf(String.valueOf(mBackend.getTransmitSize())), false);

            mFragmentGeneral.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner).setEnabled(false);
            //BackEndSliderElementSendingMessageVisitor backEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
            //MauiSlider.setUpSliderListener(mContext, null, mDialog, new FineAdjust(), backEndSliderElementSendingMessageVisitor, true, "Frequency value updated", ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep, R.id.fineAdjustInEngineeringGeneralSeekBar);
            //WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.tx3PlusFloatingActionButton), "tx_apts", 1, true, "Increment TX 3 Apertures: ");
            BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.zoomInButton), new IncrementTx3Apertures(), backEndElementSendingMessageVisitor, true, "Increment TX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.zoomOutButton), new DecrementTx3Apertures(), backEndElementSendingMessageVisitor, true, "Decrement TX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.rx3PlusFloatingActionButton), new IncrementRx3Apertures(), backEndElementSendingMessageVisitor, true, "Increment RX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.rx3MinusFloatingActionButton), new DecrementRx3Apertures(), backEndElementSendingMessageVisitor, true, "Decrement RX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.pingPlusFloatingActionButton), new IncrementPingPersistence(), backEndElementSendingMessageVisitor, true, "Increment Ping Persistence: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.pingMinusFloatingActionButton), new DecrementPingPersistence(), backEndElementSendingMessageVisitor, true, "Decrement Ping Persistence: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.framePlusFloatingActionButton), new IncrementFramePersistence(), backEndElementSendingMessageVisitor, true, "Increment Frame Persistence: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mFragmentGeneral.findViewById(R.id.frameMinusFloatingActionButton), new DecrementFramePersistence(), backEndElementSendingMessageVisitor, true, "Decrement Frame Persistence: ");
        } catch (LostCommunicationException le) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Lost Communication Error at setUpWidgetsInGeneralPage(): " + le.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Null Pointer Exception at setUpWidgetsInGeneralPage(): " + npe.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Exception at setUpWidgetsInGeneralPage(): " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, e.getMessage());
        }
    }

    private void setUpTxSizeList() {
        mTxSizeList = new ArrayList<>();
        mTxSizeList.add("1");
        mTxSizeList.add("3");
        mTxSizeList.add("5");
        mTxSizeList.add("7");
        mTxSizeList.add("9");
        //mTxSizeList.add("11");
        //mTxSizeList.add("13");
    }

    private void setUpListenersInGeneralPage() {
        setUpCompressionSpinnerListener(R.id.selectCompressionInEngineeringGeneralSpinner);
        setUpProbeSpinnerListener();
        setUpFrequencySpinnerListener(R.id.selectFrequencyInEngineeringGeneralSpinner);
        WidgetUtility.setUpUnitOfMeasureSpinnerListener(mFragmentGeneral.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner), mBackend);
        setUpSelectNumberOfTxElementsInEngineeringGeneralSpinnerListener();
    }

    private void setUpCompressionSpinnerListener(int id) {
        Spinner compressionSpinner = mFragmentGeneral.findViewById(id);
        compressionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (mReadyForCheckRealtimeStates) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    MauiToastMessage.displayToastMessage(mContext, mBackend.onSelectCompressionType(selectedItem), selectedItem + " selected: ", Toast.LENGTH_SHORT);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void setUpFrequencySpinnerListener(int id) {
        Spinner frequencySpinner = mFragmentGeneral.findViewById(id);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try {
                    if (!mReadyForCheckRealtimeStates)
                        return;
                    String filter = mFilterList.get(position);
                    String[] strings = filter.split(" / ");
                    float defaultValue = Float.valueOf(strings[0]);
                    //float min = defaultValue - Float.valueOf(strings[1]) / 2;
                    //float max = defaultValue + Float.valueOf(strings[1]) / 2;
                    MauiToastMessage.displayToastMessage(mContext, mBackend.onSelectFilter(position), "Filter selected, index: " + position, Toast.LENGTH_SHORT);
                    MauiToastMessage.displayToastMessage(mContext, mBackend.onFrequencyChanged(defaultValue), "Frequency changed to: " + defaultValue, Toast.LENGTH_SHORT);
                    //SeekBar fineAdjustInEngineeringGeneralSeekBar = mDialog.findViewById(R.id.fineAdjustInEngineeringGeneralSeekBar);
                    //fineAdjustInEngineeringGeneralSeekBar.setMin((int)min);
                    //fineAdjustInEngineeringGeneralSeekBar.setMax((int)max);
                    //fineAdjustInEngineeringGeneralSeekBar.setProgress((int)defaultValue);
                    //fineAdjustInEngineeringGeneralSeekBar.setProgress((int) ((max - min) * (defaultValue - min) / (ParameterLimits.FloatValueStep * (max - min))));
                } catch (Exception e) {
                    if (mDebugMode)
                        Toast.makeText(mContext, TAG + ", Exception at onItemSelected(): " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, e.getMessage());
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void setUpProbeSpinnerListener() {
        Spinner probeSpinner = mFragmentGeneral.findViewById(R.id.selectProbeInEngineeringGeneralSpinner);
        probeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (mReadyForCheckRealtimeStates) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    MauiToastMessage.displayToastMessage(mContext, mBackend.onSelectProbe(selectedItem), selectedItem + " selected: ", Toast.LENGTH_SHORT);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void setUpSelectNumberOfTxElementsInEngineeringGeneralSpinnerListener() {
        Spinner selectNumberOfTxElementsInEngineeringGeneralSpinner = mFragmentGeneral.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner);
        selectNumberOfTxElementsInEngineeringGeneralSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (mReadyForCheckRealtimeStates) {
                    //if (mUpdateLockNumberOfTxElements) {
                    String selectedString = parent.getItemAtPosition(position).toString();
                    int value = Integer.valueOf(selectedString);
                    MauiToastMessage.displayToastMessage(mContext, mBackend.setNumberOfTxElements(value), "set transmit size to: " + selectedString + ", ", Toast.LENGTH_SHORT);
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //}
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    public void checkRealtimeStates() {
        if (!mReadyForCheckRealtimeStates)
            return;
        try {
            ((Spinner)mFragmentGeneral.findViewById(R.id.selectProbeInEngineeringGeneralSpinner)).setSelection(mProbeList.indexOf(mBackend.getProbeName()), false);
            //Spinner selectProbeInEngineeringGeneralSpinner = mDialog.findViewById(R.id.selectProbeInEngineeringGeneralSpinner);
            //int index = probes.indexOf(mBackend.getProbeName());
            //selectProbeInEngineeringGeneralSpinner.setSelection(index);
            int position = mBackend.getFilterSelect();
            ((Spinner)mFragmentGeneral.findViewById(R.id.selectFrequencyInEngineeringGeneralSpinner)).setSelection(position, false);
            String filter = mFilterList.get(position);
            String[] strings = filter.split(" / ");
            float defaultValue = Float.valueOf(strings[0]);
            float min = defaultValue - Float.valueOf(strings[1]) / 2;
            float max = defaultValue + Float.valueOf(strings[1]) / 2;
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new FineAdjust(), mBackEndSliderElementSendingMessageVisitor, true, "Frequency value updated", min, max, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.zoomSeekBar));
            MauiSlider.setCurrentSliderPosition(mFragmentGeneral.findViewById(R.id.zoomSeekBar), mBackend.getTxFreq(), min, max, ParameterLimits.FloatValueStep);
            ((TextView)mFragmentGeneral.findViewById(R.id.tx3ValueInEngineeringGeneralTextView)).setText(String.valueOf(mBackend.getTxApertures()));
            ((TextView)mFragmentGeneral.findViewById(R.id.rx3ValueInEngineeringGeneralTextView)).setText(String.valueOf(mBackend.getRxApertures()));
            ((TextView)mFragmentGeneral.findViewById(R.id.pingValueInEngineeringGeneralTextView)).setText(String.valueOf(mBackend.getPingMode()));
            ((TextView)mFragmentGeneral.findViewById(R.id.frameValueInEngineeringGeneralTextView)).setText(mBackend.getFramePersistence());
            //String str = String.valueOf(mBackend.getTransmitSize());
            mUpdateLockNumberOfTxElements = false;
            //((Spinner)mDialog.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setSelection(mTxSizeList.indexOf(String.valueOf(mBackend.getTransmitSize())), false);
            mUpdateLockNumberOfTxElements = true;
        } catch (LostCommunicationException le) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Lost Communication Error at checkRealtimeStates(): " + le.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, le.getMessage());
        } catch (NullPointerException npe) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Null Pointer Exception at checkRealtimeStates(): " + npe.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, npe.getMessage());
        } catch (Exception e) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Exception at checkRealtimeStates(): " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.getMessage());
        }
    }
}

