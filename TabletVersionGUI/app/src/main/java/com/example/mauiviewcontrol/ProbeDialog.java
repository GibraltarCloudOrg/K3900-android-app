package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ProbeDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "Probe Dialog";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    /*final*/ AutoCompleteTextView mSelectedProbeAutoCompleteTextView = null;
    //private static final String[] STUFF = new String[] { "Thing 1", "Thing 2" };

    public ProbeDialog(Context parent) {
        mContext = parent;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.probe_view);
        setUpWidgets();
        setUpListeners();
        mDialog.show();
    }

    //public void process() {
    //}

    private void setUpWidgets() {
        Spinner selectProbeSpinner = mDialog.findViewById(R.id.selectProbeSpinner);
        ArrayList<String> probes = mBackend.onGetProbeList();
        selectProbeSpinner.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, probes));
        selectProbeSpinner.setSelection(probes.indexOf(mBackend.getProbeName()));
        //LinearLayout probeViewTopLinearLayout = mDialog.findViewById(R.id.probeViewTopLinearLayout);
        /*mSelectedProbeAutoCompleteTextView = mDialog.findViewById(R.id.selectedProbeAutoCompleteTextView);
        mSelectedProbeAutoCompleteTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSelectedProbeAutoCompleteTextView.showDropDown();
            }
        });

        final ArrayList<String> probeList = mBackend.onGetProbeList();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_dropdown_item_1line,
                probeList
        );
        mSelectedProbeAutoCompleteTextView.setAdapter(adapter);*/
        /*ComboBox comboBox = new ComboBox(mContext);
        TextView textView1 = new TextView(mContext);
        textView1.setText("Text Item 1");
        comboBox.addView(textView1, Constraints.LayoutParams.VERTICAL);
        TextView textView2 = new TextView(mContext);
        textView2.setText("Text Item 2");
        comboBox.addView(textView2, Constraints.LayoutParams.VERTICAL);
        comboBox.setText("Combo Box Item 3");
        probeViewTopLinearLayout.addView(comboBox, 1);*/
        /*String[] COUNTRIES = new String[] {"Belgium", "France", "Italy", "Germany"};
        List<String> contriesList = Arrays.asList(COUNTRIES());
        ArrayAdapter<String> adapter = new UnconditionalArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, contriesList);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.countries_list);
        textView.setAdapter(adapter);
        mSelectedProbeView = new AutoCompleteTextView(mContext);
                //(AutoCompleteTextView) findViewById(R.id.myAutoCompleteTextView);

        mSelectedProbeView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSelectedProbeView.showDropDown();
            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.simple_dropdown_item_1line//,
                //R.id.view
        );
        final ArrayList<String> probeList = mBackend.onGetProbeList();
        adapter.addAll(probeList);
        mSelectedProbeView.setAdapter(adapter);
        probeViewTopLinearLayout.addView(mSelectedProbeView, 0);
        mSelectedProbeView.setHeight(500);*/
    }

    private void setUpListeners() {
        Spinner selectProbeSpinner = mDialog.findViewById(R.id.selectProbeSpinner);
        selectProbeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                MauiToastMessage.displayToastMessage(mContext, mBackend.onSelectProbe(selectedItem), selectedItem + " selected: ", Toast.LENGTH_LONG);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        /*Button selectProbeButton = mDialog.findViewById(R.id.selectProbeButton);
        selectProbeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedProbe = mSelectedProbeAutoCompleteTextView.getText().toString();
                boolean result = mBackend.onSelectProbe(selectedProbe);
                if (result)
                    mDialog.dismiss();
                Toast.makeText(mContext, "Select Probe: " + selectedProbe + ", " + result, Toast.LENGTH_LONG).show();
            }
        });*/
        Button cancelSelectProbeButton = mDialog.findViewById(R.id.exitSelectProbeButton);
        cancelSelectProbeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(mContext, "Select Probe Dialog Dismissed..!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
