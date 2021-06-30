package com.example.mauiviewcontrol;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ElapseTimeStatisticsModel {

    static public void append(long time, String message) {
        if (!mCollectData)
            return;
        mLogData = mLogData + "\n" + time + ", " + message + "at: " + LocalDateTime.now();
        /*if (10 > mLogData.length() % 100) {
            //mLogData = "\n]n" + TAG + ": File updated at: " + timeStamp + mLogData + "\n\n";
            writeLogsToFileToInternalStorage();
            //mLogData = "";
        }*/
    }

    static public void start(long time, String message) {
        if (!mCollectData)
            return;
        s_startTime = System.nanoTime();
        mLogData = mLogData + "\n" + time + ", nano time: " + s_startTime + "[ns], " + message + "at: " + LocalDateTime.now();
        /*if (10 > mLogData.length() % 100) {
            //mLogData = "\n]n" + TAG + ": File updated at: " + timeStamp + mLogData + "\n\n";
            writeLogsToFileToInternalStorage();
            //mLogData = "";
        }*/
    }

    static public void end(long time, String message) {
        if (!mCollectData)
            return;
        mLogData = mLogData + "\n" + time + ", elapse time: " + (System.nanoTime() - s_startTime) / 1000000 + "[ms], " + message + "at: " + LocalDateTime.now();
        if (10 > mLogData.length() % 100) {
            //mLogData = "\n]n" + TAG + ": File updated at: " + timeStamp + mLogData + "\n\n";
            writeLogsToFileToInternalStorage();
            //mLogData = "";
        }
    }

    static public String getLogData() {
        return mLogData;
    }

    static public void writeLogsToFileToInternalStorage() {
        if (!mCollectData)
            return;
        File fileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyLogFiles");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        try {
            File filePath = new File(fileDir, "Maui-K3900-GetImage2-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt");
            if (filePath.exists()) {
                //FileReader reader = new FileReader(filePath);
                //String buffer = String.valueOf(reader.read());
                //mLogData = buffer + mLogData;
                //reader.close();
                boolean result = Files.deleteIfExists(filePath.toPath());
            }
            FileWriter writer = new FileWriter(filePath, false);
            writer.append(mLogData);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception at writeLogsToFileToInternalStorage: " + e.getMessage());
        }
    }

    //static private ArrayList<String> mData = new ArrayList();
    static private String mLogData = new String("");
    static private LocalDateTime mPreviousTimeStamp = null;
    private static final String TAG = "Imaging Statistics Model";
    private static boolean mCollectData = true;
    private static long s_startTime = 0;
}
