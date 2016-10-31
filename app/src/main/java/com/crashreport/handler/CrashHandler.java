package com.crashreport.handler;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.crashreport.database.DBConstant;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getSimpleName();
    public static CrashHandler instance;
    private Context context;

    public static CrashHandler getInstance(Context mContext) {
        synchronized (CrashHandler.class) {
            if (instance == null) {
                instance = new CrashHandler();
                instance.context = mContext;
            }
        }
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            processReport(throwable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCrashTracking() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void processReport(Throwable e) throws Exception {

        StringBuilder finalReportBuffer = new StringBuilder();
        finalReportBuffer.append("===============/*/ Crash Report /*/=============");
        finalReportBuffer.append("\n\n====/*/ Crash Report Generated on : ").append(new Date().toString());
        finalReportBuffer.append("\n\n===============/*/ Device Information: /*/=============\n\n").append(getDeviceInfo());
        finalReportBuffer.append("\n\n===============/*/ Crash Causes /*/=============\n\n");
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        finalReportBuffer.append(result.toString());
        finalReportBuffer.append("\n\n===============/*/ Crash Tracked /*/=============\n\n");

        Log.e(TAG, finalReportBuffer.toString());
        storeCrash(finalReportBuffer.toString(), new Date().toString());

//        Intent intent = new Intent(context, ErrorReporterActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private String getDeviceInfo() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            StringBuilder reportBuffer = new StringBuilder();
            reportBuffer.append("\nVERSION		: ").append(packageInfo.versionName);
            reportBuffer.append("\nPACKAGE      : ").append(packageInfo.packageName);

            reportBuffer.append("\nFILE-PATH    : ").append("");
            reportBuffer.append("\nPHONE-MODEL  : ").append(Build.MODEL);
            reportBuffer.append("\nANDROID_VERS : ").append(Build.VERSION.RELEASE);
            reportBuffer.append("\nBOARD        : ").append(Build.BOARD);
            reportBuffer.append("\nBRAND        : ").append(Build.BRAND);
            reportBuffer.append("\nDEVICE       : ").append(Build.DEVICE);
            reportBuffer.append("\nDISPLAY      : ").append(Build.DISPLAY);
            reportBuffer.append("\nFINGER-PRINT : ").append(Build.FINGERPRINT);
            reportBuffer.append("\nHOST         : ").append(Build.HOST);
            reportBuffer.append("\nID           : ").append(Build.ID);
            reportBuffer.append("\nMODEL        : ").append(Build.MODEL);
            reportBuffer.append("\nPRODUCT      : ").append(Build.PRODUCT);
            reportBuffer.append("\nMANUFACTURER : ").append(Build.TAGS);
            reportBuffer.append("\nTAGS         : ").append(Build.MODEL);
            reportBuffer.append("\nTIME         : ").append(Build.TIME);
            reportBuffer.append("\nTYPE         : ").append(Build.TYPE);
            reportBuffer.append("\nUSER         : ").append(Build.USER);

//            infoStringBuffer.append("\nTOTAL-INTERNAL-MEMORY     : ").append(getTotalInternalMemorySize() + " mb");
//            infoStringBuffer.append("\nAVAILABLE-INTERNAL-MEMORY : ").append(getAvailableInternalMemorySize() + " mb");

            return reportBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private void storeCrash(final String details, final String date) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstant.CRASHREPORT_FIELD_DATE, date);
            contentValues.put(DBConstant.CRASHREPORT_FIELD_DETAIL, details);
            Uri uri = context.getContentResolver().insert(DBConstant.CRASHREPORT_CONTENT_URI, contentValues);
            Log.i(TAG, uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

}
