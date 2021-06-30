package com.example.mauiviewcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.drawable.Drawable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.Backend;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import k3900.K3900;

import static android.content.Context.MODE_PRIVATE;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class WidgetUtility {

    static public void setUpListener(Context context, Dialog dialog, int buttonId, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean confirmationDialog, String dialogMessage, boolean showToast, String toastMessage, boolean dismissDialog) {
        Button button = dialog.findViewById(buttonId);
        setUpListener(context, button, dialog, element, visitor, confirmationDialog, dialogMessage, showToast, toastMessage, dismissDialog);
    }

    static public void setUpListener(Context context, Button button, Dialog dialog, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean confirmationDialog, String dialogMessage, boolean showToast, String toastMessage, boolean dismissDialog) {
        //Button button = dialog.findViewById(buttonId);
        //Dialog dialog=null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                boolean result = (showToast ? (MauiToastMessage.displayToastMessage(context, element.accept(visitor), toastMessage + element.getRuntimeSubText(), Toast.LENGTH_LONG)) : element.accept(visitor));
                                if (dismissDialog && dialog!=null)
                                    dialog.dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                            default:
                                break;
                        }
                    }
                };
                if (confirmationDialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(dialogMessage).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    MauiToastMessage.displayToastMessage(context, element.accept(visitor), toastMessage + element.getRuntimeSubText(), Toast.LENGTH_LONG);
                    if (dismissDialog && dialog!=null)
                        dialog.dismiss();
                }
            }
        });
    }

    static public void setUpListener(Context context, Switch switchInput, int switchGroup, ArrayList<SetElementMasking> setElementMaskingArrayList, Dialog dialog, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean confirmationDialog, String dialogMessage, boolean showToast, String toastMessage, boolean dismissDialog) {
        //Button button = dialog.findViewById(buttonId);
        //Dialog dialog=null;
        switchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                boolean result = (showToast ? (MauiToastMessage.displayToastMessage(context, element.accept(visitor), toastMessage + element.getRuntimeSubText(), Toast.LENGTH_LONG)) : element.accept(visitor));
                                if(result){
                                    for(int i=0; i<setElementMaskingArrayList.size();i++){
                                            setElementMaskingArrayList.get(i).setMask(true);
                                    }
                                }
                                if (dismissDialog && dialog!=null)
                                    dialog.dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                            default:
                                break;
                        }
                    }
                };
                if (confirmationDialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(dialogMessage).setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    MauiToastMessage.displayToastMessage(context, element.accept(visitor), toastMessage + element.getRuntimeSubText(), Toast.LENGTH_LONG);
                    if(switchGroup==1){
                        for(int i=0;i<64;i++){
                            setElementMaskingArrayList.get(i).setMask(switchInput.isChecked());
                        }
                    }
                    if(switchGroup==2) {
                        for (int i = 64; i < 128; i++) {
                            setElementMaskingArrayList.get(i).setMask(switchInput.isChecked());
                        }
                    }
                    if(switchGroup==3){
                        for (int i = 128; i < 192; i++) {
                            setElementMaskingArrayList.get(i).setMask(switchInput.isChecked());
                        }
                    }
                    if (dismissDialog && dialog!=null)
                        dialog.dismiss();
                }
            }
        });
    }

    static public void setUpFloatingActionButtonListener(Context context, FloatingActionButton floatingActionButton, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean showToast, String toastMessage) {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = (showToast ? (MauiToastMessage.displayToastMessage(context, element.accept(visitor), toastMessage + element.getRuntimeSubText(), Toast.LENGTH_LONG)) : element.accept(visitor));
            }
        });
    }

    static public void setUpUnitOfMeasureSpinnerListener(Spinner spinner, SwitchBackEndModel backEnd) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedString = parent.getItemAtPosition(position).toString();
                if (0 == selectedString.compareTo("Metric"))
                    backEnd.setUnitTypeMetric();
                else if (selectedString.contains("inches"))
                    backEnd.setUnitTypeInches();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    static public void updateCineLoop(ProgressBar cineLoopProgressBar) {
        try {
            SwitchBackEndModel backend = SwitchBackEndModel.getSwitchBackEndModelSingletonInstance();
            int playbackBufferSize = backend.getPlaybackBufferSize();
            int playbackStart = backend.getPlaybackStart();
            int currentFrame = backend.getCurrentFrame();
            int playbackSize = backend.getPlaybackSize();
            //Toast.makeText(MainWindowActivity.this, "Playback Buffer Size: " + playbackBufferSize + ", PlaybackStart: " + playbackStart + ", currentFrame: " + currentFrame + ", Playback Size: " + playbackSize, Toast.LENGTH_SHORT).show();
            /*cineLoopProgressBar.setScaleY(10f);
            cineLoopProgressBar.setMin(playbackStart);
            cineLoopProgressBar.setMax(playbackStart + playbackBufferSize);
            cineLoopProgressBar.setProgress(100 * playbackSize / playbackBufferSize);
            int progress = 100 * playbackSize / playbackBufferSize;*/
            cineLoopProgressBar.setScaleY(10f);  // height
            cineLoopProgressBar.setMin(0);
            cineLoopProgressBar.setMax(playbackBufferSize);
            cineLoopProgressBar.setProgress(playbackSize);
            int progress = 100 * playbackSize / playbackBufferSize;
            //Log.d(TAG, "updateCineLoop(), progress: " + progress);
        /*
            public int getPlaybackBufferSize()
            public int getPlaybackStart()
            public int getCurrentFrame()
            public int getPlaybackSize()

        int fsize = (static_cast<float>(m_pb_size)/m_pb_buff_size)*width();
        painter.drawRect(2*pw, VMARGIN + 2*pw, fsize - 4*pw, height() - 4*pw - 2*VMARGIN);

        if (m_playback && m_pb_size > 0)
        {
            painter.setPen(Qt::NoPen);
            painter.setBrush(frmMarkColor);

            int delta = m_current_frame - m_pb_start;
            int pb_frame = (delta >= 0) ? delta%m_pb_size : delta%m_pb_size + m_pb_size;
            //int pb_frame = (m_current_frame - m_pb_start)%m_pb_size;
            int fpos = (static_cast<float>(pb_frame)/m_pb_buff_size)*width();
            painter.drawRect(fpos - FRM_MRK_WIDTH/2, pw, FRM_MRK_WIDTH, height() - pw);
            //painter.drawRect( QRect( fpos - 3, 0, fpos + 3, height() ) );
        }
         */
        } catch (Exception e) {
            //Log.d(TAG, e.getMessage());
            //throw new LostCommunicationException("Lost Communication: Get Playback Buffer Size failed", e);
        }
    }

    /*static public void setCentimeterText(TextView cmTextView) {
        cmTextView.setText("cm");
    }*/

    public static void setUpPowerImageView(ImageView powerImageView, Context context) {
        //ImageView powerImageView = findViewById(R.id.powerImageView);
        powerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new PowerOffDialog(context);
                //Toast.makeText(context, "Please press and hold the 'Power' button for a few seconds to power off.", Toast.LENGTH_LONG).show();
            }
        });
        powerImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                MauiToastMessage.displayToastMessage(context, SwitchBackEndModel.getSwitchBackEndModelSingletonInstance().onRequestSystemShutdown(), "Shutdown Processing...", Toast.LENGTH_LONG);
                                ImageStreamer.getImageStreamerSingletonInstance().clear();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                            default:
                                Toast.makeText(context, "Canceled..", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Off", dialogClickListener).setNegativeButton("Cancel", dialogClickListener).show();*/
                //new PowerOffDialog(context);
                return true;
            }
        });
    }

    static public void setUpMauiLogoImageViewListener(ImageView mauiLogoImageView, Context context, Dialog dialog) {
        mauiLogoImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Turning on Engineering Mode.", Toast.LENGTH_LONG).show();
                //mDialog.dismiss();
                //showEngineeringMenu(context, dialog);
                showMauiLogoPopupMenu(context, dialog, mauiLogoImageView, true, R.style.MyPopupOtherStyle);
                return true;
            }
        });
    }

    /**
     * method responsible to show popup menu
     *
     * @param anchor      is a view where the popup will be shown
     * @param isWithIcons flag to check if icons to be shown or not
     * @param style       styling for popup menu
     */
    static private void showMauiLogoPopupMenu(Context context, Dialog dialog, View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(context, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        popup.getMenuInflater().inflate(R.menu.maui_logo_popup_menu, popup.getMenu());
        //popup.getMenu().findItem(R.id.action_users).setVisible(false);

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //SystemConfigurationDialog systemConfigurationDialog = new SystemConfigurationDialog(MainWindowActivity.this);
                switch (menuItem.getItemId()) {
                    case R.id.action_quick_save:
                        Toast.makeText(context, "Quick Save Selected", Toast.LENGTH_SHORT).show();
                        new ParameterValuesDialog(context);
                        break;
                    case R.id.action_quick_record:
                        Toast.makeText(context, "Quick Record Selected", Toast.LENGTH_SHORT).show();
                        Intent launchIntent = ((MainActivity)context).getPackageManager().getLaunchIntentForPackage("com.orpheusdroid.screenrecorder");
                        //launchIntent.putExtras(extras);
                        //launchIntent.arg
                        //Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                        if (launchIntent != null)
                            ((MainActivity)context).startActivity(launchIntent);
                        else
                            Toast.makeText(context, "There is no package available in android", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_gui_engineering:
                        Toast.makeText(context, "GUI Engineering Selected", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    static public void showEngineeringMenu(Context context, Dialog dialog/*, int buttonId, BackEndElement element, BackEndElementSendingMessageVisitor visitor, boolean confirmationDialog, String dialogMessage, boolean showToast, String toastMessage, boolean dismissDialog*/) {
        //Button button = dialog.findViewById(buttonId);
        //button.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(context, "Engineering Mode Selected.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                            default:
                                Toast.makeText(context, "Quick Save Selected.", Toast.LENGTH_LONG).show();
                                new ParameterValuesDialog(context);
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Engineering Menu").setPositiveButton("Go To Engineering Page", dialogClickListener).setNegativeButton("Quick Save as Text File", dialogClickListener).show();
            //}
        //});
    }

    public static void setUpCleanScreenButton(Button cleanScreenButton, Context context) {
        //Button cleanScreenButton = findViewById(R.id.cleanScreenButton);
        cleanScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MauiToastMessage.displayToastMessage(context, true, "Please press and hold the 'Clean Screen' button to lock the screen for cleaning.", Toast.LENGTH_LONG);
            }
        });
        cleanScreenButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new CleanScreenDialog(context);
                boolean result = true;
                MauiToastMessage.displayToastMessage(context, result, "It is safe to clean now..", Toast.LENGTH_LONG);
                return result;
            }
        });
    }

    public static void setUpDisconnectServerButton(Button disconnectServerButton, Context context) {
        //Button cleanScreenButton = findViewById(R.id.cleanScreenButton);
        disconnectServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "Please press and hold the 'Disconnect' button to disconnect from the server.", Toast.LENGTH_LONG).show();
            }
        });
        disconnectServerButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Disconnecting from the server now..", Toast.LENGTH_LONG).show();
                WifiDirectDeviceList.getWifiDirectDeviceListSingletonInstance().setSelected("");
                ImageStreamer.getImageStreamerSingletonInstance().clear();
                return true;
            }
        });
    }

    public static void setUpExitApplicationButton(Button exitButton, Context context) {
        //Button cleanScreenButton = findViewById(R.id.cleanScreenButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "Please press and hold the 'EXIT' button to close the application.", Toast.LENGTH_LONG).show();
            }
        });
        exitButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(context, "Closing the GUI Application now..", Toast.LENGTH_LONG).show();
                                ((MainActivity)context).finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                            default:
                                Toast.makeText(context, "Canceled..", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Off", dialogClickListener).setNegativeButton("Cancel", dialogClickListener).show();
                return true;
            }
        });
    }

    public static void writeToFile(String message, Context context)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("todolist.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(message);
            outputStreamWriter.close();
        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void updateDateTime(TextView timeTextView, TextView dateTextView) {
        timeTextView.setText(new SimpleDateFormat("HH:mm z", Locale.getDefault()).format(new Date()));
        dateTextView.setText(new SimpleDateFormat("EEE, MM-dd-yyyy", Locale.getDefault()).format(new Date()));
    }

    static public void updateBeamformerParameterTextView(TextView textView, String title, float value, boolean result) {
        try {
            textView.setText(title + "\n" + value);
            if (result)
                textView.setTextColor(Color.WHITE);
            else {
                textView.setTextColor(Color.YELLOW);
                textView.setText("Communication failed.");
            }
            //waitFor(3);
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "update Beamformer Parameter Text View error: " + e.getMessage());
        }
    }

    static public void updateBeamformerParameterTextView(TextView textView, String title, String value, boolean result) {
        try {
            textView.setText(title + "\n" + value);
            if (result)
                textView.setTextColor(Color.WHITE);
            else {
                textView.setTextColor(Color.YELLOW);
                textView.setText("Communication failed.");
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "update Beamformer Parameter Text View error: " + e.getMessage());
        }
    }

    static public void updateBeamformerParameterOneLineTextView(TextView textView, String title, float value, boolean result) {
        try {
            textView.setText(title + ": " + value);
            if (result)
                textView.setTextColor(Color.WHITE);
            else {
                textView.setTextColor(Color.YELLOW);
                textView.setText("Communication failed.");
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException: " + e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "update Beamformer Parameter Text View error: " + e.getMessage());
        }
    }

    static public void waitFor(int seconds) {
        seconds = seconds < 0 ? 0 : seconds;
        while (--seconds >= 0) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final String TAG = "Widget Utility";
}
