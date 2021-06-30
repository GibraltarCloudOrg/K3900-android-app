package com.example.mauiviewcontrol;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class EngineeringDialog {
    private SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    private static final String TAG = "Engineering Dialog";
    final Context mContext;
    private Dialog mDialog = null;
    private int mLastClickId = -1;
    private ElementMaskingButtonList mLeftTxElementMaskingButtonList = new ElementMaskingButtonList();
    private ElementMaskingButtonList mLeftRxElementMaskingButtonList = new ElementMaskingButtonList();
    private ElementMaskingButtonList mCenterTxElementMaskingButtonList = new ElementMaskingButtonList();
    private ElementMaskingButtonList mCenterRxElementMaskingButtonList = new ElementMaskingButtonList();
    private ElementMaskingButtonList mRightTxElementMaskingButtonList = new ElementMaskingButtonList();
    private ElementMaskingButtonList mRightRxElementMaskingButtonList = new ElementMaskingButtonList();
    private boolean mReadyForCheckRealtimeStates = false;
    private ArrayList<String> mTxSizeList = null;
    private ArrayList<String> mProbeList = null;
    private ArrayList<String> mFilterList = null;
    private BackEndSliderElementSendingMessageVisitor mBackEndSliderElementSendingMessageVisitor = null;
    private boolean mDebugMode = false;
    private boolean mUpdateLockNumberOfTxElements = true;

    public EngineeringDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.engineering_view);
        initializeElementMaskingPage();
        initializeTabPages();
        setUpTxSizeList();
        setUpProbeList();
        setUpFilterList();
        setUpWidgets();
        setUpListeners();
        mDialog.show();
        mReadyForCheckRealtimeStates = true;
        //int txSize = mBackend.getTransmitSize();
        ((Spinner)mDialog.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setSelection(mTxSizeList.indexOf(String.valueOf(mBackend.getTransmitSize())));
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

    private void initializeElementMaskingPage() {
        for (int row = 0; row < 4; ++row)
            initializeTableRow(row, R.id.leftElementMaskingTableLayout, mLeftTxElementMaskingButtonList, mLeftRxElementMaskingButtonList);
        for (int row = 0; row < 4; ++row)
            initializeTableRow(row, R.id.centerElementMaskingTableLayout, mCenterTxElementMaskingButtonList, mCenterRxElementMaskingButtonList);
        for (int row = 0; row < 4; ++row)
            initializeTableRow(row, R.id.rightElementMaskingTableLayout, mRightTxElementMaskingButtonList, mRightRxElementMaskingButtonList);
    }

    private void initializeTableRow(int row, int elementsTableLayout, ElementMaskingButtonList txElementMaskingButtonList, ElementMaskingButtonList rxElementMaskingButtonList) {
        TableLayout tableLayout = mDialog.findViewById(elementsTableLayout);
        TableRow tableRow = new TableRow(mContext);
        //leftElementMaskingTableLayout.addView(tableRow1);
        for (int column = 0; column < 4 * 4; ++column)
        {
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);
            ElementMaskingButton button = new ElementMaskingButton(mContext);
            button.setEnabled(false);
            if (0 == column % 4) {
                button.setText("T");
                txElementMaskingButtonList.add(button);
            }
            else {
                button.setText("R");
                rxElementMaskingButtonList.add(button);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.alternate();
                }
            });
            tableRow.addView(button, 120, 90);
        }
        tableLayout.addView(tableRow, 2000, 90);
    }

    private void setButtonsEnabled(ArrayList<Button> buttonList, boolean enabled) {
        for (int index = 0; index < buttonList.size(); ++index) {
            Button button = buttonList.get(index);
            if (enabled)
                button.setBackground(mContext.getResources().getDrawable(R.drawable.element_button_enabled));
            else
                button.setBackground(mContext.getResources().getDrawable(R.drawable.element_button_disabled));
        }
    }

    public void close () {
        mDialog.dismiss();
    }

    public void showGeneralPage() {
        setCurrentTabPage(0);
    }

    public void showElementMaskingPage() {
        setCurrentTabPage(1);
    }

    public void showPresetsPage() {
        setCurrentTabPage(2);
    }

    private void setCurrentTabPage(int page) {
        TabLayout engineeringTabLayout = mDialog.findViewById(R.id.engineeringTabLayout);
        engineeringTabLayout.getTabAt(page).select();
        View engineeringGeneralPage = mDialog.findViewById(R.id.engineeringGeneralPage);
        View engineeringElementMaskingPage = mDialog.findViewById(R.id.engineeringElementMaskingPage);
        View engineeringPresetsPage = mDialog.findViewById(R.id.engineeringPresetsPage);
        switch (page) {
            case 0:
                //fragment = new NewPatientFragment();
                engineeringGeneralPage.setVisibility(View.VISIBLE);
                engineeringElementMaskingPage.setVisibility(View.INVISIBLE);
                engineeringPresetsPage.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //fragment = new LookUpPatientFragment();
                engineeringGeneralPage.setVisibility(View.INVISIBLE);
                engineeringElementMaskingPage.setVisibility(View.VISIBLE);
                engineeringPresetsPage.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //fragment = new LookUpPatientFragment();
                engineeringGeneralPage.setVisibility(View.INVISIBLE);
                engineeringElementMaskingPage.setVisibility(View.INVISIBLE);
                engineeringPresetsPage.setVisibility(View.VISIBLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + page);
        }
    }

    private void initializeTabPages() {
        TabLayout engineeringTabLayout = mDialog.findViewById(R.id.engineeringTabLayout);
        engineeringTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //View engineeringGeneralPage = mDialog.findViewById(R.id.engineeringGeneralPage);
            //View engineeringElementMaskingPage = mDialog.findViewById(R.id.engineeringElementMaskingPage);
            //View engineeringPresetsPage = mDialog.findViewById(R.id.engineeringPresetsPage);
            @SuppressLint("WrongConstant")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabPage(tab.getPosition());
                //Fragment fragment = null;
                /*switch (tab.getPosition()) {
                    case 0:
                        //fragment = new NewPatientFragment();
                        engineeringGeneralPage.setVisibility(View.VISIBLE);
                        engineeringElementMaskingPage.setVisibility(View.INVISIBLE);
                        engineeringPresetsPage.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        //fragment = new LookUpPatientFragment();
                        engineeringGeneralPage.setVisibility(View.INVISIBLE);
                        engineeringElementMaskingPage.setVisibility(View.VISIBLE);
                        engineeringPresetsPage.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        //fragment = new LookUpPatientFragment();
                        engineeringGeneralPage.setVisibility(View.INVISIBLE);
                        engineeringElementMaskingPage.setVisibility(View.INVISIBLE);
                        engineeringPresetsPage.setVisibility(View.VISIBLE);
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
            ((Spinner)mDialog.findViewById(R.id.selectCompressionInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_look_and_feel_item, R.id.spinnerTextView, mBackend.onGetCompressionTypes()));

            ((Spinner)mDialog.findViewById(R.id.selectProbeInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_look_and_feel_item, R.id.spinnerTextView, mProbeList));
            ((Spinner)mDialog.findViewById(R.id.selectProbeInEngineeringGeneralSpinner)).setSelection(mProbeList.indexOf(mBackend.getProbeName()), false);

            ((Spinner)mDialog.findViewById(R.id.selectFrequencyInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_item, R.id.spinnerTextView, mFilterList));
            ((Spinner)mDialog.findViewById(R.id.selectFrequencyInEngineeringGeneralSpinner)).setSelection(mBackend.getFilterSelect(), false);

            ((Spinner)mDialog.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_look_and_feel_item, R.id.spinnerTextView, mBackend.getUnitOfMeasureList()));
            if (mBackend.isUnitTypeMetric())
                ((Spinner)mDialog.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner)).setSelection(0, false);
            else
                ((Spinner)mDialog.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner)).setSelection(1, false);

            ((Spinner)mDialog.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_item, R.id.spinnerTextView, mTxSizeList));
            //((Spinner)mDialog.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner)).setSelection(mTxSizeList.indexOf(String.valueOf(mBackend.getTransmitSize())), false);

            mDialog.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner).setEnabled(false);
            //BackEndSliderElementSendingMessageVisitor backEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
            //MauiSlider.setUpSliderListener(mContext, null, mDialog, new FineAdjust(), backEndSliderElementSendingMessageVisitor, true, "Frequency value updated", ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep, R.id.fineAdjustInEngineeringGeneralSeekBar);
            //WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.tx3PlusFloatingActionButton), "tx_apts", 1, true, "Increment TX 3 Apertures: ");
            BackEndElementSendingMessageVisitor backEndElementSendingMessageVisitor = new BackEndElementSendingMessageVisitor();
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.zoomInButton), new IncrementTx3Apertures(), backEndElementSendingMessageVisitor, true, "Increment TX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.zoomOutButton), new DecrementTx3Apertures(), backEndElementSendingMessageVisitor, true, "Decrement TX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.rx3PlusFloatingActionButton), new IncrementRx3Apertures(), backEndElementSendingMessageVisitor, true, "Increment RX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.rx3MinusFloatingActionButton), new DecrementRx3Apertures(), backEndElementSendingMessageVisitor, true, "Decrement RX 3 Apertures: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.pingPlusFloatingActionButton), new IncrementPingPersistence(), backEndElementSendingMessageVisitor, true, "Increment Ping Persistence: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.pingMinusFloatingActionButton), new DecrementPingPersistence(), backEndElementSendingMessageVisitor, true, "Decrement Ping Persistence: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.framePlusFloatingActionButton), new IncrementFramePersistence(), backEndElementSendingMessageVisitor, true, "Increment Frame Persistence: ");
            WidgetUtility.setUpFloatingActionButtonListener(mContext, mDialog.findViewById(R.id.frameMinusFloatingActionButton), new DecrementFramePersistence(), backEndElementSendingMessageVisitor, true, "Decrement Frame Persistence: ");
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

    private void setUpWidgetsInPresetsPage() {
        try {
            ((Spinner)mDialog.findViewById(R.id.selectCompressionInPresetsSpinner)).setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mBackend.onGetCompressionTypes()));
            ((Spinner)mDialog.findViewById(R.id.selectFrequencyInPresetsSpinner)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_item, R.id.spinnerTextView, mProbeList));
            //((ListView)mDialog.findViewById(R.id.activeUsersListView)).setAdapter(m_activeUserListAdapter = new ArrayAdapter<String>(mContext, R.layout.custom_text_view, R.id.customTextView, mBackend.getUserList()));
            //((Spinner)mDialog.findViewById(R.id.unitOfMeasurementSpinner2)).setAdapter(new ArrayAdapter<String>(mContext, R.layout.spinner_item, R.id.spinnerTextView, mBackend.getUnitOfMeasureList()));
            //BackEndSliderElementSendingMessageVisitor backEndSliderElementSendingMessageVisitor = new BackEndSliderElementSendingMessageVisitor();
            //MauiSlider.setUpSliderListener(mContext, null, mDialog, new FineAdjust(), backEndSliderElementSendingMessageVisitor, true, "Frequency value updated", ParameterLimits.MinEdgeFilter, ParameterLimits.MaxEdgeFilter, ParameterLimits.FloatValueStep, R.id.fineAdjustInPresetsSeekBar);
        } catch (LostCommunicationException le) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Lost Communication Error at setUpWidgetsInPresetsPage(): " + le.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, le.getMessage());
        } catch (Exception e) {
            if (mDebugMode)
                Toast.makeText(mContext, TAG + "Exception at setUpWidgetsInPresetsPage(): " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, e.getMessage());
        }
    }

    private void setUpListeners() {
        setUpListenersInGeneralPage();
        setUpListenersInElementMaskingPage();
        //setUpListenersInPresets();
        setUpExitButtonListener(R.id.exitInEngineeringGeneralButton);
        setUpExitButtonListener(R.id.exitInEngineeringElementMaskingButton);
        setUpExitButtonListener(R.id.exitInEngineeringPresetsButton);
    }

    private void setUpListenersInGeneralPage() {
        setUpCompressionSpinnerListener(R.id.selectCompressionInEngineeringGeneralSpinner);
        setUpProbeSpinnerListener();
        setUpFrequencySpinnerListener(R.id.selectFrequencyInEngineeringGeneralSpinner);
        WidgetUtility.setUpUnitOfMeasureSpinnerListener(mDialog.findViewById(R.id.unitOfMeasureInEngineeringGeneralSpinner), mBackend);
        setUpSelectNumberOfTxElementsInEngineeringGeneralSpinnerListener();
    }

    private void setUpListenersInElementMaskingPage() {
        setUpSwitchListener(R.id.elementMaskingLeftTxSwitch, "Element Masking Left Tx turned", mLeftTxElementMaskingButtonList);
        setUpSwitchListener(R.id.elementMaskingLeftRxSwitch, "Element Masking Left Rx turned", mLeftRxElementMaskingButtonList);
        setUpSwitchListener(R.id.elementMaskingCenterTxSwitch, "Element Masking Center Tx turned", mCenterTxElementMaskingButtonList);
        setUpSwitchListener(R.id.elementMaskingCenterRxSwitch, "Element Masking Center Rx turned", mCenterRxElementMaskingButtonList);
        setUpSwitchListener(R.id.elementMaskingRightTxSwitch, "Element Masking Right Tx turned", mRightTxElementMaskingButtonList);
        setUpSwitchListener(R.id.elementMaskingRightRxSwitch, "Element Masking Right Rx turned", mRightRxElementMaskingButtonList);
    }

    private void setUpListenersInPresets() {
        setUpCompressionSpinnerListener(R.id.selectCompressionInPresetsSpinner);
        setUpFrequencySpinnerListener(R.id.selectFrequencyInPresetsSpinner);
    }

    private void setUpSwitchListener(int id, String toastMessage, ElementMaskingButtonList elementMaskingButtonList) {
        Switch onOffSwitch = mDialog.findViewById(id);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                elementMaskingButtonList.setEnabled(isChecked);
                MauiToastMessage.displayToastMessageForCompoundButton(mContext, isChecked, toastMessage, Toast.LENGTH_SHORT);
            }
        });
    }

    private void setUpCompressionSpinnerListener(int id) {
        Spinner compressionSpinner = mDialog.findViewById(id);
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
        Spinner frequencySpinner = mDialog.findViewById(id);
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
        Spinner probeSpinner = mDialog.findViewById(R.id.selectProbeInEngineeringGeneralSpinner);
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
        Spinner selectNumberOfTxElementsInEngineeringGeneralSpinner = mDialog.findViewById(R.id.selectNumberOfTxElementsInEngineeringGeneralSpinner);
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

    private void setUpExitButtonListener(int exitButtonId) {
        Button exitButton = mDialog.findViewById(exitButtonId);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Engineering Dialog Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkRealtimeStates() {
        if (!mReadyForCheckRealtimeStates)
            return;
        try {
            ((Spinner)mDialog.findViewById(R.id.selectProbeInEngineeringGeneralSpinner)).setSelection(mProbeList.indexOf(mBackend.getProbeName()), false);
            //Spinner selectProbeInEngineeringGeneralSpinner = mDialog.findViewById(R.id.selectProbeInEngineeringGeneralSpinner);
            //int index = probes.indexOf(mBackend.getProbeName());
            //selectProbeInEngineeringGeneralSpinner.setSelection(index);
            int position = mBackend.getFilterSelect();
            ((Spinner)mDialog.findViewById(R.id.selectFrequencyInEngineeringGeneralSpinner)).setSelection(position, false);
            String filter = mFilterList.get(position);
            String[] strings = filter.split(" / ");
            float defaultValue = Float.valueOf(strings[0]);
            float min = defaultValue - Float.valueOf(strings[1]) / 2;
            float max = defaultValue + Float.valueOf(strings[1]) / 2;
            MauiSlider.setUpSliderListener(mContext, null, mDialog, new FineAdjust(), mBackEndSliderElementSendingMessageVisitor, true, "Frequency value updated", min, max, ParameterLimits.FloatValueStep, mDialog.findViewById(R.id.zoomSeekBar));
            MauiSlider.setCurrentSliderPosition(mDialog.findViewById(R.id.zoomSeekBar), mBackend.getTxFreq(), min, max, ParameterLimits.FloatValueStep);
            ((TextView)mDialog.findViewById(R.id.tx3ValueInEngineeringGeneralTextView)).setText(String.valueOf(mBackend.getTxApertures()));
            ((TextView)mDialog.findViewById(R.id.rx3ValueInEngineeringGeneralTextView)).setText(String.valueOf(mBackend.getRxApertures()));
            ((TextView)mDialog.findViewById(R.id.pingValueInEngineeringGeneralTextView)).setText(String.valueOf(mBackend.getPingMode()));
            ((TextView)mDialog.findViewById(R.id.frameValueInEngineeringGeneralTextView)).setText(mBackend.getFramePersistence());
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
