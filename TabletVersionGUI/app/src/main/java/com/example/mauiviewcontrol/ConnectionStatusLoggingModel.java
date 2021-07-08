package com.example.mauiviewcontrol;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConnectionStatusLoggingModel {

    public void setContext(Context context) {
        mContext = context;
    }

    public void log(String message) {
        if (!mCollectData)
            return;
        //sLogData = sLogData + "\n" + message + "at: " + LocalDateTime.now();
        mLogData = LocalDateTime.now() + ": " + message + "\n";
        writeLogsToFileToInternalStorage();
        //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public String getLogData() {
        return mLogData;
    }

    public void writeLogsToFileToInternalStorage() {
        if (!mCollectData)
            return;
        File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyLogFiles");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        try {
            File filePath = new File(fileDir, "Maui-K3900-Connection-Status-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt");
            /*if (filePath.exists()) {
                //FileReader reader = new FileReader(filePath);
                //String buffer = String.valueOf(reader.read());
                //mLogData = buffer + mLogData;
                //reader.close();
                boolean result = Files.deleteIfExists(filePath.toPath());
            }*/
            FileWriter writer = new FileWriter(filePath, true);
            writer.append(mLogData);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception at writeLogsToFileToInternalStorage: " + e.getMessage());
        }
    }

    private String mLogData = new String("");
    private final String TAG = "Connection Status Logging Model";
    private Context mContext = null;
    private boolean mCollectData = true;
    private static ConnectionStatusLoggingModel sSingletonInstance = null;
    public static ConnectionStatusLoggingModel getConnectionStatusLoggingModelSingletonInstance() {
        if (null == sSingletonInstance)
            sSingletonInstance = new ConnectionStatusLoggingModel();
        return sSingletonInstance;
    }
}
