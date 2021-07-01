package com.example.mauiviewcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EngineeringMenuDialog {
    SwitchBackEndModel mBackend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
    public static final String TAG = "Engineering Menu";
    final Context mContext;
    /*final*/ Dialog mDialog = null;
    int mLastClickId = -1;
    EngineeringImagingDialog mEngineeringImagingDialog = null;
    EngineeringDialog mEngineeringDialog = null;
    EngineeringSettingsDialog mEngineeringSettingsDialog=null;

    public EngineeringMenuDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.engineering_menu_view);
        setUpWidgets();
        setUpListeners();
        mDialog.getWindow().setGravity(Gravity.RIGHT);
        mDialog.show();
        //mEngineeringSettingsDialog=new EngineeringSettingsDialog(mContext);
    }

    private void setUpWidgets() {
        ListView engineeringMenuListView = mDialog.findViewById(R.id.engineeringMenuListView);
        engineeringMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDialog.dismiss();
                MauiListView.changeListViewSelectedItemColor(parent, view, position, mLastClickId);
                mLastClickId = position;
                String selectedString = engineeringMenuListView.getAdapter().getItem(position).toString();
                //if (null == mEngineeringDialog)
                    //mEngineeringDialog = new EngineeringDialog(mContext);
                    mEngineeringSettingsDialog=new EngineeringSettingsDialog(mContext);
                //String test = mContext.getResources().getString(R.string.general);
                if (selectedString.equals(mContext.getResources().getString(R.string.general)))
                    //mEngineeringDialog.showGeneralPage();
                    mEngineeringSettingsDialog.showDialog(mContext,0);
                //mEngineeringSettingsDialog.show(mContext,0);
                else if (selectedString.equals(mContext.getResources().getString(R.string.element_masking))) {
                    //mEngineeringDialog.showElementMaskingPage();
                    //mEngineeringDialog.close();
                    /*
                    Toast.makeText(mContext, "Loading... please wait", Toast.LENGTH_SHORT).show();
                    ViewDialog alert = new ViewDialog(mContext);
                    alert.showDialog(mContext);*/
                    mEngineeringSettingsDialog.showDialog(mContext, 1);
                }
                else if (selectedString.equals(mContext.getResources().getString(R.string.configure_presets)))
                    //mEngineeringDialog.showPresetsPage();
                    mEngineeringSettingsDialog.showDialog(mContext,2);
                //mEngineering.show(mContext, 2);
                else if (selectedString.equals(mContext.getResources().getString(R.string.imaging))) {
                    mEngineeringDialog.close();
                    //mDialog.dismiss();
                    /*Rect displayRectangle = new Rect();
                    Window window = ((MainWindowActivity)mContext).getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(((MainWindowActivity)mContext),R.style.Theme_AppCompat_Dialog);
                    ViewGroup viewGroup = mDialog.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_text_view, viewGroup, false);
                    dialogView.setMinimumWidth((int)(displayRectangle.width() * 1f));
                    dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    Button buttonOk=dialogView.findViewById(R.id.buttonOk);
                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();*/
                    //mEngineeringImagingDialog = new EngineeringImagingDialog((MainWindowActivity) mContext);
                }
            }
        });
        final ArrayList<String>list = engineeringMenuList();
        engineeringMenuListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list));
    }

    private void setUpListeners() {
    }

    private final ArrayList<String> engineeringMenuList() {
        System.out.println(TAG + ".engineeringMenuList() called.");
        final ArrayList<String> menuList = new ArrayList<String>();
        //menuList.add(mContext.getResources().getString(R.string.back));
        menuList.add(mContext.getResources().getString(R.string.general));
        menuList.add(mContext.getResources().getString(R.string.element_masking));
        menuList.add(mContext.getResources().getString(R.string.configure_presets));
        menuList.add(mContext.getResources().getString(R.string.imaging));
        return menuList;
    }

    public boolean isEngineeringImagingDialogVisible() {
        if (null == mEngineeringImagingDialog)
            return false;
        return mEngineeringImagingDialog.isVisible();
    }

    public void checkRealtimeStates() {
        if (null != mEngineeringImagingDialog)
            mEngineeringImagingDialog.checkRealtimeStates();
        if (null != mEngineeringDialog)
            mEngineeringDialog.checkRealtimeStates();
        if(null!=mEngineeringSettingsDialog){
            mEngineeringSettingsDialog.checkRealtimeStates();
        }
    }
}
